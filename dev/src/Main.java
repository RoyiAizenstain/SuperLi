import Deliveries.Domain.DeliveryManager;
import employee.Presentation.*;
import employee.Service.ServiceController;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ServiceController serviceController = new ServiceController();
        DeliveryManager deliveryManager = new DeliveryManager();
        System.out.println("Welcome to the Super-Li Management System!\n");
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println("Would you like to load data to the system? (yes/no)");
            input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes")) {
                try {
                    String url = "jdbc:sqlite:dev/src/Resources/my_database.db";
                    serviceController.connectDao(url);
                    deliveryManager.connectDao(url);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            } else if (input.equals("no")) {
                try {
                    String tempDbName = "temp_database_" + System.currentTimeMillis() + ".db";
                    String url = "jdbc:sqlite:dev/src/Resources/" + tempDbName;
                    serviceController.connectDaoEmpty(url);
                    deliveryManager.connectDao(url);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }else {
                System.out.println("Invalid input. Please type 'yes', 'no'.");
            }
        }
        while (true) {
            UserLoginView loginView = new UserLoginView();
            loginView.login();
        }
    }
}