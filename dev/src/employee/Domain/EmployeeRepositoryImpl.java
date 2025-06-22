package employee.Domain;

import employee.DataAccess.EmployeeDao;
import employee.Dto.EmployeeDTO;
import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private EmployeeDao employeeDao;
    private List<Employee> employees;
    private static EmployeeRepositoryImpl instance;

    private EmployeeRepositoryImpl(){}

    public static EmployeeRepositoryImpl getInstance() {
    if (instance == null) {
        instance = new EmployeeRepositoryImpl();
    }
    return instance;
    }

    @Override
    public void connectDao(String url) throws AbilityExistsException, SQLException {
        this.employeeDao = new EmployeeDao(url);
        employees = new ArrayList<>();
    }

    @Override
    public void connectDaoEmpty(String url){
        this.employeeDao = new EmployeeDao(url);
        employees = new ArrayList<>();
    }

    @Override
    public ArrayList<Integer> getAvaliableSuperBranches() throws SQLException {
        return employeeDao.getAvaliableSuperBranches();
    }

    @Override
    public int getSuperBranch(int id) throws SQLException {
        for(Employee emp : employees){
            if(emp.getId() == id){
                return emp.getSuperBranch();
            }
        }
        return employeeDao.getSuperBranch(id);
    }

    @Override
    public void updateSuperBranch(int id, int num) throws SQLException {
        for(Employee emp : employees){
            if(emp.getId() == id){
                emp.setSuperBranch(num);
            }
        }
        employeeDao.updateSuperBranch(id, num);
    }

    @Override
    public List<Employee> getActiveEmployees() throws AbilityExistsException, SQLException, EmployeeNotFoundException {
        List<EmployeeDTO> employeeDTOS = employeeDao.getActiveEmployees();
        List<Employee> activeEmployees = new ArrayList<>();
        for (EmployeeDTO dto : employeeDTOS) {
            activeEmployees.add(getEmployeeById(dto.id));
        }
        return activeEmployees;
    }

    @Override
    public List<Employee> getNotActiveEmployees() throws AbilityExistsException, SQLException, EmployeeNotFoundException {
        List<EmployeeDTO> employeeDTOS = employeeDao.getNotActiveEmployees();
        List<Employee> notActiveEmployees = new ArrayList<>();
        for (EmployeeDTO dto : employeeDTOS) {
            notActiveEmployees.add(getEmployeeById(dto.id));
        }
        return notActiveEmployees;
    }

    @Override
    public List<Employee> getEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        List<EmployeeDTO> employeeDTOS = employeeDao.getEmployees();
        for (EmployeeDTO dto : employeeDTOS) {
            getEmployeeById(dto.id);
        }
        return employees;
    }

//    @Override
//    public List<Employee> getEmployees(){
//        return employees;
//    }

    @Override
    public Employee getEmployeeById(int employeeId) throws SQLException, EmployeeNotFoundException, AbilityExistsException {
        for (Employee emp : employees){
            if (emp.getId() == employeeId){
                return emp;
            }
        }
        EmployeeDTO dto = employeeDao.getEmployeeById(employeeId);
        BankAccount bankAccount = new BankAccount(dto.bankNumber, dto.branchNumber, dto.accountNumber);
        Employee emp = new Employee(
                dto.name,
                dto.id,
                bankAccount,
                dto.salary,
                dto.contract,
                dto.currentJob,
                dto.phoneNumber,
                dto.branch
        );
        emp.setWorkLimits(dto.workLimits);
        emp.setWorkStatus(dto.workStatus);
        emp.setHiringDate(dto.hiringDate);
        emp.setSickDays(dto.sickDays);
        emp.setHolidays(dto.holidays);
        List<Ability> abilities = employeeDao.getAbilitiesById(dto.id);
        for(Ability ability: abilities){
            emp.addAbility(ability);
        }
        if(!employees.contains(emp)){
            employees.add(emp);
        }
        return emp;
    }

    @Override
    public List<Ability> getAbilitiesById(int employeeId) throws SQLException {
        for(Employee emp : employees){
            if(emp.getId() == employeeId){
                return emp.getAbilities();
            }
        }
        return employeeDao.getAbilitiesById(employeeId);
    }

    @Override
    public void addEmployee(String name, int id, int bank, int branchNumber, int account_number, int salary, String contract,
                               Job currentJob, String phoneNumber, int branch) throws SQLException {
        employees.add(new Employee(name,id,new BankAccount(bank,branchNumber,account_number),salary,contract,
                currentJob,phoneNumber,branch));
        employeeDao.addEmployee(name, id, bank, branchNumber, account_number, salary, contract, currentJob, phoneNumber, branch);
    }

    @Override
    public void addHistoryShiftsById(int id, ShiftType shiftType, java.util.Date date) throws SQLException, HistoryShiftExistsException {
        for(Employee emp : employees){
            if(emp.getId() == id){
                emp.addHistoryShifts(date,shiftType);
            }
        }
        employeeDao.adHistoryShiftsById(id, shiftType, date);
    }

    @Override
    public void removeHistoryShiftsById(int id, ShiftType shiftType,  java.util.Date date) throws SQLException, HistoryShiftNotFoundException {
        for(Employee emp : employees){
            if(emp.getId() == id){
                emp.removeHistoryShifts(date,shiftType);
            }
        }
        employeeDao.removeHistoryShiftsById(id, shiftType, date);
    }

    @Override
    public Role relevantRole(Ability ability, int id) throws SQLException {
        if (ability == Ability.Authorized_cancellation_card) {
            List<Ability> abilities = getAbilitiesById(id);
            if(abilities.contains(Ability.Team_Management)) {
                return Role.Shift_Manager;
            }
        }
        else if (ability == Ability.Team_Management) {
            List<Ability> abilities = getAbilitiesById(id);
            if(abilities.contains(Ability.Authorized_cancellation_card)) {
                return Role.Shift_Manager;
            }
        }else if (ability == Ability.Operate_cash_register) {
            return Role.Cashier;
        } else if (ability == Ability.Driving_license) {
            return Role.Driver;
        } else if (ability == Ability.Knowledge_terminals_and_inventory_system) {
            return (Role.Store_Keeper);
        } else if (ability == Ability.Operate_cutting_and_packaging_machine) {
            return (Role.Deli_worker);
        } else if (ability == Ability.Qualified_with_industrial_baking_ovens) {
            return (Role.Bakery_worker);
        } else if (ability == Ability.Operating_cutting_machine) {
            return (Role.Butcher);
        } else if (ability == Ability.Knowledge_of_commercial_law_procedures) {
            return (Role.Shelf_organizer);
        } else if (ability == Ability.Certification_for_cleaning_car) {
            return (Role.Cleaning_Worker);
        } else if (ability == Ability.Weapons_license) {
            return (Role.Security_Officer);
        }
        return null;
    }

    @Override
    public void removeAbilityById(int id, Ability ability) throws SQLException, AbilityNotFoundException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.removeAbility(ability);
            }
        }
        employeeDao.removeAbilityById(id, ability, relevantRole(ability,id));
    }

    @Override
    public void addAbilityById(int id, Ability ability) throws SQLException, AbilityExistsException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.addAbility(ability);
            }
        }
        employeeDao.addAbilityById(id, ability, relevantRole(ability,id));
    }

    @Override
    public void updateWorkStatusById(int id, WorkStatus workStatus) throws SQLException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.setWorkStatus(workStatus);
            }
        }
        employeeDao.updateWorkStatusById(id, workStatus);
    }

    @Override
    public void updateWorkLimitsById(int id, String workLimits) throws SQLException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.setWorkLimits(workLimits);
            }
        }
        employeeDao.updateWorkLimitsById(id, workLimits);
    }

    @Override
    public void updateContractById(int id, String contract) throws SQLException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.setContract(contract);
            }
        }
        employeeDao.updateContractById(id, contract);
    }

    @Override
    public void updatePhoneNumberById(int id, String phoneNumber) throws SQLException, InvalidInputException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.setPhoneNumber(phoneNumber);
            }
        }
        employeeDao.updatePhoneNumberById(id, phoneNumber);
    }

    @Override
    public void updateBankNumberById(int id, int bankNumber) throws SQLException, InvalidInputException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.getBankDetails().setBankNumber(bankNumber);
            }
        }
        employeeDao.updateBankNumberById(id, bankNumber);
    }

    @Override
    public void updateBranchNumberById(int id, int branchNumber) throws SQLException, InvalidInputException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.getBankDetails().setBranchNumber(branchNumber);
            }
        }
        employeeDao.updateBranchNumberById(id, branchNumber);
    }

    @Override
    public void updateAccountNumberById(int id, int accountNumber) throws SQLException, InvalidInputException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.getBankDetails().setAccountNumber(accountNumber);
            }
        }
        employeeDao.updateAccountNumberById(id, accountNumber);
    }

    @Override
    public void updateSalaryById(int id, int salary) throws SQLException, InvalidInputException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.setSalary(salary);
            }
        }
        employeeDao.updateSalaryById(id, salary);
    }

    @Override
    public void updateSickDaysById(int id, int sickDays) throws SQLException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.setSickDays(sickDays);
            }
        }
        employeeDao.updateSickDaysById(id, sickDays);
    }

    @Override
    public void updateHoliDaysById(int id, int holiDays) throws SQLException {
        for (Employee emp : employees){
            if (emp.getId() == id){
                emp.setHolidays(holiDays);
            }
        }
        employeeDao.updateHoliDaysById(id, holiDays);
    }

    @Override
    public Map<java.util.Date, ShiftType> getEmployeeHistory(int id) throws SQLException {
        return employeeDao.getEmployeeHistory(id);
    }

    @Override
    public boolean isThereEmployees() throws AbilityExistsException, SQLException {
        if (employees.isEmpty() && employeeDao.getEmployees().isEmpty()){
            return false;
        }
        return true;
    }

    public void close() {
        if (employeeDao != null)
            employeeDao.close();
    }

}
