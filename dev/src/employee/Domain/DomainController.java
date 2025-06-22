package employee.Domain;

import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DomainController {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final YearlyShiftsRepository yearlyShiftsRepository;

    public DomainController() {
        this.employeeRepository = EmployeeRepositoryImpl.getInstance();
        this.userRepository = UserRepositoryImpl.getInstance();
        this.yearlyShiftsRepository = YearlyShiftsRepositoryImpl.getInstance();
    }

    public void connectDao(String url) throws AbilityExistsException, SQLException, InvalidInputException, UserNotFound {
        employeeRepository.connectDao(url);
        userRepository.connectDao(url);
        yearlyShiftsRepository.connectDao(url);
    }

    public void connectDaoEmpty(String url) throws SQLException, AbilityExistsException, InvalidInputException, UserNotFound {
        employeeRepository.connectDaoEmpty(url);
        yearlyShiftsRepository.connectDaoEmpty(url);
        userRepository.connectDaoEmpty(url);
    }

    public void connectDaoTEST(String url) throws InvalidInputException, SQLException, UserNotFound, AbilityExistsException {
        userRepository.connectDaoTEST(url);
        employeeRepository.connectDao(url);
        yearlyShiftsRepository.connectDaoTEST(url);
    }

    public void addEmployee(String name, int id, int bank, int branchNumber, int account_number,
                            int salary, String contract, Job currentJob, String phoneNumber, int branch) throws SQLException {
        employeeRepository.addEmployee(name, id, bank, branchNumber, account_number, salary, contract, currentJob, phoneNumber,branch);
    }

    public Employee findEmployeeById(int id) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        if (id < 0) {
            throw new InvalidInputException("ID should be a positive number");
        }
        return employeeRepository.getEmployeeById(id);
    }

    public void printAllEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        printEmployeesByStatus(List.of(WorkStatus.Active, WorkStatus.Fired, WorkStatus.Resigned));
    }

    public void printActiveEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        List<Employee> employees = employeeRepository.getActiveEmployees();
        if (employees.isEmpty()) {
            System.out.println("No matching employees found.");
            return;
        }
        for (Employee e : employees) {
            e.printEmployee();
        }
    }

    public void printOldEmployees() throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        List<Employee> employees = employeeRepository.getNotActiveEmployees();
        if (employees.isEmpty()) {
            System.out.println("No matching employees found.");
            return;
        }
        for (Employee e : employees) {
            e.printEmployee();
        }
    }

    public void printEmployeesByStatus(List<WorkStatus> statuses) throws SQLException, AbilityExistsException, EmployeeNotFoundException {
        List<Employee> employees = employeeRepository.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        boolean found = false;
        for (Employee e : employees) {
            if (statuses.contains(e.getWorkStatus())) {
                e.printEmployee();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching employees found.");
        }
    }

    public Date getNextWeekday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void addHistoryShifts(int id, ShiftType shiftType) throws EmployeeNotFoundException, HistoryShiftExistsException, SQLException, AbilityExistsException {
        Date date = getNextWeekday();
        Employee emp = employeeRepository.getEmployeeById(id);
        emp.addHistoryShifts(date, shiftType);//add shift to employee history shifts
        employeeRepository.addHistoryShiftsById(id,shiftType, date);
    }

    public void removeHistoryShifts(int id, ShiftType shiftType) throws EmployeeNotFoundException, HistoryShiftNotFoundException, SQLException, AbilityExistsException {
        Date date = getNextWeekday();
        Employee emp = employeeRepository.getEmployeeById(id);
        emp.removeHistoryShifts(date, shiftType);//remove shift from employee history shifts
        employeeRepository.removeHistoryShiftsById(id,shiftType,date);
    }

    public void printShiftHistory(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee employee = employeeRepository.getEmployeeById(userId);
        Map<Date, ShiftType> history = employeeRepository.getEmployeeHistory(userId);
        employee.printWorkHistory(history);
    }

    public void printEmployee(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        employeeRepository.getEmployeeById(userId).printEmployee();
    }

    public void isValidEmployee(int userId) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        if (userId < 0) {
            throw new InvalidInputException("ID should be a positive number");
        }
        try {
            Employee employee = employeeRepository.getEmployeeById(userId);
        }
        catch (Exception e){
            throw new EmployeeNotFoundException("employee with ID " + userId + " not found");
        }


//        List<Employee> employees = employeeRepository.getEmployees();
//        for (Employee employee : employees) {
//            if (employee.getId() == userId) {
//                return;
//            }
//        }

    }

    public int getHolidays(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(userId);
        return e.getHolidays();
    }

    public int getSickDays(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(userId);
        return e.getSickDays();
    }

    public void printBankDetails(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(userId);
        BankAccount bank = e.getBankDetails();
        bank.printBankDetails();
    }

    public String getTerms(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(userId);
        return e.getContract();
    }

    public void printAbilities(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(userId);
        e.printAbilities();
    }

    public void printEmploymentTerms(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(userId);
        e.printEmploymentTerms();
    }

    public void printRelevantRoles(int userId) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(userId);
        e.printRelevantRoles();
    }

    public void addAbility(int employeeId, Ability ability) throws EmployeeNotFoundException, AbilityExistsException, SQLException {
//        Employee e = employeeRepository.getEmployeeById(employeeId);
//        e.addAbility(ability);
        employeeRepository.addAbilityById(employeeId,ability);
    }

    public void removeAbility(int employeeId, Ability ability) throws EmployeeNotFoundException, AbilityNotFoundException, SQLException, AbilityExistsException {
//        Employee e = employeeRepository.getEmployeeById(employeeId);
//        e.removeAbility(ability);
        employeeRepository.removeAbilityById(employeeId,ability);
    }

    public String getWorkLimits(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(id);
        return e.getWorkLimits();
    }

    public WorkStatus getWorkStatus(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(id);
        return e.getWorkStatus();
    }

    public void setWorkStatus(int id, WorkStatus newStatus) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
//        Employee e = employeeRepository.getEmployeeById(id);;
//        e.setWorkStatus(newStatus);
        employeeRepository.updateWorkStatusById(id,newStatus);
    }

    public void setWorkLimits(int id, String newLimit) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
//        Employee e = employeeRepository.getEmployeeById(id);
//        e.setWorkLimits(newLimit);
        employeeRepository.updateWorkLimitsById(id,newLimit);
    }

    public void setContract(int id, String contract) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
//        Employee e = employeeRepository.getEmployeeById(id);;
//        e.setContract(contract);
        employeeRepository.updateContractById(id,contract);
    }

    public void setPhoneNumber(int id, String phoneNumber) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
//        Employee e = employeeRepository.getEmployeeById(id);
//        e.setPhoneNumber(phoneNumber);
        employeeRepository.updatePhoneNumberById(id,phoneNumber);
    }

    public void setBankNumber(int id, int bankNum) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
//        Employee e = employeeRepository.getEmployeeById(id);
//        BankAccount bank = e.getBankDetails();
//        bank.setBankNumber(bankNum);
        employeeRepository.updateBankNumberById(id,bankNum);
    }

    public void setBranchNumber(int id, int branchNum) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
//        Employee e = employeeRepository.getEmployeeById(id);
//        BankAccount bank = e.getBankDetails();
//        bank.setBranchNumber(branchNum);
        employeeRepository.updateBranchNumberById(id,branchNum);
    }

    public void setAccountNumber(int id, int accountNum) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
//        Employee e = employeeRepository.getEmployeeById(id);;
//        BankAccount bank = e.getBankDetails();
//        bank.setAccountNumber(accountNum);
        employeeRepository.updateAccountNumberById(id,accountNum);
    }

    public int getSalary(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(id);;
        return e.getSalary();
    }

    public void setSalary(int id, int newSalary) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
//        Employee e = employeeRepository.getEmployeeById(id);
//        e.setSalary(newSalary);
        employeeRepository.updateSalaryById(id,newSalary);
    }

    public String getContract(int id) throws EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(id);
        return e.getContract();
    }

    public String getPhoneNumber(int id) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        Employee e = findEmployeeById(id);
        return e.getPhoneNumber();
    }


    public void reduceSickDays(int id, int amount) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        Employee e = findEmployeeById(id);
        e.reduceSickDay(amount);
        int sickDays = e.getSickDays();
        employeeRepository.updateSickDaysById(id, sickDays);
    }

    public void reduceHoliDays(int id, int amount) throws InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee e = employeeRepository.getEmployeeById(id);
        e.reduceHoliDay(amount);
        int holidays = e.getHolidays();
        employeeRepository.updateHoliDaysById(id, holidays);
    }

    public void addCurrentWeek() throws SQLException {
        addCurrentWeekToHistory();
        removeOldYears();
    }

    public void addCurrentWeekToHistory() throws SQLException {
        yearlyShiftsRepository.addCurrentToHistoryShift();
        yearlyShiftsRepository.moveNextWeekToCurrent();
        yearlyShiftsRepository.resetNextWeek();
    }

    private void removeOldYears() throws SQLException {
        int currentYear = java.time.Year.now().getValue();
        int earliestAllowedYear = currentYear - 7;
        yearlyShiftsRepository.removeOldYears(earliestAllowedYear);
    }

    public void addShift(ShiftType type, Date date) throws ShiftCantBeAddedException, SQLException, InvalidInputException {
        //Shift shift = new Shift(type,date);
//        yearlyShiftsRepository.getNextWeek().addShift(shift);
        yearlyShiftsRepository.addShiftToNextWeek(type, date);
    }

    public void removeShift(Days day, ShiftType shiftType) throws ShiftNotEmptyException, SQLException, InvalidInputException {
        yearlyShiftsRepository.removeShiftFromNextWeek(day, shiftType);
    }

    public boolean allowedToShift(int id, Role role, Days day, ShiftType shiftType, boolean thisWeek) throws InvalidInputException, SQLException {
        WeeklyShifts week;
        if (thisWeek){
            week = yearlyShiftsRepository.getCurrentWeek();
        }
        else {  week = yearlyShiftsRepository.getNextWeek(); }
        Shift shift = week.findShift(day, shiftType);
        //there is no need for a worker in this role
        if (!shift.getRequirements().containsKey(role) ) {
            return false;
        }
        // no need for more workers in this role (for next week only)
        if (!thisWeek && shift.getRequirements().get(role) == 0) {
            return false;
        }
        //already in this shift
        if (!shift.canWorkInShift(id)) {
            return false;
        }

        if (shift.getType() == ShiftType.Morning) {
            if (week.hasEveningShift(day)) {
                ArrayList<Shift> shifts = week.getShifts(day);
                for (Shift shift1 : shifts) {
                    if (shift1.getType() == ShiftType.Evening) {
                        if (!shift1.canWorkInShift(id)) {
                            return false; // can't have worker in two shifts in the same day
                        }
                    }
                }
            }
        } else {
            if (week.hasDayShift(day)) {
                ArrayList<Shift> shifts = week.getShifts(day);
                for (Shift shift1 : shifts) {
                    if (shift1.getType() == ShiftType.Morning) {
                        if (!shift1.canWorkInShift(id)) {
                            return false; // can't have worker in two shifts in the same day
                        }
                    }
                }
            }
        }
        return true;
    }

    public void assignShiftToEmployeeNextWeek(int id, Role role, Days day, ShiftType shiftType) throws CantAssignShiftException, InvalidInputException, EmployeeNotFoundException, SQLException, AbilityExistsException {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee.getWorkStatus() != WorkStatus.Active) {
            throw new CantAssignShiftException("employee work status is not active.");
        }
        Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);
        boolean allowed = allowedToShift(id, role, day, shiftType, false);
        if (allowed) {
            try {
                try {
                    //shift.AddWorker(role, id);
                    yearlyShiftsRepository.addWorkerNextWeek(id,shiftType,day,role,shift.getDate());
//                    addHistoryShifts(id, shiftType);
                } catch (Exception e) {
                    removeEmployeeFromShiftNextWeek(id, role, day, shiftType);
                    throw new CantAssignShiftException("shift cant be assigned" + e.getMessage());
                }
                if (!shift.hasPreference(id)) {
                    System.out.println("Notice: Not a shift that the employee " + id + " wanted");
                }

                if (!employee.getWorkLimits().equals("None")) {
                    System.out.println("Notice: Employee " + id + " has work limitations: " + employee.getWorkLimits());
                }
            } catch (Exception e) {
                throw new CantAssignShiftException("shift cant be assigned" + e.getMessage());
            }
        } else {
            throw new CantAssignShiftException("employee is not allowed to this shift");
        }
    }

    public void removeEmployeeFromShiftNextWeek(int id, Role role, Days day, ShiftType shiftType) throws InvalidInputException, CantAssignShiftException, SQLException {
        Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);
        if (id < 0) {
            throw new InvalidInputException("ID should be a positive number");
        }
        try {
            //shift.removeWorker(role, id);
            yearlyShiftsRepository.removeWorkerNextWeek(id,role,shiftType,day,shift.getDate());
//            try {
//                removeHistoryShifts(id, shiftType);
//            } catch (Exception e) {
//                assignShiftToEmployeeNextWeek(id, role, day, shiftType);
//            }
        } catch (Exception e) {
            throw new CantAssignShiftException(e.getMessage());
        }
    }

    public void updateEmployeeAssignmentInCurrentWeek(int oldId, int newId, Role role, Days day, ShiftType shiftType) throws InvalidInputException, CantAssignShiftException, SQLException, AbilityExistsException, EmployeeNotFoundException {
        Shift shift = yearlyShiftsRepository.getCurrentWeek().findShift(day, shiftType);
        if (oldId < 0 || newId < 0) {
            throw new InvalidInputException("ID should be a positive number");
        }

        boolean allowed = allowedToShift(newId, role, day, shiftType,true);
        if (!allowed) throw new CantAssignShiftException("employee "+newId+" is not allowed to this shift");

        Employee employee = employeeRepository.getEmployeeById(newId);
        if (employee.getWorkStatus() != WorkStatus.Active) {
            throw new CantAssignShiftException("employee "+newId+" work status is not active.");
        }

        try {
//            shift.removeWorker(role, oldId);
//            shift.AddWorker(role, newId);
            yearlyShiftsRepository.updateEmployeeAssignmentInCurrentWeek(role,oldId,newId,shiftType,day,shift.getDate());
            if (!shift.hasPreference(newId)) {
                System.out.println("Notice: Not a shift that the employee " + newId + " wanted");
            }
            if (!employee.getWorkLimits().equals("None")) {
                System.out.println("Notice: Employee " + newId + " has work limitations: " + employee.getWorkLimits());
            }

        } catch (Exception e) {
            throw new CantAssignShiftException("shift cant be assigned " + e.getMessage());
        }

    }

    public void printWeeklyShiftsDetails() throws SQLException, InvalidInputException {
        yearlyShiftsRepository.getNextWeek().printWeeklyShiftsDetails();
    }

    public void printCurrentWeekShiftsDetails() throws SQLException, InvalidInputException {
        yearlyShiftsRepository.getCurrentWeek().printWeeklyShiftsDetails();
    }

    public boolean isShiftExists(Days day, ShiftType shiftType) throws SQLException, InvalidInputException {
        ArrayList<Shift> shifts = yearlyShiftsRepository.getNextWeek().getShifts(day);
        for (Shift shift : shifts) {
            if (shift.getType() == shiftType) {
                return true;
            }
        }
        return false;
    }

    public void addRoleToShift(Role role, int amount, Days day, ShiftType shiftType) throws InvalidInputException, RoleExistsException, SQLException {
        //Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);

        yearlyShiftsRepository.addRoleNextWeekShift(role,amount,shiftType,day);
    }

    public void viewShiftPreferences(Days day, ShiftType shiftType) throws InvalidInputException, SQLException {
        Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);
        shift.printPreferences();
    }

    public boolean areShiftsReady() throws SQLException, InvalidInputException {
        boolean ready = true;
        for (Days day : Days.values()) {
            ArrayList<Shift> shifts = yearlyShiftsRepository.getNextWeek().getShifts(day);
            for (Shift shift : shifts) {
                if (!shift.shiftIsReady()) {
                    ready = false;
                }
            }
        }
        return ready;
    }

    public void decreaseAmountSpecificRole(Role role, int amount, Days day, ShiftType shiftType) throws InvalidInputException, RoleNotFoundException, SQLException, ShiftHasManager {
        Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);
        shift.decreaseAmountSpecificRole(role, amount);
        yearlyShiftsRepository.updateRoleNextWeekShift(role,shift.getRequirements().get(role),shiftType,day);
    }

    public void increaseAmountSpecificRole(Role role, int amount, Days day, ShiftType shiftType) throws InvalidInputException, RoleNotFoundException, SQLException {
        Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);
        shift.increaseAmountSpecificRole(role, amount);
        yearlyShiftsRepository.updateRoleNextWeekShift(role,shift.getRequirements().get(role),shiftType,day);
    }

    public void setShiftTime(String start, String end, Days day, ShiftType shiftType) throws InvalidInputException, SQLException {
//        WeeklyShifts weeklyShifts = yearlyShiftsRepository.getNextWeek();
//        weeklyShifts.setShiftTime(start, end, day, shiftType);
        yearlyShiftsRepository.updateShiftTimeNextWeekShift(shiftType,day,start + "-" + end);
    }

    public void addPreference(int userID, Days day, ShiftType shiftType) throws InvalidInputException, SQLException {
        //Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);
        //shift.addPreference(userID);
        yearlyShiftsRepository.addShiftPreferencesNextWeekShift(shiftType,day,userID);
    }

    public void removePreference(int userID, Days day, ShiftType shiftType) throws InvalidInputException, SQLException {
        //Shift shift = yearlyShiftsRepository.getNextWeek().findShift(day, shiftType);
        //shift.removePreference(userID);
        yearlyShiftsRepository.removeShiftPreferencesNextWeekShift(shiftType,day,userID);
    }

    public void printPreferredShiftsDetails(int userId) throws SQLException, InvalidInputException {
        yearlyShiftsRepository.getNextWeek().printPreferredShiftsDetails(userId);
    }

    public void printNonePreferredShiftsDetails(int userId) throws SQLException, InvalidInputException {
        yearlyShiftsRepository.getNextWeek().printNonePreferredShiftsDetails(userId);
    }

    public void printAvailableYears() throws SQLException {
        List<Integer> years = yearlyShiftsRepository.getAvailableYears();
        Collections.sort(years);
        if (years.isEmpty()) {
            System.out.println("There are no shifts available in the system..");
        } else {
            System.out.println("Available years in the system:");
            for (int year : years) {
                System.out.println("- " + year);
            }
        }
    }

    public void printShiftsInYear(int year) throws InvalidInputException, SQLException {
        if (year <= 0) {
            throw new InvalidInputException("Year cannot be negative.");
        }
        Map<Date, Map<ShiftType, List<Integer>>> shiftsByDate = yearlyShiftsRepository.getYearlyShifts(year);

        System.out.println("Shifts for year: " + year);
        for (Map.Entry<Date, Map<ShiftType, List<Integer>>> entry : shiftsByDate.entrySet()) {
            Date date = entry.getKey();
            Map<ShiftType, List<Integer>> shiftTypes = entry.getValue();

            for (Map.Entry<ShiftType, List<Integer>> shiftEntry : shiftTypes.entrySet()) {
                ShiftType type = shiftEntry.getKey();
                List<Integer> workers = shiftEntry.getValue();

                String workersStr = workers.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                System.out.println("Date: "+ date + ", Shift: " + type + ", Workers: " + workersStr);
            }
        }
    }

    public void printShiftsInWeekAndYear(int year, int weekNum) throws InvalidInputException, SQLException {
        if (year <= 0) {
            throw new InvalidInputException("Year cannot be negative.");
        }
        if (weekNum < 0 || weekNum > 52) {
            throw new InvalidInputException("Week number must be between 0 and 52.");
        }

        Map<Date, Map<ShiftType, List<Integer>>> shiftsByDate = yearlyShiftsRepository.getYearlyShifts(year);
        boolean found = false;
        System.out.println("Shifts for year: " + year + ", week: " + weekNum);

        for (Map.Entry<Date, Map<ShiftType, List<Integer>>> entry : shiftsByDate.entrySet()) {
            Date date = entry.getKey();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int entryYear = cal.get(Calendar.YEAR);
            int entryWeek = cal.get(Calendar.WEEK_OF_YEAR) - 1;

            if (entryYear == year && entryWeek == weekNum) {
                found = true;
                Map<ShiftType, List<Integer>> shiftTypes = entry.getValue();

                for (Map.Entry<ShiftType, List<Integer>> shiftEntry : shiftTypes.entrySet()) {
                    ShiftType type = shiftEntry.getKey();
                    List<Integer> workers = shiftEntry.getValue();

                    String workersStr = workers.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));

                    System.out.println("Date: " + date + ", Shift: " + type + ", Workers: " + workersStr);
                }
            }
        }

        if (!found) {
            throw new InvalidInputException("No weekly shift found for year " + year + " and week " + weekNum);
        }
    }

    public void printUserWeekShifts(int userId, Date today) throws InvalidInputException, EmployeeNotFoundException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        WeeklyShifts thisWeek = yearlyShiftsRepository.getCurrentWeek();
        boolean foundWorker = false;
        for (Days day : Days.values()) {
            ArrayList<Shift> shifts = thisWeek.getShifts(day);
            for (Shift shift : shifts) {
                for (ArrayList<Integer> workerList : shift.getWorkers().values()) {
                    if (workerList.contains(userId)) {
                        foundWorker = true;
                        shift.printShiftDetails();
                    }
                }
            }

        }
        if (!foundWorker) {
            throw new EmployeeNotFoundException("The employee is not assigned to shifts this week...");
        }
    }

    public void printManagerWeekShifts(int userId, Date today) throws InvalidInputException, EmployeeNotFoundException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        WeeklyShifts thisWeek = yearlyShiftsRepository.getCurrentWeek();
        boolean foundManager = false;
        for (Days day : Days.values()) {
            ArrayList<Shift> shifts = thisWeek.getShifts(day);
            for (Shift shift : shifts) {
                if (shift.getManagerID() == userId) {
                    foundManager = true;
                    shift.printShiftDetails();
                }
            }
        }
        if (!foundManager) {
            throw new EmployeeNotFoundException("The employee is not assigned to Shift Manager role in the shifts this week...");
        }
    }

    public void isUserExists(String username) throws UserNotFound, SQLException {
        this.userRepository.getUserByUserName(username);
    }

    public void isJobMatchUser(String username, Job job) throws UserNotFound, SQLException {
        User user = userRepository.getUserByUserName(username);
        if (user.getJob() != job) {
            throw new UserNotFound("The selected job does not match username.");
        }
    }

    public void isPasswordMatchUser(String username, String password) throws UserNotFound, SQLException {
        User user = userRepository.getUserByUserName(username);
        if (!Objects.equals(user.getPassword(), password)) {
            throw new UserNotFound("Incorrect password.");
        }
    }

    public int getId(String username) throws UserNotFound, SQLException {
        User user = userRepository.getUserByUserName(username);
        return user.getId();
    }


    public boolean isEmployeeWorksInCurrnetShift(int employeeIdToReplace,Role role, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        WeeklyShifts currentWeek = yearlyShiftsRepository.getCurrentWeek();
        Shift shift = currentWeek.findShift(shiftDay,shiftType);
        return shift.isEmployeeInShift(employeeIdToReplace,role);
    }

    public boolean isDateSATURDAY(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    public void isThereEmployees() throws AbilityExistsException, SQLException, EmployeeNotFoundException {
        if (!employeeRepository.isThereEmployees()){
            throw new EmployeeNotFoundException("no employees found.");
        }
    }

    public void printAvaliableSuperBranches() throws SQLException {
        ArrayList<Integer> superBranches = employeeRepository.getAvaliableSuperBranches();
        if (!superBranches.isEmpty()) {
            System.out.println("Available super branch: ");
            for (Integer branch : superBranches) {
                System.out.println("# " + branch);
            }
        } else {
            System.out.println("No super branch assigned.");
        }
    }

    public boolean isSuperBranchExists(int branch) throws SQLException {
        if (employeeRepository.getAvaliableSuperBranches().contains(branch)){
            return true;
        }
        return false;
    }

    public int getSuperBranch(int id) throws SQLException {
        return employeeRepository.getSuperBranch(id);
    }

    public void updateSuperBranch(int id, int num) throws SQLException, InvalidInputException {
        if(isSuperBranchExists(num))
            employeeRepository.updateSuperBranch(id, num);
        else
            throw new InvalidInputException("super branch is not exist");
    }

    public void closeConnections() {
        if (userRepository instanceof UserRepositoryImpl)
            ((UserRepositoryImpl) userRepository).close();
        if (employeeRepository instanceof EmployeeRepositoryImpl)
            ((EmployeeRepositoryImpl) employeeRepository).close();
        if (yearlyShiftsRepository instanceof YearlyShiftsRepositoryImpl)
            ((YearlyShiftsRepositoryImpl) yearlyShiftsRepository).close();
    }

}
