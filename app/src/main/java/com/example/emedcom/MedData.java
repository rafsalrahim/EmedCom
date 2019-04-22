package com.example.emedcom;

public class MedData {

    String compname;
    String from;
    String medname;
    String quantity;
    String sell;
    String collection_center;
    String exp_date;
    String usr_id;

    public MedData(){

    }

    public MedData(String compname, String from, String medname, String quantity, String sell,String collection_center,String exp_date,String usr_id) {
        this.compname = compname;
        this.from = from;
        this.medname = medname;
        this.quantity = quantity;
        this.sell = sell;
        this.usr_id=usr_id;
        this.collection_center=collection_center;
        this.exp_date=exp_date;
    }

    public String getCompname() {
        return compname;
    }

    public void setCompname(String compname) {
        this.compname = compname;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMedname() {
        return medname;
    }

    public void setMedname(String medname) {
        this.medname = medname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getCollection_center() {
        return collection_center;
    }

    public void setCollection_center(String collection_center) {
        this.collection_center = collection_center;
    }

    public String getExp_date() {
        return exp_date;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }
}
