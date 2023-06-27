package com.example.minipojectv2.models;

public class Colors {
    private int id;
    private String name;
    private String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Colors(String name,String code) {
        this.name = name;
        this.code = code;
    }
    @Override
    public String toString() {
        return String.format(" %s      %s",name,code);
    }
}
