import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO<Type> {
    private final Class<Type> typeClass;
    protected Connection connection;
    
    public DAO(Class<Type> typeClass){
        this.typeClass= typeClass;
        final String url = "jdbc:postgresql://localhost:5432/hoteldb";
        final String user = "postgres";
        final String pass = "proiectjava";
        try {
                this.connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Connection established for "+ typeClass.getSimpleName());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    public abstract int create(Type object) throws SQLException;
    public abstract Type read(int i) throws SQLException;
    public abstract void update(Type object, int id) throws SQLException;
    public abstract void delete(int id) throws SQLException;
    protected void log(String action) throws SQLException{
        AuditService.getInstance().log(action);
    }
}
