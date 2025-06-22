package employee.Service;

import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.SQLException;
import java.util.Date;

public class ServiceController {
    private EmployeeService employeeService;
    private ShiftAssignmentService shiftAssignmentService;
    private YearlyShiftsService yearlyShiftsService;
    private UserService userService;

    public ServiceController() {
        employeeService = new EmployeeService();
        shiftAssignmentService = new ShiftAssignmentService();
        userService = new UserService();
        yearlyShiftsService = new YearlyShiftsService();
    }

    public void addEmployee(String name, int id, int bank, int branchNumber, int account_number,
                            int salary, String contract, Job currentJob, String phoneNumber, int branch) throws SQLException {
        employeeService.addEmployee(name, id, bank, branchNumber, account_number, salary, contract, currentJob, phoneNumber, branch);
    }

    public void printShiftHistory(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.printShiftHistory(userId);
    }

    public void printAllEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        employeeService.printAllEmployees();
    }

    public void printActiveEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        employeeService.printActiveEmployees();
    }

    public void printOldEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        employeeService.printOldEmployees();
    }

    public void printEmployee(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.printEmployee(userId);
    }

    public void isValidEmployee(int userId) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.isValidEmployee(userId);
    }

    public int getHolidays(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return employeeService.getHolidays(userId);
    }

    public int getSickDays(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return employeeService.getSickDays(userId);
    }

    public void printBankDetails(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.printBankDetails(userId);
    }

    public String getTerms(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return employeeService.getTerms(userId);
    }

    public void printAbilities(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.printAbilities(userId);
    }

    public void printEmploymentTerms(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.printEmploymentTerms(userId);
    }

    public void printRelevantRoles(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.printRelevantRoles(userId);
    }

    public void addAbility(int employeeId, Ability ability) throws AbilityExistsException, EmployeeNotFoundException, SQLException {
        employeeService.addAbility(employeeId, ability);
    }

    public void removeAbility(int employeeId, Ability ability) throws AbilityNotFoundException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.removeAbility(employeeId, ability);
    }

    public String getWorkLimits(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return employeeService.getWorkLimits(id);
    }

    public WorkStatus getWorkStatus(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return employeeService.getWorkStatus(id);
    }

    public void setWorkStatus(int id, WorkStatus newStatus) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.setWorkStatus(id, newStatus);
    }

    public void setWorkLimits(int id, String newLimit) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.setWorkLimits(id, newLimit);
    }

    public void setContract(int id, String contract) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.setContract(id, contract);
    }

    public void setPhoneNumber(int id, String phoneNumber) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        employeeService.setPhoneNumber(id, phoneNumber);
    }

    public void setBankNumber(int id, int bankNum) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        employeeService.setBankNumber(id, bankNum);
    }

    public void setBranchNumber(int id, int branchNum) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.setBranchNumber(id, branchNum);
    }

    public void setAccountNumber(int id, int accountNum) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.setAccountNumber(id, accountNum);
    }

    public int getSalary(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return employeeService.getSalary(id);
    }

    public void setSalary(int id, int newSalary) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.setSalary(id, newSalary);
    }

    public String getContract(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        return employeeService.getContract(id);
    }

    public String getPhoneNumber(int id) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        return employeeService.getPhoneNumber(id);
    }

    public void reduceSickDays(int id, int amount) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        employeeService.reduceSickDays(id, amount);
    }

    public void reduceHoliDays(int id, int amount) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeService.reduceHoliDays(id, amount);
    }

    public void saveAndReset() throws InvalidInputException, SQLException {
        shiftAssignmentService.saveAndReset();
    }

    public void assignShiftToEmployee(int id, Role role, Days day, ShiftType shiftType) throws CantAssignShiftException, InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        shiftAssignmentService.assignShiftToEmployee(id, role, day, shiftType);
    }

    public void removeEmployeeFromShiftNextWeek(int id, Role role, Days day, ShiftType shiftType) throws InvalidInputException, CantAssignShiftException, SQLException {
        shiftAssignmentService.removeEmployeeFromShiftNextWeek(id, role, day, shiftType);
    }

    public void updateEmployeeAssignmentInCurrentWeek(int oldId, int newId, Role role, Days day, ShiftType shiftType) throws InvalidInputException, CantAssignShiftException, SQLException, AbilityExistsException, EmployeeNotFoundException {
        shiftAssignmentService.updateEmployeeAssignmentInCurrentWeek(oldId, newId, role, day, shiftType);
    }

    public void addShift(ShiftType type, Date date) throws ShiftCantBeAddedException, InvalidInputException, SQLException {
        shiftAssignmentService.addShift(type, date);
    }

    public void removeShift(Days day, ShiftType shiftType) throws ShiftNotEmptyException, InvalidInputException, SQLException {
        shiftAssignmentService.removeShift(day, shiftType);
    }

    public void printWeeklyShiftsDetails() throws InvalidInputException, SQLException {
        shiftAssignmentService.printWeeklyShiftsDetails();
    }

    public void printCurrentWeekShiftsDetails() throws InvalidInputException, SQLException {
        shiftAssignmentService.printCurrentWeekShiftsDetails();
    }

    public boolean isShiftExists(Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        return shiftAssignmentService.isShiftExists(shiftDay, shiftType);
    }

    public void addRoleToShift(Role role, int amount, Days shiftDay, ShiftType shiftType) throws InvalidInputException, RoleExistsException, SQLException {
        shiftAssignmentService.addRoleToShift(role, amount, shiftDay, shiftType);
    }

    public void viewShiftPreferences(Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        shiftAssignmentService.viewShiftPreferences(shiftDay, shiftType);
    }

    public boolean areShiftsReady() throws InvalidInputException, SQLException {
        return shiftAssignmentService.areShiftsReady();
    }

    public void decreaseAmountSpecificRole(Role role, int amount, Days shiftDay, ShiftType shiftType) throws InvalidInputException, RoleNotFoundException, SQLException, ShiftHasManager {
        shiftAssignmentService.decreaseAmountSpecificRole(role, amount, shiftDay, shiftType);
    }

    public void increaseAmountSpecificRole(Role role, int amount, Days shiftDay, ShiftType shiftType) throws InvalidInputException, RoleNotFoundException, SQLException {
        shiftAssignmentService.increaseAmountSpecificRole(role, amount, shiftDay, shiftType);
    }

    public void findEmployeeById(int id) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        shiftAssignmentService.findEmployeeById(id);
    }

    public void setShiftTime(String start, String end, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        shiftAssignmentService.setShiftTime(start, end, shiftDay, shiftType);
    }

    public void addPreference(int userID, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        shiftAssignmentService.addPreference(userID, shiftDay, shiftType);
    }

    public void removePreference(int userID, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        shiftAssignmentService.removePreference(userID, shiftDay, shiftType);
    }

    public void printPreferredShiftsDetails(int userId) throws InvalidInputException, SQLException {
        shiftAssignmentService.printPreferredShiftsDetails(userId);
    }

    public void printNonPreferredShiftsDetails(int userId) throws InvalidInputException, SQLException {
        shiftAssignmentService.printNonPreferredShiftsDetails(userId);
    }

    public boolean isEmployeeWorksInCurrnetShift(int employeeIdToReplace, Role role, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        return shiftAssignmentService.isEmployeeWorksInCurrnetShift(employeeIdToReplace, role, shiftDay, shiftType);
    }

    public boolean isDateSATURDAY() {
        return shiftAssignmentService.isDateSATURDAY();
    }

    public void isUserExists(String username) throws UserNotFound, SQLException {
        userService.isUserExists(username);
    }

    public void isJobMatchUser(String username, Job job) throws UserNotFound, SQLException {
        userService.isJobMatchUser(username, job);
    }

    public void isPasswordMatchUser(String username, String password) throws UserNotFound, SQLException {
        userService.isPasswordMatchUser(username, password);
    }

    public int getId(String username) throws UserNotFound, SQLException {
        return userService.getId(username);
    }

    public void connectDao(String url) throws InvalidInputException, AbilityExistsException, SQLException, UserNotFound {
        userService.connectDao(url);
    }

    public void connectDaoEmpty(String url) throws SQLException, InvalidInputException, AbilityExistsException, UserNotFound {
        userService.connectDaoEmpty(url);
    }

    public void printAvailableYears() throws SQLException {
        yearlyShiftsService.printAvailableYears();
    }

    public void printShiftsInYear(int year) throws InvalidInputException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        yearlyShiftsService.printShiftsInYear(year);
    }

    public void printShiftsInWeekAndYear(int year, int week) throws InvalidInputException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        yearlyShiftsService.printShiftsInWeekAndYear(year, week);
    }

    public void printUserWeekShifts(int userId, Date date) throws InvalidInputException, WeeklyShiftsNotReadyException, EmployeeNotFoundException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        yearlyShiftsService.printUserWeekShifts(userId, date);
    }

    public void printManagerWeekShifts(int userId, Date date) throws InvalidInputException, WeeklyShiftsNotReadyException, EmployeeNotFoundException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        yearlyShiftsService.printManagerWeekShifts(userId, date);
    }

    public void isThereEmployees() throws AbilityExistsException, SQLException, EmployeeNotFoundException {
        employeeService.isThereEmployees();
    }

    public void printAvaliableSuperBranches() throws SQLException {
        employeeService.printAvaliableSuperBranches();
    }

    public boolean isSuperBranchExists(int branch) throws SQLException {
        return employeeService.isSuperBranchExists(branch);
    }

    public int getSuperBranch(int id) throws SQLException {
        return employeeService.getSuperBranch(id);
    }

    public void updateSuperBranch(int id, int num) throws SQLException, InvalidInputException {
        employeeService.updateSuperBranch(id,num);
    }

    public void closeConnection(){
        userService.closeConnection();
    }
}