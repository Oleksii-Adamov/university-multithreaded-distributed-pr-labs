package ua.lab9.web.entities;

public class Country implements Entity {
    public int code;
    public String name;

    public Country(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public Country() {

    }
}
