/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author hasun
 */
@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
public class CheckSignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (req.getSession().getAttribute("user") != null) {

            //already signin
            User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");
            response_DTO.setSuccess(true);
            response_DTO.setContent(user_DTO);

        } else {

            //not sign in
            response_DTO.setContent("Not Signed In");

        }

        jsonObject.add("response_DTO", gson.toJsonTree(response_DTO));
        
        //get last 4 products
        Criteria criteria1 = session.createCriteria(Product.class);
        criteria1.addOrder(Order.desc("id"));
        criteria1.setMaxResults(7);
        List<Product> productList = criteria1.list();
        for (Product product : productList) {
            product.setUser(null);
        }

        Gson gson1 = new Gson();
        jsonObject.add("products", gson1.toJsonTree(productList));

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));
    }

}
