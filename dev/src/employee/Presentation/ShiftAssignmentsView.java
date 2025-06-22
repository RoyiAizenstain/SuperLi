package employee.Presentation;

import employee.Service.*;
import employee.Enums.*;
import employee.Exceptions.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;

public class ShiftAssignmentsView {

    public void assignShifts() {
        ServiceController serviceController = new ServiceController();
        ConsoleInput consoleInput = new ConsoleInput();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        //loop until the user chooses to exit
        while (!exit) {
            final int VIEW_WEEKLY_SHIFT_OPTION = 1;
            final int ADD_SHIFT_OPTION = 2;
            final int ADD_SHIFT_OPTION_SIMULATION = 3;
            final int REMOVE_SHIFT_OPTION = 4;
            final int EDIT_SHIFT_HOURS_OPTION = 5;
            final int ASSIGN_TO_SHIFT_OPTION = 6;
            final int REMOVE_FROM_SHIFT_OPTION = 7;
            final int ADD_ROLE_TO_SHIFT_OPTION = 8;
            final int VIEW_PREFERENCES_OPTION = 9;
            final int REDUCE_ROLE_REQUIREMENT_OPTION = 10;
            final int INCREASE_ROLE_REQUIREMENT_OPTION = 11;
            final int SHIFTS_ARE_READY_OPTION = 12;
            final int SHIFTS_ARE_READY_SATURDAY_OPTION= 13;
            final int CHANGE_CURRENT_WEEK_OPTION = 14;
            final int EXIT_VIEW_OPTION = 15;
            System.out.println("--- Shift Assignment Menu ---");
            System.out.println(VIEW_WEEKLY_SHIFT_OPTION + ". View weekly shifts");
            System.out.println(ADD_SHIFT_OPTION + ". Add shift");
            System.out.println(ADD_SHIFT_OPTION_SIMULATION + ". Add shift - simulation");
            System.out.println(REMOVE_SHIFT_OPTION + ". Remove shift");
            System.out.println(EDIT_SHIFT_HOURS_OPTION + ". Edit shift hours");
            System.out.println(ASSIGN_TO_SHIFT_OPTION + ". Assign worker to shift");
            System.out.println(REMOVE_FROM_SHIFT_OPTION + ". Remove worker from shift");
            System.out.println(ADD_ROLE_TO_SHIFT_OPTION + ". Add role to shift");
            System.out.println(VIEW_PREFERENCES_OPTION + ". View employee preferences");
            System.out.println(REDUCE_ROLE_REQUIREMENT_OPTION + ". Reduce requirement in a specific role");
            System.out.println(INCREASE_ROLE_REQUIREMENT_OPTION + ". Increase requirement in a specific role");
            System.out.println(SHIFTS_ARE_READY_OPTION + ". Shifts are ready to publish");
            System.out.println(SHIFTS_ARE_READY_SATURDAY_OPTION + ". Shifts are ready to publish - Saturday simulation");
            System.out.println(CHANGE_CURRENT_WEEK_OPTION + ". Change the current week's shift schedule");
            System.out.println(EXIT_VIEW_OPTION + ". Back to previous View");
            System.out.print("Choose: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            switch (choice) {
                case VIEW_WEEKLY_SHIFT_OPTION: {
                    try {
                        System.out.println("--------- next week shifts ---------");
                        serviceController.printWeeklyShiftsDetails();
                        System.out.println("------------------------------------");
                    } catch (Exception e) {
                        System.out.println("cant view weekly shifts because " + e.getMessage());
                    }
                    break;
                }
                case ADD_SHIFT_OPTION: {
                    ShiftType shiftType = consoleInput.inputShiftType();
                    Date shiftDate = consoleInput.inputDate();
                    if (consoleInput.isDateInNextWeek(shiftDate)) {
                        try {
                            serviceController.addShift(shiftType, shiftDate);
                            System.out.println("shift was added successfully!");
                        }
                        catch (Exception e){
                            System.out.println("fail! shift can't be added...");
                        }
                    } else {
                        System.out.println("You can only add shifts for next week (Sunday to Saturday).");
                    }
                    break;
                }
                case ADD_SHIFT_OPTION_SIMULATION: {
                    ShiftType shiftType = consoleInput.inputShiftType();
                    Date shiftDate = consoleInput.inputDate();
                    try {
                        serviceController.addShift(shiftType, shiftDate);
                        System.out.println("shift was added successfully!");
                    }
                    catch (Exception e){
                        System.out.println("fail! shift can't be added...");
                    }
                    break;
                }
                case REMOVE_SHIFT_OPTION: {
                    Days shiftDay = consoleInput.inputDay();
                    ShiftType shiftType = consoleInput.inputShiftType();
                    try {
                        serviceController.removeShift(shiftDay, shiftType);
                        System.out.println("shift was deleted successfully!");
                    } catch (ShiftNotEmptyException e) {
                        System.out.println("fail! can't remove shift that has workers...");
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                break;
                }
                case EDIT_SHIFT_HOURS_OPTION: {
                    Days shiftDay = consoleInput.inputDay();
                    ShiftType shiftType = consoleInput.inputShiftType();
                    try {
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            try {
                                LocalTime[] times = consoleInput.getValidTimeRange();
                                serviceController.setShiftTime(times[0].toString(), times[1].toString(), shiftDay, shiftType);
                                System.out.println("shift time was updated successfully!");
                            } catch (InvalidInputException e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            System.out.println("fail! please view the existing shifts...");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case ASSIGN_TO_SHIFT_OPTION: {
                    int employeeId = consoleInput.inputPositiveNumber("Please enter id of the employee to assign:");
                    try{
                        serviceController.findEmployeeById(employeeId);
                        Days shiftDay = consoleInput.inputDay();
                        ShiftType shiftType = consoleInput.inputShiftType();
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            Role role = consoleInput.getEnumInput(Role.class, "Role: ");
                            serviceController.assignShiftToEmployee(employeeId, role, shiftDay, shiftType);
                            System.out.println("employee assigned to this shift successfully!");
                        }
                        else {
                            System.out.println("please view existing shifts...");
                        }
                    }catch (Exception e) {
                        System.out.println("can't assign shift because " + e.getMessage());
                    }
                    break;
                }
                case REMOVE_FROM_SHIFT_OPTION: {
                    int employeeId = consoleInput.inputPositiveNumber("Please enter id of the employee to remove:");
                    try{
                        serviceController.findEmployeeById(employeeId);
                        Days shiftDay = consoleInput.inputDay();
                        ShiftType shiftType = consoleInput.inputShiftType();
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            Role role = consoleInput.getEnumInput(Role.class, "Role: ");
                            serviceController.removeEmployeeFromShiftNextWeek(employeeId, role, shiftDay, shiftType);
                            System.out.println("employee removed from this shift successfully!");
                        } else {
                            System.out.println("please view existing shifts...");
                        }
                    } catch (Exception e) {
                        System.out.println("can't remove employee from shift because: " + e.getMessage());
                    }
                    break;
                }
                case ADD_ROLE_TO_SHIFT_OPTION: {
                    Days shiftDay = consoleInput.inputDay();
                    ShiftType shiftType = consoleInput.inputShiftType();
                    try {
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            Role role = consoleInput.getEnumInput(Role.class, "enter a role: ");
                            int amount = consoleInput.inputPositiveNumber("how many workers for this role: ");
                            try {
                                serviceController.addRoleToShift(role, amount, shiftDay, shiftType);
                                System.out.println("role added to this shift successfully!");
                            } catch (Exception e) {
                                System.out.println("can't add this role because: " + e.getMessage());
                            }
                        } else {
                            System.out.println("shift not found...");
                        }
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case VIEW_PREFERENCES_OPTION: {
                    Days shiftDay = consoleInput.inputDay();
                    ShiftType shiftType = consoleInput.inputShiftType();
                    try {
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            try {
                                serviceController.viewShiftPreferences(shiftDay, shiftType);
                            } catch (InvalidInputException e) {
                                System.out.println("can't view employee preferences because: " + e.getMessage());
                            }
                        } else {
                            System.out.println("shift not found...");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case REDUCE_ROLE_REQUIREMENT_OPTION: {
                    Days shiftDay = consoleInput.inputDay();
                    ShiftType shiftType = consoleInput.inputShiftType();
                    try {
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            Role role = consoleInput.getEnumInput(Role.class, "enter a role: ");
                            int amount = consoleInput.inputPositiveNumber("By how much to decrease the requirement? ");
                            try {
                                serviceController.decreaseAmountSpecificRole(role, amount, shiftDay, shiftType);
                                System.out.println("role requirement decreased successfully!");
                            } catch (Exception e) {
                                System.out.println("can't lower the requirement because: " + e.getMessage());
                            }
                        } else {
                            System.out.println("shift not found...");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case INCREASE_ROLE_REQUIREMENT_OPTION: {
                    Days shiftDay = consoleInput.inputDay();
                    ShiftType shiftType = consoleInput.inputShiftType();
                    try {
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            Role role = consoleInput.getEnumInput(Role.class, "enter a role: ");
                            int amount = consoleInput.inputPositiveNumber("By how much to Increase the requirement? ");
                            try {
                                serviceController.increaseAmountSpecificRole(role, amount, shiftDay, shiftType);
                                System.out.println("role requirement increased successfully!");
                            } catch (Exception e) {
                                System.out.println("can't increase the requirement because: " + e.getMessage());
                            }
                        } else {
                            System.out.println("shift not found...");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case SHIFTS_ARE_READY_OPTION: {
                    try {
                        boolean shiftsAreReady = serviceController.areShiftsReady();
                        if (shiftsAreReady) {
                            try {
                                if(serviceController.isDateSATURDAY()){
                                    serviceController.saveAndReset();
                                    System.out.println("Weekly shifts saved successfully...");

                                }
                                else {
                                    System.out.println("fail! You can only publish shifts in Saturday...");
                                }
                            } catch (InvalidInputException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case SHIFTS_ARE_READY_SATURDAY_OPTION: {
                    try {
                        boolean shiftsAreReady = serviceController.areShiftsReady();
                        if (shiftsAreReady) {
                            try {
                                serviceController.saveAndReset();
                                System.out.println("Weekly shifts saved successfully...");
                            } catch (InvalidInputException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case CHANGE_CURRENT_WEEK_OPTION: {
                    try {
                        System.out.println("--------- this week shifts ---------");
                        serviceController.printCurrentWeekShiftsDetails();
                        System.out.println("------------------------------------");
                    } catch (Exception e) {
                        System.out.println("cant view this week shifts because " + e.getMessage());
                    }
                    int employeeIdToReplace = consoleInput.inputPositiveNumber("Please enter id of the employee to replace:");
                    try{
                        serviceController.findEmployeeById(employeeIdToReplace);
                        Days shiftDay = consoleInput.inputDay();
                        ShiftType shiftType = consoleInput.inputShiftType();
                        boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                        if (shiftExists) {
                            Role role = consoleInput.getEnumInput(Role.class, "Role: ");
                            //check if this employee works in this shift in this role
                            if (serviceController.isEmployeeWorksInCurrnetShift(employeeIdToReplace, role, shiftDay, shiftType)) {
                                int newEmployeeId = consoleInput.inputPositiveNumber("Please enter id of the employee to add instead:");
                                serviceController.findEmployeeById(newEmployeeId);//check if the employee exists
                                serviceController.updateEmployeeAssignmentInCurrentWeek(employeeIdToReplace, newEmployeeId, role, shiftDay, shiftType);
                                System.out.println("Notice: employee replaced in existing shift");
                                System.out.println("The employees were successfully replaced!");
                            }
                            else {
                                System.out.println("employee doesn't work in this role in this shift...");
                            }
                        }
                        else {
                            System.out.println("please view existing shifts...");
                        }
                    }catch (Exception e) {
                        System.out.println("can't replace employees because " + e.getMessage());
                    }
                    break;
                }
                case EXIT_VIEW_OPTION: {
                    exit = true;
                    break;
                }
                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 15.");
            }
        }
    }
}