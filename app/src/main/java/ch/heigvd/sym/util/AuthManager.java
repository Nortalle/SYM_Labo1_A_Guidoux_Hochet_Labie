/*
 * File         : AuthManager.java
 * Project      : TemplateActivity
 * Authors      : Hochet Guillaume 7 octobre 2018
 *                Labie Marc 7 octobre 2018
 *                Guidoux Vincent 7 octobre 2018
 *
 * Description  : Handle the account to log in the application
 *
 */

package ch.heigvd.sym.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle authentification with 3 hard-coded accounts
 */
public class AuthManager {

    private Map<String, Account> accounts = new HashMap<>();

    public AuthManager() {

        Account[] pool = new Account[]{
            new Account("guillaume.hochet@heig-vd.ch", "yolo", "pepe.png"),
            new Account("marc.labie@heig-vd.ch", "swag", "marc.jpg"),
            new Account("vincent.guidoux1@heig-vd.ch", "mashallah", "guidoux.jpg")
        };

        for(Account account : pool)
            accounts.put(account.getEmail(), account);
    }

    /**
     * @param email : mail we want to know if it exist
     * @return if the email exist in the authentification manager
     */
    public Boolean exist(String email) {
        return accounts.containsKey(email);
    }

    /**
     *
     * @param email     : email of the user who wnats to login
     * @param password  : password of the user who wnats to login
     * @return if the user exist, and if the password coreesponds
     */
    public Boolean login(String email, String password) {
        return accounts.containsKey(email) && accounts.get(email).getPassword().equals(password);
    }

    /**
     * @param email : temail of the account we want
     * @return the account of the given email
     */
    public Account getAccount(String email) {
        return accounts.get(email);
    }
}
