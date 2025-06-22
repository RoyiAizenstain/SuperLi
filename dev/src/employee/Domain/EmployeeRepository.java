package employee.Domain;

import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface EmployeeRepository {

    Employee getEmployeeById(int employeeId) throws SQLException, EmployeeNotFoundException, AbilityExistsException;

    List<Ability> getAbilitiesById(int employeeId) throws SQLException;
    
    void addEmployee(String name, int id, int bank, int branchNumber, int account_number,
                     int salary, String contract, Job currentJob, String phoneNumber, int branch) throws SQLException;

    List<Employee> getActiveEmployees() throws AbilityExistsException, SQLException, EmployeeNotFoundException;

    List<Employee> getNotActiveEmployees() throws AbilityExistsException, SQLException, EmployeeNotFoundException;

    List<Employee> getEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException;

    void addHistoryShiftsById(int id, ShiftType shiftType, java.util.Date date) throws SQLException, HistoryShiftExistsException;

    void removeHistoryShiftsById(int id, ShiftType shiftType, java.util.Date date) throws SQLException, HistoryShiftNotFoundException;

    Role relevantRole(Ability ability, int id) throws SQLException;

    void removeAbilityById(int id, Ability ability) throws SQLException, AbilityNotFoundException;

    void addAbilityById(int id, Ability ability) throws SQLException, AbilityExistsException;

    void updateWorkStatusById(int id, WorkStatus workStatus) throws SQLException;

    void updateWorkLimitsById(int id, String workLimits) throws SQLException;

    void updateContractById(int id, String contract) throws SQLException;

    void updatePhoneNumberById(int id, String phoneNumber) throws SQLException, InvalidInputException;

    void updateBankNumberById(int id, int bankNumber) throws SQLException, InvalidInputException;

    void updateBranchNumberById(int id, int branchNumber) throws SQLException, InvalidInputException;

    void updateAccountNumberById(int id, int accountNumber) throws SQLException, InvalidInputException;

    void updateSalaryById(int id, int salary) throws SQLException, InvalidInputException;

    void updateSickDaysById(int id, int sickDays) throws SQLException;

    void updateHoliDaysById(int id, int holiDays) throws SQLException;

    Map<java.util.Date, ShiftType> getEmployeeHistory(int id) throws SQLException;

    boolean isThereEmployees() throws AbilityExistsException, SQLException;

    void connectDao(String url) throws AbilityExistsException, SQLException;

    ArrayList<Integer> getAvaliableSuperBranches() throws SQLException;

    int getSuperBranch(int id) throws SQLException;

    void updateSuperBranch(int id, int num) throws SQLException;

    void connectDaoEmpty(String url);
}