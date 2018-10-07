package ch.heigvd.sym.util;

public class Account {

    private String email;

    private String password;

    private String image;

    public Account(String email, String password, String image) {

        this.email      = email;
        this.password   = password;
        this.image      = image;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }
}
