/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Brand;
import entity.Category;
import entity.Color;
import entity.Product_type;
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

/**
 *
 * @author hasun
 */
@WebServlet(name = "AdvancedSearch", urlPatterns = {"/AdvancedSearch"})
public class AdvancedSearch extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        Gson gson  = new Gson();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        //load data
        
        //Get category list from DB
        Criteria categorySearch = session.createCriteria(Category.class);
        List<Category> categoryList = categorySearch.list();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        
        //Get product type list from DB
        Criteria productTypeSearch = session.createCriteria(Product_type.class);
        List<Product_type> productTypeList = productTypeSearch.list();
        jsonObject.add("productTypeList", gson.toJsonTree(productTypeList));
        
        //Get Brand list from DB
        Criteria brandSearch = session.createCriteria(Brand.class);
        List<Brand> brandList = brandSearch.list();
        jsonObject.add("brandList", gson.toJsonTree(brandList));
        
        //Get Color list From DB
        Criteria colorSearch = session.createCriteria(Color.class);
        List<Color> colorList = colorSearch.list();
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        
        //load data
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));
        
    }

    

}
