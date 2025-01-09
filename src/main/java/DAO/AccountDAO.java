package DAO;
import Model.Account;


public interface AccountDAO {

    public boolean usernameExist (String username);
    public boolean accountExist (int accountId);
    public Account registerAccount(Account account);
    public Account userLogin(Account acc);

    




}
