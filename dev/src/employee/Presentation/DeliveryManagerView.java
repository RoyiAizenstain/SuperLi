package employee.Presentation;
import Deliveries.Presentation.UI;
import java.util.Scanner;

public class DeliveryManagerView {
    public void showDeliveryManagerMenu(int userId){
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        //loop until the user chooses to exit
        while (!exit) {
            System.out.println("--- Delivery Manager Menu ---");
            System.out.println("1. View my personal details");
            System.out.println("2. login Transportation System");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    EmployeePersonalDetailsView employeePersonalDetailsView = new EmployeePersonalDetailsView();
                    employeePersonalDetailsView.showMyDetailsMenu(userId);
                    break;
                case 2:
                    UI ui = new UI();
                    //TODO: Working on it from here
                    ui.showMainMenu();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 3.");
            }
        }
    }

}
