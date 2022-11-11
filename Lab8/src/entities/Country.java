package entities;

import java.io.Serializable;

public class Country implements Entity, Serializable {
    public int code;
    public String name;

    public Country(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public Country() {

    }

    @Override
    public void print() {
        System.out.println("code: " + code);
        System.out.println("name: " + name);
    }
}
