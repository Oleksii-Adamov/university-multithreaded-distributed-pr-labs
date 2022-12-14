package ua.lab9.web.entities;

public class City implements Entity {
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
}
