import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance= null;
    private BufferedWriter out;
    private AuditService(){
        try{
            File audit_file= new File("C:/Facultate/OOPA-Java/Project/files/audit_log.csv");
            this.out= new BufferedWriter(new FileWriter(audit_file, true));
            if(!audit_file.exists()){
                out.write("action, timestamp\n");
                out.flush();
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static synchronized  AuditService getInstance(){
        if(AuditService.instance==null)
            AuditService.instance= new AuditService();
        return AuditService.instance;
    }

    public void log(String action) throws SQLException{
        AuditServiceDAO auditServiceDAO= AuditServiceDAO.getInstance();
        try {
            String recorded= String.format("\"%s\", \"%s\"%n", action, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            String[] data= {action, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)};
            auditServiceDAO.create(data);
            out.write(recorded);
            out.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void close() {
        try {
            if (this.out != null) {
                this.out.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void cleanAudit() throws SQLException{
        AuditServiceDAO dao= AuditServiceDAO.getInstance();
        dao.delete();
        try {
            if (this.out != null) {
                this.out.close();
            }
            File auditFile = new File("C:/Facultate/OOPA-Java/Project/files/audit_log.csv");
            if (auditFile.exists()) {
                boolean deleted = auditFile.delete();
                if (!deleted) {
                    throw new IOException("Failed to delete audit file");
                }
            }
            this.out = new BufferedWriter(new FileWriter(auditFile, true)); 
            out.write("action, timestamp\n");
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to clean CSV audit file: " + e.getMessage());
        }
    }
}
