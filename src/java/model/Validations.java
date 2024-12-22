/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author hasun
 */
public class Validations {
    
    public static boolean isEmailValid(String email){
        return email.matches("^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
    }
    
    public static boolean isPasswordValid(String password){
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$");
    }
    
    public static boolean isDouble(String price){
        return price.matches("^\\d+(\\.\\d{2})?$");
    }
    
    public static boolean isInteger(String price){
        return price.matches("^\\d+$");
    }
}
