package ua.lab9.web.entities;

import java.io.Serializable;

public class Country implements Entity, Serializable {
    private int code;
    private String name;

    public Country(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public Country() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
