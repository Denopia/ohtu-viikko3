package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logInSub(String username, String password, User user){
        return user.getUsername().equals(username)&& user.getPassword().equals(password);
    }
    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (logInSub(username, password, user)) {
                return true;
            }
        }
        return false;
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null) {
            return false;
        }
        if (invalid(username, password)) {
            return false;
        }
        userDao.add(new User(username, password));
        return true;
    }

    private boolean checkNameSub(String username, String letters, int j){
        boolean ok = false;
        for (int i = 0; i < letters.length(); i++) {
            if (username.toLowerCase().charAt(j) == letters.charAt(i)) ok = true;       
        }
        return !ok;
    }
    private boolean checkName(String username, String letters, int j) {
        while (j < username.length()) {
            if(checkNameSub(username, letters, j)) return true;
            j++;
        }return false;
    }
    
    private boolean checkPasswordSub(String password, String letters, int u, int k){
        boolean ok = false;
        for (int i = 0; i < letters.length(); i++) {
            if (password.toLowerCase().charAt(k) == letters.charAt(i)) ok = true;    
        }
        return ok;
    }

    private boolean checkPassword(String password, String letters, int u, int k) {
        while (k < password.length()) {     
            if (checkPasswordSub(password, letters, u, k))u++;     
            k++;
        }return u == password.length();
    }

    private boolean invalid(String username, String password) {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        if(checkNameValidity(username, letters)){
            return true;
        }
        return checkPasswordValidity(password, letters);
    }
    
    private boolean checkNameValidity(String username, String letters){
        if (username.length() < 3)return true;
        return checkName(username, letters, 0);
    }
    
    private boolean checkPasswordValidity(String password, String letters){
        if (password.length() < 8)return true;
        return checkPassword(password, letters, 0, 0);
    }
}
