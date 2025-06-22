package employee.DataAccess;

import employee.Dto.UserDTO;
import employee.Enums.*;
import employee.Exceptions.*;
import employee.util.SQLiteClient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Deliveries.DAO.*;

public class UserDao {
    private SQLiteClient sqLiteClient;

    public UserDao(String url){
        this.sqLiteClient = SQLiteClient.getInstance(url);
    }

    public List<UserDTO> getUsers() throws SQLException, UserNotFound {
        Connection connection = this.sqLiteClient.getConnection();
        List<UserDTO> userDTOS = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("SELECT username, job, password, id FROM loginPasswords");
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            userDTOS.add(new UserDTO(rs.getString("username"), Job.valueOf(rs.getString("job")),
                    rs.getString("password"), rs.getInt("id")));
        }
        pstmt.close();
        return userDTOS;
    }

    private void createTablesTest(Connection conn) throws SQLException {
        String createEmployeesTable = "CREATE TABLE employees (\n" +
                "    employee_name TEXT NOT NULL,\n" +
                "    id INT NOT NULL PRIMARY KEY,\n" +
                "    job TEXT NOT NULL,\n" +
                "    bankNumber INT NOT NULL,\n" +
                "    branchNumber INT NOT NULL,\n" +
                "    accountNumber INT NOT NULL,\n" +
                "    contract TEXT NOT NULL,\n" +
                "    salary INT NOT NULL CHECK (salary >= 0),\n" +
                "    workStatus TEXT NOT NULL CHECK (workStatus IN ('Active', 'Fired', 'Resigned')),\n" +
                "    hiringDate DATE NOT NULL,\n" +
                "    workLimits TEXT NOT NULL,\n" +
                "    phoneNumber TEXT NOT NULL,\n" +
                "    sickDays INT NOT NULL CHECK (sickDays >= 0),\n" +
                "    holiDays INT NOT NULL CHECK (holiDays >= 0),\n" +
                "    branch INT NOT NULL\n" +
                ");";
        String insertIntoEmployessTable = "\n" +
                "INSERT INTO employees (employee_name, id,job,bankNumber,branchNumber,accountNumber,contract,salary,workStatus,hiringDate,workLimits,phoneNumber,sickDays,holiDays, branch) VALUES\n" +
                "('Bob', 2, 'ShiftManager', 10,987654321,222,'Part-time', 7000, 'Active', '2025-03-12', 'None', '0585247669',10,20,1),\n" +
                "('Charlie', 3, 'Regular',11,111222333,333,'Full-time', 6000, 'Active', '2025-03-12', 'None', '0541238579',10,20,1);\n";
        String createAbilitiesAndRolesTable = "CREATE TABLE abilitiesAndRoles (\n" +
                "    id INT NOT NULL,\n" +
                "    ability TEXT NOT NULL,\n" +
                "    role TEXT,\n" +
                "    PRIMARY KEY (id, ability)\n" +
                ");";
        String insertIntoAbilitiesAndRolesTable = "INSERT INTO abilitiesAndRoles (id,role,ability)  VALUES\n" +
                "(2,'Shift_Manager','Team_Management'),\n" +
                "(2,'Shift_Manager','Authorized_cancellation_card'),\n" +
                "(3,'Cashier','Operate_cash_register');";
        String createNextWeekShiftsTable = "CREATE TABLE nextWeekShifts (\n" +
                "    date DATE NOT NULL,\n" +
                "    managerId INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    time TEXT NOT NULL,\n" +
                "    PRIMARY KEY (date, shift_type, day)\n" +
                ");";
        String insertToNextWeekShiftsTable = "INSERT INTO nextWeekShifts (date, managerId, shift_type, day,time)  VALUES\n" +
                "('2025-05-18',0,'Morning','SUNDAY','08:00-14:00'),\n" +
                "('2025-05-18',9,'Evening','SUNDAY','14:00-20:00');";
        String createNextWeekWorkersTable = "CREATE TABLE nextWeekWorkers (\n" +
                "    date DATE NOT NULL,\n" +
                "    id INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    role TEXT NOT NULL,\n" +
                "    PRIMARY KEY (date, shift_type, day, id)\n" +
                ");";
        String insertIntoNextWeekWorkers = "INSERT INTO nextWeekWorkers (date, id, shift_type, day,role)  VALUES\n" +
                "('2025-05-18',3,'Morning','SUNDAY','Cashier'),\n" +
                "('2025-05-18',2,'Morning','SUNDAY','Shift_Manager');";
        String createNextWeekPreferencesTable = "CREATE TABLE nextWeekPreferences (\n" +
                "    date DATE NOT NULL,\n" +
                "    id INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    PRIMARY KEY (date, shift_type, day, id)\n" +
                ");";
        String createNextWeekRequirementsTable = "CREATE TABLE nextWeekRequirements (\n" +
                "    role TEXT NOT NULL,\n" +
                "    amount INT NOT NULL CHECK (amount >= 0),\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    date DATE NOT NULL,\n" +
                "    PRIMARY KEY (role, shift_type, day)\n" +
                ");";
        String insertIntoRequirments = "INSERT INTO nextWeekRequirements (date, role,amount, shift_type, day)  VALUES\n" +
                "('2025-05-18','Shift_Manager',0,'Morning','SUNDAY'), ('2025-05-18','Shift_Manager',0,'Evening','SUNDAY');";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createEmployeesTable);
            stmt.execute(createAbilitiesAndRolesTable);
            stmt.execute(createNextWeekShiftsTable);
            stmt.execute(createNextWeekWorkersTable);
            stmt.execute(createNextWeekPreferencesTable);
            stmt.execute(createNextWeekRequirementsTable);
            stmt.execute(insertIntoEmployessTable);
            stmt.execute(insertIntoAbilitiesAndRolesTable);
            stmt.execute(insertToNextWeekShiftsTable);
            stmt.execute(insertIntoNextWeekWorkers);
            stmt.execute(insertIntoRequirments);
        }
    }

    public void createTables(Connection conn) throws SQLException {
        String createEmployeesTable = "CREATE TABLE employees (\n" +
                "    employee_name TEXT NOT NULL,\n" +
                "    id INT NOT NULL PRIMARY KEY,\n" +
                "    job TEXT NOT NULL,\n" +
                "    bankNumber INT NOT NULL,\n" +
                "    branchNumber INT NOT NULL,\n" +
                "    accountNumber INT NOT NULL,\n" +
                "    contract TEXT NOT NULL,\n" +
                "    salary INT NOT NULL CHECK (salary >= 0),\n" +
                "    workStatus TEXT NOT NULL CHECK (workStatus IN ('Active', 'Fired', 'Resigned')),\n" +
                "    hiringDate DATE NOT NULL,\n" +
                "    workLimits TEXT NOT NULL,\n" +
                "    phoneNumber TEXT NOT NULL,\n" +
                "    sickDays INT NOT NULL CHECK (sickDays >= 0),\n" +
                "    holiDays INT NOT NULL CHECK (holiDays >= 0),\n" +
                "    branch INT NOT NULL\n" +
                ");";
        String createLoginPasswordsTable = "CREATE TABLE loginPasswords (\n" +
                "    id INT NOT NULL,\n" +
                "    username TEXT NOT NULL,\n" +
                "    job TEXT NOT NULL,\n" +
                "    password TEXT NOT NULL,\n" +
                "    PRIMARY KEY (id, username)\n" +
                ");";
        String insertIntoLoginPasswords = "INSERT INTO loginPasswords (id, username, job, password)  VALUES" +
                "(1,'alice_hr','HR','a111'), (2,'bob_mgr','ShiftManager','b222'), (3,'charlie_emp','Regular','c333')," +
                "(4,'ron_delivery','DeliveryManager','r444');";
        String createAbilitiesAndRolesTable = "CREATE TABLE abilitiesAndRoles (\n" +
                "    id INT NOT NULL,\n" +
                "    ability TEXT NOT NULL,\n" +
                "    role TEXT,\n" +
                "    PRIMARY KEY (id, ability)\n" +
                ");";
        String createHistoryShiftsTable = "CREATE TABLE historyShifts (\n" +
                "    id INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    date DATE NOT NULL,\n" +
                "    PRIMARY KEY (id, shift_type, day, date)\n" +
                ");";
        String createCurrentWeekShiftsTable = "CREATE TABLE currentWeekShifts (\n" +
                "    date DATE NOT NULL,\n" +
                "    managerId INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    time TEXT NOT NULL,\n" +
                "    PRIMARY KEY (date, shift_type, day)\n" +
                ");";
        String createCurrentWeekWorkersTable = "CREATE TABLE currentWeekWorkers (\n" +
                "    date DATE NOT NULL,\n" +
                "    id INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    role TEXT NOT NULL,\n" +
                "    PRIMARY KEY (date, shift_type, day, id)\n" +
                ");";
        String createCurrentWeekPreferencesTable = "CREATE TABLE currentWeekPreferences (\n" +
                "    date DATE NOT NULL,\n" +
                "    id INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    PRIMARY KEY (date, shift_type, day, id)\n" +
                ");";
        String createNextWeekShiftsTable = "CREATE TABLE nextWeekShifts (\n" +
                "    date DATE NOT NULL,\n" +
                "    managerId INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    time TEXT NOT NULL,\n" +
                "    PRIMARY KEY (date, shift_type, day)\n" +
                ");";
        String createNextWeekWorkersTable = "CREATE TABLE nextWeekWorkers (\n" +
                "    date DATE NOT NULL,\n" +
                "    id INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    role TEXT NOT NULL,\n" +
                "    PRIMARY KEY (date, shift_type, day, id)\n" +
                ");";
        String createNextWeekPreferencesTable = "CREATE TABLE nextWeekPreferences (\n" +
                "    date DATE NOT NULL,\n" +
                "    id INT NOT NULL,\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    PRIMARY KEY (date, shift_type, day, id)\n" +
                ");";
        String createNextWeekRequirementsTable = "CREATE TABLE nextWeekRequirements (\n" +
                "    role TEXT NOT NULL,\n" +
                "    amount INT NOT NULL CHECK (amount >= 0),\n" +
                "    shift_type TEXT NOT NULL CHECK (shift_type IN ('Morning', 'Evening')),\n" +
                "    day TEXT NOT NULL CHECK (day IN ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),\n" +
                "    date DATE NOT NULL,\n" +
                "    PRIMARY KEY (role, shift_type, day)\n" +
                ");";


        String trucks = "CREATE TABLE IF NOT EXISTS Trucks (" +
                "truck_id VARCHAR PRIMARY KEY," +
                "truckType TEXT," +
                "licensePlate TEXT," +
                "tareWeight REAL," +
                "maxWeight REAL," +
                "availability BOOLEAN" +
                ")";

        String sites= "CREATE TABLE IF NOT EXISTS Sites (" +
                "site_id VARCHAR PRIMARY KEY," +
                "address TEXT NOT NULL," +
                "contact_person TEXT NOT NULL," +
                "phone_number TEXT NOT NULL," +
                "site_name TEXT NOT NULL" +
                ")";

        String shippingZone = "CREATE TABLE IF NOT EXISTS ShippingZones (" +
                "shipping_zone_id VARCHAR PRIMARY KEY," +
                "shipping_zone_name TEXT NOT NULL" +
                ")";

        String orders = "CREATE TABLE IF NOT EXISTS Orders (" +
                "order_id VARCHAR PRIMARY KEY," +
                "destination_id VARCHAR NOT NULL," +
                "shipping_zone_id VARCHAR NOT NULL," +
                "is_shipped BOOLEAN NOT NULL DEFAULT 0," +
                "FOREIGN KEY (destination_id) REFERENCES Sites(site_id) ON DELETE CASCADE," +
                "FOREIGN KEY (shipping_zone_id) REFERENCES ShippingZones(shipping_zone_id) ON DELETE CASCADE" +
                ")";

        String driverLicenses = "CREATE TABLE IF NOT EXISTS DriverLicenses (" +
                "driver_id VARCHAR(10) PRIMARY KEY," +
                "license_type VARCHAR(5) NOT NULL," +
                "license_number VARCHAR(20) NOT NULL," +
                "is_available BOOLEAN DEFAULT TRUE," +
                " FOREIGN KEY (driver_id) REFERENCES employees(id)" +
                ")";

        String sql_deliveries = "CREATE TABLE IF NOT EXISTS Deliveries (" +
                "delivery_id VARCHAR PRIMARY KEY ," + // Changed from VARCHAR to INTEGER
                "delivery_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "truck_id VARCHAR NOT NULL," +
                "driver_id VARCHAR NOT NULL," +
                "origin_id VARCHAR NOT NULL," +
                "status VARCHAR(20) NOT NULL," +
                "FOREIGN KEY (driver_id) REFERENCES employees(id) ON DELETE CASCADE," +
                "FOREIGN KEY (truck_id) REFERENCES Trucks(truck_id) ON DELETE CASCADE," +
                "FOREIGN KEY (origin_id) REFERENCES Sites(site_id) ON DELETE CASCADE" +
                ")";

        String sql_deliveries_destinations = "CREATE TABLE IF NOT EXISTS Deliveries_Destinations (" +
                "delivery_id VARCHAR NOT NULL," +
                "site_id VARCHAR NOT NULL," +
                "PRIMARY KEY (delivery_id, site_id)," +
                "FOREIGN KEY (delivery_id) REFERENCES Deliveries(delivery_id) ON DELETE CASCADE," +
                "FOREIGN KEY (site_id) REFERENCES Sites(site_id) ON DELETE CASCADE" +
                ")";

        String sql_deliveries_orders = "CREATE TABLE IF NOT EXISTS Deliveries_Orders (" +
                "delivery_id VARCHAR NOT NULL," +
                "order_id VARCHAR NOT NULL," +
                "PRIMARY KEY (delivery_id, order_id)," +
                "FOREIGN KEY (delivery_id) REFERENCES Deliveries(delivery_id) ON DELETE CASCADE," +
                "FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE" +
                ")";

        String cargo = "CREATE TABLE IF NOT EXISTS Cargo (" +
                "cargo_id VARCHAR PRIMARY KEY," +
                "order_id VARCHAR NOT NULL," +
                "description TEXT NOT NULL," +
                "weight REAL NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE" +
                ")";

        String shippingZone_Sites = "CREATE TABLE IF NOT EXISTS ShippingZone_Sites (" +
                "shipping_zone_id VARCHAR NOT NULL," +
                "site_id VARCHAR NOT NULL," +
                "PRIMARY KEY (shipping_zone_id, site_id)," +
                "FOREIGN KEY (shipping_zone_id) REFERENCES ShippingZones(shipping_zone_id) ON DELETE CASCADE," +
                "FOREIGN KEY (site_id) REFERENCES Sites(site_id) ON DELETE CASCADE" +
                ")";


        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createEmployeesTable);
            stmt.execute(createLoginPasswordsTable);
            stmt.execute(createAbilitiesAndRolesTable);
            stmt.execute(createHistoryShiftsTable);
            stmt.execute(createCurrentWeekShiftsTable);
            stmt.execute(createCurrentWeekWorkersTable);
            stmt.execute(createCurrentWeekPreferencesTable);
            stmt.execute(createNextWeekShiftsTable);
            stmt.execute(createNextWeekWorkersTable);
            stmt.execute(createNextWeekPreferencesTable);
            stmt.execute(createNextWeekRequirementsTable);
            stmt.execute(sites);
            stmt.execute(insertIntoLoginPasswords);

            stmt.execute(trucks);
            stmt.execute(shippingZone);
            stmt.execute(orders);
            stmt.execute(driverLicenses);
            stmt.execute(sql_deliveries);
            stmt.execute(sql_deliveries_destinations);
            stmt.execute(sql_deliveries_orders);
            stmt.execute(cargo);
            stmt.execute(shippingZone_Sites);
        }
    }

    public void connectDaoEmpty() throws SQLException {
        Connection conn = this.sqLiteClient.getConnection();
        createTables(conn); //create empty tables
    }

    public void connectDataBaseTEST() throws SQLException {
        Connection conn = this.sqLiteClient.getConnection();
        createTablesTest(conn); //create test tables
    }

    public void close() {
        if (sqLiteClient != null) {
            sqLiteClient.close();
        }
    }


}
