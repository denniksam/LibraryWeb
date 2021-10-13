package step.library.utils;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
