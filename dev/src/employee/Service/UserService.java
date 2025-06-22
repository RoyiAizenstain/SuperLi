package employee.Service;

import employee.Domain.DomainController;
import employee.Enums.Job;
import employee.Exceptions.*;

import java.sql.SQLException;

public class UserService {
    DomainController controller = new DomainController();

    public void isUserExists(String username) throws UserNotFound, SQLException {
        controller.isUserExists(username);
    }

    public void isJobMatchUser(String username,Job job) throws UserNotFound, SQLException {
        controller.isJobMatchUser(username,job);
    }

    public void isPasswordMatchUser(String username, String password) throws UserNotFound, SQLException {
        controller.isPasswordMatchUser(username,password);
    }

    public int getId(String username) throws UserNotFound, SQLException {
        return controller.getId(username);
    }

    public void connectDao(String url) throws InvalidInputException, AbilityExistsException, SQLException, UserNotFound {
        controller.connectDao(url);
    }

    public void connectDaoEmpty(String url) throws SQLException, InvalidInputException, AbilityExistsException, UserNotFound {
        controller.connectDaoEmpty(url);
    }

    public boolean isDateSATURDAY(){
        return controller.isDateSATURDAY();
    }

    public void saveAndReset() throws InvalidInputException, SQLException {
        controller.addCurrentWeek();
    }

    public void closeConnection(){
        controller.closeConnections();
    }
}
