import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuditServiceDAO extends DAO<String[]>{
    private static AuditServiceDAO instance= null;

    private AuditServiceDAO(){
        super(String[].class);
    }

    public static AuditServiceDAO getInstance(){
        if(AuditServiceDAO.instance== null)
            AuditServiceDAO.instance= new AuditServiceDAO();
        return AuditServiceDAO.instance;
    }

    @Override
    public int create(String[] object) throws SQLException{
        String sql= "insert into audit_logs (action_name, time) values (?, ?) returning id";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setString(1, object[0]);
            pstm.setObject(2, LocalDateTime.parse(object[1]));
            ResultSet rs= pstm.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch(Exception e){
            throw new SQLException("Failed to create new audit_log.", e);
        }
    }
    @Override
    public String[] read(int id) throws SQLException{
        String sql="select action_name, time from audit_logs where id= ?";
        String[] string= new String[2];
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, id);
            ResultSet rs= pstm.executeQuery();
            if(rs.next()){
                string[0]= rs.getString(1);
                string[1]= rs.getTime(2).toString();
            }
        }catch(Exception e){
            throw new SQLException("Failed to read audit_log with id"+ id, e);
        }
        return string;
    }
    @Override
    public void update(String[] object, int id) throws SQLException{
        String sql= "update audit_logs set action_name= ?, time= ? where id= ?";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setString(1, object[0]);
            pstm.setTime(2, java.sql.Time.valueOf(object[1]));
            pstm.setInt(3, id);
            pstm.executeUpdate();
        }catch(Exception e){
            throw new SQLException("Failed to update audit_log with id"+ id, e);
        }
    }
    @Override
    public void delete(int id) throws SQLException{
        String sql= "delete from audit_logs where id= ?";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, id);
            int result = pstm.executeUpdate();
            if(result== 0)
                throw new SQLException("No log with the id found: " + id);
        }catch(Exception e){
            throw new SQLException("Failed to delete a audit_log.", e);
        }
    }
    public void delete() throws SQLException{
        String sql= "delete from audit_logs";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.executeUpdate();
        }catch(Exception e){
            throw new SQLException("Failed to delete all audit_logs.", e);
        }
    }
}
