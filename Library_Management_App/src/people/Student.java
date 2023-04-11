package people;

public class Student extends Person{
    protected String university;
    private String studentIdCard;
    private String favoriteGenre;

    public Student(){}
    public Student(String firstName, String lastName, int age, String university, String student_id_card, String favorite_genre) {
        super(firstName, lastName, age);
        this.university = university;
        this.studentIdCard = student_id_card;
        this.favoriteGenre = favorite_genre;
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
        output += " Favorite genre: " + this.favoriteGenre + "\n";

        return output;
    }
}
