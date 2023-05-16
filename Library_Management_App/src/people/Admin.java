package people;

public class Admin extends Person{
    private final static String verificationCode = "admin01";

    public Admin(){}

    public Admin(int id,String firstName, String lastName, int age) {
        super(id,firstName, lastName, age);
    }

    public static String getVerificationCode(){return verificationCode;}

}
