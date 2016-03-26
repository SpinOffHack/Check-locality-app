package kpi.pti.spinoffhackkpi.app.utils;


import java.util.ArrayList;

public class User {
    private int id;
    private String login;
    private String password;
    private ArrayList<Locality> localities;

    public User(){}
    public User(String login,String password){
        this.login=login;
        this.password=password;
    }
    public User(String login,String password,ArrayList<Locality> localities){
        this.login=login;
        this.password=password;
        this.localities=localities;
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

    public ArrayList<Locality> getLocalities() {
        return localities;
    }

    public void setLocalities(ArrayList<Locality> localities) {
        this.localities = localities;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", localities=" + localities.toString() +
                '}';
    }
}
