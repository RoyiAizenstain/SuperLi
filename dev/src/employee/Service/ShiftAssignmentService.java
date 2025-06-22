package employee.Service;

import employee.Domain.DomainController;
import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.SQLException;
import java.util.Date;

public class ShiftAssignmentService {
    DomainController controller = new DomainController();

    public void saveAndReset() throws SQLException {
        controller.addCurrentWeek();
    }

    public void assignShiftToEmployee(int id, Role role, Days day, ShiftType shiftType) throws CantAssignShiftException, InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
      controller.assignShiftToEmployeeNextWeek(id, role, day, shiftType);
    }

    public void removeEmployeeFromShiftNextWeek(int id, Role role, Days day, ShiftType shiftType) throws  InvalidInputException, CantAssignShiftException, SQLException {
        controller.removeEmployeeFromShiftNextWeek(id, role,day,shiftType);
    }

    public void updateEmployeeAssignmentInCurrentWeek(int oldId, int newId, Role role, Days day, ShiftType shiftType) throws InvalidInputException, CantAssignShiftException, SQLException, AbilityExistsException, EmployeeNotFoundException {
        controller.updateEmployeeAssignmentInCurrentWeek(oldId, newId, role, day, shiftType);
    }

    public void addShift(ShiftType type, Date date) throws ShiftCantBeAddedException, InvalidInputException, SQLException {
        controller.addShift(type,date);
    }

    public void removeShift(Days day, ShiftType shiftType) throws ShiftNotEmptyException, InvalidInputException, SQLException {
        controller.removeShift(day, shiftType);
    }

    public void printWeeklyShiftsDetails() throws InvalidInputException, SQLException {
        controller.printWeeklyShiftsDetails();
    }

    public void printCurrentWeekShiftsDetails() throws InvalidInputException, SQLException {
        controller.printCurrentWeekShiftsDetails();
    }

    public boolean isShiftExists(Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        return controller.isShiftExists(shiftDay, shiftType);
    }

    public void addRoleToShift(Role role, int amount, Days shiftDay, ShiftType shiftType) throws InvalidInputException, RoleExistsException, SQLException {
        controller.addRoleToShift(role, amount,shiftDay, shiftType);
    }

    public void viewShiftPreferences(Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        controller.viewShiftPreferences(shiftDay, shiftType);
    }

    public boolean areShiftsReady() throws InvalidInputException, SQLException {
        return controller.areShiftsReady();
    }

    public void decreaseAmountSpecificRole(Role role, int amount, Days shiftDay, ShiftType shiftType) throws InvalidInputException, RoleNotFoundException, SQLException, ShiftHasManager {
        controller.decreaseAmountSpecificRole(role, amount, shiftDay, shiftType);
    }

    public void increaseAmountSpecificRole(Role role, int amount, Days shiftDay, ShiftType shiftType) throws InvalidInputException, RoleNotFoundException, SQLException {
        controller.increaseAmountSpecificRole(role, amount, shiftDay, shiftType);
    }

    public void findEmployeeById(int id) throws InvalidInputException, EmployeeNotFoundException, AbilityExistsException, SQLException {
        controller.findEmployeeById(id);
    }

    public void setShiftTime(String start, String end, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        controller.setShiftTime(start, end, shiftDay, shiftType);
    }

    public void addPreference(int userID, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        controller.addPreference(userID, shiftDay, shiftType);
    }

    public void removePreference(int userID, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        controller.removePreference(userID, shiftDay, shiftType);
    }

    public void printPreferredShiftsDetails(int userId) throws InvalidInputException, SQLException {
        controller.printPreferredShiftsDetails(userId);
    }

    public void printNonPreferredShiftsDetails(int userId) throws InvalidInputException, SQLException {
        controller.printNonePreferredShiftsDetails(userId);
    }

    public boolean isEmployeeWorksInCurrnetShift(int employeeIdToReplace,Role role, Days shiftDay, ShiftType shiftType) throws InvalidInputException, SQLException {
        return controller.isEmployeeWorksInCurrnetShift(employeeIdToReplace, role, shiftDay, shiftType);
    }

    public boolean isDateSATURDAY(){
        return controller.isDateSATURDAY();
    }
}
