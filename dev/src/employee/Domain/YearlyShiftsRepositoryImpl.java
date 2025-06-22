package employee.Domain;

import employee.DataAccess.YearlyShiftsDao;
import employee.Dto.*;
import employee.Enums.*;
import employee.Exceptions.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class YearlyShiftsRepositoryImpl implements YearlyShiftsRepository{
    private YearlyShiftsDao yearlyShiftsDao;
    private static YearlyShiftsRepositoryImpl instance;
    private WeeklyShifts currentWeek;
    private WeeklyShifts nextWeek;

    private YearlyShiftsRepositoryImpl(){}

    public static YearlyShiftsRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new YearlyShiftsRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void connectDao(String url) throws InvalidInputException, SQLException {
        this.yearlyShiftsDao = new YearlyShiftsDao(url);
        currentWeek = loadCurrentWeek();
        nextWeek = loadNextWeek();
    }

    @Override
    public void connectDaoEmpty(String url) throws InvalidInputException, SQLException {
        this.yearlyShiftsDao = new YearlyShiftsDao(url);
        currentWeek = new WeeklyShifts();
        nextWeek = new WeeklyShifts();
    }

    @Override
    public void connectDaoTEST(String url) throws InvalidInputException, SQLException {
        this.yearlyShiftsDao = new YearlyShiftsDao(url);
        nextWeek = loadNextWeek();
    }

    public WeeklyShifts loadNextWeek() throws SQLException, InvalidInputException {
        WeeklyShiftsDTO weeklyShiftsDTO = yearlyShiftsDao.getNextWeek();
        WeeklyShifts weeklyShifts = new WeeklyShifts();

        for (Days day : Days.values()) {
            List<ShiftDTO> shiftDTOs = weeklyShiftsDTO.weeklyShifts.get(day);

            for (ShiftDTO shiftDTO : shiftDTOs) {
                Shift shift = new Shift(shiftDTO.type, shiftDTO.date);
                shift.changeManagerShift(shiftDTO.managerID);
                shift.setShiftTime(shiftDTO.startTime,shiftDTO.endTime);
                shift.setRequirements(yearlyShiftsDao.getNextWeekRequirements(shiftDTO.date,shiftDTO.type));
                shift.setWorkers(yearlyShiftsDao.getNextWeekWorkers(shiftDTO.date,shiftDTO.type));
                shift.setPreferences(yearlyShiftsDao.getNextWeekPreferences(shiftDTO.date,shiftDTO.type));
                weeklyShifts.getShifts(day).add(shift);
            }
        }
        return weeklyShifts;
    }

    public WeeklyShifts loadCurrentWeek() throws SQLException, InvalidInputException {
        WeeklyShiftsDTO weeklyShiftsDTO = yearlyShiftsDao.getCurrentWeek();
        WeeklyShifts weeklyShifts = new WeeklyShifts();

        for (Days day : Days.values()) {
            List<ShiftDTO> shiftDTOs = weeklyShiftsDTO.weeklyShifts.get(day);

            for (ShiftDTO shiftDTO : shiftDTOs) {
                Shift shift = new Shift(shiftDTO.type, shiftDTO.date);
                shift.changeManagerShift(shiftDTO.managerID);
                shift.setShiftTime(shiftDTO.startTime,shiftDTO.endTime);
                shift.setWorkers(yearlyShiftsDao.getCurrentWeekWorkers(shiftDTO.date,shiftDTO.type));
                shift.setPreferences(yearlyShiftsDao.getCurrentWeekPreferences(shiftDTO.date,shiftDTO.type));
                weeklyShifts.getShifts(day).add(shift);
            }
        }
        for (Days day : Days.values()) {
            for (Shift shift : weeklyShifts.getShifts(day)) {
                for (Role role : shift.getWorkers().keySet()) {
                    if (role != Role.Shift_Manager)
                        shift.getRequirements().put(role, shift.getRequirements().getOrDefault(role, 0));
                    else
                        shift.getRequirements().put(role, 0);
                }
            }
        }
        return weeklyShifts;
    }

    @Override
    public WeeklyShifts getNextWeek(){
        return nextWeek;
    }

    @Override
    public WeeklyShifts getCurrentWeek(){
        return currentWeek;
    }

    @Override
    public void addShiftToNextWeek(ShiftType type, Date date) throws SQLException, ShiftCantBeAddedException {
        nextWeek.addShift(new Shift(type,date));
        yearlyShiftsDao.addShiftToNextWeek(type,date);
    }

    @Override
    public void removeShiftFromNextWeek(Days day, ShiftType shiftType) throws SQLException, InvalidInputException, ShiftNotEmptyException {
        nextWeek.removeShift(day, shiftType);
        yearlyShiftsDao.removeShiftFromNextWeek(day, shiftType);
    }

    @Override
    public void addCurrentToHistoryShift() throws SQLException {
        Map<ShiftDTO, ArrayList<Integer>> details = new HashMap<>();
        for (Days day : Days.values()) {
            List<Shift> shifts = currentWeek.getShifts(day);
            for (Shift shift : shifts) {
                ShiftDTO shiftDTO = new ShiftDTO(shift.getType(),shift.getDate(),shift.getManagerID(),shift.getTime(),shift.getTime());
                ArrayList<Integer> ids = new ArrayList<>();
                for (Map.Entry<Role, ArrayList<Integer>> entry : shift.getWorkers().entrySet()) {
                    ids.addAll(entry.getValue());
                }
                details.put(shiftDTO,ids);
            }
        }
        yearlyShiftsDao.addCurrentToHistoryShift(details);
    }

    @Override
    public void resetNextWeek() throws SQLException {
        nextWeek = new WeeklyShifts();
        yearlyShiftsDao.resetNextWeek();
    }

    @Override
    public void moveNextWeekToCurrent() throws SQLException {
        currentWeek = nextWeek;
        yearlyShiftsDao.moveNextWeekToCurrent();
    }

    @Override
    public void removeOldYears(int limitYear) throws SQLException {
        yearlyShiftsDao.removeOldYears(limitYear);
    }

    @Override
    public void addWorkerNextWeek(int id, ShiftType shiftType, Days day, Role role, Date date) throws SQLException, InvalidInputException, RoleNotFoundException {
        for (Shift shift : nextWeek.getShifts(day)){
            if(shift.getType() == shiftType){
                shift.AddWorker(role, id);

            }
        }
        yearlyShiftsDao.addWorkerNextWeek(id, shiftType,day,role,date);
    }

    @Override
    public void removeWorkerNextWeek(int id,Role role, ShiftType shiftType, Days day, Date date) throws SQLException, InvalidInputException, RoleNotFoundException, EmployeeNotFoundException {
        for (Shift shift : nextWeek.getShifts(day)){
            if(shift.getType() == shiftType){
//                Map<Role, ArrayList<Integer>> workers = shift.getWorkers();
//                for (ArrayList<Integer> ids : workers.values()) {
//                    ids.removeIf(workerId -> workerId == id);
//                }
                shift.removeWorker(role, id);
            }
        }
        yearlyShiftsDao.removeWorkerNextWeek(id, shiftType,day,date);
    }

    @Override
    public void updateEmployeeAssignmentInCurrentWeek(Role role, int oldId, int newId, ShiftType shiftType, Days day, Date date) throws SQLException, InvalidInputException, RoleNotFoundException, EmployeeNotFoundException {
        for (Shift shift : currentWeek.getShifts(day)) {
            if (shift.getType() == shiftType) {
                shift.removeWorker(role, oldId);
                shift.AddWorker(role, newId);
//                for (Map.Entry<Role, ArrayList<Integer>> entry : shift.getWorkers().entrySet()) {
//                    ArrayList<Integer> ids = entry.getValue();
//                    if (ids.contains(oldId)) {
//                        ids.remove((Integer) oldId);
//                        ids.add(newId);
//                        break;
//                    }
//                }
            }
        }
        yearlyShiftsDao.updateEmployeeAssignmentInCurrentWeek(oldId,newId,shiftType,day,date);
    }

    @Override
    public void addRoleNextWeekShift(Role role, int amount, ShiftType shiftType, Days day) throws SQLException, InvalidInputException, RoleExistsException {
        for (Shift shift : nextWeek.getShifts(day)) {
            if (shift.getType() == shiftType) {
                shift.addRole(role, amount);
                yearlyShiftsDao.addRoleNextWeekShift(role, amount, shiftType, day, shift.getDate());
            }
        }

    }

    @Override
    public void updateRoleNextWeekShift(Role role, int amount, ShiftType shiftType, Days day) throws SQLException {
        for (Shift shift : nextWeek.getShifts(day)) {
            if (shift.getType() == shiftType) {
                shift.getRequirements().put(role,amount);
            }
        }
        yearlyShiftsDao.updateRoleNextWeekShift(role, amount, shiftType, day);
    }

    @Override
    public void updateShiftTimeNextWeekShift(ShiftType shiftType, Days day, String time) throws SQLException {
        for (Shift shift : nextWeek.getShifts(day)) {
            if (shift.getType() == shiftType) {
                shift.updateShiftTime(time);
            }
        }
        yearlyShiftsDao.updateShiftTimeNextWeekShift(shiftType, day, time);
    }

    @Override
    public void addShiftPreferencesNextWeekShift(ShiftType shiftType, Days day, int id) throws SQLException, InvalidInputException {
        for (Shift shift : nextWeek.getShifts(day)) {
            if (shift.getType() == shiftType) {
                shift.addPreference(id);
                yearlyShiftsDao.addShiftPreferencesNextWeekShift(shift.getDate(), shiftType, day, id);
            }
        }

    }

    @Override
    public void removeShiftPreferencesNextWeekShift(ShiftType shiftType, Days day, int id) throws SQLException, InvalidInputException {
        for (Shift shift : nextWeek.getShifts(day)) {
            if (shift.getType() == shiftType) {
                shift.removePreference(id);
                yearlyShiftsDao.removeShiftPreferencesNextWeekShift(shift.getDate(), shiftType, day, id);
            }
        }

    }

    @Override
    public List<Integer> getAvailableYears() throws SQLException {
        return yearlyShiftsDao.getAvailableYears();
    }

    @Override
    public Map<Date, Map<ShiftType, List<Integer>>> getYearlyShifts(int year) throws SQLException, InvalidInputException {
        return yearlyShiftsDao.getYearlyShifts(year);
    }

    public void close() {
        if (yearlyShiftsDao != null)
            yearlyShiftsDao.close();
    }
}
