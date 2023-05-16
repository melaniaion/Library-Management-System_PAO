package people;

public class Librarian extends Person{
    protected int experience;
    protected String specialization;
    protected int librarianId;
    protected static int counter = 0;

    public Librarian(){}

    public Librarian(int id,String firstName, String lastName, int age, int experience, String specialization) {
        super(id,firstName, lastName, age);
        this.experience = experience;
        this.specialization = specialization;
        counter++;
        this.librarianId = (age * 10) + counter;
    }
    public Librarian(int id,String firstName, String lastName, int age, int experience, String specialization,int librarianId) {
        super(id,firstName, lastName, age);
        this.experience = experience;
        this.specialization = specialization;
        counter++;
        this.librarianId = librarianId;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(int librarianId) {
        this.librarianId = librarianId;
    }

    @Override
    public String toString()
    {
        String output = "- Info about the librarian " + "\n";
        output += "Librarian code: " + this.librarianId + "\n";
        output += "First Name: " + this.firstName + "\n";
        output += "Last Name: " + this.lastName + "\n";
        output += "Age: " + this.age + " years\n";
        output += "Exeprience: " + this.experience + " years\n";
        output += "Specialization: " + this.specialization + "\n";

        return output;
    }
}
