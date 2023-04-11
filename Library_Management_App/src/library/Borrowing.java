package library;

import assests.Book;
import people.Librarian;
import people.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Borrowing {
    protected Student student;
    protected Book book;
    protected Librarian librarian;

    protected LocalDate DateWhenBorrowed;
    protected LocalDate returnedDate;
    private static final AtomicInteger sequence = new AtomicInteger();
    protected int borrowingCode;

    public Borrowing(){}

    public Borrowing(Student student, Book book, Librarian librarian) {
        this.student = student;
        book.setQuantity(book.getQuantity() - 1);
        this.book = book;
        this.librarian = librarian;
        this.DateWhenBorrowed = LocalDate.now();
        this.returnedDate = null;
        this.borrowingCode = sequence.incrementAndGet();
    }

    public Student getStudent() {
        return student;
    }

    public Book getBook() {
        return book;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public LocalDate getDateWhenBorrowed() {
        return DateWhenBorrowed;
    }

    public LocalDate getReturn_date() {
        return returnedDate;
    }

    public int getBorrowingCode() {
        return borrowingCode;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public void setDateWhenBorrowed(LocalDate dateWhenBorrowed) {
        this.DateWhenBorrowed = dateWhenBorrowed;
    }

    public void setReturn_date(LocalDate return_date) {
        this.returnedDate = return_date;
    }

    public void setBorrowingCode(int borrowingCode) {
        this.borrowingCode = borrowingCode;
    }

    @Override
    public String toString()
    {
        String output = "Book: " + this.book.getName() + " \n";
        output += "Book serial number: " + this.book.getSerialNumber() + "\n";
        output += "Librarian: " + this.librarian.getFirstName() + " " + this.librarian.getLastName() + " with code: " + this.librarian.getLibrarianId() + "\n";
        DateTimeFormatter myformatDateB = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateB = this.DateWhenBorrowed.format(myformatDateB);
        output += "The book was borrowed on: " + formattedDateB + "\n";
        if (returnedDate != null){
            DateTimeFormatter myformatDateR = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDateR = this.returnedDate.format(myformatDateR);
            output += "The book was returned on: " + formattedDateR + "\n";
        }
        else{
            output += "The book hasn't been returned yet!\n";
        }
        output += "Code: " + this.borrowingCode + "\n";

        return output;
    }

    public static void sortBorrowingsByDate(List<Borrowing> borrowings) {
        Comparator<Borrowing> comparator = new Comparator<Borrowing>() {
            @Override
            public int compare(Borrowing b1, Borrowing b2) {
                return b1.getDateWhenBorrowed().compareTo(b2.getDateWhenBorrowed());
            }
        };
        Collections.sort(borrowings, comparator);
    }
}
