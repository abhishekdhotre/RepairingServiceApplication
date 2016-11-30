package edu.uta.utarepairingservices;

// This class will have all the getter setter methods required to use in other Activities
// For Ex. uta_net_id is a field which will be used most of the time to perform CRUD opertions

import android.app.Application;

public class UserInfo extends Application {

    private static String uta_net_id;
    private static String RoleId;
    private static int serviceId;
    private static int spID;
    private static int custID;
    private static int requestID;
    private static String loginMessage;
    private static String message="";

    public UserInfo() {
    }

    public UserInfo(String uta_net_id) {
        this.uta_net_id = uta_net_id;
    }

    public static String getUta_net_id() {
        return uta_net_id;
    }

    public void setUta_net_id(String uta_net_id) {
        this.uta_net_id = uta_net_id;
    }

    public static String getRoleId() {
        return RoleId;
    }

    public static void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public static int getServiceId() {
        return serviceId;
    }

    public static void setServiceId(int serviceId) {
        UserInfo.serviceId = serviceId;
    }

    public static int getSpID() {
        return spID;
    }

    public static void setSpID(int spID) {
        UserInfo.spID = spID;
    }

    public static int getCustID() {
        return custID;
    }

    public static void setCustID(int custID) {
        UserInfo.custID = custID;
    }

    public static int getRequestID() {
        return requestID;
    }

    public static void setRequestID(int requestID) {
        UserInfo.requestID = requestID;
    }

    public static String getLoginMessage() {
        return loginMessage;
    }

    public static void setLoginMessage(String loginMessage) {
        UserInfo.loginMessage = loginMessage;
    }

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        UserInfo.message = message;
    }
}
