package main;

//import service.Service;
import databaseConfig.DatabaseConfig;
import service.ServiceDB;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServiceDB service = ServiceDB.getInstance();

        try{
            service.loadDB();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        int option = 1;
        Scanner input = new Scanner(System.in);
        while (option != 19) {
            service.showMenu();
            System.out.print("Please, select an option: ");
            option = service.check_type();

            switch (option) {
                case 1 -> service.infoLibrary();
                case 2 -> service.showAddress();
                case 3 -> service.checkQuantity();
                case 4 -> service.showsSpecsBook();
                case 5 -> service.borrowABook();
                case 6 -> service.returnBook();
                case 7 -> service.registerStudent();
                case 8 -> service.updateInfoStudent();
                case 9 -> service.checkTime();
                case 10 -> service.showAllBooksStudent();
                case 11 -> service.addBooks();
                case 12 -> service.updateBooks();
                case 13 -> service.deleteBooks();
                case 14 -> service.deleteBorrowing();
                case 15 -> service.deleteStudent();
                case 16 -> service.updateAuthor();
                case 17 -> service.deleteAuthor();
                case 18 -> service.updateAddress();
                case 19 -> {}
                default -> System.out.print("Invalid option, please try again! \n\n");
            }
        }
        System.out.print("Bye! Have a nice day! \n");
        DatabaseConfig.closeDatabaseConnection();
    }
}