package people;

public class Student extends Person{
    protected String university;
    private String studentIdCard;
    private String favoriteGenre;

    public Student(){}
    public Student(int id, String firstName, String lastName, int age, String university, String studentIdCard, String favoriteGenre) {
        super(id,firstName, lastName, age);
        this.university = university;
        this.studentIdCard = studentIdCard;
        this.favoriteGenre = favoriteGenre;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getStudentIdCard() {
        return studentIdCard;
    }

    public void setStudentIdCard(String studentIdCard) {
        this.studentIdCard = studentIdCard;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(String favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }

    @Override
    public String toString() {
        String output = "- Info about the student -\n";
        output += "First Name: " + this.firstName + "\n";
        output += "Last Name: " + this.lastName + "\n";
        output += "Age: " + this.age + " years\n";
        output += "University: " + this.university + "\n";
        output += "Student id card: " + this.studentIdCard + "\n";
        output += "Favorite genre: " + this.favoriteGenre + "\n";

        return output;
    }
}
