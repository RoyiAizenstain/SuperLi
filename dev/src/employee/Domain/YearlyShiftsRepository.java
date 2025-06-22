package employee.Domain;

import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.*;
import java.util.*;
import java.util.Date;


public interface YearlyShiftsRepository {

    void connectDao(String url) throws InvalidInputException, SQLException;

    void connectDaoEmpty(String url) throws InvalidInputException, SQLException;

    void connectDaoTEST(String url) throws InvalidInputException, SQLException;

    void addShiftToNextWeek(ShiftType type, Date date) throws SQLException, ShiftCantBeAddedException;

    void removeShiftFromNextWeek(Days day, ShiftType shiftType) throws SQLException, InvalidInputException, ShiftNotEmptyException;

    WeeklyShifts getNextWeek() throws SQLException, InvalidInputException;

    WeeklyShifts getCurrentWeek() throws SQLException, InvalidInputException;

    void addCurrentToHistoryShift() throws SQLException;

    void resetNextWeek() throws SQLException;

    void moveNextWeekToCurrent() throws SQLException;

    void removeOldYears(int limitYear) throws SQLException;

    void addWorkerNextWeek(int id,ShiftType shiftType,Days day,Role role,Date date) throws SQLException, InvalidInputException, RoleNotFoundException;

    void removeWorkerNextWeek(int id,Role role,ShiftType shiftType,Days day,Date date) throws SQLException, InvalidInputException, RoleNotFoundException, EmployeeNotFoundException;

    void updateEmployeeAssignmentInCurrentWeek(Role role,int oldId, int newId,ShiftType shiftType,Days day,Date date) throws SQLException, InvalidInputException, RoleNotFoundException, EmployeeNotFoundException;

    void addRoleNextWeekShift(Role role,int amount,ShiftType shiftType,Days day) throws SQLException, InvalidInputException, RoleExistsException;

    void updateRoleNextWeekShift(Role role,int amount,ShiftType shiftType,Days day) throws SQLException;

    void updateShiftTimeNextWeekShift(ShiftType shiftType,Days day,String time) throws SQLException;

    void addShiftPreferencesNextWeekShift(ShiftType shiftType,Days day,int id) throws SQLException, InvalidInputException;

    void removeShiftPreferencesNextWeekShift(ShiftType shiftType,Days day,int id) throws SQLException, InvalidInputException;

    List<Integer> getAvailableYears() throws SQLException;

    Map<Date, Map<ShiftType, List<Integer>>> getYearlyShifts(int year) throws SQLException, InvalidInputException;
}
