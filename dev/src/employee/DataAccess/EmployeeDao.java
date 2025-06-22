package employee.DataAccess;

import employee.Dto.EmployeeDTO;
import employee.Enums.*;
import employee.Exceptions.*;
import employee.util.SQLiteClient;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDao {
    private SQLiteClient sqLiteClient;

    public EmployeeDao(String url){
        this.sqLiteClient = SQLiteClient.getInstance(url);
    }

    public EmployeeDTO getEmployeeById(int employeeId) throws SQLException, EmployeeNotFoundException{
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM employees WHERE id = ?");
        pstmt.setInt(1, employeeId);
        ResultSet rs = pstmt.executeQuery();
        EmployeeDTO emp;
        if(rs.next()){ //if employee exists
            emp = new EmployeeDTO(rs.getString("employee_name"),rs.getInt("id"),
                    rs.getInt("bankNumber"), rs.getInt("branchNumber"), rs.getInt("accountNumber"),
                    java.sql.Date.valueOf(rs.getString("hiringDate")), rs.getInt("salary"),rs.getString("contract"),
                    rs.getString("workLimits"),rs.getInt("sickDays"),rs.getInt("holiDays"),
                    WorkStatus.valueOf(rs.getString("workStatus")),Job.valueOf(rs.getString("job")),
                    rs.getString("phoneNumber"), rs.getInt("branch"));
        }
        else{
            throw new EmployeeNotFoundException("employee with ID " + employeeId + " not found");
        }
        pstmt.close();
        return emp;
    }

    public List<Ability> getAbilitiesById(int employeeId) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        List<Ability> abilities = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("SELECT ability FROM abilitiesAndRoles WHERE id = ?;");
        pstmt.setInt(1, employeeId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) { //if employee exists
            String abilityName = rs.getString("ability");
            abilities.add(Ability.valueOf(abilityName));
        }
        pstmt.close();
        return abilities;
    }

    public List<EmployeeDTO> getActiveEmployees() throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM employees WHERE workStatus = 'Active'");
        List<EmployeeDTO> employees = new ArrayList<>();
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){ //if employee exists
            EmployeeDTO emp = new EmployeeDTO(rs.getString("employee_name"),rs.getInt("id"),
                    rs.getInt("bankNumber"), rs.getInt("branchNumber"), rs.getInt("accountNumber"),
                    java.sql.Date.valueOf(rs.getString("hiringDate")), rs.getInt("salary"),rs.getString("contract"),
                    rs.getString("workLimits"),rs.getInt("sickDays"),rs.getInt("holiDays"),
                    WorkStatus.valueOf(rs.getString("workStatus")),Job.valueOf(rs.getString("job")),
                    rs.getString("phoneNumber"), rs.getInt("branch"));
            employees.add(emp);
        }
        pstmt.close();
        return employees;
    }

    public List<EmployeeDTO> getNotActiveEmployees() throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM employees WHERE workStatus != 'Active'");
        List<EmployeeDTO> employees = new ArrayList<>();
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){ //if employee exists
            EmployeeDTO emp = new EmployeeDTO(rs.getString("employee_name"),rs.getInt("id"),
                    rs.getInt("bankNumber"), rs.getInt("branchNumber"), rs.getInt("accountNumber"),
                    java.sql.Date.valueOf(rs.getString("hiringDate")), rs.getInt("salary"),rs.getString("contract"),
                    rs.getString("workLimits"),rs.getInt("sickDays"),rs.getInt("holiDays"),
                    WorkStatus.valueOf(rs.getString("workStatus")),Job.valueOf(rs.getString("job")),
                    rs.getString("phoneNumber"), rs.getInt("branch"));
            employees.add(emp);
        }
        pstmt.close();
        return employees;
    }

    public void addEmployee(String name, int id, int bank, int branchNumber, int account_number,
                            int salary, String contract, Job currentJob, String phoneNumber, int branch) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO employees (employee_name, id,job,bankNumber,branchNumber,accountNumber," +
        "contract,salary,workStatus,hiringDate,workLimits,phoneNumber,sickDays,holiDays,branch) VALUES\n" +
        "('" + name + "'," + id + ",'" + currentJob + "'," + bank + "," + branchNumber + "," + account_number + ",'" +
        contract + "'," + salary + ",'Active', '" + java.sql.Date.valueOf(LocalDate.now()) + "' ,'None','" +
        phoneNumber + "'," + 10 + "," + 20 + "," + branch + ");");
        pstmt.executeUpdate();
        pstmt.close();
    }

    public List<EmployeeDTO> getEmployees() throws SQLException{
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM employees");
        List<EmployeeDTO> employees = new ArrayList<>();
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){ //if employee exists
            EmployeeDTO emp = new EmployeeDTO(rs.getString("employee_name"),rs.getInt("id"),
                    rs.getInt("bankNumber"), rs.getInt("branchNumber"), rs.getInt("accountNumber"),
                    java.sql.Date.valueOf(rs.getString("hiringDate")), rs.getInt("salary"),rs.getString("contract"),
                    rs.getString("workLimits"),rs.getInt("sickDays"),rs.getInt("holiDays"),
                    WorkStatus.valueOf(rs.getString("workStatus")),Job.valueOf(rs.getString("job")),
                    rs.getString("phoneNumber"), rs.getInt("branch"));
            employees.add(emp);
        }
        pstmt.close();
        return employees;
    }

    public void adHistoryShiftsById(int id, ShiftType shiftType, java.util.Date date) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Days day = Days.valueOf(localDate.getDayOfWeek().name());
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO historyShifts (id, shift_type, day,date)  VALUES" +
                "(" + id + ",'" + shiftType + "','" + day + "','" + date + "');");
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void removeHistoryShiftsById(int id, ShiftType shiftType, java.util.Date date) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Days day = Days.valueOf(localDate.getDayOfWeek().name());
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM historyShifts WHERE id = ? AND shift_type = ? AND day = ? AND date = ?;");
        pstmt.setInt(1, id);
        pstmt.setString(2, String.valueOf(shiftType));
        pstmt.setString(3, String.valueOf(day));
        pstmt.setString(4, date.toString());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void removeAbilityById(int id, Ability ability, Role role) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt;
        if (role == Role.Shift_Manager) {
            pstmt = connection.prepareStatement("DELETE FROM abilitiesAndRoles WHERE id = ? AND ability = ?");
            pstmt.setInt(1, id);
            pstmt.setString(2, String.valueOf(ability));
            pstmt.executeUpdate();
            List<Ability> abilities = getAbilitiesById(id);
            if(ability == Ability.Team_Management) {
                if(abilities.contains(Ability.Authorized_cancellation_card)){
                    pstmt = connection.prepareStatement("UPDATE abilitiesAndRoles SET role = ? WHERE ability = ? AND id = ?;");
                    pstmt.setString(1, null);
                    pstmt.setString(2, String.valueOf(Ability.Authorized_cancellation_card));
                    pstmt.setInt(3, id);
                }
            }
            else {
                if(abilities.contains(Ability.Team_Management)){
                    pstmt = connection.prepareStatement("UPDATE abilitiesAndRoles SET role = ? WHERE ability = ? AND id = ?;");
                    pstmt.setString(1, null);
                    pstmt.setString(2, String.valueOf(Ability.Team_Management));
                    pstmt.setInt(3, id);
                }
            }
        }
        else{
            pstmt = connection.prepareStatement("DELETE FROM abilitiesAndRoles WHERE id = ? AND ability = ?");
            pstmt.setInt(1, id);
            pstmt.setString(2, String.valueOf(ability));
        }
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void addAbilityById(int id, Ability ability, Role role) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt;
        if(role == null){
            pstmt = connection.prepareStatement("INSERT INTO abilitiesAndRoles (id, ability, role)  VALUES" +
                    "(" + id + ",'" + ability + "'," + null + ");");
        }
        else if (role == Role.Shift_Manager) {
            pstmt = connection.prepareStatement("INSERT INTO abilitiesAndRoles (id, ability, role)  VALUES" +
                    "(" + id + ",'" + ability + "','" + role + "');");
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement("UPDATE abilitiesAndRoles SET role = ? WHERE ability = ? AND id = ?;");
            pstmt.setString(1, String.valueOf(role));
            if(ability == Ability.Team_Management) {
                pstmt.setString(2, String.valueOf(Ability.Authorized_cancellation_card));
            }
            else {
                pstmt.setString(2, String.valueOf(Ability.Team_Management));
            }
            pstmt.setInt(3, id);
        }
        else{
            pstmt = connection.prepareStatement("INSERT INTO abilitiesAndRoles (id, ability, role)  VALUES" +
                    "(" + id + ",'" + ability + "','" + role + "');");
        }
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateWorkStatusById(int id, WorkStatus workStatus) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET workStatus = ? WHERE id = ?;");
        pstmt.setString(1, String.valueOf(workStatus));
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateWorkLimitsById(int id, String workLimits) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET workLimits = ? WHERE id = ?;");
        pstmt.setString(1, workLimits);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateContractById(int id, String contract) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET contract = ? WHERE id = ?;");
        pstmt.setString(1, contract);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updatePhoneNumberById(int id, String phoneNumber) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET phoneNumber = ? WHERE id = ?;");
        pstmt.setString(1, phoneNumber);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateBankNumberById(int id, int bankNumber) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET bankNumber = ? WHERE id = ?;");
        pstmt.setInt(1, bankNumber);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateBranchNumberById(int id, int branchNumber) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET branchNumber = ? WHERE id = ?;");
        pstmt.setInt(1, branchNumber);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateAccountNumberById(int id, int accountNumber) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET accountNumber = ? WHERE id = ?;");
        pstmt.setInt(1, accountNumber);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateSalaryById(int id, int salary) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET salary = ? WHERE id = ?;");
        pstmt.setInt(1, salary);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateSickDaysById(int id, int sickDays) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET sickDays = ? WHERE id = ?;");
        pstmt.setInt(1, sickDays);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateHoliDaysById(int id, int holiDays) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET holiDays = ? WHERE id = ?;");
        pstmt.setInt(1, holiDays);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public Map<java.util.Date, ShiftType> getEmployeeHistory(int id) throws SQLException {
        Map<java.util.Date, ShiftType> history = new HashMap<>();
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT date, shift_type FROM historyShifts WHERE id = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Date date = java.sql.Date.valueOf(rs.getString("date"));
            ShiftType type = ShiftType.valueOf(rs.getString("shift_type"));
            history.put(date, type);
        }
        rs.close();
        pstmt.close();
        return history;
    }

    public ArrayList<Integer> getAvaliableSuperBranches() throws SQLException {
        ArrayList<Integer> superBranches = new ArrayList<>();
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT site_id FROM Sites");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            superBranches.add(rs.getInt("site_id"));
        }
        rs.close();
        pstmt.close();
        return superBranches;
    }

    public int getSuperBranch(int id) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT branch FROM Sites WHERE id = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        return rs.getInt("branch");
    }

    public void updateSuperBranch(int id,int newNum) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE employees SET branch = ? WHERE id = ?;");
        pstmt.setInt(1, newNum);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void close() {
        if (sqLiteClient != null) {
            sqLiteClient.close();
        }
    }
}
