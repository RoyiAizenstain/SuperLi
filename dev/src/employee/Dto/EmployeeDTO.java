package employee.Dto;

import employee.Enums.*;

import java.util.Date;

/**
 * Data Transfer Object representing an Employee.
 * Immutable and serializable; contains no behaviour.
 */

public class EmployeeDTO {

    public final String name;
    public final int id;
    public final int bankNumber;
    public final int branchNumber;
    public final int accountNumber;
    public final Date hiringDate;
    public final int salary;
    public final String contract;
    public final String workLimits;
    public final int sickDays;
    public final int holidays;
    public final WorkStatus workStatus;
    public final Job currentJob;
    public final String phoneNumber;
    public final int branch;

    public EmployeeDTO(
            String name,
            int id,
            int bankNumber,
            int branchNumber,
            int accountNumber,
            Date hiringDate,
            int salary,
            String contract,
            String workLimits,
            int sickDays,
            int holidays,
            WorkStatus workStatus,
            Job currentJob,
            String phoneNumber,
            int branch
    ) {
        this.name = name;
        this.id = id;
        this.bankNumber = bankNumber;
        this.branchNumber = branchNumber;
        this.accountNumber = accountNumber;
        this.hiringDate = hiringDate;
        this.salary = salary;
        this.contract = contract;
        this.workLimits = workLimits;
        this.sickDays = sickDays;
        this.holidays = holidays;
        this.workStatus = workStatus;
        this.currentJob = currentJob;
        this.phoneNumber = phoneNumber;
        this.branch = branch;
    }
}
