package library;

import assests.Author;
import assests.Book;
import people.Admin;
import people.Librarian;
import people.Student;

import java.util.*;

public class Library {
    List<Borrowing> borrowings;
    Set<Student> students;
    Set<Address> addresses;
    Set<Author> authors;
    Set<Book> books;
    List<Librarian> librarians;
    List<Admin> admins;

    public  Library(){
        books = new HashSet<>();
        students = new HashSet<>();
        borrowings = new ArrayList<>();
        admins = new ArrayList<>();
        librarians = new ArrayList<>();
        authors = new HashSet<>();
        addresses = new HashSet<>();
    }

    public Library(List<Borrowing> borrowings, Set<Student> students, Set<Address> addresses, Set<Author> authors, Set<Book> books,List<Librarian> librarians, List<Admin> admins) {
        this.borrowings = borrowings;
        this.students = students;
        this.addresses = addresses;
        this.authors = authors;
        this.books = books;
        this.librarians = librarians;
        this.admins = admins;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public void setLibrarians(List<Librarian> librarians) {
        this.librarians = librarians;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public void setBorrowings(List<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void setLibrarians(ArrayList<Librarian> librarians) {
        this.librarians = librarians;
    }

    public List<Borrowing> getBorrowings() {
        return borrowings;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public List<Librarian> getLibrarians() {
        return librarians;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString(){
        String output = "-- Details about the library --\n\n";

        if(addresses.size() == 0){
            output += "We don't have a location yet. \n";
        }
        else{
            for(Address address : addresses){
                output += address.toString() + "\n\n";
            }
        }
        if(librarians.size() == 0){
            output += "We haven't hired librarians yet. \n";
        }
        else{
            for(Librarian librarian : librarians){
                output += librarian.toString() + "\n\n";
            }
        }

        if(books.size() == 0){
            output += "We have 0 books at this moment.\n";
        }
        else{
            int i = 1;
            output += "Library books: \n";
            for(Book book : books){
                output += i + ". "  + book.toString() + "\n";
                i++;
            }
        }
        output += "\n";
        if(students.size() == 0){
            output += "There is no student registered. \n";
        }
        else{

            output += "Registered students: \n";
            for(Student student : students){
                output += student.toString() + "\n";
            }
        }
        output += "\n";
        if(borrowings.size() == 0){
            output += "There was no book checked-out. \n";
        }
        else{
            int j = 1;
            output += "Info about books that were borrowed:  \n";
            for(Borrowing borrowing : borrowings){
                output += "Borrowing no." + j + "\n";
                output += "Student's first name: " + borrowing.student.getFirstName() + "\n";
                output += "Student's last Name: " + borrowing.student.getLastName()+ "\n";
                output += borrowing.toString() + "\n";
                j++;
            }
        }
        output += "Important: Please,remember that a book shouldn't be check-out more than 3 weeks! \n";
        output += "Otherwise, please inform a librarian about your case! \n";

        return output;
    }

    public void addBook(Book new_book){
        books.add(new_book);
    }

    public void addAuthor(Author author){
        authors.add(author);
    }

    
    public List<Book> searchBook(String book_name){
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books){
            if (book_name.equals(book.getName())){
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    public Book searchBook(int serial_number){
        for (Book book : books) {
            if (serial_number == book.getSerialNumber()) {
                return book;
            }
        }
        return null;
    }

    public void deleteBook(int serial_number){
        Book book = searchBook(serial_number);
        books.remove(book);

    }

    public void deleteStudent(String studentIdCard){
        Student student = searchStudent(studentIdCard);
        students.remove(student);
    }

    public void borrowBook(Borrowing borrowing){
        borrowings.add(borrowing);
    }

    public Borrowing searchBorrowing(int code){
        for (Borrowing borrowing : borrowings){
            if(code == borrowing.getBorrowingCode()){
                return borrowing;
            }
        }
        return null;
    }

    public void deleteBorrowing(int code){
        Borrowing borrowing = searchBorrowing(code);
        borrowings.remove(borrowing);
    }

    public void registerStudent(Student student){
        students.add(student);
    }

    public void addLibrarian(Librarian librarian){
        librarians.add(librarian);
    }

    public Student searchStudent(String card){
        for (Student student : students){
            if (card.equals(student.getStudentIdCard())){
                return student;
            }
        }
        return null;
    }

    public List<Borrowing> allBorrowingsStudent(String student_code){
        List<Borrowing> allBooksStudent = new ArrayList<>();
        for (Borrowing borrowing : borrowings){
            if(student_code.equals(borrowing.student.getStudentIdCard())){
                allBooksStudent.add(borrowing);
            }
        }
        return allBooksStudent;
    }

    public Librarian searchLibrarian(int librarian_code){
        for (Librarian librarian : librarians){
            if (librarian_code == librarian.getLibrarianId()){
                return librarian;
            }
        }
        return null;
    }

    public Author searchAuthor(String name){
        for (Author author : authors){
            if (name.equals(author.getName())){
                return author;
            }
        }
        return null;
    }

    public Author searchAuthor(int id){
        for (Author author : authors){
            if (id == author.getId()){
                return author;
            }
        }
        return null;
    }

    public void deleteAuthor(Author author){
        authors.remove(author);
    }

    public void addAdmin(Admin admin) {admins.add(admin);}
    public void addAddress(Address address) {addresses.add(address);}
    public void printAddress(){
        for (Address address : addresses){
            System.out.println(address.toString());
        }
    }

    public void updateAddress(int id,String country,String city,int zipCode,String street,int number){
        for (Address address :addresses){
            if (id == address.getId()){
                address.setCountry(country);
                address.setCity(city);
                address.setZipCode(zipCode);
                address.setNumber(number);
                address.setStreet(street);
            }
        }
    }

}
