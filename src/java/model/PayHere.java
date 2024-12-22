package model;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

public class PayHere {

    public static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
