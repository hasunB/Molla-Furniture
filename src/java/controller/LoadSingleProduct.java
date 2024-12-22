/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Category;
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
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hasun
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            String productId = req.getParameter("id");

            Gson gson = new Gson();
            Session session = HibernateUtil.getSessionFactory().openSession();

            Response_DTO response_DTO = new Response_DTO();

            if (Validations.isInteger(productId)) {

                Product product = (Product) session.get(Product.class, Integer.valueOf(productId));
                product.getUser().setPassword(null);
                product.getUser().setVerification(null);
                product.getUser().setEmail(null);

                Category category = product.getCategory();

                Criteria criteria = session.createCriteria(Product.class);
                criteria.add(Restrictions.eq("category.id", category.getId()));
                criteria.setMaxResults(5);

                List<Product> productList = criteria.list();

                for (Product product1 : productList) {
                    product1.getUser().setPassword(null);
                    product1.getUser().setVerification(null);
                    product1.getUser().setEmail(null);
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.add("product", gson.toJsonTree(product));
                jsonObject.add("productList", gson.toJsonTree(productList));

                resp.getWriter().write(gson.toJson(jsonObject));
//                response_DTO.setContent("Product Found");

            } else {
//                response_DTO.setContent("Product Not Found");
            }

        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
        }

    }

}
