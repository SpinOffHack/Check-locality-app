package kpi.pti.spinoffhackkpi.app.utils;


import java.util.ArrayList;

public class User {
    private int id;
    private String login;
    private String password;
    private ArrayList<Locality> localities;

    public User(){}
    public User(String login,String password){
        super();
        this.login=login;
        this.password=password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
