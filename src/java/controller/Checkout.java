/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Country;
import entity.District;
import entity.Invoice;
import entity.Order_Item;
import entity.Order_Status;
import entity.Orders;
import entity.Product;
import entity.Province;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hasun
 */
@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        HttpSession httpSession = req.getSession();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
        String first_name = requestJsonObject.get("first_name").getAsString();
        String last_name = requestJsonObject.get("last_name").getAsString();
        String city_id = requestJsonObject.get("city_id").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
        String postal_code = requestJsonObject.get("postal_code").getAsString();
        String country_id = requestJsonObject.get("country").getAsString();
        String province_id = requestJsonObject.get("province").getAsString();
        String district_id = requestJsonObject.get("district").getAsString();

        if (httpSession.getAttribute("user") != null) {

            //user sign in
            //get user DB
            User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            if (isCurrentAddress) {
                //get current address
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {

                    //current address not found
                    responseJsonObject.addProperty("message", "Current address not found. Please create a new address");

                } else {

                    //current address found
                    //complete
                    Address address = (Address) criteria2.list().get(0);
                    saveOrders(session, transaction, user, address, responseJsonObject);

                }

            } else {
                //create a new address

                if (first_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill first name");
                } else if (last_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill last name");
                } else if (address1.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill address line 1");
                } else if (address2.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill address line 2");
                } else if (postal_code.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill postal code");
                } else if (!Validations.isInteger(country_id)) {
                    responseJsonObject.addProperty("message", "Invalid country");
                } else if (!Validations.isInteger(province_id)) {
                    responseJsonObject.addProperty("message", "Invalid province");
                } else if (!Validations.isInteger(district_id)) {
                    responseJsonObject.addProperty("message", "Invalid district");
                } else if (!Validations.isInteger(city_id)) {
                    responseJsonObject.addProperty("message", "Invalid city");
                } else if (!Validations.isInteger(postal_code)) {
                    responseJsonObject.addProperty("message", "Invalid Postal Code");
                } else {

                    //check city from db
                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.valueOf(city_id)));

                    //check district from db
                    Criteria criteria4 = session.createCriteria(District.class);
                    criteria4.add(Restrictions.eq("id", Integer.valueOf(district_id)));

                    //check province from db
                    Criteria criteria5 = session.createCriteria(Province.class);
                    criteria5.add(Restrictions.eq("id", Integer.valueOf(province_id)));

                    //check country from db
                    Criteria criteria6 = session.createCriteria(Country.class);
                    criteria6.add(Restrictions.eq("id", Integer.valueOf(country_id)));

                    if (criteria6.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid Country");
                    } else if (criteria5.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid Provice");
                    } else if (criteria4.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid District");
                    } else if (criteria3.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid City");
                    } else {
                        //found
                        Country country = (Country) criteria6.list().get(0);
                        Province province = (Province) criteria5.list().get(0);
                        District district = (District) criteria4.list().get(0);
                        City city = (City) criteria3.list().get(0);

                        Address address = new Address();
                        address.setCity(city);
                        address.setCountry(country);
                        address.setDistrict(district);
                        address.setLine1(address1);
                        address.setLine2(address2);
                        address.setFname(first_name);
                        address.setLname(last_name);
                        address.setPostalCode(postal_code);
                        address.setProvince(province);
                        address.setUser(user);

                        session.save(address);
                        saveOrders(session, transaction, user, address, responseJsonObject);

                    }

                }

            }

        } else {

            //not signed in
            responseJsonObject.addProperty("message", "User not signed in");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));
    }

    private void saveOrders(Session session, Transaction transaction, User user, Address address, JsonObject responseJsonObject) {
        try {

            //complete checkout process
            Orders order = new Orders();
            order.setAddress(address);
            order.setDate_time(new Date());
            order.setUser(user);

            int order_id = (int) session.save(order);

            Criteria criteria7 = session.createCriteria(Cart.class);
            criteria7.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria7.list();

            Order_Status order_Status = (Order_Status) session.get(Order_Status.class, 1); //1 - payment pending
            double amount = 0;
            String items = "";
            for (Cart cart : cartList) {

                //amount
                amount += cart.getQty() * (cart.getProduct().getPrice() + cart.getProduct().getDelivery_fee());

                //get product
                Product product = cart.getProduct();

                items += cart.getProduct().getTitle() + " x" + cart.getQty() + " ";

                Order_Item order_Item = new Order_Item();
                order_Item.setOrders(order);
                order_Item.setOrder_status(order_Status);
                order_Item.setProduct(cart.getProduct());
                order_Item.setQty(cart.getQty());

                session.save(order_Item);

                //update product qty in db
                product.setQuantity(product.getQuantity() - cart.getQty());
                session.update(product);

                //delete cart item in db
                session.delete(cart);

            }
            transaction.commit();

            //set PayHERE
            String merchant_id = "1222069";
            String formatedAmount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "";
            String merchantSecretM5Hash = PayHere.generateMD5(merchantSecret);

            JsonObject payhere = new JsonObject();
            payhere.addProperty("merchant_id", merchant_id);

            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "");

            payhere.addProperty("first_name", user.getFname());
            payhere.addProperty("last_name", user.getLname());
            payhere.addProperty("email", user.getEmail());
            payhere.addProperty("phone", "0773720462");
            payhere.addProperty("address", "no11 pahala imbulgoda, gampaha");
            payhere.addProperty("city", "gampaha");
            payhere.addProperty("country", "Sri-Lanka");
            payhere.addProperty("order_id", String.valueOf(order_id));
            payhere.addProperty("items", items);
            payhere.addProperty("currency", currency);
            payhere.addProperty("amount", formatedAmount);
            payhere.addProperty("sandbox", true);

            //generate md5
            String md5Hash = PayHere.generateMD5(merchant_id + order_id + formatedAmount + currency + merchantSecretM5Hash);
            payhere.addProperty("hash", md5Hash);
            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("message", "Your Order Has Been Placed");

            Gson gson = new Gson();
            responseJsonObject.add("payhereJson", gson.toJsonTree(payhere));
        } catch (Exception e) {
            transaction.rollback();
        }
    }

}
