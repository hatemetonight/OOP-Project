package org.project.models;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private final String name;
    private final ArrayList<User> users;
    private final ArrayList<Account> accounts;

    public Bank(String name) {
        this.name = name;
        accounts = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);
    }

    public String getNewUserUUID() {
        Random rng = new Random();
        boolean nonUnique;
        StringBuilder uuid;
        int len = 6;

        do {
            uuid = new StringBuilder();
            for (int c = 0; c<len; c++) {
                uuid.append(((Integer) rng.nextInt(10)));
            }
            nonUnique = false;
            for (User u: this.users){
                if (uuid.toString().compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid.toString();
    }

    public String getNewAccountUUID() {
        Random rng = new Random();
        boolean nonUnique;
        StringBuilder uuid;
        int len = 10;

        do {
            uuid = new StringBuilder();
            for (int c = 0; c<len ; c++){
                uuid.append(((Integer) rng.nextInt(10)));
            }
            nonUnique = false;
            for (Account a: this.accounts) {
                if (uuid.toString().compareTo(a.getUUID()) == 0){
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid.toString();
    }

    public User addUser(String firstName, String lastName, String pin) {
        User newUser = new User (firstName,  lastName,  pin, this);
        this.users.add(newUser);

        Account newAccount = new Account("savings", newUser, this);
        this.accounts.add(newAccount);
        newUser.addAccount(newAccount);
        return newUser;
    }

    public User userLogin (String userID, String pin) {
        for (User u: this.users) {
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin))
                return u;
        }
        return null;
    }
    public String getName() {
        return this.name;
    }
}