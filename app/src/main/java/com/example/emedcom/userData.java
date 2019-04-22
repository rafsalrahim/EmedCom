package com.example.emedcom;

public class userData {
    private  String Email;
    private  String district;
    //private String items;
    private String name;
    private String phone;
    private String user_type;

    public userData (){

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    /*public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
