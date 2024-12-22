/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author hasun
 */
@Entity
@Table(name = "customer")
public class User implements Serializable{

    @Id
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "fname", length = 50, nullable = false)
    private String fname;

    @Column(name = "lname", length = 50, nullable = false)
    private String lname;

    @Column(name = "password", length = 12, nullable = false)
    private String password;

    @Column(name = "mobile", length = 10, nullable = false)
    private String mobile;

    @Column(name = "date")
    private String datetime;

    @Column(name = "verification_code", length = 20, nullable = false)
    private String verification;

    @Column(name = "status")
    private int status;

    @Column(name = "gender_id")
    private int gender;

    public User() {
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
