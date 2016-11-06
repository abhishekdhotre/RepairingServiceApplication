package edu.uta.utarepairingservices.TableInfo;

/**
 * Created by Sunil on 11/5/2016.
 */
public class UserDetails {
    private long loginID;
    private String netID;
    private String pwd;
    private String role;

    public UserDetails(long loginID, String netID, String pwd, String role){
        this.loginID = loginID;
        this.netID = netID;
        this.pwd = pwd;
        this.role = role;
    }

    public UserDetails(){

    }

    public long getLoginID(){
        return loginID;
    }

    public void setLoginID(long insertid){
        this.loginID = loginID;
    }

    public String getNetID(){
        return netID;
    }

    public void setNetID(String netID){
        this.netID = netID;
    }

    public String getPwd(){
        return pwd;
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }
}
