package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class AuditService {
    private AuditService(){}
    private static AuditService instance = null;
    public static AuditService getInstance(){
        if(instance == null){
            instance = new AuditService();
        }
        return instance;
    }
    public void writeAudit(String methodName){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        try(var out = new BufferedWriter(new FileWriter("C:\\Users\\Melania Ion\\Desktop\\Library-Management-System_PAO\\Library_Management_App\\src\\csvFile\\Audit.csv", true))){
            out.write(methodName +","+time + "\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
