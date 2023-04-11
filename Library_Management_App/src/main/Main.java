package main;

import service.Service;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Service service = Service.getInstance();

        int option = 1;
        Scanner input = new Scanner(System.in);
        while (option != 13) {
            service.showMenu();
            System.out.print("Please, select an option: ");
            option = input.nextInt();

            switch (option) {
                case 1 -> service.infoLibrary();
                case 2 -> service.checkQuantity();
                case 3 -> service.showsSpecsBook();
                case 4 -> service.borrowABook();
                case 5 -> service.returnBook();
                case 6 -> service.registerStudent();
                case 7 -> service.updateInfoStudent();
                case 8 -> service.checkTime();
                case 9 -> service.showAllBooksStudent();
                case 10 -> service.addBooks();
                case 11 -> service.deleteBooks();
                case 12 -> service.deleteBorrowing();
                default -> System.out.print("Invalid option, please try again! \n\n");
            }
        }
        System.out.print("Bye! Have a nice day! \n");
    }
}