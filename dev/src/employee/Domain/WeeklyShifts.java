package employee.Domain;

import employee.Enums.*;
import employee.Exceptions.*;

import java.util.*;

public class WeeklyShifts {
    private Map<Days, ArrayList<Shift>> weeklyShifts;

    public WeeklyShifts() {
        this.weeklyShifts = new HashMap<>();
        for (Days day : Days.values()) {
            this.weeklyShifts.put(day, new ArrayList<>());
        }
    }

    public boolean hasDayShift(Days day) {
        ArrayList<Shift> shifts = this.weeklyShifts.get(day);
        if (!shifts.isEmpty()) {
            for (Shift shift : shifts) {
                if (shift.getType() == ShiftType.Morning) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasEveningShift(Days day) {
        ArrayList<Shift> shifts = this.weeklyShifts.get(day);
        if (!shifts.isEmpty()) {
            for (Shift shift : shifts) {
                if (shift.getType() == ShiftType.Evening) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addShift(Shift shift) throws ShiftCantBeAddedException {
        Days day = shift.getDayByShift();

        if (shift.getType() == ShiftType.Morning) {
            if (hasDayShift(day)) {
                throw new ShiftCantBeAddedException("cant have 2 same ShiftTypes in the same day");
            }
        }
        else if (shift.getType() == ShiftType.Evening) {
            if (hasEveningShift(day)) {
                throw new ShiftCantBeAddedException("cant have 2 same ShiftTypes in the same day");
            }
        }

        ArrayList<Shift> shifts = this.weeklyShifts.get(day);

        if(shift.getType()==ShiftType.Morning) shifts.add(0,shift);
        else shifts.add(shift);

        this.weeklyShifts.put(day, shifts);
    }

    public void removeShift(Days day, ShiftType type) throws ShiftNotEmptyException, InvalidInputException {
        Shift shift = findShift(day,type);
        if (shift.shiftIsEmpty()){//we can remove shift only if there are no workers yet
            this.weeklyShifts.get(day).remove(shift);
            return;
        }
        else {
            throw new ShiftNotEmptyException("shift cant be removes if there are workers in it");
        }

//        ArrayList<Shift> shifts = this.weeklyShifts.get(day);
//        for (Shift shift : shifts) {
//            if (shift.getType() == type) {
//                if (shift.shiftIsEmpty()){//we can remove shift only if there are no workers yet
//                    shifts.remove(shift);
//                    //this.weeklyShifts.put(day, shifts);
//                    return;
//                }
//                else {
//                    throw new ShiftNotEmptyException("shift cant be removes if there are workers in it");
//                }
//            }
//        }
//        throw new InvalidInputException("Shift not exists - no " + type + " shift found on " + day);
    }

    public ArrayList<Shift> getShifts(Days day){
        return weeklyShifts.get(day);
    }

    public Shift findShift(Days day, ShiftType shiftType) throws InvalidInputException {
        ArrayList<Shift> dayShifts = this.getShifts(day);
        if (dayShifts == null) throw new InvalidInputException("shift not exists - no shift found on " + day);
        for (Shift shift: dayShifts){
            if (shift.getType() == shiftType) {
                return shift;
            }
        }
        throw new InvalidInputException("shift not exists - no " + shiftType + " shift found on " + day);
    }

    public int getYear() throws InvalidInputException{
        for (ArrayList<Shift> dayShifts : weeklyShifts.values()) {
            for (Shift shift : dayShifts) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(shift.getDate());
                return cal.get(Calendar.YEAR);
            }
        }
        throw new InvalidInputException("No shifts available to determine the year.");
    }

    public void printWeeklyShiftsDetails() {
        for (Days day : Days.values()) {
            System.out.println("day: " + day + ", shifts: ");
            ArrayList<Shift> shifts = this.weeklyShifts.get(day);
            if (shifts.isEmpty()) {
                System.out.println("No shifts");
            } else {
                for (Shift shift : shifts) {
                    shift.printShiftDetails();
                }
            }
        }
    }

    public void printPreferredShiftsDetails(int userId) {
        for (Days day : Days.values()) {
            System.out.println("day: " + day + ", shifts: ");
            ArrayList<Shift> shifts = this.weeklyShifts.get(day);
            if (shifts.isEmpty()) {
                System.out.println("No shifts");
            } else {
                boolean preferredShift = false;
                for (Shift shift : shifts) {
                    if (shift.getEmployeePreferences().contains(userId)) {
                        preferredShift = true;
                        String shiftStr = "ShiftType: " + shift.getType() +
                                "\ndate: " + shift.getDate() +
                                "\nShift hours: " + shift.getTime();
                        System.out.println(shiftStr);
                    }
                }
                if (!preferredShift){
                    System.out.println("There are no preferred shifts...");
                }
            }
        }
    }

    public void printNonePreferredShiftsDetails(int userId) {
        for (Days day : Days.values()) {
            System.out.println("day: " + day + ", shifts: ");
            ArrayList<Shift> shifts = this.weeklyShifts.get(day);
            if (shifts.isEmpty()) {
                System.out.println("No shifts");
            } else {
                boolean nonePreferredShift = false;
                for (Shift shift : shifts) {
                    if (shift.getEmployeePreferences().contains(userId)) {
                        continue;
                    }
                    nonePreferredShift = true;
                    String shiftStr = "ShiftType: " + shift.getType() +
                            "\ndate: " + shift.getDate() +
                            "\nShift hours: " + shift.getTime();
                    System.out.println(shiftStr);
                }
                if (!nonePreferredShift) {
                    System.out.println("There are no none preferred shifts...");
                }
            }
        }
    }

    public void setShiftTime(String start, String end, Days day, ShiftType shiftType) throws InvalidInputException {
        ArrayList<Shift> dayShifts = this.getShifts(day);
        if (dayShifts == null) throw new InvalidInputException("shift not exists - no shift found on " + day);

        Shift targetShift = null;
        Shift otherShift = null;

        //find the shift to update and the other shift (if exists)
        for (Shift shift : dayShifts) {
            if (shift.getType() == shiftType) {
                targetShift = shift;
            } else {
                otherShift = shift;
            }
        }

        if (targetShift == null) {
            throw new InvalidInputException("Shift of type " + shiftType + " does not exist on " + day);
        }
        if (otherShift != null) {
            String[] otherTimeParts = otherShift.getTime().split("-");
            String otherStart = otherTimeParts[0];
            String otherEnd = otherTimeParts[1];

            //check for time conflict: if start < otherEnd and otherStart < end, there is an overlap
            if (start.compareTo(otherEnd) < 0 && otherStart.compareTo(end) < 0) {
                throw new InvalidInputException("Time conflict with existing " + otherShift.getType() + " shift on " + day);
            }
        }
        //no conflict, update the shift time
        targetShift.setShiftTime(start, end);

    }

}


