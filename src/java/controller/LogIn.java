/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hasun
 */
@WebServlet(name = "LogIn", urlPatterns = {"/LogIn"})
public class LogIn extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_DTO = gson.fromJson(req.getReader(), User_DTO.class);
        
        Response_DTO response_DTO = new Response_DTO();
        
        if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please Enter Your Email");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please Enter Your Password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            Criteria criteriaSearrch = session.createCriteria(User.class);
            criteriaSearrch.add(Restrictions.eq("email", user_DTO.getEmail()));
            criteriaSearrch.add(Restrictions.eq("password", user_DTO.getPassword()));
            
            if (!criteriaSearrch.list().isEmpty()) {
                
                User user = (User) criteriaSearrch.list().get(0);
                
                if (user.getStatus() != 2) {
                    //Not Verified

                    req.getSession().setAttribute("email", user_DTO.getEmail());
                    response_DTO.setContent("UnVerified");
                } else {
                    //Verified
                    user_DTO.setFname(user.getFname());
                    user_DTO.setLname(user.getLname());
                    user_DTO.setPassword(null);
                    req.getSession().setAttribute("user", user_DTO);
                    
                    if (req.getSession().getAttribute("sessionCart") != null) {
                        
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) req.getSession().getAttribute("sessionCart");
                        
                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria2.list();
                        
                        if (dbCart.isEmpty()) {
                            //DB cart Empty
                            for (Cart_DTO cart_DTO : sessionCart) {
                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct());
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                            }
                        } else {
                            //DB cart is not empty
                            for (Cart_DTO cart_DTO : sessionCart) {
                                boolean isFoundInDBCart = false;
                                for (Cart cart : dbCart) {
                                    if (cart_DTO.getProduct().getId() == cart.getProduct().getId()) {
                                        isFoundInDBCart = true;
                                        
                                        if ((cart_DTO.getQty() + cart.getQty()) <= cart.getProduct().getQuantity()) {
                                            cart.setQty(cart_DTO.getQty() + cart.getQty());
                                            session.update(cart);
                                        } else {
                                            cart.setQty(cart.getProduct().getQuantity());
                                            session.update(cart);
                                        }
                                    }
                                }
                                
                                if (!isFoundInDBCart) {
                                    Cart cart = new Cart();
                                    cart.setProduct(cart_DTO.getProduct());
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(cart);
                                }
                            }
                            
                        }
                        req.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();
                        
                    }
                    
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Login Success");
                }
                
            } else {
                response_DTO.setContent("This Email Alreay Exists");
            }
        }
        
        System.out.println(response_DTO.getContent());
        resp.getWriter().write(gson.toJson(response_DTO));
    }
    
}
