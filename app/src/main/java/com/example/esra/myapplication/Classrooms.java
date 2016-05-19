package com.example.esra.myapplication;

/**
 * Created by Esra on 5.05.2016.
 */
public class Classrooms {


    private String code;
    private int ID;

   public Classrooms(String code,int ID) {
        this.code=code;
        this.ID=ID;

    }
    public String getCode() {
        return code;
    }
    public int getID() {
        return ID;
    }

    @Override
    public String toString(){
        return code+" "+ID;
    }
}
