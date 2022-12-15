package ua.lab9.web.entities;

import java.io.Serializable;

public class City implements Entity, Serializable {
    private int code;
    private String name;
    private int isCapital;
    private int count;
    private int countryCode;

    public City(int code, String name, int isCapital, int count,
                int countryCode) {
        this.code = code;
        this.name = name;
        this.isCapital = isCapital;
        this.count = count;
        this.countryCode = countryCode;
    }

    public City() {

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

    public int getIsCapital() {
        return isCapital;
    }

    public void setIsCapital(int isCapital) {
        this.isCapital = isCapital;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }
}
