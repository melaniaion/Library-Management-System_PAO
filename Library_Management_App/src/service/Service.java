package service;

import assests.Author;
import assests.Book;
import people.Admin;
import people.Librarian;
import people.Student;
import library.Borrowing;
import library.Library;
import library.Seed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Service {
    private static Service instance = null;
    final private Library library;
    final private Scanner input;

    private Service() {
        this.library = new Library();
        this.input = new Scanner(System.in);
        Seed.createLibrary(library);
    }
    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void showMenu() {
        String menu = "library\n";
        menu += "1. Show info about the library\n";
        menu += "2. Search a book and check quantity\n";
        menu += "3. Show the specifications of a book\n";
        menu += "4. Check-out a book\n";
        menu += "5. Check-in a book\n";
        menu += "6. Register a student\n";
        menu += "7. Update a student's info\n";
        menu += "8. Check status (time left) regarding a checked-out book\n";
        menu += "9. Show all books borrowed by a student\n";
        menu += "! Please remember that the following options are for the admin only!\n";
        menu += "10. Add a new set of books\n";
        menu += "11. Delete a set of books\n";
        menu += "12. Delete information about a checked-out book\n";
        menu += "13. Exit\n";
        System.out.println(menu);
    }

    public void infoLibrary() {
        System.out.println(library.toString());
    }

    public void checkQuantity() {
        System.out.println("What's the name of the book?");
        String book_name = input.nextLine();
        List<Book> books = library.searchBook(book_name);
        if (books.size() != 0) {
            for (Book book : books) {
                if (book.getQuantity() != 0) {
                    String output = "Book name: " + book.getName() + " by " + book.getAuthor().getName() + "\n";
                    output += "Serial number: " + book.getSerialNumber() + "\n";
                    output += "Has quantity of: " + book.getQuantity() + " books\n";
                    System.out.println(output);
                } else {
                    String output = "Book name: " + book.getName() + " by " + book.getAuthor().getName() + "\n";
                    output += "Serial number: " + book.getSerialNumber() + "\n";
                    output += "Sorry, all the books with that title are checked-out!\n";
                    output += "Come back later :(";
                    System.out.println(output);

                }

            }
        } else {
            String output = "Sorry, we don't have a book with that title! \n";
            System.out.println(output);
        }
    }

    public void showsSpecsBook() {
        System.out.println("What book are you interested in?");
        String book_name2 = input.nextLine();
        List<Book> books = library.searchBook(book_name2);
        if (books.size() != 0) {
            System.out.println("Here are all the books with that title:");
            for (Book book : books) {
                System.out.println(book.toString());
            }
        } else {
            String output = "Sorry, we don't have a book with that title! \n";
            System.out.println(output);
        }
    }

    public Student infoStudent() {
        System.out.println("Please, enter the student's first name: ");
        String first_name = input.nextLine();

        System.out.println("Last name: ");
        String last_name = input.nextLine();

        System.out.println("Age:");
        int age = input.nextInt();
        input.nextLine();

        if (age > 16) {
            System.out.println("University:");
            String university = input.nextLine();

            System.out.println("We will need your student id card: ");
            String student_id_card = input.nextLine();

            System.out.println("What is the genre that you are most interested in? :) ");
            String favorite_genre = input.nextLine();

            Student new_student = new Student(first_name, last_name, age, university, student_id_card, favorite_genre);
            return new_student;
        }
        return null;
    }

    public void registerStudent() {
        Student new_student = infoStudent();

        if (new_student != null) {
            library.registerStudent(new_student);
            System.out.println("The registration was successful! ");
        } else {
            System.out.println("Sorry, in order to register you must be at least 16 years old! ");
            System.out.println("You may ask an advisor to help you ");
        }
    }

    public void updateInfoStudent() {
        System.out.println("Please, enter the student id card used for registration: ");
        String student_id_card = input.nextLine();
        Student student = library.searchStudent(student_id_card);
        if (student != null) {
            System.out.println("These are the current info:\n" + student.toString());
            System.out.println("-------------------------------");
            System.out.println("- You can now start to update - \n");
            Student updated_student = infoStudent();
            if (updated_student != null) {
                student.setFirstName(updated_student.getFirstName());
                student.setLastName(updated_student.getLastName());
                student.setAge(updated_student.getAge());
                student.setUniversity(updated_student.getUniversity());
                student.setStudentIdCard(updated_student.getStudentIdCard());
                student.setFavoriteGenre(updated_student.getFavoriteGenre());
                System.out.println("The update was successful! \n");
            } else {
                System.out.println("Sorry, something wrong happened! We couldn't update the info. ");
            }

        } else {
            System.out.println("Sorry, you couldn't find the student in our system! ");
        }
    }

    public void showAllBooksStudent() {
        System.out.println("Please, enter the student id card: ");
        String student_id_card = input.nextLine();
        List<Borrowing> allBooksStudent = library.allBorrowingsStudent(student_id_card);
        Borrowing.sortBorrowingsByDate(allBooksStudent);
        Student student = library.searchStudent(student_id_card);
        if (student != null) {
            if (allBooksStudent.size() != 0) {
                System.out.print("Student's first name: " + student.getFirstName() + "\n");
                System.out.print("Student's last name: " + student.getLastName() + "\n\n");
                int i = 1;
                for (Borrowing borrowing : allBooksStudent) {
                    System.out.println(i + ". " + borrowing.toString());
                    i++;
                }
            } else {
                System.out.println("There was no book borrowed by the student with id: " + student_id_card);
            }
        } else {
            System.out.println("We couldn't find a student with the given id.");
        }
    }

    public Librarian findLibrarian() {
        System.out.println("Please, select a librarian (by their code) to assist you: ");
        System.out.println("Or if you want to cancel, choose '0' ");
        int chosen_librarian = input.nextInt();
        if (chosen_librarian != 0) {
            Librarian librarian = library.searchLibrarian(chosen_librarian);
            if (librarian != null) {
                return librarian;
            } else {
                System.out.println("Sorry, wrong code!");
                findLibrarian();
            }
        }
        return null;
    }

    public Book findBook() {
        System.out.println("Please, tell me the book serial number.  ");
        System.out.println("If you don't know it, you'll find it in book's specifications ");
        System.out.println("Enter '0' to return to menu or enter the serial number: ");
        int serial_number = input.nextInt();
        if (serial_number != 0) {
            Book book = library.searchBook(serial_number);
            if (book != null) {
                return book;
            } else {
                System.out.println("The serial number was wrong! ");
                findBook();
            }
        }
        return null;

    }

    public void borrowABook() {
        System.out.println("Please, enter your student id card: ");
        String student_id = input.nextLine();
        Student student = library.searchStudent(student_id);
        if (student != null) {
            List<Librarian> librarians = library.getLibrarians();
            System.out.println("The librarians that can help you: ");
            for (Librarian librarian : librarians) {
                System.out.println(librarian.toString());
            }
            Librarian librarian = findLibrarian();
            if (librarian != null) {
                System.out.println("Hi! My name is " + librarian.getFirstName());
                System.out.println("We have lots of books to choose from as you've already noticed!");
                Book book = findBook();
                if (book != null) {
                    if (book.getQuantity() > 0) {
                        Borrowing borrowing = new Borrowing(student, book, librarian);
                        library.borrowBook(borrowing);

                        System.out.println("Perfect, done! Please remember this code for check-in: ");
                        System.out.println("Code: " + borrowing.getBorrowingCode() + "\n");
                        System.out.println("See you!");
                    } else {
                        System.out.println("Sorry, quantity is 0 for the selected book! Come back later!");
                    }
                }

            }
        } else {
            System.out.println("We couldn't find a student with the given id. ");
            System.out.println("Please consider registering or enter a valid id! ");
        }

    }

    public void returnBook() {
        System.out.println("Please,enter the code given when you check-out the book: ");
        int code = input.nextInt();
        Borrowing borrowing = library.searchBorrowing(code);
        if (borrowing != null) {
            if (borrowing.getReturn_date() == null) {
                Book returnedBook = borrowing.getBook();
                returnedBook.setQuantity(returnedBook.getQuantity() + 1);
                borrowing.setReturn_date(LocalDate.now());
                System.out.println("The book was checked-in! Thank you!");
            } else {
                System.out.println("The book was already checked-in!");
            }
        } else {
            System.out.println("Something went wrong, try again!");
        }

    }

    public void checkTime() {
        System.out.println("Please,enter the code given when you check-out the book: ");
        int code = input.nextInt();
        Borrowing borrowing = library.searchBorrowing(code);
        if (borrowing != null) {
            if (borrowing.getReturn_date() == null) {
                LocalDate should_return = borrowing.getDateWhenBorrowed().plus(3, ChronoUnit.WEEKS);
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), should_return);
                if (daysLeft < 0) {
                    DateTimeFormatter myFormatDateR = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String formattedDateR = should_return.format(myFormatDateR);
                    System.out.println("Late! The book should've been already checked-in on " + formattedDateR);
                } else {
                    System.out.println("There are " + daysLeft + " days left.");
                    System.out.println("The book was check-out on: " + borrowing.getDateWhenBorrowed());
                }
            } else {
                System.out.println("The book was already checked-in!");
            }
        } else {
            System.out.println("Something went wrong, try again!");
        }

    }

    public void addBooks() {
        System.out.println("Please, enter the authorization code: ");
        String code = input.nextLine();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Name: ");
            String name = input.nextLine();

            System.out.println("Year of publication: ");
            int year = input.nextInt();
            input.nextLine();

            System.out.println("Quantity: ");
            int quantity = input.nextInt();
            input.nextLine();


            System.out.println("Now add details about the author or type 'select' to choose one: ");
            System.out.println("Name: ");
            String option = input.nextLine();

            if (!Objects.equals(option, "select")) {
                System.out.println("Birthdate: ");
                String birthdate = input.nextLine();

                System.out.println("Nationality: ");
                String nationality = input.nextLine();

                System.out.println("Genre: ");
                String genre = input.nextLine();

                Author author = new Author(option, birthdate, nationality, genre);
                library.addAuthor(author);

                Book book = new Book(author, name, year, quantity);
                library.addBook(book);
            } else {
                Set<Author> authors = library.getAuthors();
                int i = 1;
                for (Author author : authors) {
                    System.out.println(i + ". " + author.toString());
                }
                System.out.println("Choose an author by name: ");
                String nameA = input.nextLine();
                Author author = library.searchAuthor(nameA);

                Book book = new Book(author, name, year, quantity);
                library.addBook(book);
            }
            System.out.println("Book added! 😊");
        } else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }
    }

    public void deleteBooks() {
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();
        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Enter the serial number: ");
            int number = input.nextInt();
            library.deleteBook(number);
            System.out.println("The book was removed! ");
        } else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }
    }

    public void deleteBorrowing() {
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();
        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Please enter the code given at check-out: ");
            int codeB = input.nextInt();
            library.deleteBorrowing(codeB);
            System.out.println("The info has been successfully removed!");
        } else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }
    }

}
