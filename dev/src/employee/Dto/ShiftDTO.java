package employee.Dto;

import employee.Enums.*;

import java.util.Date;

public class ShiftDTO {
    public ShiftType type;
    public Date date;
    public int managerID; // id of the manager
    public String startTime;
    public String endTime;

    public ShiftDTO(ShiftType type, Date date,int managerID,String startTime,String endTime){
        this.date = date;
        this.type = type;
        this.managerID = managerID;
        this.startTime = startTime;
        this.endTime = endTime;
    }




}
