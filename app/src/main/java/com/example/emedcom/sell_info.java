package com.example.emedcom;

public class sell_info {

    public String medname, compname,from,quantity,sell,usr_id,collection_center,exp_date;
    public sell_info(){
    }

    public sell_info(String medname,String compname,String from,String quantity,String val,String usr_id,String collection_center,String exp_date){
        this.medname=medname;
        this.compname=compname;
        this.from=from;
        this.quantity=quantity;
        sell=val;
        this.usr_id=usr_id;
        this.collection_center=collection_center;
        this.exp_date=exp_date;
    }
}
