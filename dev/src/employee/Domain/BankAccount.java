package employee.Domain;

import employee.Exceptions.InvalidInputException;

public class BankAccount {
    private int BankNumber;
    private int BranchNumber;
    private int AccountNumber;

    public BankAccount(int bank, int branch, int account_number){
        this.AccountNumber = account_number;
        this.BankNumber = bank;
        this.BranchNumber = branch;
    }

    public void setAccountNumber(int newNum) throws InvalidInputException {
        if (newNum < 0) {
            throw new InvalidInputException("Account Number cannot be negative");
        }
        String numString = Integer.toString(newNum);
        if (numString.length() != 3) {
            throw new InvalidInputException("Account Number should be 3 digits");
        }
        this.AccountNumber = newNum;
    }

    public void setBranchNumber(int newNum) throws InvalidInputException {
        if (newNum < 0) {
            throw new InvalidInputException("Branch Number cannot be negative");
        }
        String numString = Integer.toString(newNum);
        if (numString.length() != 9) {
            throw new InvalidInputException("Branch Number should be 9 digits");
        }
        this.BranchNumber = newNum;
    }

    public void setBankNumber(int newNum) throws InvalidInputException {
        if (newNum < 0) {
            throw new InvalidInputException("Bank Number cannot be negative");
        }
        String numString = Integer.toString(newNum);
        if (numString.length() != 2) {
            throw new InvalidInputException("Bank Number should be 2 digits");
        }
        this.BankNumber = newNum;
    }

    public int getBankNumber(){
        return this.BankNumber;
    }

    public int getBranchNumber(){
        return this.BranchNumber;
    }

    public int getAccountNumber(){
        return this.AccountNumber;
    }

    public void printBankDetails(){
        System.out.println("Bank number: " + this.BankNumber + ", Branch number: " + this.BranchNumber
        + ", Account number: " + this.AccountNumber);
    }
}
