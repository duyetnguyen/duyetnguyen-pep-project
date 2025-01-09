package Service;

import Model.Account;
import DAO.AccountDAOImpl;



public class AccountServiceImpl implements AccountService {

    //instance of AccountDAOImpl
    AccountDAOImpl accountDAOImpl = new AccountDAOImpl();

    public Account registerAccount (Account account){
        String username = account.getUsername();
        String password = account.getPassword();

        //fullfilled conditions
        if (username == "" || username == null){
            return null;
        }

        if (password.length()< 4){
            return null;
        }

        //check if username is already exist
        if(!accountDAOImpl.usernameExist(username)){
            Account newAccount = accountDAOImpl.registerAccount(account);
            return newAccount;
        }

        return null;

    }

    /*
     * method to verify if user login is match credential of an existing account
     */
    public Account userLogin (Account acc){
        Account account = accountDAOImpl.userLogin(acc);
        return account;  
    }

}

