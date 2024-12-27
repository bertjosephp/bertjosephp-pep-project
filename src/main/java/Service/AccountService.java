package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        boolean isValidUsername = username != null && !username.isEmpty();
        boolean isAccountExisting = accountDAO.getAccountByUsername(username) != null;
        boolean isValidPassword = password.length() >= 4;

        if (isValidUsername && isValidPassword && !isAccountExisting) {
            return accountDAO.insertAccount(account);
        }
        return null;
    }

    public Account getAccountByUsernameAndPassword(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
}
