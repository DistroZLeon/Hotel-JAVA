import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestDAO extends DAO<Guest> {
    private static GuestDAO instance= null;

    private GuestDAO(){
        super(Guest.class);
    }

    public static GuestDAO getInstance(){
        if(GuestDAO.instance== null)
            GuestDAO.instance= new GuestDAO();
        return GuestDAO.instance;
    }

    @Override
    public int create(Guest object) throws SQLException{
        return 0;
    }
    public int create(Guest object, int groupId) throws SQLException{
        String sql="insert into guests (group_id, name, cnp, preferred_menu, age, room_index) values (?, ?, ?, ?, ?, ?) returning id";
        try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
            pstm.setInt(1, groupId);
            pstm.setString(2, object.getName());
            pstm.setString(3, object.getCNP());
            pstm.setString(4, object.getPreferredMenu());
            pstm.setInt(5, object.getAge());
            pstm.setInt(6, object.getRoomIndex());
            ResultSet rs= pstm.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new SQLException("Failed to create Guest", e);
        }
    }
    @Override
    public Guest read(int id) throws SQLException{
        String sql="select name, cnp, preferred_menu, age, room_index from guests where id=?";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, id);
            ResultSet rs= pstm.executeQuery();
            return rs.next()? new Guest(
                rs.getString("name"),
                rs.getString("cnp"),
                rs.getString("preferred_menu"),
                rs.getInt("age"),
                rs.getInt("room_index")
            ): null;
        }
        catch(Exception e){
            throw new SQLException("Problems when reading Guest", e);
        }
    }
    public ArrayList<Guest> read(int groupId, int decide) throws SQLException{
        String sql="select name, cnp, preferred_menu, age, room_index from guests where group_id=?";
        ArrayList<Guest> guests = new ArrayList<>();
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, groupId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Guest guest = new Guest(
                        rs.getString("name"),
                        rs.getString("cnp"),
                        rs.getString("preferred_menu"),
                        rs.getInt("age"),
                        rs.getInt("room_index")
                    );
                    guests.add(guest);
                }
            }
        }
        catch(Exception e){
            throw new SQLException("Problems when reading Guests", e);
        }
        return guests;
    }
    @Override
    public void update(Guest object, int id) throws SQLException{
        String sql="update guests set name= ?, cnp= ?, preferred_menu= ?, age= ?, room_index= ? where id=?";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setString(1, object.getName());
            pstm.setString(2, object.getCNP());
            pstm.setString(3, object.getPreferredMenu());
            pstm.setInt(4, object.getAge());
            pstm.setInt(5, object.getRoomIndex());
            pstm.setInt(6, id);
            pstm.executeUpdate();
        }
        catch(Exception e){
            throw new SQLException("Failed to update guest with id "+ id, e);
        }
    }
    
    @Override
    public void delete(int groupId) throws SQLException{
        String sql= "delete from guests where group_id= ?";
        try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
            pstm.setInt(1, groupId);
            int affectedRows = pstm.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("No guests found that have the groupId: " + groupId);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to delete guests that have the groupId: " + groupId, e);
        }
    }
}
