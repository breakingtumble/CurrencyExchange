package com.breakingtumble.exchanger.model;

public class Currencyy {
    private int id;
    private String name;
    private String code;
    private String sign;

    public Currencyy(int id, String name, String code, String sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public Currencyy(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSign() {
        return sign;
    }
}
