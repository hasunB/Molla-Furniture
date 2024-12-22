/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.Color;
import entity.Product_type;
import entity.User;
import entity.ProductTypeHasBrand;
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
@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteriaCategorySearrch = session.createCriteria(Category.class);
        List<Category> CategoryList = criteriaCategorySearrch.list();

        Criteria criteriaProductTypeSearrch = session.createCriteria(Product_type.class);
        List<Product_type> ProductTypeList = criteriaProductTypeSearrch.list();

        Criteria criteriaColorSearrch = session.createCriteria(Color.class);
        criteriaColorSearrch.addOrder(Order.asc("name"));
        List<Color> ColorList = criteriaColorSearrch.list();

        Criteria criteriaBrandSearrch = session.createCriteria(Brand.class);
        criteriaBrandSearrch.addOrder(Order.asc("name"));
        List<Brand> BrandList = criteriaBrandSearrch.list();

        Criteria criteriaProductTypeHasBrandSearrch = session.createCriteria(ProductTypeHasBrand.class);
        List<ProductTypeHasBrand> ProductTypeHasBrandList = criteriaProductTypeHasBrandSearrch.list();

        JsonObject json0bject = new JsonObject();
        json0bject.add("categoryList", gson.toJsonTree(CategoryList));
        json0bject.add("productTypeList", gson.toJsonTree(ProductTypeList));
        json0bject.add("colorList", gson.toJsonTree(ColorList));
        json0bject.add("brandList", gson.toJsonTree(BrandList));
        json0bject.add("productTypeHasBrandList", gson.toJsonTree(ProductTypeHasBrandList));

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(json0bject));
        
        session.close();

    }

}
