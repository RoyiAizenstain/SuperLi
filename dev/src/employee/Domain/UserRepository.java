package employee.Domain;

import employee.Exceptions.UserNotFound;

import java.sql.*;

public interface UserRepository {

    User getUserByUserName(String username) throws SQLException, UserNotFound;
    void connectDao(String url) throws UserNotFound, SQLException;
    void connectDaoEmpty(String url) throws SQLException, UserNotFound;
    void connectDaoTEST(String url) throws SQLException, UserNotFound;
}


