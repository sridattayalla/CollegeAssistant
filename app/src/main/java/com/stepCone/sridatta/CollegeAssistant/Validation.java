package com.stepCone.sridatta.CollegeAssistant;

import java.util.regex.Pattern;

/**
 * Created by HP-PC on 29-01-2018.
 */

public class Validation {
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("[0-9]");
    private final Pattern hasSpecialChar = Pattern.compile("[@$#&_]");
    private final Pattern hasSpace = Pattern.compile("[ ]");

    String idValidation(String emp_id){
        if(emp_id.equals("")) {
            return ("ID cannot be blank");
        }
        if(emp_id.length()>10){
            return ("ID length cannot be more than 10");
        }
        if(!emp_id.matches("[a-z0-9]+")){
            return ("ID should contain Lower case letters and digits only");
        }

        return "true";
    }
    String phoneValidation(String emp_phone){
        if(emp_phone.equals("")){
            return("Phone number Cannot be blank");
        }
        if(emp_phone.length()!=10){
            return("Phone number Should be of length 10.Do not use any code");
        }
        if(hasSpace.matcher(emp_phone).find()){
            return("Phone number Should not contain space");
        }
        return "true";
    }
    public String passwordValidation(String emp_password){
        if(emp_password.equals("")){
            return("Password Cannot be blank");
        }
        if(!hasUppercase.matcher(emp_password).find()){
            return("Password Should contain atleast an Uppercase Letter");
        }
        if(!hasLowercase.matcher(emp_password).find()){
            return("Password Should contain atleast a Lowercase Letter");
        }
        if(!hasNumber.matcher(emp_password).find()){
            return("Password Should contain atleast one digit");
        }
        if(!hasSpecialChar.matcher(emp_password).find()){
            return("Password Should contain anyone of @,$,#,&,_");
        }
        if(hasSpace.matcher(emp_password).find()){
            return("Password Should not contain any space");
        }
        if(emp_password.length()<8){
            return("Password Size should be atleast 8");
        }
        return "true";
    }
}
