package employee.util;

import java.sql.Connection;

public interface DataBaseClient {
    Connection getConnection();
    void close();
}
