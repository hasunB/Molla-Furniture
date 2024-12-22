/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Brand;
import entity.Category;
import entity.Color;
import entity.Product;
import entity.ProductStatus;
import entity.ProductTypeHasBrand;
import entity.Product_type;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hasun
 */
@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new Gson();

        String categoryId = req.getParameter("categoryId");
        String productTypeId = req.getParameter("productTypeId");
        String brandId = req.getParameter("brandId");
        String colorId = req.getParameter("colorId");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String quantity = req.getParameter("quantity");
        String price = req.getParameter("price");

        Part mainImage = req.getPart("mainImage");
        Part image1 = req.getPart("image1");
        Part image2 = req.getPart("image2");
        Part image3 = req.getPart("image3");

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (Integer.parseInt(categoryId) == 0) {
            response_DTO.setContent("Please Select a Category");
        } else if (!Validations.isInteger(categoryId)) {
            response_DTO.setContent("Invalid Category");
        } else if (Integer.parseInt(productTypeId) == 0) {
            response_DTO.setContent("Please Select a Product Type");
        } else if (!Validations.isInteger(productTypeId)) {
            response_DTO.setContent("Invalid ProductType");
        } else if (Integer.parseInt(brandId) == 0) {
            response_DTO.setContent("Please Select a Brand");
        } else if (!Validations.isInteger(brandId)) {
            response_DTO.setContent("Invalid Brand");
        } else if (Integer.parseInt(colorId) == 0) {
            response_DTO.setContent("Please Select a Color");
        } else if (!Validations.isInteger(colorId)) {
            response_DTO.setContent("Invalid Color");
        } else if (title.isEmpty()) {
            response_DTO.setContent("Please Fill the Title");
        } else if (description.isEmpty()) {
            response_DTO.setContent("Please Fill the Description");
        } else if (quantity.isEmpty()) {
            response_DTO.setContent("Please Fill the Quantity");
        } else if (!Validations.isInteger(quantity)) {
            response_DTO.setContent("Invalid Quantity");
        } else if (Integer.parseInt(quantity) <= 0) {
            response_DTO.setContent("Quantity Must Be Greater Than 0");
        } else if (price.isEmpty()) {
            response_DTO.setContent("Please Fill the Price");
        } else if (!Validations.isDouble(price)) {
            response_DTO.setContent("Invalid Price");
        } else if (Double.parseDouble(price) <= 0) {
            response_DTO.setContent("Price Must Be Greater Than 0");
        } else if (mainImage.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload the Main Image");
        } else if (image1.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload the Image 1");
        } else if (image2.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload the Image 2");
        } else if (image3.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload the Image 3");
        } else {
            Category category = (Category) session.get(Category.class, Integer.valueOf(categoryId));

            if (category == null) {
                response_DTO.setContent("Please select a valid category");
            } else {
                Product_type productType = (Product_type) session.get(Product_type.class, Integer.valueOf(productTypeId));

                if (productType == null) {
                    response_DTO.setContent("Please select a valid product Type");
                } else {
                    Brand brand = (Brand) session.get(Brand.class, Integer.valueOf(brandId));

                    if (brand == null) {
                        response_DTO.setContent("Please select a valid Brand");
                    } else {
                        Color color = (Color) session.get(Color.class, Integer.valueOf(colorId));

                        if (color == null) {
                            response_DTO.setContent("Please select a valid Color");
                        } else {

                            //get date
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                            //get Active Status
                            ProductStatus productStatus = (ProductStatus) session.load(ProductStatus.class, 1);

                            //get ProductTypeHasBrand
                            Criteria criteria = session.createCriteria(ProductTypeHasBrand.class);
                            criteria.add(Restrictions.eq("productType.id", Integer.valueOf(productTypeId)));
                            criteria.add(Restrictions.eq("brand.id", Integer.valueOf(brandId)));

                            ProductTypeHasBrand productTypeHasBrand = (ProductTypeHasBrand) criteria.uniqueResult();
                            if (productTypeHasBrand != null) {
                                int id = productTypeHasBrand.getId();

                                User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");
                                Criteria criteria2 = session.createCriteria(User.class);
                                criteria2.add(Restrictions.eq("email", user_DTO.getEmail()));
                                User user = (User) criteria2.uniqueResult();

                                Product product = new Product();
                                product.setCategory(category);
                                product.setColor(color);
                                product.setDateTime(currentDateTime.format(formatter));
                                product.setDelivery_fee(Double.parseDouble("500"));
                                product.setDescription(description);
                                product.setPrice(Double.parseDouble(price));
                                product.setProductTypeHasBrand(productTypeHasBrand);
                                product.setQuantity(Integer.parseInt(quantity));
                                product.setStatus(productStatus);
                                product.setTitle(title);
                                product.setUser(user);

                                int pid = (int) session.save(product);
                                session.beginTransaction().commit();
                                response_DTO.setSuccess(true);

                                String applicationPath = req.getServletContext().getRealPath("");
                                String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");

                                File folder = new File(newApplicationPath + File.separator + "product_images");
                                folder.mkdir();

                                File folder2 = new File(folder, String.valueOf(pid));
                                folder2.mkdir();

                                File mainFile = new File(folder2, "mainImage.png");
                                InputStream mainInputStream = mainImage.getInputStream();
                                Files.copy(mainInputStream, mainFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                File file1 = new File(folder2, "image1.png");
                                InputStream inputStream1 = image1.getInputStream();
                                Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                File file2 = new File(folder2, "image2.png");
                                InputStream inputStream2 = image2.getInputStream();
                                Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                File file3 = new File(folder2, "image3.png");
                                InputStream inputStream3 = image3.getInputStream();
                                Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                response_DTO.setContent("Product Added");

                            } else {
                                response_DTO.setContent("No Matches Found in the database. Please Try Again");
                            }

                        }

                    }

                }
            }

        }

        System.out.println(response_DTO.getContent());
        resp.getWriter().write(gson.toJson(response_DTO));

    }

}
