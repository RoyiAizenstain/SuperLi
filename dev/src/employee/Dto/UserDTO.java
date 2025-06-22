package employee.Dto;

import employee.Enums.Job;

public class UserDTO {
    public final String username;
    public final Job job;
    public final String password;
    public final int id;

    public UserDTO(String username, Job job, String password, int id) {
        this.username = username;
        this.job = job;
        this.password = password;
        this.id = id;
    }
}
