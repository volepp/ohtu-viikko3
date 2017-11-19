package ohtu.authentication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ohtu.data_access.UserDao;
import ohtu.domain.User;
import ohtu.util.CreationStatus;

public class AuthenticationService {

    private UserDao userDao;

    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public CreationStatus createUser(String username, String password, String passwordConfirmation) {
        CreationStatus status = new CreationStatus();
        
        if(password.length() < 8) {
        	status.addError("password should have at least 8 characters");
        }
        
        Pattern p = Pattern.compile("[^a-zA-Z]");
        Matcher m = p.matcher(password);
        
        if(!m.find()) {
        	status.addError("password should contain at least one number or special character");
        }
        
        if(!password.equals(passwordConfirmation)) {
        	status.addError("password and password confirmation do not match");
        }
        
        if (userDao.findByName(username) != null) {
            status.addError("username is already taken");
        }

        if (username.length()<3 ) {
            status.addError("username should have at least 3 characters");
        }

        if (status.isOk()) {
            userDao.add(new User(username, password));
        }        
        
        return status;
    }

}
