package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerNewAccount(Account newAccount) {
        if (newAccount.getUsername().isBlank() || 
            newAccount.getPassword().length() < 4 || 
            this.accountDAO.searchAccountByUsername(newAccount.getUsername()) != null) {
            return null;
        }
        return this.accountDAO.registerAccount(newAccount);
    }

    public Account loginAccount(Account currentAccount) {
        return this.accountDAO.checkAccountCredentials(currentAccount.getUsername(), currentAccount.getPassword());
    }
}
