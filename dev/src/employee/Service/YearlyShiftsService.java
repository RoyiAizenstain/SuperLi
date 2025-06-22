package employee.Service;

import employee.Domain.DomainController;
import employee.Exceptions.*;

import java.sql.SQLException;
import java.util.Date;

public class YearlyShiftsService {
    DomainController controller = new DomainController();

    public void printAvailableYears() throws SQLException {
        controller.printAvailableYears();
    }

    public void printShiftsInYear(int year) throws InvalidInputException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        controller.printShiftsInYear(year);
    }

    public void printShiftsInWeekAndYear(int year, int week) throws InvalidInputException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        controller.printShiftsInWeekAndYear(year,week);
    }

    public void printUserWeekShifts(int userId, Date date) throws InvalidInputException, WeeklyShiftsNotReadyException, EmployeeNotFoundException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        controller.printUserWeekShifts(userId,date);
    }

    public void printManagerWeekShifts(int userId, Date date) throws InvalidInputException, WeeklyShiftsNotReadyException, EmployeeNotFoundException, ShiftCantBeAddedException, SQLException, RoleNotFoundException {
        controller.printManagerWeekShifts(userId,date);
    }

}
