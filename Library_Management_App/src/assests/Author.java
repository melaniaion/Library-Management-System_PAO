package assests;

public class Author {
    protected  int id;

    protected String name;
    protected String birthdate;
    protected String nationality;

    protected String genre;

    public Author(){

    }
    public Author(int id,String name, String birthdate, String nationality, String genre) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.nationality = nationality;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString()
    {
        String output = "- Info about the author -\n";
        output += "--------------------------------------\n";
        output += "Author's id: " + this.id + "\n";
        output += "Author's name: " + this.name + "\n";
        output += "Date of birth: " + this.birthdate + "\n";
        output += "Nationality: " + this.nationality + "\n";
        output += "Genre: " + this.genre + "\n";

        return output;
    }
}
