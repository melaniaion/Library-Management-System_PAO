package assests;

public class Book {
    Author author;
    protected String name;
    protected int yearOfPublication;

    private static final int serialGenerator = 200;
    protected int serialNumber;
    protected int quantity;

    public Book(){}

    public Book(Author author, String name, int year_of_publication, int quantity) {
        this.author = author;
        this.name = name;
        this.yearOfPublication = year_of_publication;
        this.quantity = quantity;
        serialNumber = (serialGenerator + year_of_publication) * 10;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString()
    {
        String output = "- Info about the book -\n";
        output += "--------------------------------------\n";
        output += "Name: " + this.name + "\n";
        output += "Author: " + this.author.getName() + "\n";
        output += "Originally published in: " +  this.yearOfPublication + "\n";
        output += "Serial number of the set: " + this.serialNumber + "\n";

        return output;
    }
}
