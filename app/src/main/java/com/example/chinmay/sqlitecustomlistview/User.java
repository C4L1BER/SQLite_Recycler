package com.example.chinmay.sqlitecustomlistview;

public class User
{
    private String uName;
    private String eMail;
    private String phone;
    private String id;

    public User(String id, String uName, String eMail, String phone)
    {
        this.id = id;
        this.uName = uName;
        this.eMail = eMail;
        this.phone = phone;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
