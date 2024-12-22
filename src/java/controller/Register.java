/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import java.time.LocalDateTime;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Mail;
import model.MailTemplate;

/**
 *
 * @author hasun
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_DTO = gson.fromJson(req.getReader(), User_DTO.class);

        Response_DTO response_DTO = new Response_DTO();

        if (user_DTO.getFname().isEmpty()) {
            response_DTO.setContent("Please Enter Your First Name");
        } else if (user_DTO.getLname().isEmpty()) {
            response_DTO.setContent("Please Enter Your Last Name");
        } else if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please Enter Your Email");
        } else if (!Validations.isEmailValid(user_DTO.getEmail())) {
            response_DTO.setContent("Invalid Email");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please Enter Your Password");
        } else if (!Validations.isPasswordValid(user_DTO.getPassword())) {
            response_DTO.setContent("Password Must Contain Minimum eight and maximum 10 "
                    + "characters, at least one uppercase letter, a lowercase letter, number and special character:");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteriaSearrch = session.createCriteria(User.class);
            criteriaSearrch.add(Restrictions.eq("email", user_DTO.getEmail()));

            if (!criteriaSearrch.list().isEmpty()) {
                response_DTO.setContent("This Email Alreay Exists");
            } else {

                //generate verification code
                int code = (int) (Math.random() * 1000000);

                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                final User user = new User();
                user.setEmail(user_DTO.getEmail());
                user.setFname(user_DTO.getFname());
                user.setLname(user_DTO.getLname());
                user.setPassword(user_DTO.getPassword());
                user.setMobile("0000000000");
                user.setDatetime(currentDateTime.format(formatter));
                user.setStatus((int) 1);
                user.setGender((int) 1);
                user.setVerification(String.valueOf(code));

                //send verification email
                Thread mailThread = new Thread() {
                    @Override
                    public void run() {
                        Mail.sendMail(
                                user_DTO.getEmail(),
                                "Molla Store Verification",
                                MailTemplate.registrationVerificationEmail(code)
                        );
                    }

                };
                mailThread.start();

                session.save(user);
                session.beginTransaction().commit();

                req.getSession().setAttribute("email", user_DTO.getEmail());
                response_DTO.setSuccess(true);
                response_DTO.setContent("Registration Completed. Please Check Your Inbox");

            }
            session.close();

        }

        System.out.println(response_DTO.getContent());
        resp.getWriter().write(gson.toJson(response_DTO));

    }
}
