
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelDAO extends DAO<HotelConfig>{
    private static HotelDAO instance= null;

    private HotelDAO(){
        super(HotelConfig.class);
    }

    public static HotelDAO getInstance(){
        if(HotelDAO.instance== null)
            HotelDAO.instance= new HotelDAO();
        return HotelDAO.instance;
    }

    @Override
    public int create(HotelConfig object) throws SQLException{
        String sql = """
            INSERT INTO hotels (structure_hash, structure_file, days_state)
            VALUES (?, ?, ?)
            RETURNING id
            """;
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setString(1, object.getHashStructure());
            pstm.setBytes(2, serialize(object.getStructure()));
            pstm.setBytes(3, serialize(object.getRoomDays()));
            ResultSet rs = pstm.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch(Exception e){
            throw new SQLException("Problems when creating HotelConfig", e);
        }
    }
    @Override
    public HotelConfig read(int i) throws SQLException{
        return new HotelConfig(null, null);
    }
    public HotelConfig read(String hash) throws SQLException{
        String sql = "SELECT id, structure_file, days_state FROM hotels WHERE structure_hash = ?";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setString(1, hash);
            ResultSet rs= pstm.executeQuery();
            HotelConfig newConfig;
            if(rs.next()) {
                newConfig=new HotelConfig(deserializeStructure(rs.getBytes(2)), deserializeRDays(rs.getBytes(3)));
                newConfig.setId(rs.getInt(1));
                File file = new File("C:/Facultate/OOPA-Java/Project/files/temporary.txt");
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        throw new IOException("Failed to delete audit file");
                    }
                }
            }
            else 
                newConfig= null;
            
            return newConfig;
        }catch(Exception e){
            throw new SQLException("Problems when reading a HotelConfig", e);
        }
    }

    @Override
    public void update(HotelConfig object, int id) throws SQLException{
        String sql = "UPDATE hotels SET days_state = ? WHERE id = ?";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setBytes(1,serialize(object.getRoomDays()));
            pstm.setInt(2,id);
            pstm.executeUpdate();
        }catch(Exception e){
            throw new SQLException("Problems when updating a HotelConfig whit id "+ id, e);
        }
    }
    
    @Override
    public void delete(int id) throws SQLException{
        String sql = "DELETE FROM hotels WHERE id = ?";
        try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            int affectedRows = pstm.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("No hotel found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to delete hotel with ID: " + id, e);
        }
    }

    private byte[] serialize(ArrayList<ArrayList<Integer>> rDays) throws IOException{
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeInt(rDays.size());
                for(ArrayList<Integer> room: rDays)
                    for(Integer day: room)
                        dos.writeInt(day);
                return bos.toByteArray();
            }
    }
    
    private byte[] serialize(File file) throws IOException{
        try (FileInputStream fis = new FileInputStream(file);
         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) 
                bos.write(buffer, 0, bytesRead);
            return bos.toByteArray();
        }
    }

    private ArrayList<ArrayList<Integer>> deserializeRDays(byte[] data) throws IOException{
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bis)) {
                int rooms = dis.readInt();
                ArrayList<ArrayList<Integer>> list= new ArrayList<>();
                for(int i=0; i<rooms; ++i)
                    {
                        ArrayList<Integer> days= new ArrayList<>();
                        for(int j=1; j<366; ++j)
                            days.add(dis.readInt());
                        list.add(days);
                    }
                return list;
            }
    }
    private File deserializeStructure(byte[] data) throws IOException{
        File output= new File("C:/Facultate/OOPA-Java/Project/files/temporary.txt");
        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(data);
        }
        return output;
    }
}
