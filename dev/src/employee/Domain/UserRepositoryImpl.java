package employee.Domain;

import employee.DataAccess.UserDao;
import employee.Dto.UserDTO;
import employee.Exceptions.UserNotFound;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepositoryImpl implements UserRepository{
    private UserDao userDao;
    private List<User> users;
    private static UserRepositoryImpl instance;

    private UserRepositoryImpl(){}

    public static UserRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void connectDao(String url) throws UserNotFound, SQLException {
        this.userDao = new UserDao(url);
        users = new ArrayList<>();
        loadUsers();
    }

    public void loadUsers() throws UserNotFound, SQLException {
        List<UserDTO> userDTOs = userDao.getUsers();
        for (UserDTO userDTO : userDTOs){
            users.add(new User(userDTO.username,userDTO.job,userDTO.password,userDTO.id));
        }
    }

    @Override
    public void connectDaoEmpty(String url) throws SQLException, UserNotFound {
        this.userDao = new UserDao(url);
        userDao.connectDaoEmpty();
        users = new ArrayList<>();
        loadUsers();
    }

    @Override
    public void connectDaoTEST(String url) throws SQLException, UserNotFound {
        this.userDao = new UserDao(url);
        userDao.connectDataBaseTEST();
        users = new ArrayList<>();
    }

    @Override
    public User getUserByUserName(String username) throws UserNotFound {
        for(User user :users){
            if(Objects.equals(user.getUsername(), username)){
                return user;
            }
        }
        throw new UserNotFound("user not found.");
    }

    public void close() {
        if (userDao != null)
            userDao.close();
    }

}
