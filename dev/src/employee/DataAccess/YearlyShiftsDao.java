package employee.DataAccess;
import java.sql.Date;
import java.sql.ResultSet;

import employee.Dto.ShiftDTO;
import employee.Dto.WeeklyShiftsDTO;
import employee.Enums.*;
import employee.Exceptions.*;
import employee.util.SQLiteClient;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class YearlyShiftsDao {
    private SQLiteClient sqLiteClient;

    public YearlyShiftsDao(String url) {
        this.sqLiteClient = SQLiteClient.getInstance(url);
    }

    public void addShiftToNextWeek(ShiftType type, java.util.Date date) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Days day = Days.values()[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        String time = (type == ShiftType.Morning) ? "08:00-14:00" : "14:00-20:00";
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date sqlDate = Date.valueOf(localDate);
        //insert into nextWeekShifts
        String sql = "INSERT INTO nextWeekShifts (date, day, shift_type, managerId, time) VALUES (" +
                "'" + sqlDate + "'," +
                "'" + day.name() + "'," +
                "'" + type.name() + "'," +
                "0," +
                "'" + time + "');";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
        //insert requirement for shift manager
        String reqSql = "INSERT INTO nextWeekRequirements (date, day, shift_type, role, amount) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement reqStmt = connection.prepareStatement(reqSql);
        reqStmt.setString(1, sqlDate.toString());
        reqStmt.setString(2, day.name());
        reqStmt.setString(3, type.name());
        reqStmt.setString(4, "Shift_Manager"); // assuming "Shift_Manager" is the role name
        reqStmt.setInt(5, 1); // 1 manager required
        reqStmt.executeUpdate();
        reqStmt.close();
    }

    public void removeShiftFromNextWeek(Days day, ShiftType shiftType) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement stmt = null;

        String[] tables = {"nextWeekShifts", "nextWeekPreferences", "nextWeekRequirements", "nextWeekWorkers"};
        for (String table : tables) {
            try {
                stmt = connection.prepareStatement("DELETE FROM " + table + " WHERE shift_type = ? AND day = ?");
                stmt.setString(1, shiftType.name());
                stmt.setString(2, day.name());
                stmt.executeUpdate();
            } finally {
                if (stmt != null) stmt.close();
            }
        }
    }

    public WeeklyShiftsDTO getNextWeek() throws SQLException, InvalidInputException {
        Connection connection = this.sqLiteClient.getConnection();
        WeeklyShiftsDTO weeklyShiftsDTO = new WeeklyShiftsDTO();

        ShiftDTO shift;
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM nextWeekShifts");
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Date date = Date.valueOf(rs.getString("date"));
            Days day = Days.valueOf(rs.getString("day"));
            ShiftType type = ShiftType.valueOf(rs.getString("shift_type"));
            int managerId = rs.getInt("managerId");

            //set shift time
            String time = rs.getString("time");
            String[] parts = time.split("-");
            if (parts.length == 2) {
                shift = new ShiftDTO(type, date, managerId, parts[0], parts[1]);
            } else {
                throw new InvalidInputException("Invalid time format in database.");
            }
            if (type==ShiftType.Morning) weeklyShiftsDTO.weeklyShifts.get(day).add(0,shift);
            else weeklyShiftsDTO.weeklyShifts.get(day).add(shift);
        }
        return weeklyShiftsDTO;
    }


//    public WeeklyShiftsDTO getNextWeek111() throws SQLException, InvalidInputException {
//        Connection connection = this.sqLiteClient.getConnection();
//        WeeklyShifts weeklyShifts = new WeeklyShifts();
//
//        ShiftDTO shift;
//        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM nextWeekShifts");
//        ResultSet rs = pstmt.executeQuery();

//        while (rs.next()) {
//            Date date = Date.valueOf(rs.getString("date"));
//            Days day = Days.valueOf(rs.getString("day"));
//            ShiftType type = ShiftType.valueOf(rs.getString("shift_type"));
//            int managerId = rs.getInt("managerId");
//
//            //set shift time
//            String time = rs.getString("time");
//            String[] parts = time.split("-");
//            if (parts.length == 2) {
//                shift = new ShiftDTO(type, date, managerId,parts[0], parts[1]);
//            } else {
//                throw new InvalidInputException("Invalid time format in database.");
//            }

//            nextWeekRequirements(date,type,shift);


//            //nextWeekPreferences
//            PreparedStatement prefsStmt = connection.prepareStatement("SELECT * FROM nextWeekPreferences WHERE date = ? AND shift_type = ? AND day = ?");
//            prefsStmt.setString(1, date.toString());
//            prefsStmt.setString(2, String.valueOf(type));
//            prefsStmt.setString(3, String.valueOf(day));
//            ResultSet prefsRs = prefsStmt.executeQuery();
//            while (prefsRs.next()) {
//                int empId = prefsRs.getInt("id");
//                shift.getEmployeePreferences().add(empId);
//            }

//            if (type==ShiftType.Morning) weeklyShifts.getShifts(day).add(0,shift);
//            else weeklyShifts.getShifts(day).add(shift);
//
//        }
//        pstmt.close();
//        return weeklyShifts;
//    }

    public Map<Role, Integer> getNextWeekRequirements(java.util.Date date, ShiftType type) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        Map<Role, Integer> requirements = new HashMap<>();
        PreparedStatement reqStmt = connection.prepareStatement("SELECT * FROM nextWeekRequirements WHERE date = ? AND shift_type = ?");
        reqStmt.setString(1, date.toString());
        reqStmt.setString(2, String.valueOf(type));
        ResultSet reqRs = reqStmt.executeQuery();
        while (reqRs.next()) {
            Role role = Role.valueOf(reqRs.getString("role"));
            int amount = reqRs.getInt("amount");
            requirements.put(role, amount);
        }
        reqStmt.close();
        return requirements;
    }

    public Map<Role, ArrayList<Integer>> getNextWeekWorkers(java.util.Date date, ShiftType type) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        Map<Role, ArrayList<Integer>> workers = new HashMap<>();
        PreparedStatement workersStmt = connection.prepareStatement("SELECT * FROM nextWeekWorkers WHERE date = ? AND shift_type = ?");
        workersStmt.setString(1, date.toString());
        workersStmt.setString(2, String.valueOf(type));
//        workersStmt.setString(3, String.valueOf(day));
        ResultSet workersRs = workersStmt.executeQuery();
        while (workersRs.next()) {
            Role role = Role.valueOf(workersRs.getString("role"));
            int empId = workersRs.getInt("id");
            workers.computeIfAbsent(role, k -> new ArrayList<>()).add(empId);
        }
        workersStmt.close();
        return workers;
    }


    public ArrayList<Integer> getNextWeekPreferences(java.util.Date date, ShiftType type) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        ArrayList<Integer> preferences = new ArrayList<>();
        PreparedStatement prefsStmt = connection.prepareStatement("SELECT * FROM nextWeekPreferences WHERE date = ? AND shift_type = ?");
        prefsStmt.setString(1, date.toString());
        prefsStmt.setString(2, String.valueOf(type));
//        prefsStmt.setString(3, String.valueOf(day));
        ResultSet prefsRs = prefsStmt.executeQuery();
        while (prefsRs.next()) {
            int empId = prefsRs.getInt("id");
            preferences.add(empId);
        }
        return preferences;
    }

    public WeeklyShiftsDTO getCurrentWeek() throws SQLException, InvalidInputException {
        Connection connection = this.sqLiteClient.getConnection();
        WeeklyShiftsDTO weeklyShiftsDTO = new WeeklyShiftsDTO();

        ShiftDTO shift;
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM currentWeekShifts");
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Date date = Date.valueOf(rs.getString("date"));
            Days day = Days.valueOf(rs.getString("day"));
            ShiftType type = ShiftType.valueOf(rs.getString("shift_type"));
            int managerId = rs.getInt("managerId");

            //set shift time
            String time = rs.getString("time");
            String[] parts = time.split("-");
            if (parts.length == 2) {
                shift = new ShiftDTO(type, date, managerId, parts[0], parts[1]);
            } else {
                throw new InvalidInputException("Invalid time format in database.");
            }
            if (type==ShiftType.Morning) weeklyShiftsDTO.weeklyShifts.get(day).add(0,shift);
            else weeklyShiftsDTO.weeklyShifts.get(day).add(shift);
        }
        return weeklyShiftsDTO;
    }

    public Map<Role, ArrayList<Integer>> getCurrentWeekWorkers(java.util.Date date, ShiftType type) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        Map<Role, ArrayList<Integer>> workers = new HashMap<>();
        PreparedStatement workersStmt = connection.prepareStatement("SELECT * FROM currentWeekWorkers WHERE date = ? AND shift_type = ?");
        workersStmt.setString(1, date.toString());
        workersStmt.setString(2, String.valueOf(type));
//            workersStmt.setString(3, String.valueOf(day));
        ResultSet workersRs = workersStmt.executeQuery();
        while (workersRs.next()) {
            Role role = Role.valueOf(workersRs.getString("role"));
            int empId = workersRs.getInt("id");
            workers.computeIfAbsent(role, k -> new ArrayList<>()).add(empId);
        }
        workersStmt.close();
        return workers;
    }

    public ArrayList<Integer> getCurrentWeekPreferences(java.util.Date date, ShiftType type) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        ArrayList<Integer> preferences = new ArrayList<>();
        PreparedStatement prefsStmt = connection.prepareStatement("SELECT * FROM currentWeekPreferences WHERE date = ? AND shift_type = ?");
        prefsStmt.setString(1, date.toString());
        prefsStmt.setString(2, String.valueOf(type));
//        prefsStmt.setString(3, String.valueOf(day));
        ResultSet prefsRs = prefsStmt.executeQuery();
        while (prefsRs.next()) {
            int empId = prefsRs.getInt("id");
            preferences.add(empId);
        }
        return preferences;
    }


//    public WeeklyShifts getCurrentWeek() throws SQLException, InvalidInputException {
//        Connection connection = this.sqLiteClient.getConnection();
//        WeeklyShifts weeklyShifts = new WeeklyShifts();
//
//        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM currentWeekShifts");
//        ResultSet rs = pstmt.executeQuery();
//
//        while (rs.next()) {
//            Date date = Date.valueOf(rs.getString("date"));
//            Days day = Days.valueOf(rs.getString("day"));
//            ShiftType type = ShiftType.valueOf(rs.getString("shift_type"));
//            int managerId = rs.getInt("managerId");
//
//            Shift shift = new Shift(type, date);
//            shift.changeManagerShift(managerId);
//
//            //set shift time
//            String time = rs.getString("time");
//            String[] parts = time.split("-");
//            if (parts.length == 2) {
//                shift.setShiftTime(parts[0], parts[1]);
//            } else {
//                throw new InvalidInputException("Invalid time format in database.");
//            }

//            //currentWeekWorkers
//            PreparedStatement workersStmt = connection.prepareStatement("SELECT * FROM currentWeekWorkers WHERE date = ? AND shift_type = ? AND day = ?");
//            workersStmt.setString(1, date.toString());
//            workersStmt.setString(2, String.valueOf(type));
//            workersStmt.setString(3, String.valueOf(day));
//            ResultSet workersRs = workersStmt.executeQuery();
//            while (workersRs.next()) {
//                Role role = Role.valueOf(workersRs.getString("role"));
//                int empId = workersRs.getInt("id");
//                shift.getWorkers().computeIfAbsent(role, k -> new ArrayList<>()).add(empId);
//                if (role!= Role.Shift_Manager) shift.getRequirements().put(role, shift.getRequirements().getOrDefault(role, 0));
//                else shift.getRequirements().put(role, 0);
//            }

//            //currentWeekPreferences
//            PreparedStatement prefsStmt = connection.prepareStatement("SELECT * FROM currentWeekPreferences WHERE date = ? AND shift_type = ? AND day = ?");
//            prefsStmt.setString(1, date.toString());
//            prefsStmt.setString(2, String.valueOf(type));
//            prefsStmt.setString(3, String.valueOf(day));
//            ResultSet prefsRs = prefsStmt.executeQuery();
//            while (prefsRs.next()) {
//                int empId = prefsRs.getInt("id");
//                shift.getEmployeePreferences().add(empId);
//            }

//            if (type==ShiftType.Morning) weeklyShifts.getShifts(day).add(0,shift);
//            else weeklyShifts.getShifts(day).add(shift);
//        }
//        pstmt.close();
//        return weeklyShifts;
//    }

    public void addCurrentToHistoryShift(Map<ShiftDTO, ArrayList<Integer>> shifts) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        for (Map.Entry<ShiftDTO, ArrayList<Integer>> entry : shifts.entrySet()) {
            ShiftDTO shiftDTO = entry.getKey();
            ArrayList<Integer> ids = entry.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(shiftDTO.date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            Days day = Days.values()[(dayOfWeek + 5) % 7];
            for (int workerId : ids) {
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO historyShifts (id, shift_type, day, date) VALUES (?, ?, ?, ?);");
                pstmt.setInt(1, workerId);
                pstmt.setString(2, shiftDTO.type.name());
                pstmt.setString(3, day.name());
                pstmt.setString(4, shiftDTO.date.toString());
                pstmt.executeUpdate();
                pstmt.close();
            }
        }
    }

    public void resetNextWeek() throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM nextWeekShifts;");
        pstmt.executeUpdate();
        pstmt = connection.prepareStatement("DELETE FROM nextWeekRequirements;");
        pstmt.executeUpdate();
        pstmt = connection.prepareStatement("DELETE FROM nextWeekPreferences;");
        pstmt.executeUpdate();
        pstmt = connection.prepareStatement("DELETE FROM nextWeekWorkers;");
        pstmt.executeUpdate();
        pstmt.close();

    }

    public void moveNextWeekToCurrent() throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();

        //delete current Week
        connection.prepareStatement("DELETE FROM currentWeekShifts;").executeUpdate();
        connection.prepareStatement("DELETE FROM currentWeekPreferences;").executeUpdate();
        connection.prepareStatement("DELETE FROM currentWeekWorkers;").executeUpdate();

        //insert to current Week
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO currentWeekShifts SELECT * FROM nextWeekShifts;");
        pstmt.executeUpdate();
        pstmt = connection.prepareStatement("INSERT INTO currentWeekPreferences SELECT * FROM nextWeekPreferences;");
        pstmt.executeUpdate();
        pstmt = connection.prepareStatement("INSERT INTO currentWeekWorkers SELECT * FROM nextWeekWorkers;");
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void removeOldYears(int limitYear) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM historyShifts WHERE CAST(strftime('%Y', date) AS INTEGER) < ?;");
        pstmt.setInt(1, limitYear);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void addWorkerNextWeek(int id,ShiftType shiftType,Days day,Role role,java.util.Date date) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        //add to nextWeekWorkers
        PreparedStatement insertStmt = connection.prepareStatement(
                "INSERT INTO nextWeekWorkers (date, id, shift_type, day, role) VALUES (?, ?, ?, ?, ?);"
        );
        insertStmt.setString(1, formattedDate);
        insertStmt.setInt(2, id);
        insertStmt.setString(3, shiftType.name());
        insertStmt.setString(4, day.name());
        insertStmt.setString(5, role.name());
        insertStmt.executeUpdate();
        insertStmt.close();

        //update nextWeekRequirements
        PreparedStatement updateStmt = connection.prepareStatement(
                "UPDATE nextWeekRequirements SET amount = amount - 1 WHERE date = ? AND shift_type = ? AND day = ? AND role = ?;"
        );
        updateStmt.setString(1, formattedDate);
        updateStmt.setString(2, shiftType.name());
        updateStmt.setString(3, day.name());
        updateStmt.setString(4, role.name());
        updateStmt.executeUpdate();
        updateStmt.close();

        if (role == Role.Shift_Manager) {
            PreparedStatement updateManagerStmt = connection.prepareStatement(
                    "UPDATE nextWeekShifts SET managerId = ? WHERE date = ? AND shift_type = ? AND day = ?;"
            );
            updateManagerStmt.setInt(1, id);
            updateManagerStmt.setString(2, formattedDate);
            updateManagerStmt.setString(3, shiftType.name());
            updateManagerStmt.setString(4, day.name());
            updateManagerStmt.executeUpdate();
            updateManagerStmt.close();
        }
    }

    public void removeWorkerNextWeek(int id,ShiftType shiftType,Days day,java.util.Date date) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        //select the role
        PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT role FROM nextWeekWorkers WHERE id = ? AND shift_type = ? AND day = ? AND date = ?;"
        );
        selectStmt.setInt(1, id);
        selectStmt.setString(2, shiftType.name());
        selectStmt.setString(3, day.name());
        selectStmt.setString(4, formattedDate);
        ResultSet rs = selectStmt.executeQuery();

        String role = null;
        if (rs.next()) {
            role = rs.getString("role");
        }
        rs.close();
        selectStmt.close();

        //delete the worker
        PreparedStatement deleteStmt = connection.prepareStatement(
                "DELETE FROM nextWeekWorkers WHERE id = ? AND shift_type = ? AND day = ? AND date = ?;"
        );
        deleteStmt.setInt(1, id);
        deleteStmt.setString(2, shiftType.name());
        deleteStmt.setString(3, day.name());
        deleteStmt.setString(4, formattedDate);
        deleteStmt.executeUpdate();
        deleteStmt.close();

        //update nextWeekRequirements
        if (role != null) {
            PreparedStatement updateStmt = connection.prepareStatement(
                    "UPDATE nextWeekRequirements SET amount = amount + 1 WHERE date = ? AND shift_type = ? AND day = ? AND role = ?;"
            );
            updateStmt.setString(1, formattedDate);
            updateStmt.setString(2, shiftType.name());
            updateStmt.setString(3, day.name());
            updateStmt.setString(4, role);
            updateStmt.executeUpdate();
            updateStmt.close();
        }

        if (role != null && Role.valueOf(role) == Role.Shift_Manager) {
            PreparedStatement clearManagerStmt = connection.prepareStatement(
                    "UPDATE nextWeekShifts SET managerId = 0 WHERE date = ? AND shift_type = ? AND day = ?;"
            );
            clearManagerStmt.setString(1, formattedDate);
            clearManagerStmt.setString(2, shiftType.name());
            clearManagerStmt.setString(3, day.name());
            clearManagerStmt.executeUpdate();
            clearManagerStmt.close();
        }
    }

    public void updateEmployeeAssignmentInCurrentWeek(int oldId, int newId,ShiftType shiftType,Days day,java.util.Date date) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        //select the role
        PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT role FROM currentWeekWorkers WHERE id = ? AND shift_type = ? AND day = ? AND date = ?;"
        );
        selectStmt.setInt(1, oldId);
        selectStmt.setString(2, shiftType.name());
        selectStmt.setString(3, day.name());
        selectStmt.setString(4, date.toString());
        ResultSet rs = selectStmt.executeQuery();

        String role = null;
        if (rs.next()) {
            role = rs.getString("role");
        }
        rs.close();
        selectStmt.close();

        //update the worker
        PreparedStatement updateStmt = connection.prepareStatement(
                "UPDATE currentWeekWorkers SET id = ? WHERE id = ? AND shift_type = ? AND day = ? AND date = ?;"
        );
        updateStmt.setInt(1, newId);
        updateStmt.setInt(2, oldId);
        updateStmt.setString(3, shiftType.name());
        updateStmt.setString(4, day.name());
        updateStmt.setString(5, date.toString());
        updateStmt.executeUpdate();
        updateStmt.close();

    }

    public void addRoleNextWeekShift(Role role,int amount,ShiftType shiftType,Days day,java.util.Date date) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO nextWeekRequirements (date, role,amount, shift_type, day)  VALUES" +
                "('" + date + "','" + role + "'," + amount + ",'" + shiftType +"','" + day +"');");
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateRoleNextWeekShift(Role role,int amount,ShiftType shiftType,Days day) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE nextWeekRequirements SET amount = ? WHERE role = ? AND shift_type = ? AND day = ?;");
        pstmt.setInt(1, amount);
        pstmt.setString(2, String.valueOf(role));
        pstmt.setString(3, String.valueOf(shiftType));
        pstmt.setString(4, String.valueOf(day));
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateShiftTimeNextWeekShift(ShiftType shiftType,Days day,String time) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE nextWeekShifts SET time = ? WHERE shift_type = ? AND day = ?;");
        pstmt.setString(1, time);
        pstmt.setString(2, String.valueOf(shiftType));
        pstmt.setString(3, String.valueOf(day));
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void addShiftPreferencesNextWeekShift(java.util.Date date, ShiftType shiftType,Days day,int id) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO nextWeekPreferences (date, id, shift_type, day) VALUES" +
                "('" + date + "'," + id + ",'" + shiftType +"','" + day +"');");
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void removeShiftPreferencesNextWeekShift(java.util.Date date, ShiftType shiftType,Days day,int id) throws SQLException {
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM nextWeekPreferences WHERE id = ? AND shift_type = ? AND day = ? AND date = ?;");
        pstmt.setInt(1, id);
        pstmt.setString(2, String.valueOf(shiftType));
        pstmt.setString(3, String.valueOf(day));
        pstmt.setString(4, date.toString());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public List<Integer> getAvailableYears() throws SQLException {
        List<Integer> years = new ArrayList<>();
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT DISTINCT strftime('%Y', date) AS year FROM historyShifts;");
        ResultSet prefsRs = pstmt.executeQuery();
        while (prefsRs.next()) {
            int year = Integer.parseInt(prefsRs.getString("year"));
            years.add(year);
        }
        pstmt.close();
        return years;
    }

    public Map<java.util.Date, Map<ShiftType, List<Integer>>> getYearlyShifts(int year) throws SQLException, InvalidInputException {
        Map<java.util.Date, Map<ShiftType, List<Integer>>> result = new HashMap<>();
        Connection connection = this.sqLiteClient.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM historyShifts WHERE strftime('%Y', date) = ? ORDER BY date");
        pstmt.setString(1, String.valueOf(year));
        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
            Date date = Date.valueOf(rs.getString("date"));
            ShiftType type = ShiftType.valueOf(rs.getString("shift_type"));
            int employeeId = rs.getInt("id");

            result.computeIfAbsent(date, k -> new HashMap<>())
                    .computeIfAbsent(type, k -> new ArrayList<>())
                    .add(employeeId);
        }
        rs.close();
        pstmt.close();
        if (!found) {
            throw new InvalidInputException("No shift data found for year: " + year);
        }
        return result;
    }

    public void close() {
        if (sqLiteClient != null) {
            sqLiteClient.close();
        }
    }

}
