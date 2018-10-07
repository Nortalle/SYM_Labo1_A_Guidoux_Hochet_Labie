package ch.heigvd.sym.util;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {

    private Map<String, Account> accounts = new HashMap<>();

    public AuthManager() {

        Account[] pool = new Account[]{
            new Account("guillaume.hochet@heig-vd.ch", "yolo", "pepe.png"),
            new Account("marc.labie@heig-vd.ch", "swag", "marc.jpg"),
            new Account("vincent.guidoux@heig-vd.ch", "mashallah", "guidoux.jpg")
        };

        for(Account account : pool)
            accounts.put(account.getEmail(), account);
    }

    public Boolean exist(String email) {
        return accounts.containsKey(email);
    }

    public Boolean login(String email, String password) {
        return accounts.containsKey(email) && accounts.get(email).getPassword().equals(password);
    }

    public Account getAccount(String email) {
        return accounts.get(email);
    }
}
