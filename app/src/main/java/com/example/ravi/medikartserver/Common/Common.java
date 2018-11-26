package com.example.ravi.medikartserver.Common;


import com.example.ravi.medikartserver.Model.User;


public class Common {
    public static User currentUser;

    public static final String UPDATE="Update";
    public static final String DELETE="Delete";

    public static final int PICK_IMAGE_REQUEST=70;

    public static String convertCodeToStatus(String code)
    {
       if(code.equals("0"))
           return "Placed";
       else if(code.equals("1"))
           return "On the Way";
       else
           return "shipped";
    }

}
