package library;

public class Address {
    private int id;
    protected String country;
    protected String city;
    protected int zipCode;
    protected String street;
    protected int number;

    public Address(){
        this.id = 1;
        this.country = "United States";
        this.city = "Los Angeles";
        this.street = "South Spring Street";
        this.number = 93;
        this.zipCode =  90014;
    }

    public Address(int id, String country, String city, int zip_code, String street, int number) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.zipCode = zip_code;
        this.street = street;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        String output = "--Address--\n";
        output += "Country: " + this.country + "\n";
        output += "City: " + this.city + "\n";
        output += "Street: " + this.street + "\n";
        output += "Number: " + this.number + "\n";
        output += "Zip code: " + this.zipCode + "\n";

        return output;
    }
}
