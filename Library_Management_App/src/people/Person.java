package people;

public class Person {
    protected String firstName;
    protected String lastName;
    protected int age;

    public Person(){}
    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString()
    {
        String output = "- Info about the person -\n";
        output += "First Name: " + this.firstName + "\n";
        output += "Last Name: " + this.lastName + "\n";
        output += "Age: " + this.age + " years\n";

        return output;
    }
}
