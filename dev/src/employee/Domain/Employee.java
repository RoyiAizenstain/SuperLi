package employee.Domain;

import employee.Enums.*;
import employee.Exceptions.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class Employee {
    private String name;
    private int id;
    private BankAccount bankDetails;
    private Date hiringDate;
    private int salary;
    private String contract;
    private ArrayList<Role> relevantRoles;
    private ArrayList<Ability> abilities;
    private Map<Date, ShiftType> workHistory;
    private String workLimits;
    private int sickDays;
    private int holidays;
    private WorkStatus workStatus;
    private Job currentJob;
    private String phoneNumber;
    private int branch;

    public Employee(String name, int id, BankAccount bank_details,
                    int salary, String contract, Job currentJob, String phoneNumber, int branch) {
        this.name = name;
        this.id = id;
        this.bankDetails = bank_details;
        this.salary = salary;
        this.contract = contract;
        this.relevantRoles = new ArrayList<>();
        this.abilities = new ArrayList<>();
        this.workHistory = new HashMap<>();
        this.currentJob = currentJob;
        this.phoneNumber = phoneNumber;
        this.branch = branch;
    }

    public void printEmployee(){
        System.out.println("Name: " + this.name + ", ID: " + this.id + ", Phone Number: " + this.phoneNumber + ", Super Branch: " + branch);
    }

    public void setSuperBranch(int num){
        this.branch = num;
    }

    public void setHiringDate(Date hiringDate){
        this.hiringDate = hiringDate;
    }

    public void setSickDays(int sickDays){
        this.sickDays = sickDays;
    }

    public void setHolidays(int holidays){
        this.holidays = holidays;
    }

    public int getId() {
        return this.id;
    }

    public int getSuperBranch(){
        return this.branch;
    }

    public BankAccount getBankDetails() {
        return this.bankDetails;
    }

    public Date getHiringDate() {
        return this.hiringDate;
    }

    public int getSalary() {
        return this.salary;
    }

    public String getContract() {
        return this.contract;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public ArrayList<Role> getRelevantRoles() {
        return this.relevantRoles;
    }

    public ArrayList<Ability> getAbilities() {
        return this.abilities;
    }

    public String getWorkLimits() {
        return this.workLimits;
    }

    public int getSickDays() {
        return this.sickDays;
    }

    public int getHolidays() {
        return this.holidays;
    }

    public WorkStatus getWorkStatus() {
        return this.workStatus;
    }

    public void setSalary(int newSalary) throws InvalidInputException {
        if (newSalary <= 0) {
            throw new InvalidInputException("salary should be a positive number");
        }
        this.salary = newSalary;
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidInputException {
        if (phoneNumber.length() != 10) {
            throw new InvalidInputException("Phone number should be 10 digits");
        }
        if (!phoneNumber.matches("\\d{10}")) {
            throw new InvalidInputException("Phone number should contain only digits");
        }
        if (phoneNumber.charAt(0) != '0') {
            throw new InvalidInputException("Phone number should start with 0");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setContract(String newContract) {
        this.contract = newContract;
    }

    public void addRole(Role role) throws RoleExistsException {
        if (this.relevantRoles.contains(role)) {
            throw new RoleExistsException("Role already exists");
        } else {
            this.relevantRoles.add(role);
        }
    }

    public void removeRole(Role role) throws RoleNotFoundException {
        if (this.relevantRoles.contains(role)) {
            this.relevantRoles.remove(role);
        }
        else {
            throw new RoleNotFoundException("Role not exists in shift");
        }
    }

    public void addAbility(Ability ability) throws AbilityExistsException {
        if (this.abilities.contains(ability)) {
            throw new AbilityExistsException("Ability already exists");
        } else {
            this.abilities.add(ability);
            updateRelevantRoles();
        }
    }

    public void removeAbility(Ability ability) throws AbilityNotFoundException {
        if (this.abilities.contains(ability)) {
            this.abilities.remove(ability);
            updateRelevantRoles();
        }
        else {
            throw new AbilityNotFoundException("ability not exists");
        }
    }

    public void addHistoryShifts(Date date, ShiftType type) throws HistoryShiftExistsException {
        if (this.workHistory.containsKey(date) && this.workHistory.get(date) == type) {
            throw new HistoryShiftExistsException("the shift already exists in employee work history");
        }
        this.workHistory.put(date,type);
    }

    public void removeHistoryShifts(Date date, ShiftType type) throws HistoryShiftNotFoundException {
        if (!(this.workHistory.containsKey(date) && this.workHistory.get(date) == type)) {
            throw new HistoryShiftNotFoundException("the shift not exists in employee work history");
        }
        this.workHistory.remove(date, type);
    }

    public void setWorkLimits(String newLimit) {
        this.workLimits = newLimit;
    }

    public void reduceSickDay(int amount) throws InvalidInputException {
        if (amount<=0){
            throw new InvalidInputException("Amount to reduce cannot be negative");
        }
        if (amount > this.sickDays) {
            throw new IllegalArgumentException("Cannot reduce more sick days than available");
        }
        this.sickDays -= amount;
    }

    public void reduceHoliDay(int amount) throws InvalidInputException {
        if (amount<=0){
            throw new InvalidInputException("Amount to reduce cannot be negative");
        }
        if (amount > this.holidays) {
            throw new IllegalArgumentException("Cannot reduce more holidays than available");
        }
        this.holidays -= amount;
    }

    public void setWorkStatus(WorkStatus workstatus) {
        this.workStatus = workstatus;
    }

    public boolean canWorkInThisRole(Role role) {
        if (this.relevantRoles.contains(role)) {
            return true;
        }
        return false;
    }

    public void updateRole(Role role){
        if (!this.relevantRoles.contains(role)) {
            this.relevantRoles.add(role);
        }
    }

    public void updateRelevantRoles() {
        this.relevantRoles = new ArrayList<>();
        for (Ability ability : this.abilities) {
            if (ability == Ability.Authorized_cancellation_card) {
                for (Ability ability2 : this.abilities) {
                    if (ability2 == Ability.Team_Management) {
                        updateRole(Role.Shift_Manager);
                    }
                }
            } else if (ability == Ability.Operate_cash_register) {
                updateRole(Role.Cashier);
            } else if (ability == Ability.Knowledge_terminals_and_inventory_system) {
                updateRole(Role.Store_Keeper);
            }
            else if(ability == Ability.Driving_license){
                updateRole(Role.Driver);
            } else if (ability == Ability.Operate_cutting_and_packaging_machine) {
                updateRole(Role.Deli_worker);
            } else if (ability == Ability.Qualified_with_industrial_baking_ovens) {
                updateRole(Role.Bakery_worker);
            } else if (ability == Ability.Operating_cutting_machine) {
                updateRole(Role.Butcher);
            } else if (ability == Ability.Knowledge_of_commercial_law_procedures) {
                updateRole(Role.Shelf_organizer);
            } else if (ability == Ability.Certification_for_cleaning_car) {
                updateRole(Role.Cleaning_Worker);
            } else if (ability == Ability.Weapons_license) {
                updateRole(Role.Security_Officer);
            }
        }
    }

    public void printEmploymentTerms(){
        System.out.println("Employment Status: " + getWorkStatus());
        System.out.println("Hiring Date: " + new SimpleDateFormat("dd/MM/yyyy").format(getHiringDate()));
        System.out.println("Salary: " + getSalary());
        System.out.println("Contract: " + getContract());
        System.out.println("Work Limits: " + getWorkLimits());
    }

    public void printAbilities() {

        ArrayList<Ability> abilities = getAbilities();
        if (!abilities.isEmpty()) {
            System.out.println("Employee Abilities:");
            for (Ability ability : abilities) {
                System.out.println("- " + ability.name().replace('_', ' '));
            }
        } else {
            System.out.println("No abilities assigned.");
        }
    }

    public void printRelevantRoles() {
        ArrayList<Role> roles = getRelevantRoles();
        if (!roles.isEmpty()) {
            System.out.println("Employee Relevant Roles:");
            for (Role role : roles) {
                System.out.println("- " + role.name().replace('_', ' '));
            }
        } else {
            System.out.println("No relevant roles assigned.");
        }
    }

    public void printWorkHistory(Map<Date, ShiftType> history){
        if (history.isEmpty()) {
            System.out.println("No shifts found in your shift history.");
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Map.Entry<Date, ShiftType> entry : history.entrySet()) {
            String formattedDate = dateFormat.format(entry.getKey());
            ShiftType shift = entry.getValue();
            System.out.println("Date: " + formattedDate + ", Shift type: " + shift);
        }
    }



}
