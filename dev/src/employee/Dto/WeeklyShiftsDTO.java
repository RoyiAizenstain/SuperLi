package employee.Dto;

import employee.Enums.Days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object representing a WeeklyShifts object.
 */
public class WeeklyShiftsDTO{
    public Map<Days, ArrayList<ShiftDTO>> weeklyShifts;

    public WeeklyShiftsDTO() {
        this.weeklyShifts = new HashMap<>();
        for (Days day : Days.values()) {
            weeklyShifts.put(day, new ArrayList<>());
        }
    }

}
