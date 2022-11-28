package org.example;

import java.util.Scanner;

import org.example.models.Account;
import org.example.models.Bank;
import org.example.models.User;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner (System.in);
        Bank theBank = new Bank("Bank");
        User newUser = theBank.addUser("Maxim", "Belov", "2002");
        Account newAccount = new Account ("savings" , newUser , theBank);
        newUser.addAccount(newAccount);
        theBank.addAccount(newAccount);
        User curUser;
        while (true) {
            curUser = ATM.menuPrompt(theBank ,sc );
            ATM.printUserMenu(curUser, sc);
        }
    }

    public static  User menuPrompt(Bank theBank , Scanner sc ){
        String userID ;
        String pin ;
        User authUser;
        do {
            System.out.printf("\n\nДобро пожаловать в %s\n\n", theBank.getName());

            System.out.println("Введите ваш ID: ");
            userID = sc.nextLine();

            System.out.println("Введите ваш pin-код: ");
            pin = sc.nextLine();
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null){
                System.out.println("Неверные данные");
            }
        } while (authUser == null);
        
        return authUser;
    }

    public static void printUserMenu(User user, Scanner sc) {
        int choice;

        do {
            System.out.println("Добро пожаловать, " + user.getFirstName());
            user.printAccountsSummary();

            System.out.println("Выберите действие:");
            System.out.println("  1) Показать историю транзакций");
            System.out.println("  2) Снять средства");
            System.out.println("  3) Внести средства");
            System.out.println("  4) Перевести средства");
            System.out.println("  5) Выйти");
            choice = sc.nextInt();

            if (choice <0 || choice > 5 ) {
                System.out.println("Неверный ввод ");
            }
        } while (choice <0 || choice > 5);

        switch (choice) {
            case 1 -> {
                ATM.showAccountTransactionHistory(user, sc);
            }
            case 2 -> {
                ATM.withdrawFunds(user, sc);
            }
            case 3 -> {
                ATM.depositeFunds(user, sc);
            }
            case 4 -> {
                ATM.transferFunds(user, sc);
            }
        }
        if (choice != 5)
            printUserMenu(user, sc);
    }

    public static void transferFunds(User user, Scanner sc){
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do {
            System.out.printf("Введите номер (1-%d) счета, с которого вы хотите перевести средства:",
                    user.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(fromAcct);

        do {
            System.out.printf("Введите номер (1-%d) счета, на который вы хотите перевести средства :", user.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());

        do {
            System.out.printf("Введите сумму перевода (max $%.02f): $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Сумма должна быть больше нуля.");
            } else if (amount > acctBal) {
                System.out.printf("Сумма не должна превышать баланс " +
                        "$.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        user.addAcctTransaction(fromAcct, -1*amount, String.format(
                "Перевод на счет %s", user.getAcctUUID(toAcct)));
        user.addAcctTransaction(toAcct, amount, String.format(
                "Перевод со счета %s", user.getAcctUUID(fromAcct)));
    }

    public static void withdrawFunds(User theUser , Scanner sc){
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Введите номер (1-%d) счета," +
                    "чтобы снять средства:  ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        do {
            System.out.printf("Введите сумму для вывода (max $%.02f): $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Сумма должна быть больше нуля.");
            } else if (amount > acctBal) {
                System.out.printf("Сумма не должна превышать баланс $%.02f.\n ", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        sc.nextLine();
        System.out.print("Введите memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(fromAcct, -amount, memo);
    }

    public static void depositeFunds(User theUser , Scanner sc){
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Введите номер (1-%d) счета, на который вы хотите внести средства: "
                    , theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        do {
            System.out.printf("Введите сумму для внесения (max $%.02f): $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Сумма должна быть больше нуля.");
            }
        } while (amount < 0);

        sc.nextLine();

        System.out.print("Введите memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(toAcct, amount, memo);
    }

    public static void showAccountTransactionHistory(User theUser , Scanner sc){
        int theAcc ;

        do {
            System.out.printf("Введите номер (1-%d) счета  " +
                    ": ", theUser.numAccounts());
            theAcc = sc.nextInt()-1;
            if (theAcc < 0 || theAcc >= theUser.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (theAcc < 0 || theAcc >= theUser.numAccounts());
        theUser.printAcctTransHistory(theAcc);
    }
}
