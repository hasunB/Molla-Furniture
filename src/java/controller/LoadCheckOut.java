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
import entity.Province;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hasun
 */
@WebServlet(name = "LoadCheckOut", urlPatterns = {"/LoadCheckOut"})
public class LoadCheckOut extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Gson gson = new Gson();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);

        HttpSession httpSession = req.getSession();
        Session session = HibernateUtil.getSessionFactory().openSession();

        if (httpSession.getAttribute("user") != null) {

            User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");

            //get user from db
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            //get user last address from db
            Criteria criteria2 = session.createCriteria(Address.class);
            criteria2.add(Restrictions.eq("user", user));
            criteria2.addOrder(Order.desc("id"));
            criteria2.setMaxResults(1);
            Address address = (Address) criteria2.list().get(0);

            //get city from db
            Criteria criteria3 = session.createCriteria(City.class);
            criteria3.addOrder(Order.asc("name"));
            List<City> cityList = criteria3.list();
            
            //get district from db
            Criteria criteria4 = session.createCriteria(District.class);
            criteria4.addOrder(Order.asc("name"));
            List<District> districtList = criteria4.list();
            
            //get province from db
            Criteria criteria5 = session.createCriteria(Province.class);
            criteria5.addOrder(Order.asc("name"));
            List<Province> provinceList = criteria5.list();
            
            //get country from db
            Criteria criteria6 = session.createCriteria(Country.class);
            criteria6.addOrder(Order.asc("name"));
            List<Country> countryList = criteria6.list();

            //get cart items from db
            Criteria criteria7 = session.createCriteria(Cart.class);
            criteria7.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria7.list();

            //pack address in json obj
            address.setUser(null);
            jsonObject.add("address", gson.toJsonTree(address));

            //pack cityList in json obj
            jsonObject.add("cityList", gson.toJsonTree(cityList));
            
            //pack districtList in json obj
            jsonObject.add("districtList", gson.toJsonTree(districtList));
            
            //pack cityList in json obj
            jsonObject.add("provinceList", gson.toJsonTree(provinceList));
            
            //pack cityList in json obj
            jsonObject.add("countryList", gson.toJsonTree(countryList));

            //pack cartList in json obj
            for (Cart cart : cartList) {
                cart.setUser(null);
                cart.getProduct().setUser(null);
            }

            jsonObject.add("cartList", gson.toJsonTree(cartList));
            jsonObject.addProperty("success", true);

        } else {
            //not signed in
            jsonObject.addProperty("meesage", "Not Signed In");
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));
        session.close();
    }
}
