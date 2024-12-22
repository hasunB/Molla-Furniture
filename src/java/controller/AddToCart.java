/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hasun
 */
@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Response_DTO response_DTO = new Response_DTO();
        
        Gson gson = new Gson();

        try {
            String id = req.getParameter("id");
            String qty = req.getParameter("qty");

            if (!Validations.isInteger(id)) {
                //Product Not Found
                response_DTO.setContent("Product Not Found");
            } else if (!Validations.isInteger(qty)) {
                //Invalid Quantity
                response_DTO.setContent("Invalid Quantity");
            } else {

                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

                if (productQty <= 0) {
                    //Qustime more than 
                    response_DTO.setContent("Quantity Must Be Greater Than zero");
                } else {
                    Product product = (Product) session.get(Product.class, productId);
                    if (product != null) {
                        //product found
                        if (req.getSession().getAttribute("user") != null) {
                            //DB Cart

                            User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");

                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                            User user = (User) criteria1.uniqueResult();

                            //check in db cart
                            Criteria criteria2 = session.createCriteria(Cart.class);
                            criteria2.add(Restrictions.eq("user", user));
                            criteria2.add(Restrictions.eq("product", product));

                            if (criteria2.list().isEmpty()) {
                                //item not found in cart
                                if (productQty <= product.getQuantity()) {
                                    //add products into cart

                                    Cart cart = new Cart();
                                    cart.setProduct(product);
                                    cart.setQty(productQty);
                                    cart.setUser(user);
                                    session.save(cart);
                                    transaction.commit();
                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Product added to the Cart");
                                } else {
                                    //quantity not available
                                    response_DTO.setContent("Cart item not avaliable");
                                }
                            } else {
                                //item found in the cart
                                Cart cartItem = (Cart) criteria2.uniqueResult();

                                if (cartItem.getQty() + productQty <= product.getQuantity()) {
                                    cartItem.setQty(cartItem.getQty() + productQty);
                                    session.update(cartItem);
                                    transaction.commit();
                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Item Quantity Updated");
                                } else {
                                    //can't cannot update
                                    response_DTO.setContent("can't update item Quantiity. Quantity Not Avaliable");
                                }
                            }

                        } else {
                            //Session Cart

                            HttpSession httpSession = req.getSession();

                            if (httpSession.getAttribute("sessionCart") != null) {
                                //session cart found

                                ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                                Cart_DTO foundCart_DTO = null;

                                for (Cart_DTO cart_DTO : sessionCart) {
                                    if (cart_DTO.getProduct().getId() == product.getId()) {
                                        foundCart_DTO = cart_DTO;
                                        break;
                                    }
                                }
                                if (foundCart_DTO != null) {
                                    //product found

                                    if (foundCart_DTO.getQty() + productQty <= product.getQuantity()) {
                                        //update quantity

                                        foundCart_DTO.setQty(foundCart_DTO.getQty() + productQty);
                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Item Quantity Updated");
                                    } else {
                                        //quantity not avalaiable
                                        response_DTO.setContent("can't update item Quantiity. Quantity Not Avaliable");
                                    }

                                } else {
                                    //product not found
                                    if (productQty <= product.getQuantity()) {
                                        //add to session cart

                                        Cart_DTO cart_DTO = new Cart_DTO();
                                        cart_DTO.setProduct(product);
                                        cart_DTO.setQty(productQty);
                                        sessionCart.add(cart_DTO);

                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Product added to the Cart");

                                    } else {
                                        //quantity not avaliable
                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Quantity Not Avaliable");
                                    }
                                }

                                System.out.println("session cart found");
                            } else {
                                //session cart not found

                                if (productQty <= product.getQuantity()) {
                                    //add to session cart

                                    ArrayList<Cart_DTO> sessionCart = new ArrayList<>();

                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setProduct(product);
                                    cart_DTO.setQty(productQty);
                                    sessionCart.add(cart_DTO);

                                    httpSession.setAttribute("sessionCart", sessionCart);
                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Product added to the Cart");
                                } else {
                                    //quantity not avaliable
                                    response_DTO.setContent("Quantity Not Avaliable");
                                }
                            }
                        }
                    } else {
                        //product not found
                        response_DTO.setContent("Product Not Found");
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            response_DTO.setContent("Something Went Wrong Please Try Again Later");
        }
        
        System.out.println(response_DTO.getContent());
        resp.getWriter().write(gson.toJson(response_DTO));
        session.close();

    }
}
