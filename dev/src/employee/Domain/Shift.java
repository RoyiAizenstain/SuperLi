package employee.Domain;

import employee.Enums.*;
import employee.Exceptions.*;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Shift {
    private ShiftType type;
    private Date date;
    private Map<Role, Integer> requirements; //how many people needed from each role
    private Map<Role, ArrayList<Integer>> workers; //which workers in each role
    private int managerID; // id of the manager
    private ArrayList<Integer> employeePreferences; //id of the employees that prefer to work in this shift
    private String time;

    public Shift(ShiftType type, Date date) {
        this.type = type;
        this.date = date;
        this.requirements = new HashMap<>();
        this.requirements.put(Role.Shift_Manager, 1); //each shift has a manager
        this.workers = new HashMap<>();
        this.managerID = 0;
        this.employeePreferences = new ArrayList<>();
        if(this.type == ShiftType.Morning) {
            this.time = "08:00-14:00"; //default hours
        }
        else {  //Evening Shift
            this.time = "14:00-20:00";} //default hours
    }

    public ShiftType getType() {
        return this.type;
    }

    public Date getDate() {
        return this.date;
    }

    public String getTime(){ return this.time;}

    public void updateShiftTime(String time){
        this.time = time;
    }

    //increase the amount in a specific role
    public void increaseAmountSpecificRole(Role role, int amount) throws RoleNotFoundException, InvalidInputException {
        if (!this.requirements.containsKey(role)){
            throw new RoleNotFoundException("role not exists");
        }
        if(role == Role.Shift_Manager && this.requirements.get(Role.Shift_Manager) > 0){
            throw new InvalidInputException("There is only one manager in a shift");
        }
        amount += this.requirements.get(role);
        this.requirements.put(role, amount);
    }

    //decrease the amount in a specific role
    public void decreaseAmountSpecificRole(Role role, int amount) throws RoleNotFoundException, InvalidInputException, ShiftHasManager {
        if (!this.requirements.containsKey(role)){
            throw new RoleNotFoundException("role not exists");
        }
        int res = this.requirements.get(role);
        res -= amount;
        if (res <0){
            throw new InvalidInputException("amount is more than requirement or an employee is assigned to this role.");
        }
        if(role == Role.Shift_Manager && res == 0){
            throw new ShiftHasManager("Each shift must have a shift manager");
        }
        this.requirements.put(role, res);
    }

    public void addRole(Role role, int amount) throws InvalidInputException, RoleExistsException {
        if (amount <= 0) {
            throw new InvalidInputException("amount should be positive");
        }
        if(this.requirements.containsKey(role)){
            throw new RoleExistsException("role already exists");
        }
        if (role == Role.Shift_Manager){
            throw new InvalidInputException("There is only one manager in a shift");
        }
        this.requirements.put(role, amount);

    }

    public void removeRole(Role role) throws RoleNotFoundException {
        if (this.requirements.containsKey(role)) {
            this.workers.remove(role);
            this.requirements.remove(role);
        }
        else {
            throw new RoleNotFoundException("Role not exists");
        }
    }

    public boolean hasPreference(int id){
        return this.employeePreferences.contains(id);
    }

    public void addPreference(int id) throws InvalidInputException {
        if (id < 0) {
           throw new InvalidInputException("id should be a positive number");
        }
        if (this.employeePreferences.contains(id)) {
            throw new InvalidInputException("Employee " + id + " already preferred this shift...");
        }
        this.employeePreferences.add(id);
    }

    public void removePreference(int id) throws InvalidInputException {
        if (id < 0) {
            throw new InvalidInputException("ID should be a positive number");
        }
        if (this.employeePreferences.contains(id)) {
            this.employeePreferences.remove(Integer.valueOf(id));
        }
        else {
            throw new InvalidInputException("employee id not in this shift preferences");
        }

    }

    public boolean canWorkInShift(int id) {
        if (id < 0) {
            return false;
        }
        for (Map.Entry<Role, ArrayList<Integer>> entry : this.workers.entrySet()) {
            if (entry.getValue().contains(id)) {
                return false;
            }
        }
        return true;
    }

    public Map<Role, Integer> getRequirements(){
        return this.requirements;
    }

    public void setRequirements(Map<Role, Integer> requirements){
        this.requirements = requirements;
    }

    public void setWorkers(Map<Role, ArrayList<Integer>> workers){
        this.workers = workers;
    }

    public void setPreferences(ArrayList<Integer> preferences){
        this.employeePreferences = preferences;
    }

    public ArrayList<Integer> getEmployeePreferences() {
        return this.employeePreferences;
    }

    public void changeManagerShift(int id) {
        this.managerID = id;
    }

    public boolean hasManager() {
       if(this.managerID == 0){
           return false;
       }
       return true;
    }

    public boolean allRolesAssigned() {
        for (int num : this.requirements.values()){
            if (num != 0) {
                printShiftDetails();
                printUnfilledRoles();
                return false;
            }
        }
        return true;
    }

    public boolean shiftIsEmpty() { //no workers in the shift
        if (this.workers.isEmpty()){
            if (hasManager()){
                return false;
            }
            return true;
        }
        return false;
    }

    public void printUnfilledRoles() {
        System.out.println("There are unfilled roles:");
        for (Map.Entry<Role, Integer> entry : this.requirements.entrySet()) {
            Role role = entry.getKey();
            int count = entry.getValue();
            if (count != 0) {
                System.out.println("\tRole: " + role + ", Number required: " + count);
            }
        }
    }

    public boolean shiftIsReady(){
        return allRolesAssigned();
    }

    public Map<Role, ArrayList<Integer>> getWorkers(){
        return this.workers;
    }

    public Days getDayByShift(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return Days.values()[dayOfWeek - 1];
    }

    public void setShiftTime(String start, String end) throws InvalidInputException {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        if (startTime.isBefore(endTime)) {
            this.time = start + "-" + end;
        } else {
            throw new InvalidInputException("Start time must be before end time.");
        }
    }

    public int getManagerID() {
        return managerID;
    }

    public void printShiftDetails() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Date: " + dateFormat.format(date));
        System.out.println("Shift Type: " + type + ", Time: " + time);

        if (requirements.isEmpty()) {
            System.out.println("Roles: Not yet defined.");
        }
        else {
            System.out.println("Roles:");
            for (Role role : Role.values()) {
                String roleName = role.name();
                int requiredCount = requirements.getOrDefault(role, -1);
                ArrayList<Integer> assigned = workers.getOrDefault(role, new ArrayList<>());

                List<String> ids = new ArrayList<>();
                for (int id : assigned) {
                    ids.add(String.valueOf(id));
                }
                while (ids.size() < requiredCount) {
                    ids.add("None");
                }

                if (requiredCount >= 0) {
                    System.out.println("- "+roleName.replace('_', ' ') + " - " + String.join(", ", ids));
                }
            }
        }

    }

    public void printPreferences(){
        System.out.print("Employee Preferences: ");
        if (this.employeePreferences.isEmpty()) {
            System.out.print("None");
        }
        else {
            for (Integer integer : this.employeePreferences) {
                System.out.print(integer + " ");
            }
        }
        System.out.println();
    }

    public void AddWorker(Role role, int id) throws InvalidInputException, RoleNotFoundException {
        if (id < 0) {
            throw new InvalidInputException("id should be a positive number");
        }
        //decrease the required amount of workers in this role
        try {
            decreaseAmountSpecificRole(role, 1);
        }
        catch (ShiftHasManager e){
            this.requirements.put(role, 0);
        }
        if(workers.containsKey(role)){
            workers.get(role).add(id);
        }
        else {
            // it is the first worker in this role
            ArrayList<Integer> workersList = new ArrayList<>();
            workersList.add(id);
            workers.put(role, workersList);
        }
        if (role==Role.Shift_Manager) managerID = id;
    }

    public void removeWorker(Role role, int id) throws InvalidInputException, RoleNotFoundException, EmployeeNotFoundException {
        if (id < 0) {
            throw new InvalidInputException("ID should be a positive number");
        }
        if(workers.containsKey(role) && workers.get(role).contains(id)){
            workers.get(role).remove(Integer.valueOf(id));
            //increase the required amount of workers in this role
            increaseAmountSpecificRole(role, 1);
            if (workers.get(role).isEmpty()){
                workers.remove(role);
            }
        }
        else {
            throw new EmployeeNotFoundException("The employee is not assigned to this shift under the role \"" + role.name().replace('_', ' ') + "\".");
        }
        if (role==Role.Shift_Manager) managerID = 0;
    }

    public boolean isEmployeeInShift(int id,Role role){
        if (workers.containsKey(role)) {
            ArrayList<Integer> ids = workers.get(role);
            return ids.contains(id);
        }
        return false;
    }

}











