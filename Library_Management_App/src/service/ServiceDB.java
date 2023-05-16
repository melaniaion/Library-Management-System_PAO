package service;
import assests.*;
import databaseConfig.DatabaseConfig;
import library.*;
import people.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ServiceDB {
    private static ServiceDB instance = null;
    final private Library library;
    final private Scanner input;
    AuditService auditService;
    final private Connection connection;

    private ServiceDB() {
        this.library = new Library();
        this.input = new Scanner(System.in);
        this.connection = DatabaseConfig.getDatabaseConnection();
        this.auditService = AuditService.getInstance();
    }

    public static ServiceDB getInstance() {
        if (instance == null) {
            instance = new ServiceDB();
        }
        return instance;
    }

    public void showMenu() {
        String menu = "\nThe Library\n";
        menu += "1. Show info about the library\n";
        menu += "2. Show the address\n";
        menu += "3. Search a book and check quantity\n";
        menu += "4. Show the specifications of a book\n";
        menu += "5. Check-out a book\n";
        menu += "6. Check-in a book\n";
        menu += "7. Register a student\n";
        menu += "8. Update a student's info\n";
        menu += "9. Check status (time left) regarding a checked-out book\n";
        menu += "10. Show all books borrowed by a student\n";
        menu += "! Please remember that the following options are for the admin only!\n";
        menu += "11. Add a new set of books\n";
        menu += "12. Update a set of books \n";
        menu += "13. Delete a set of books\n";
        menu += "14. Delete information about a checked-out book\n";
        menu += "15. Delete a registered student\n";
        menu += "16. Update an author\n";
        menu += "17. Delete an author\n";
        menu += "18. Update the address\n";
        menu += "19. Exit\n";
        System.out.println(menu);
    }

    public void loadDB() throws SQLException {

        // load students
        String query = "select * from students join persons on students.id = persons.id";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet res = preparedStatement.executeQuery();

        while (res.next()) {
            int id = res.getInt("id");
            String firstName = res.getString("firstName");
            String lastName = res.getString("lastName");
            int age = res.getInt("age");
            String university = res.getString("university");
            String studentIdCard = res.getString("studentIdCard");
            String favoriteGenre = res.getString("favoriteGenre");

            Student newStudent = new Student(id, firstName, lastName, age, university, studentIdCard, favoriteGenre);
            this.library.registerStudent(newStudent);

        }

        // load authors
        String query2 = "select * from authors";
        preparedStatement = connection.prepareStatement(query2);
        res = preparedStatement.executeQuery();

        while (res.next()) {
            int id = res.getInt("id");
            String name = res.getString("name");
            String birthdate = res.getString("birthdate");
            String nationality = res.getString("nationality");
            String genre = res.getString("genre");

            Author newAuthor = new Author(id, name, birthdate, nationality, genre);
            this.library.addAuthor(newAuthor);

        }

        // load books
        String query3 = "select * from books";
        preparedStatement = connection.prepareStatement(query3);
        res = preparedStatement.executeQuery();

        while (res.next()) {
            String nameBook = res.getString("name");
            int authorId = res.getInt("author_id");
            int yearOfPublication = res.getInt("yearOfPublication");
            int quantity = res.getInt("quantity");
            int serialNumber = res.getInt("serialNumber");

            Book newBook = new Book(this.library.searchAuthor(authorId), nameBook, yearOfPublication, quantity, serialNumber);
            this.library.addBook(newBook);
        }

        // load librarians
        String query4 = "select * from librarians join persons on librarians.id = persons.id";
        preparedStatement = connection.prepareStatement(query4);
        res = preparedStatement.executeQuery();

        while (res.next()) {
            int id = res.getInt("id");
            String firstName = res.getString("firstName");
            String lastName = res.getString("lastName");
            int age = res.getInt("age");
            int experience = res.getInt("experience");
            String specialization = res.getString("specialization");
            int librarianId = res.getInt("librarianId");

            Librarian newLibrarian = new Librarian(id, firstName, lastName, age, experience, specialization, librarianId);
            this.library.addLibrarian(newLibrarian);

        }

        // load borrowings
        String query5 = "select * from borrowings";
        preparedStatement = connection.prepareStatement(query5);
        res = preparedStatement.executeQuery();
        while (res.next()) {
            int BookSerialNumber = res.getInt("bookSerialNumber");
            String studentIdCard = res.getString("studentIdCard");
            int librarianId = res.getInt("librarianId");
            java.sql.Date dateB = res.getDate("dateWhenBorrowed");
            java.sql.Date dateR = res.getDate("returnedDate");
            LocalDate dateWhenBorrowed = dateB.toLocalDate();
            LocalDate returnedDate;
            if (dateR != null) {
                returnedDate = dateR.toLocalDate();
            } else {
                returnedDate = null;
            }

            int borrowingCode = res.getInt("borrowingCode");

            Borrowing newBorrowing = new Borrowing(this.library.searchStudent(studentIdCard), this.library.searchBook(BookSerialNumber),
                    this.library.searchLibrarian(librarianId), borrowingCode, dateWhenBorrowed, returnedDate);
            this.library.borrowBook(newBorrowing);
        }

        //load addresses
        preparedStatement = connection.prepareStatement("select * from addresses");
        res = preparedStatement.executeQuery();
        while (res.next()) {
            int id = res.getInt("id");
            String country = res.getString("country");
            String city = res.getString("city");
            int zipCode = res.getInt("zipCode");
            String street = res.getString("street");
            int number = res.getInt("number");

            Address newAddress = new Address(id,country, city, zipCode, street, number);
            this.library.addAddress(newAddress);
        }

        //load Admins
        preparedStatement = connection.prepareStatement("select * from admins join persons on admins.id = persons.id");
        res = preparedStatement.executeQuery();
        while (res.next()) {
            int id = res.getInt("id");
            String firstName = res.getString("firstName");
            String lastName = res.getString("lastName");
            int age = res.getInt("age");

            Admin newAdmin = new Admin(id, firstName, lastName, age);
            this.library.addAdmin(newAdmin);
        }
    }

    public void infoLibrary() {
        System.out.println(library.toString());
        auditService.writeAudit("infoLibrary");
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
                    output += "Quantity: " + book.getQuantity() + " books\n";
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
        auditService.writeAudit("checkQuantity");
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
        auditService.writeAudit("showsSpecsBook");
    }

    public Librarian findLibrarian() {
        System.out.println("Please, select a librarian (by their code) to assist you: ");
        System.out.println("Or if you want to cancel, choose '0' ");
        int chosen_librarian = 0;
        boolean check_type = false;
        while (!check_type) {
            try {
                chosen_librarian = input.nextInt();
                check_type = true;

            } catch (Exception e) {
                System.out.println("Wrong type! Try again");
                input.nextLine();
            }
        }

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
        int serial_number = 0;
        boolean check_type = false;
        while (!check_type) {
            try {
                serial_number = input.nextInt();
                check_type = true;

            } catch (Exception e) {
                System.out.println("Wrong type! Try again");
                input.nextLine();
            }
        }

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

    public void updateQuantity(Borrowing borrowing) {
        try {
            String updateQuantityAfterBorrow = "UPDATE books SET quantity=? WHERE serialNumber=?";
            PreparedStatement preparedStatementU = connection.prepareStatement(updateQuantityAfterBorrow);
            preparedStatementU.setInt(1, borrowing.getBook().getQuantity());
            preparedStatementU.setInt(2, borrowing.getBook().getSerialNumber());
            preparedStatementU.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        String insertBorrowingSql = "INSERT INTO borrowings (studentIdCard, bookSerialNumber,librarianId,dateWhenBorrowed,returnedDate,borrowingCode) VALUES(?,?,?,?,?,?)";
                        try {
                            PreparedStatement preparedStatementI = connection.prepareStatement(insertBorrowingSql);
                            preparedStatementI.setString(1, borrowing.getStudent().getStudentIdCard());
                            preparedStatementI.setInt(2, borrowing.getBook().getSerialNumber());
                            preparedStatementI.setInt(3, borrowing.getLibrarian().getLibrarianId());
                            preparedStatementI.setDate(4, java.sql.Date.valueOf(borrowing.getDateWhenBorrowed()));
                            preparedStatementI.setDate(5, null);
                            preparedStatementI.setInt(6, borrowing.getBorrowingCode());
                            preparedStatementI.executeUpdate();
                            library.borrowBook(borrowing);
                            updateQuantity(borrowing);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Perfect, done! Please remember this code for check-in: ");
                        System.out.println("Code: " + borrowing.getBorrowingCode());
                        System.out.println("See you!" + "\n");
                    } else {
                        System.out.println("Sorry, quantity is 0 for the selected book! Come back later!");
                    }
                }

            }
        } else {
            System.out.println("We couldn't find a student with the given id. ");
            System.out.println("Please consider registering or enter a valid id! ");
        }
        auditService.writeAudit("borrowABook");

    }

    public void returnBook() {
        System.out.println("Please,enter the code given when you check-out the book: ");
        int code = -1;
        boolean check_type = false;
        while (!check_type) {
            try {
                code = input.nextInt();
                check_type = true;

            } catch (Exception e) {
                System.out.println("Wrong type! Try again");
                input.nextLine();
            }
        }

        Borrowing borrowing = library.searchBorrowing(code);
        if (borrowing != null) {
            if (borrowing.getReturnedDate() == null) {
                String updateReturnedDate = "UPDATE borrowings SET returnedDate=? WHERE borrowingCode=?";
                try {
                    Book returnedBook = borrowing.getBook();
                    returnedBook.setQuantity(returnedBook.getQuantity() + 1);
                    borrowing.setReturn_date(LocalDate.now());

                    PreparedStatement preparedStatement = connection.prepareStatement(updateReturnedDate);
                    preparedStatement.setDate(1, java.sql.Date.valueOf(borrowing.getReturnedDate()));
                    preparedStatement.setInt(2, code);

                    preparedStatement.executeUpdate();
                    updateQuantity(borrowing);
                    System.out.println("The book was checked-in! Thank you!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("The book was already checked-in!");
            }
        } else {
            System.out.println("Something went wrong, try again! Maybe wrong borrowing code");
        }

        auditService.writeAudit("returnBook");

    }

    public void deleteBorrowing() {
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();
        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Please enter the code given at check-out: ");
            int codeB = -1;
            boolean check_type = false;
            while (!check_type) {
                try {
                    codeB = input.nextInt();
                    check_type = true;

                } catch (Exception e) {
                    System.out.println("Wrong type! Try again");
                    input.nextLine();
                }
            }

            if (library.searchBorrowing(codeB) != null) {
                String deleteBorrowing = "delete from borrowings where borrowingCode = ?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteBorrowing);
                    preparedStatement.setInt(1, codeB);
                    preparedStatement.executeUpdate();

                    library.deleteBorrowing(codeB);
                    System.out.println("The info has been successfully removed!");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Something went wrong, wrong code! Please, try again!");
            }

        } else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }

        auditService.writeAudit("deleteBorrowing");
    }


    public void checkTime() {
        System.out.println("Please,enter the code given when you check-out the book: ");
        int code = -1;
        boolean check_type = false;
        while (!check_type) {
            try {
                code = input.nextInt();
                check_type = true;

            } catch (Exception e) {
                System.out.println("Wrong type! Try again");
                input.nextLine();
            }
        }

        Borrowing borrowing = library.searchBorrowing(code);
        if (borrowing != null) {
            if (borrowing.getReturnedDate() == null) {
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

        auditService.writeAudit("checkTime");
    }

    public void showAllBooksStudent() {
        System.out.println("Please, enter the student id card: ");
        String student_id_card = input.nextLine();
        Student student = library.searchStudent(student_id_card);
        if (student != null) {
            List<Borrowing> allBooksStudent = library.allBorrowingsStudent(student_id_card);
            if (allBooksStudent.size() != 0) {
                Borrowing.sortBorrowingsByDate(allBooksStudent);
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

        auditService.writeAudit("showAllBooksStudent");
    }

    public Student infoStudent() {
        System.out.println("Please, enter the student's first name: ");
        String first_name = input.nextLine();

        System.out.println("Last name: ");
        String last_name = input.nextLine();

        System.out.println("Age:");
        int age = -1;
        boolean check_type = false;
        while (!check_type) {
            try {
                age = input.nextInt();
                check_type = true;

            } catch (Exception e) {
                System.out.println("Wrong type! Try again");
                input.nextLine();
            }
        }
        input.nextLine();

        if (age > 16) {
            System.out.println("University:");
            String university = input.nextLine();

            System.out.println("We will need your student id card: ");
            String student_id_card = input.nextLine();

            System.out.println("What is the genre that you are most interested in? :) ");
            String favorite_genre = input.nextLine();
            int id = -1;

            Student new_student = new Student(id, first_name, last_name, age, university, student_id_card, favorite_genre);
            return new_student;
        }
        return null;
    }

    public void registerStudent() {
        Student new_student = infoStudent();

        if (new_student != null) {
            String insertPerson = "insert into persons (firstName,lastName,age) values(?,?,?)";
            String insertStudent = "insert into students (id,university,studentIdCard,favoriteGenre) values(?,?,?,?)";
            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(insertPerson, Statement.RETURN_GENERATED_KEYS);
                preparedStatement1.setString(1, new_student.getFirstName());
                preparedStatement1.setString(2, new_student.getLastName());
                preparedStatement1.setInt(3, new_student.getAge());
                preparedStatement1.executeUpdate();
                ResultSet ids = preparedStatement1.getGeneratedKeys();
                int id = 0;
                if (ids.next()) {
                    id = ids.getInt(1);
                }

                PreparedStatement preparedStatement2 = connection.prepareStatement(insertStudent);
                preparedStatement2.setInt(1, id);
                preparedStatement2.setString(2, new_student.getUniversity());
                preparedStatement2.setString(3, new_student.getStudentIdCard());
                preparedStatement2.setString(4, new_student.getFavoriteGenre());
                preparedStatement2.executeUpdate();

                library.registerStudent(new_student);
                System.out.println("The registration was successful! ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Sorry, in order to register you must be at least 16 years old! ");
            System.out.println("You may ask an advisor to help you ");
        }

        auditService.writeAudit("registerStudent");
    }

    public void updateInfoStudent() {
        System.out.println("Please, enter the student id card used for registration: ");
        String studentIdCard = input.nextLine();
        Student student = library.searchStudent(studentIdCard);
        if (student != null) {
            System.out.println("These are the current info:\n" + student.toString());
            System.out.println("-------------------------------");
            System.out.println("- You can now start to update - \n");
            Student updatedStudent = infoStudent();
            if (updatedStudent != null) {
                String updateStudent = "UPDATE persons SET firstName=?,lastName=?, age=? WHERE id=?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(updateStudent);
                    preparedStatement.setString(1, updatedStudent.getFirstName());
                    preparedStatement.setString(2, updatedStudent.getLastName());
                    preparedStatement.setInt(3, updatedStudent.getAge());
                    preparedStatement.setInt(4, student.getId());
                    preparedStatement.executeUpdate();

                    updateStudent = "UPDATE students SET university=?,studentIdCard=?, favoriteGenre=? WHERE id=?";
                    preparedStatement = connection.prepareStatement(updateStudent);
                    preparedStatement.setString(1, updatedStudent.getUniversity());
                    preparedStatement.setString(2, updatedStudent.getStudentIdCard());
                    preparedStatement.setString(3, updatedStudent.getFavoriteGenre());
                    preparedStatement.setInt(4, student.getId());
                    preparedStatement.executeUpdate();

                    student.setFirstName(updatedStudent.getFirstName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setAge(updatedStudent.getAge());
                    student.setUniversity(updatedStudent.getUniversity());
                    student.setStudentIdCard(updatedStudent.getStudentIdCard());
                    student.setFavoriteGenre(updatedStudent.getFavoriteGenre());

                    System.out.println("The update was successful! \n");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Sorry, something wrong happened! We couldn't update the info. ");
            }

        } else {
            System.out.println("Sorry, you couldn't find the student in our system! ");
        }
        auditService.writeAudit("updateInfoStudent");
    }

    public void deleteStudent(){
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Please, enter the student id card used for registration: ");
            String studentIdCard = input.nextLine();
            Student student = library.searchStudent(studentIdCard);
            if (student != null) {
                String deleteStudent = "DELETE FROM persons WHERE id=?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteStudent);
                    preparedStatement.setInt(1, student.getId());
                    preparedStatement.executeUpdate();
                    library.deleteStudent(studentIdCard);
                    System.out.println("The deletion was successful! \n");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Sorry, you couldn't find the student in our system! ");
            }
        }
        else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }

        auditService.writeAudit("deleteStudent");
    }

    public void addBooks() {
        System.out.println("Please, enter the authorization code: ");
        String code = input.nextLine();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Name: ");
            String name = input.nextLine();

            System.out.println("Year of publication: ");
            int year = -1;
            boolean check_type = false;
            while (!check_type) {
                try {
                    year = input.nextInt();
                    check_type = true;

                } catch (Exception e) {
                    System.out.println("Wrong type! Try again");
                    input.nextLine();
                }
            }
            input.nextLine();

            System.out.println("Quantity: ");
            int quantity = -1;
            check_type = false;
            while (!check_type) {
                try {
                    quantity = input.nextInt();
                    check_type = true;

                } catch (Exception e) {
                    System.out.println("Wrong type! Try again");
                    input.nextLine();
                }
            }
            input.nextLine();


            System.out.println("Now add details about the author or type 'select' to choose one: ");
            System.out.println("Name: ");
            String option = input.nextLine();
            String addAuthor = "insert into authors (name,birthdate,nationality,genre) values(?,?,?,?)";
            String addBook = "insert into books (author_id,name,yearOfPublication,quantity,serialNumber) values(?,?,?,?,?)";

            if (!Objects.equals(option, "select")) {
                System.out.println("Birthdate: ");
                String birthdate = input.nextLine();

                System.out.println("Nationality: ");
                String nationality = input.nextLine();

                System.out.println("Genre: ");
                String genre = input.nextLine();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(addAuthor, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1,option);
                    preparedStatement.setString(2,birthdate);
                    preparedStatement.setString(3,nationality);
                    preparedStatement.setString(4,genre);
                    preparedStatement.executeUpdate();
                    ResultSet ids = preparedStatement.getGeneratedKeys();
                    int id = 0;
                    if (ids.next()) {
                        id = ids.getInt(1);
                    }
                    Author author = new Author(id,option, birthdate, nationality, genre);
                    library.addAuthor(author);

                    Book book = new Book(author, name, year, quantity);
                    preparedStatement = connection.prepareStatement(addBook);
                    preparedStatement.setInt(1,id);
                    preparedStatement.setString(2,name);
                    preparedStatement.setInt(3,year);
                    preparedStatement.setInt(4,quantity);
                    preparedStatement.setInt(5,book.getSerialNumber());
                    preparedStatement.executeUpdate();
                    library.addBook(book);
                }
                catch (SQLException e){
                    e.printStackTrace();
                }

            } else {
                Set<Author> authors = library.getAuthors();
                int i = 1;
                for (Author author : authors) {
                    System.out.println(i + ". " + author.toString());
                    i++;
                }
                System.out.println("Choose an author by id: ");
                int authorId = -1;
                check_type = false;
                while (!check_type) {
                    try {
                        authorId = input.nextInt();
                        check_type = true;

                    } catch (Exception e) {
                        System.out.println("Wrong type! Try again");
                        input.nextLine();
                    }
                }
                input.nextLine();
                Author author = library.searchAuthor(authorId);
                try {
                    Book book = new Book(author, name, year, quantity);
                    PreparedStatement preparedStatement = connection.prepareStatement(addBook);
                    preparedStatement.setInt(1,authorId);
                    preparedStatement.setString(2,name);
                    preparedStatement.setInt(3,year);
                    preparedStatement.setInt(4,quantity);
                    preparedStatement.setInt(5,book.getSerialNumber());
                    preparedStatement.executeUpdate();
                    library.addBook(book);
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            System.out.println("Book added! 😊");
        } else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }

        auditService.writeAudit("addBooks");
    }

    public void updateBooks(){
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Enter the serial number: ");
            int number = -1;
            boolean check_type = false;
            String updateBook = "update books set name = ?, yearOfPublication = ?, quantity=? where serialNumber = ?";
            while (!check_type) {
                try {
                    number = input.nextInt();
                    check_type = true;

                } catch (Exception e) {
                    System.out.println("Wrong type! Try again");
                    input.nextLine();
                }
            }
            input.nextLine();
            Book book = library.searchBook(number);
            if (book != null) {
                System.out.println("Name: ");
                String name = input.nextLine();

                System.out.println("Year of publication: ");
                int year = -1;
                check_type = false;
                while (!check_type) {
                    try {
                        year = input.nextInt();
                        check_type = true;

                    } catch (Exception e) {
                        System.out.println("Wrong type! Try again");
                        input.nextLine();
                    }
                }
                input.nextLine();

                System.out.println("Quantity: ");
                int quantity = -1;
                check_type = false;
                while (!check_type) {
                    try {
                        quantity = input.nextInt();
                        check_type = true;

                    } catch (Exception e) {
                        System.out.println("Wrong type! Try again");
                        input.nextLine();
                    }
                }
                input.nextLine();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(updateBook);
                    preparedStatement.setString(1,name);
                    preparedStatement.setInt(2,year);
                    preparedStatement.setInt(3,quantity);
                    preparedStatement.setInt(4, number);
                    preparedStatement.executeUpdate();

                    book.setName(name);
                    book.setYearOfPublication(year);
                    book.setQuantity(quantity);

                    System.out.println("The book was updated!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Wrong serial number!");
            }
        }
        else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }

        auditService.writeAudit("updateBooks");
    }

    public void deleteBooks() {
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Enter the serial number: ");
            int number = -1;
            boolean check_type = false;
            String deleteBook = "delete from books where serialNumber = ?";
            while (!check_type) {
                try {
                    number = input.nextInt();
                    check_type = true;

                } catch (Exception e) {
                    System.out.println("Wrong type! Try again");
                    input.nextLine();
                }
            }
            Book book = library.searchBook(number);
            if (book != null) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteBook);
                    preparedStatement.setInt(1, number);
                    preparedStatement.executeUpdate();
                    library.deleteBook(number);
                    System.out.println("The book was removed! ");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Wrong serial number!");
            }
        }
            else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }

        auditService.writeAudit("deleteBooks");
    }

    public void updateAuthor(){
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Please, select the author's id you want to update");
            int id = -1;
            boolean check_type = false;
            while (!check_type) {
                try {
                    id = input.nextInt();
                    check_type = true;

                } catch (Exception e) {
                    System.out.println("Wrong type! Try again");
                    input.nextLine();
                }
            }
            input.nextLine();
            Author author = library.searchAuthor(id);
            if (author != null) {
                String updateAuthor = "update authors set name = ?, birthdate = ?, nationality = ?, genre = ? where id = ?";
                System.out.println("Name: ");
                String name = input.nextLine();

                System.out.println("Birthdate: ");
                String birthdate = input.nextLine();

                System.out.println("Nationality: ");
                String nationality = input.nextLine();

                System.out.println("Genre: ");
                String genre = input.nextLine();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(updateAuthor);
                    preparedStatement.setString(1,name);
                    preparedStatement.setString(2,birthdate);
                    preparedStatement.setString(3,nationality);
                    preparedStatement.setString(4,genre);
                    preparedStatement.setInt(5, id);
                    preparedStatement.executeUpdate();

                    author.setName(name);
                    author.setBirthdate(birthdate);
                    author.setNationality(nationality);
                    author.setGenre(genre);

                    System.out.println("Author's info updated!");


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("There's no author with the given id.");
            }
        }
        else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }
        auditService.writeAudit("updateAuthor");
    }

    public void deleteAuthor(){
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("Please, select the author's id you want to delete");
            int id = -1;
            boolean check_type = false;
            while (!check_type) {
                try {
                    id = input.nextInt();
                    check_type = true;

                } catch (Exception e) {
                    System.out.println("Wrong type! Try again");
                    input.nextLine();
                }
            }
            Author author = library.searchAuthor(id);
            if (author != null) {
                String deleteAuthor = "delete from authors where id = ?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteAuthor);
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    library.deleteAuthor(author);
                    System.out.println("Author removed!");


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("There's no author with the given id.");
            }
        }
        else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }
        auditService.writeAudit("deleteAuthor");
    }

    public void showAddress(){
        library.printAddress();
        auditService.writeAudit("showAddress");
    }

    public int check_type(){
        int var = -1;
        boolean check_type = false;
        while (!check_type) {
            try {
                var = input.nextInt();
                check_type = true;

            } catch (Exception e) {
                System.out.println("Wrong type! Try again");
                input.nextLine();
            }
        }
        input.nextLine();
        return var;
    }

    public void updateAddress(){
        System.out.println("Please, enter the authorization code: ");
        String code = input.next();

        if (Objects.equals(code, Admin.getVerificationCode())) {
            System.out.println("This is the current address:");
            library.printAddress();

            input.nextLine();
            System.out.println("Update the country:");
            String country = input.nextLine();

            System.out.println("Update the city:");
            String city = input.nextLine();

            System.out.println("Update the street:");
            String street = input.nextLine();

            System.out.println("Update the number:");
            int number = check_type();

            System.out.println("Update the zip code");
            int zipCode = check_type();

            String updateAddress = "update addresses set country = ?, city = ?, zipCode = ?, street = ?, number = ? where id = ? ";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(updateAddress);
                preparedStatement.setString(1, country);
                preparedStatement.setString(2, city);
                preparedStatement.setInt(3, zipCode);
                preparedStatement.setString(4, street);
                preparedStatement.setInt(5, number);
                preparedStatement.setInt(6, 2);
                preparedStatement.executeUpdate();

                library.updateAddress(2, country, city, zipCode, street, number);
                System.out.println("Address updated!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Sorry, access denied! Only authorised persons allowed.");
        }

        auditService.writeAudit("updateAddress");
    }
}
