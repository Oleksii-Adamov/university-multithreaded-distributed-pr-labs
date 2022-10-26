public class City {
    public int code;
    public String name;
    public boolean isCapital;
    public int count;
    public Country country;

    public City(int code, String name, boolean isCapital, int count,
                Country country) {
        this.code = code;
        this.name = name;
        this.isCapital = isCapital;
        this.count = count;
        this.country = country;
    }
}
