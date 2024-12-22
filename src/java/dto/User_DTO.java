/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 *
 * @author hasun
 */
public class User_DTO implements Serializable {

    @Expose
    private String email;
    
    @Expose
    private String fname;
    
    @Expose
    private String lname;
    
    @Expose(deserialize = true,serialize = false)
    private String password;
    
    public User_DTO(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
