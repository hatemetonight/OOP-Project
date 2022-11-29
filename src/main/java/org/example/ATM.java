package org.example;

import java.util.Scanner;

import org.example.models.Account;
import org.example.models.Bank;
import org.example.models.User;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank("Bank");
        User user = bank.addUser("Maxim", "Belov", "2002");
        Account newAccount = new Account ("savings", user, bank);
        user.addAccount(newAccount);
        bank.addAccount(newAccount);
        User curUser;
        while (true) {
            curUser = ATM.menuPrompt(bank, sc);
            ATM.printUserMenu(curUser, sc);
        }
    }

    public static User menuPrompt(Bank bank, Scanner sc) {
        String userID ;
        String pin ;
        User authUser;
        do {
            System.out.printf("\n\nДобро пожаловать в %s\n\n", bank.getName());
            System.out.println("Введите ваш ID: ");
            userID = sc.nextLine();
            System.out.println("Введите ваш pin-код: ");
            pin = sc.nextLine();
            authUser = bank.userLogin(userID, pin);
            if (authUser == null) {
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
            default -> {
                printUserMenu(user, sc);
            }
        }
    }

    public static void transferFunds(User user, Scanner sc){
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do {
            System.out.printf("Введите номер (1-%d) счета, с которого вы хотите перевести средства:", user.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(fromAcct);

        do {
            System.out.printf("Введите номер (1-%d) счета, на который вы хотите перевести средства :", user.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());

        do {
            System.out.printf("Введите сумму перевода (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Сумма должна быть больше нуля.");
            } else if (amount > acctBal) {
                System.out.printf("Сумма не должна превышать баланс $.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        user.addAcctTransaction(fromAcct, -1*amount, String.format("Перевод на счет %s", user.getAcctUUID(toAcct)));
        user.addAcctTransaction(toAcct, amount, String.format("Перевод со счета %s", user.getAcctUUID(fromAcct)));
    }

    public static void withdrawFunds(User user , Scanner sc){
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Введите номер (1-%d) счета," +
                    "чтобы снять средства:  ", user.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(fromAcct);

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

        user.addAcctTransaction(fromAcct, -amount, memo);
    }

    public static void depositeFunds(User user, Scanner sc) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Введите номер (1-%d) счета, на который вы хотите внести средства: ", user.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(toAcct);

        do {
            System.out.printf("Введите сумму для внесения (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Сумма должна быть больше нуля.");
            }
        } while (amount < 0);

        sc.nextLine();

        System.out.print("Введите memo: ");
        memo = sc.nextLine();

        user.addAcctTransaction(toAcct, amount, memo);
    }

    public static void showAccountTransactionHistory(User user, Scanner sc) {
        int theAcc;

        do {
            System.out.printf("Введите номер (1-%d) счета: ", user.numAccounts());
            theAcc = sc.nextInt()-1;
            if (theAcc < 0 || theAcc >= user.numAccounts()) {
                System.out.println("Недействительная учетная запись. Пожалуйста, попробуйте снова.");
            }
        } while (theAcc < 0 || theAcc >= user.numAccounts());
        user.printAcctTransHistory(theAcc);
    }
}
