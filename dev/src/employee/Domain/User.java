package employee.Domain;

import employee.Enums.*;

public class User {
    private String username;
    private Job job;
    private String password;
    private int id;


    public User(String username, Job job, String password, int id){
        this.username = username;
        this.job = job;
        this.password = password;
        this.id = id;
    }

    public Job getJob(){
        return this.job;
    }

    public String getPassword(){
        return this.password;
    }

    public int getId(){
        return this.id;
    }

    public String getUsername(){ return this.username;}
}
