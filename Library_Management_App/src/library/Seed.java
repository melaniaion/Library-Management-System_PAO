package library;

import assests.Book;
import assests.Author;
import people.Student;

import java.time.LocalDate;


public class Seed {
    public static void createLibrary(Library library){
        Student student1 = new Student("John", "Doe", 20, "Harvard", "4321", "Science Fiction");
        Student student2 = new Student("Jane", "Doe", 21, "MIT", "5678", "Mystery");
        Student student3 = new Student("Bob", "Smith", 22, "Stanford", "9012", "Romance");
        Student student4 = new Student("Alice", "Johnson", 23, "Caltech", "1234", "Fantasy");

        library.registerStudent(student1);
        library.registerStudent(student2);
        library.registerStudent(student3);
        library.registerStudent(student4);

        Author author1 = new Author("J.K. Rowling", "31 July 1965", "British", "Fantasy");
        Author author2 = new Author("Stephen King", "21 September 1947", "American", "Horror");
        Author author3 = new Author("Agatha Christie", "15 September 1890", "British", "Mystery");

        library.addAuthor(author1);
        library.addAuthor(author2);
        library.addAuthor(author3);

        Book book1 = new Book(author1, "Harry Potter and the Philosopher's Stone", 1997, 3);
        Book book2 = new Book(author1, "Harry Potter and the Chamber of Secrets", 1998, 5);
        Book book3 = new Book(author2, "The Shining", 1977, 2);
        Book book4 = new Book(author2, "IT", 1986, 4);
        Book book5 = new Book(author3, "Murder on the Orient Express", 1934, 1);

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);

        Borrowing borrowing1 = new Borrowing(student1, book1, library.librarian1);
        Borrowing borrowing2 = new Borrowing(student2, book2, library.librarian2);
        Borrowing borrowing3 = new Borrowing(student3, book3, library.librarian1);
        Borrowing borrowing4 = new Borrowing(student4, book4, library.librarian2);
        Borrowing borrowing5 = new Borrowing(student4, book5, library.librarian1);
        Borrowing borrowing6 = new Borrowing(student1, book2, library.librarian1);

        borrowing1.setDateWhenBorrowed(LocalDate.of(2022, 12, 1));
        borrowing1.setReturn_date(LocalDate.of(2022, 12, 22));
        borrowing2.setDateWhenBorrowed(LocalDate.of(2023, 3, 1));
        borrowing2.setReturn_date(LocalDate.of(2023, 3, 22));
        borrowing3.setDateWhenBorrowed(LocalDate.of(2022, 12, 15));
        borrowing3.setReturn_date(LocalDate.of(2023, 1, 5));
        borrowing4.setDateWhenBorrowed(LocalDate.of(2023, 4, 1));

        library.borrowBook(borrowing1);
        library.borrowBook(borrowing2);
        library.borrowBook(borrowing3);
        library.borrowBook(borrowing4);
        library.borrowBook(borrowing5);
        library.borrowBook(borrowing6);
    }

}
