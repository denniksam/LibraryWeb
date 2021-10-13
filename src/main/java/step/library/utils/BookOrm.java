package step.library.utils;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookOrm {
    private Connection connection ;
    private final String PREFIX ;
    private final JSONObject config ;

    BookOrm(Connection connection, String PREFIX, JSONObject config) {
        this.connection = connection ;
        this.PREFIX = PREFIX ;
        this.config = config ;
    }

    public boolean isTableExists() {
        String query ;
        String dbms = config.getString( "dbms" ) ;
        if( dbms.equalsIgnoreCase( "Oracle") ) {
            query = "SELECT COUNT(*) " +
                    "FROM USER_TABLES T " +
                    "WHERE T.TABLE_NAME = " +
                    "'" + PREFIX + "BOOKS'" ;
        } else {
            return false ;
        }
        try ( ResultSet res = connection
                .createStatement()
                .executeQuery( query ) ) {
            if( res.next() ) {
                return res.getInt( 1 ) == 1 ;
            }
        }
        catch( SQLException ex ) {
            System.err.println(
                    "BookOrm.isTableExists: "
                    + ex.getMessage() ) ;
        }
        return false ;
    }

    public boolean installTable() {
        String query ;
        String dbms = config.getString( "dbms" ) ;
        if( dbms.equalsIgnoreCase( "Oracle") ) {
            query = "CREATE TABLE " + PREFIX + "BOOKS (" +
                    "id       RAW(16) DEFAULT SYS_GUID() PRIMARY KEY," +
                    "author   NVARCHAR2(128) NOT NULL," +
                    "title    NVARCHAR2(128) NOT NULL," +
                    "cover    NVARCHAR2(128) )" ;
        } else {
            return false ;
        }
        try( Statement statement = connection.createStatement() ) {
            statement.executeUpdate( query ) ;
            return true ;
        } catch( SQLException ex ) {
            System.err.println(
                    "BookOrm.installTable: " + ex.getMessage() + "\n" + query ) ;
        }
        return false ;
    }
}
