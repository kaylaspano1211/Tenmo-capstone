package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);


    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        System.out.println("\nYour current balance is: $" + balance);
    }

    public void printFilteredList(List<User> filteredUserList) {
        System.out.println("-------------------------------------------");
        System.out.printf("%-5s %-5s\n", "UserId", "Username");
        System.out.println("-------------------------------------------");
        for (User user : filteredUserList) {
            System.out.printf("%-5d %-5s\n", user.getId(), user.getUsername());
        }
        System.out.println("---------");
    }

    public void printTransferList(List<Transfer> transferList, String username) {
        System.out.println("-------------------------------------------");
        System.out.printf("%-5s %14s %12s\n", "Transfers \nID", "From/To", "Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transferList) {
            if (username.equals(transfer.getUsernameFrom())) {
                System.out.printf("%-8d %8s %-5s $%-5.2f\n", transfer.getTransferId(), "To: ", transfer.getUsernameTo(), transfer.getAmount());
            } else {
                System.out.printf("%-8d %8s %-5s $%-5.2f\n", transfer.getTransferId(), "From: ", transfer.getUsernameFrom(), transfer.getAmount());
            }
        }
        System.out.println("---------");
    }

    public int retrieveTransferId(String prompt){
        System.out.println("Please enter transfer ID to view details (0 to cancel): ");

        return Integer.parseInt(scanner.nextLine());
    }

    public void printTransferDetails(Transfer transfer){
        System.out.println("-------------------------------------------");
        System.out.println("\nTransfer Details\n");
        System.out.println("-------------------------------------------");
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + transfer.getUsernameFrom());
        System.out.println("To: " + transfer.getUsernameTo());
        System.out.println("Type: Send");
        System.out.println("Status: Approved");
        System.out.println("Amount: " + transfer.getAmount());
    }
}
