package employee.Service;

import employee.Domain.DomainController;
import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.SQLException;

public class EmployeeService {
    DomainController controller = new DomainController();

    public void addEmployee(String name, int id, int bank, int branchNumber, int account_number,
                            int salary, String contract, Job currentJob, String phoneNumber, int branch) throws SQLException {
        controller.addEmployee(name, id, bank, branchNumber, account_number, salary, contract, currentJob, phoneNumber, branch);
    }

    public void printShiftHistory(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.printShiftHistory(userId);
    }

    public void printAllEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        controller.printAllEmployees();
    }

    public void printActiveEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        controller.printActiveEmployees();
    }

    public void printOldEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        controller.printOldEmployees();
    }

    public void printEmployee(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.printEmployee(userId);
    }

    public void isValidEmployee(int userId) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.isValidEmployee(userId);
    }

    public int getHolidays(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return controller.getHolidays(userId);
    }

    public int getSickDays(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return controller.getSickDays(userId);
    }

    public void printBankDetails(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.printBankDetails(userId);
    }

    public String getTerms(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return controller.getTerms(userId);
    }

    public void printAbilities(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.printAbilities(userId);
    }

    public void printEmploymentTerms(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.printEmploymentTerms(userId);
    }

    public void printRelevantRoles(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.printRelevantRoles(userId);
    }

    public void addAbility(int employeeId, Ability ability) throws AbilityExistsException, EmployeeNotFoundException, SQLException {
        controller.addAbility(employeeId, ability);
    }

    public void removeAbility(int employeeId, Ability ability) throws AbilityNotFoundException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.removeAbility(employeeId, ability);
    }

    public String getWorkLimits(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return controller.getWorkLimits(id);
    }

    public WorkStatus getWorkStatus(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return controller.getWorkStatus(id);
    }

    public void setWorkStatus(int id, WorkStatus newStatus) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.setWorkStatus(id, newStatus);
    }

    public void setWorkLimits(int id, String newLimit) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.setWorkLimits(id, newLimit);
    }

    public void setContract(int id, String contract) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.setContract(id, contract);
    }

    public void setPhoneNumber(int id, String phoneNumber) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        controller.setPhoneNumber(id, phoneNumber);
    }

    public void setBankNumber(int id, int bankNum) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        controller.setBankNumber(id, bankNum);
    }

    public void setBranchNumber(int id, int branchNum) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.setBranchNumber(id, branchNum);
    }

    public void setAccountNumber(int id, int accountNum) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.setAccountNumber(id, accountNum);
    }

    public int getSalary(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return controller.getSalary(id);
    }

    public void setSalary(int id, int newSalary) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.setSalary(id, newSalary);
    }

    public String getContract(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return controller.getContract(id);
    }

    public String getPhoneNumber(int id) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        return controller.getPhoneNumber(id);
    }

    public void reduceSickDays(int id, int amount) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        controller.reduceSickDays(id, amount);
    }

    public void reduceHoliDays(int id, int amount) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        controller.reduceHoliDays(id, amount);
    }

    public void isThereEmployees() throws AbilityExistsException, SQLException, EmployeeNotFoundException {
        controller.isThereEmployees();
    }

    public void printAvaliableSuperBranches() throws SQLException {
        controller.printAvaliableSuperBranches();
    }

    public boolean isSuperBranchExists(int branch) throws SQLException {
        return controller.isSuperBranchExists(branch);
    }

    public int getSuperBranch(int id) throws SQLException {
        return controller.getSuperBranch(id);
    }

    public void updateSuperBranch(int id, int num) throws SQLException, InvalidInputException {
        controller.updateSuperBranch(id, num);
    }
}