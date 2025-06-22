package employee.Presentation;

import employee.Enums.*;
import employee.Service.*;

import java.util.*;

public class UserLoginView {

    public void login() {
        ServiceController serviceController = new ServiceController();
        try {
            if (serviceController.isDateSATURDAY()) {
                serviceController.saveAndReset();
            }
        }
        catch (Exception e){
            System.out.print("cant save weeklyShifts in the system because: " + e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = false;
        Job job = null;
        int userId = 0;

        while (!isLoggedIn) {
            System.out.println("\nWelcome to the Super-Li Management System!");
            //input username and verify
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            try {
                serviceController.isUserExists(username);
                //username found
                userId = serviceController.getId(username);
            }
            catch (Exception e){
                System.out.println("Username not found. Please try again.");
                continue;
            }

            //input job and verify
            System.out.print("Enter job: ");
            String inputJob = scanner.nextLine();
            job = verifyJob(inputJob);
            if (job == null) {
                System.out.println("Invalid job. Please try again.");
                continue;
            }
            try {
                serviceController.isJobMatchUser(username,job);
                //job match username
            }
            catch (Exception e){
                System.out.println(e.getMessage() + " Please try again.");
                continue;
            }

            //input password and verify
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            try {
                serviceController.isPasswordMatchUser(username,password);//checks if the password matches the user
                //password match username
            }
            catch (Exception e){
                System.out.println(e.getMessage() + " Please try again.");
                continue;
            }

            System.out.println("Login successful! Welcome, " + username + " (" + job + ")\n");
            isLoggedIn = true;
        }
        if (job == Job.Regular) {
            RegularEmployeeMenuView regularEmployeeMenuView = new RegularEmployeeMenuView();
            regularEmployeeMenuView.showRegularEmployeeMenu(userId);
        } else if (job == Job.HR) {
            HRMenuView hrMenuView = new HRMenuView();
            hrMenuView.showHRMenu(userId);
        } else if (job == Job.ShiftManager) {
            ShiftManagerMenuView shiftManagerMenuView = new ShiftManagerMenuView();
            shiftManagerMenuView.showShiftManagerMenu(userId);
        }
        else if (job == Job.DeliveryManager){
            DeliveryManagerView deliveryManagerView = new DeliveryManagerView();
            deliveryManagerView.showDeliveryManagerMenu(userId);
        }
        System.out.println("Logging out...");
        }

        public Job verifyJob (String inputJob){
            for (Job job : Job.values()) {
                if (job.name().equalsIgnoreCase(inputJob)) {
                    return job;
                }
            }
            return null;
        }
    }
