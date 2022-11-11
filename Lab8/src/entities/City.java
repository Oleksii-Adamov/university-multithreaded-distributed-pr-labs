package entities;

import java.io.Serializable;

public class City implements Entity, Serializable {
    public int code;
    public String name;
    public int isCapital;
    public int count;
    public int countryCode;

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

    @Override
    public void print() {
        System.out.println("code: " + code);
        System.out.println("name: " + name);
        System.out.println("iscap: " + isCapital);
        System.out.println("count: " + count);
        System.out.println("country code: " + countryCode);
    }
}
