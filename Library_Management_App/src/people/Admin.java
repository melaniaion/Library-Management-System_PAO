package people;

public class Admin extends Person{
    private final static String verificationCode = "admin01";
    private static int counter = 123;
    private int adminId;

    public Admin(){
        firstName = "Melania";
        lastName = "Ion";
        age = 20;
        adminId = ++counter;
    }

    public Admin(String firstName, String lastName, int age) {
        super(firstName, lastName, age);
        this.adminId = ++counter;
    }

    public static int getCounter() {
        return counter;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
    public static String getVerificationCode(){return verificationCode;}

}
