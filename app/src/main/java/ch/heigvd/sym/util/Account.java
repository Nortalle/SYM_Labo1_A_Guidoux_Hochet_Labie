/*
 * File         : Account.java
 * Project      : TemplateActivity
 * Authors      : Hochet Guillaume 7 octobre 2018
 *                Labie Marc 7 octobre 2018
 *                Guidoux Vincent 7 octobre 2018
 *
 * Description  : represents an Account with an email, a password and an image
 *
 */

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

    /**
     * @return the email of the account
     */
    public String getEmail() {
        return email;
    }


    /**
     * @return the password of the account
     */
    public String getPassword() {
        return password;
    }


    /**
     * @return the image of the account
     */
    public String getImage() {
        return image;
    }
}
