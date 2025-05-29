import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class GuestGroupDAO extends DAO<GuestGroup>{
    private static GuestGroupDAO instance= null;

    private GuestGroupDAO(){
        super(GuestGroup.class);
    }

    public static GuestGroupDAO getInstance(){
        if(GuestGroupDAO.instance== null)
            GuestGroupDAO.instance= new GuestGroupDAO();
        return GuestGroupDAO.instance;
    }

    @Override
    public int create(GuestGroup object) throws SQLException{
        return 0;
    }
    
    public int create(GuestGroup object, int hotelId) throws SQLException{
        String sql= "insert into guestgroups (hotel_id, nr_days, min_day, normals_array, apartments_array, scenes_array, individuals_array) values (?, ?, ?, ?, ?, ?, ?) returning id";
        GuestDAO guestDAO= GuestDAO.getInstance();
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, hotelId);
            pstm.setInt(2, object.getNrOfDays());
            pstm.setInt(3, object.getMinDay());

            ArrayList<Integer> arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfNormals(); ++i)
                arrays.add(object.getNormalsArray(i));
            Integer[] intArray= arrays.toArray(Integer[]::new);
            java.sql.Array sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(4, sqlarray);
            arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfApartments(); ++i)
                arrays.add(object.getApartmentsArray(i));
            intArray= arrays.toArray(Integer[]::new);
            sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(5, sqlarray);
            arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfScenes(); ++i)
                arrays.add(object.getScenesArray(i));
            intArray= arrays.toArray(Integer[]::new);
            sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(6, sqlarray);
            arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfIndividuals(); ++i)
                arrays.add(object.getIndividualsArray(i));
            intArray= arrays.toArray(Integer[]::new);
            sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(7, sqlarray);

            ResultSet rs= pstm.executeQuery();
            rs.next();
            int id= rs.getInt(1);
            for(int i=0; i< object.getNrOfGuests(); ++i)
                guestDAO.create(object.getGuest(i), id);
            rs.next();
            return id;
        }
        catch(Exception e){
            throw new SQLException("Problems when creating GuestGroup", e);
        }
    }

    @Override
    public GuestGroup read(int id) throws SQLException{
        String sql= "select nr_days, min_day, normals_array, apartments_array, scenes_array, individuals_array from guestgroups where id= ?";
        GuestDAO guestDAO= GuestDAO.getInstance();
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, id);
            ResultSet rs= pstm.executeQuery();
            GuestGroup group= null;
            if(rs.next()){
                ArrayList<Guest> guestsArray= guestDAO.read(id, 0);
                Guest[] guests= new Guest[guestsArray.size()+1];
                for(int i=0; i< guestsArray.size(); ++i)
                    guests[i]= guestsArray.get(i);
                java.sql.Array sqlArray = rs.getArray(3);
                Integer[] integerArray = (Integer[]) sqlArray.getArray();
                int[] normalArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                sqlArray = rs.getArray(4);
                integerArray = (Integer[]) sqlArray.getArray();
                int[] apartmentArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                sqlArray = rs.getArray(5);
                integerArray = (Integer[]) sqlArray.getArray();
                int[] sceneArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                sqlArray = rs.getArray(6);
                integerArray = (Integer[]) sqlArray.getArray();
                int[] individualArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                return new GuestGroup(rs.getInt(1), id, rs.getInt(2), apartmentArray, normalArray, sceneArray, individualArray, guests);
            }
            return group;
        }
        catch(Exception e){
            throw new SQLException("Problems when reading GuestGroup", e);
        }
    }

    public ArrayList<GuestGroup> read(int hotelId, int decide) throws SQLException{
        String sql= "select id, nr_days, min_day, normals_array, apartments_array, scenes_array, individuals_array from guestgroups where hotel_id= ?";
        GuestDAO guestDAO= GuestDAO.getInstance();
        ArrayList<GuestGroup> groups= new ArrayList<>();
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, hotelId);
            ResultSet rs= pstm.executeQuery();
            while(rs.next()){
                ArrayList<Guest> guestsArray= guestDAO.read(rs.getInt(1), 0);
                Guest[] guests= guestsArray.stream().toArray(Guest[]::new);
                java.sql.Array sqlArray = rs.getArray(4);
                Integer[] integerArray = (Integer[]) sqlArray.getArray();
                int[] normalArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                sqlArray = rs.getArray(5);
                integerArray = (Integer[]) sqlArray.getArray();
                int[] apartmentArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                sqlArray = rs.getArray(6);
                integerArray = (Integer[]) sqlArray.getArray();
                int[] sceneArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                sqlArray = rs.getArray(7);
                integerArray = (Integer[]) sqlArray.getArray();
                int[] individualArray = Arrays.stream(integerArray)
                           .mapToInt(Integer::intValue)
                           .toArray();
                groups.add(new GuestGroup(rs.getInt(2), rs.getInt(1), rs.getInt(3), apartmentArray, normalArray, sceneArray, individualArray, guests));
            }
        }
        catch(Exception e){
            throw new SQLException("Problems when reading GuestGroups", e);
        }
        return groups;
    }

    @Override
    public void update(GuestGroup object, int id) throws SQLException{
        String sql= "update guestgroups set nr_days=?, min_day= ?, normals_array= ?, apartments_array= ?, scenes_array= ?, individuals_array= ? where id= ?";
        try(PreparedStatement pstm= this.connection.prepareStatement(sql)){
            pstm.setInt(1, object.getNrOfDays());
            pstm.setInt(2, object.getMinDay());
            ArrayList<Integer> arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfNormals(); ++i)
                arrays.add(object.getNormalsArray(i));
            Integer[] intArray= arrays.toArray(Integer[]::new);
            java.sql.Array sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(3, sqlarray);
            arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfApartments(); ++i)
                arrays.add(object.getApartmentsArray(i));
            intArray= arrays.toArray(Integer[]::new);
            sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(4, sqlarray);
            arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfScenes(); ++i)
                arrays.add(object.getScenesArray(i));
            intArray= arrays.toArray(Integer[]::new);
            sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(5, sqlarray);
            arrays= new ArrayList<>();
            for(int i=0; i< object.getNrOfIndividuals(); ++i)
                arrays.add(object.getIndividualsArray(i));
            intArray= arrays.toArray(Integer[]::new);
            sqlarray= this.connection.createArrayOf("integer", intArray);
            pstm.setArray(6, sqlarray);
            pstm.executeUpdate();
        }
        catch(Exception e){
            throw new SQLException("Problems when updating GuestGroup with id "+ id, e);
        }
    }
    @Override
    public void delete(int id) throws SQLException{
        String sql="delete from guestgroups where id= ?";
        try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            int affectedRows = pstm.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("No GuestGroup found with id " + id);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to delete the GuestGroup with id " + id, e);
        }
    }
}
