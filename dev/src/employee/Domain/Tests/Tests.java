package employee.Domain.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.*;

import org.testng.annotations.Test;
import employee.Domain.*;
import employee.Exceptions.*;
import employee.Enums.*;

public class Tests {

    @Test
    public void testAddValidRoleToShift() throws Exception{
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addRole(Role.Cleaning_Worker, 2);
        assertTrue(shift.getRequirements().containsKey(Role.Cleaning_Worker));
    }

    @Test
    public void testAddRoleWithNegativeAmountToShift(){
        Shift shift = new Shift(ShiftType.Morning, new Date());
        assertThrows(InvalidInputException.class, () -> shift.addRole(Role.Cashier, -1));
    }

    @Test
    public void testAddExistingRoleToShift() throws Exception{
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addRole(Role.Cleaning_Worker, 2);
        assertThrows(RoleExistsException.class, () -> shift.addRole(Role.Cleaning_Worker, 3));
    }

    @Test
    public void testRemoveRoleFromShift() throws Exception{
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addRole(Role.Cleaning_Worker, 2);
        shift.removeRole(Role.Cleaning_Worker);
        assertFalse(shift.getRequirements().containsKey(Role.Cleaning_Worker));
    }

    @Test
    public void testAddValidPreferenceToShift() throws Exception{
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addPreference(222);
        assertTrue(shift.getEmployeePreferences().contains(222));
    }

    @Test
    public void testAddPreferenceWithNegativeIDToShift(){
        Shift shift = new Shift(ShiftType.Morning, new Date());
        assertThrows(InvalidInputException.class, () -> shift.addPreference(-1));
    }

    @Test
    public void testAddExistingPreferenceToShift() throws Exception{
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addPreference(222);
        assertThrows(InvalidInputException.class, () -> shift.addPreference(222));
    }

    @Test
    public void testRemovePreferenceFromShift() throws Exception{
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addPreference(222);
        shift.removePreference(222);
        assertFalse(shift.getEmployeePreferences().contains(222));
    }

    @Test
    public void testAddValidWorkerToShift() throws Exception {
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addRole(Role.Cashier, 2);
        shift.AddWorker(Role.Cashier, 3);
        assertTrue(shift.getWorkers().get(Role.Cashier).contains(3));
    }

    @Test
    public void testAddWorkerWithNegativeId() throws Exception {
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addRole(Role.Cashier, 2);
        assertThrows(InvalidInputException.class, () -> shift.AddWorker(Role.Cashier, -1));
    }

    @Test
    public void testAddWorkerToNonExistingRole() {
        Shift shift = new Shift(ShiftType.Morning, new Date());
        assertThrows(RoleNotFoundException.class, () -> shift.AddWorker(Role.Driver, 3));
    }

    @Test
    public void testRemoveExistingWorkerFromShift() throws Exception {
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addRole(Role.Cashier, 1);
        shift.AddWorker(Role.Cashier, 2);
        shift.removeWorker(Role.Cashier, 2);
        assertFalse(shift.getWorkers().containsKey(Role.Cashier));
    }

    @Test
    public void testRemoveWorkerNotAssignedToShift() throws Exception {
        Shift shift = new Shift(ShiftType.Morning, new Date());
        shift.addRole(Role.Cashier, 1);
        shift.AddWorker(Role.Cashier, 1);
        assertThrows(EmployeeNotFoundException.class, () -> shift.removeWorker(Role.Cashier, 22));
    }

    @Test
    public void testAddShiftToWeeklyShiftsSuccessfully() throws Exception {
        WeeklyShifts weeklyShift = new WeeklyShifts();
        Shift shift = new Shift(ShiftType.Morning, new Date());

        weeklyShift.addShift(shift);
        assertEquals(shift, weeklyShift.getShifts(shift.getDayByShift()).get(0));
    }

    @Test
    public void testAddDuplicateShiftsToWeeklyShifts() throws Exception {
        WeeklyShifts weeklyShift = new WeeklyShifts();
        Shift shift1 = new Shift(ShiftType.Morning, new Date());
        Shift shift2 = new Shift(ShiftType.Morning, new Date());
        weeklyShift.addShift(shift1);
        assertThrows(ShiftCantBeAddedException.class, () -> weeklyShift.addShift(shift2));
    }

    @Test
    public void testFindExistingShiftInWeeklyShifts() throws Exception {
        WeeklyShifts weeklyShift = new WeeklyShifts();
        Shift shift = new Shift(ShiftType.Morning, new Date());
        weeklyShift.addShift(shift);
        Shift found = weeklyShift.findShift(shift.getDayByShift(), ShiftType.Morning);
        assertEquals(shift, found);
    }

    @Test
    public void testFindNonExistingShiftInWeeklyShifts() {
        WeeklyShifts weeklyShift = new WeeklyShifts();
        assertThrows(InvalidInputException.class, () -> weeklyShift.findShift(Days.SUNDAY, ShiftType.Evening));
    }

    @Test
    public void testRemoveEmptyShiftFromWeeklyShiftsSuccessfully() throws Exception {
        WeeklyShifts weeklyShift = new WeeklyShifts();
        Shift shift = new Shift(ShiftType.Evening, new Date());
        weeklyShift.addShift(shift);
        weeklyShift.removeShift(shift.getDayByShift(), ShiftType.Evening);
        assertTrue(weeklyShift.getShifts(shift.getDayByShift()).isEmpty());
    }

    @Test
    public void testRemoveNonEmptyShiftFromWeeklyShifts() throws Exception {
        WeeklyShifts weeklyShift = new WeeklyShifts();
        Shift shift = new Shift(ShiftType.Evening, new Date());
        shift.addRole(Role.Cashier, 1);
        shift.AddWorker(Role.Cashier, 100);
        weeklyShift.addShift(shift);

        assertThrows(ShiftNotEmptyException.class, () -> weeklyShift.removeShift(shift.getDayByShift(), ShiftType.Evening));
    }

    @Test
    public void testAddAbilityToEmployeeSuccessfully() throws Exception {
        Employee emp = new Employee("Alice", 1111, new BankAccount(10, 111111111,333), 5000, "Full-time", Job.Regular, "0543333222",1 );
        emp.addAbility(Ability.Weapons_license);
        assertTrue(emp.getAbilities().contains(Ability.Weapons_license));
    }

    @Test
    public void testAddDuplicateAbilityToEmployee() throws Exception {
        Employee emp = new Employee("Alice", 1111, new BankAccount(10, 111111111,333), 5000, "Full-time", Job.Regular, "0543333222",1 );
        emp.addAbility(Ability.Weapons_license);
        assertThrows(AbilityExistsException.class, () -> emp.addAbility(Ability.Weapons_license));
    }

    @Test
    public void testAddAbilityUpdateRoleToEmployeeSuccessfully() throws Exception {
        Employee emp = new Employee("Alice", 1111, new BankAccount(10, 111111111,333), 5000, "Full-time", Job.Regular, "0543333222",1 );
        emp.addAbility(Ability.Weapons_license);
        assertTrue(emp.getRelevantRoles().contains(Role.Security_Officer));
    }

    private static DomainController controller = new DomainController();
    private static File dbFile;
    @Test
    public void testAddEmployeeToShiftNextWeekIntegrationTests() throws Exception {
        controller.connectDaoTEST("jdbc:sqlite:dev/src/Resources/test_database.db");

        //create new shift
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2025);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        //add shift, role and employee to shift
        controller.addShift(ShiftType.Morning, date); //add to next week WeeklyShifts
        controller.addRoleToShift(Role.Cashier, 1, Days.MONDAY ,ShiftType.Morning);
        controller.assignShiftToEmployeeNextWeek(3, Role.Cashier, Days.MONDAY, ShiftType.Morning);
        controller.closeConnections();
    }

}