/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hasun
 */
@WebServlet(name = "VerifyAccount", urlPatterns = {"/VerifyAccount"})
public class VerifyAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(req.getReader(), JsonObject.class);
        String verification = dto.get("verification").getAsString();

        Response_DTO response_DTO = new Response_DTO();

        if (req.getSession().getAttribute("email") != null) {

            String email = req.getSession().getAttribute("email").toString();

            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteriaSearrch = session.createCriteria(User.class);
            criteriaSearrch.add(Restrictions.eq("email", email));
            criteriaSearrch.add(Restrictions.eq("verification", verification));

            if (!criteriaSearrch.list().isEmpty()) {
                User user = (User) criteriaSearrch.list().get(0);
                 user.setStatus((int) 2);

                session.update(user);
                session.beginTransaction().commit();

                response_DTO.setSuccess(true);
                response_DTO.setContent("Verification Success");
            } else {
                response_DTO.setContent("Inavalid verification Code!");
            }

        } else {
            response_DTO.setContent("Verification Unavaliable. Please SignIn Again");
        }

        System.out.println(response_DTO.getContent());
        resp.getWriter().write(gson.toJson(response_DTO));

    }

}
