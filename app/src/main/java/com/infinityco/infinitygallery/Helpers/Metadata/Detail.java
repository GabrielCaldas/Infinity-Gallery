package com.infinityco.infinitygallery.Helpers.Metadata;

public class Detail {

    private String detailName,detailValue;

    public Detail(String Name, String Value){
        this.detailName = Name;
        this.detailValue = Value;
    }

    public String getDetailName(){
        return detailName;
    }

    public String getDetailValue(){
        return detailValue;
    }
}
