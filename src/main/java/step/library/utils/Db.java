package step.library.utils;

import org.json.JSONObject;

import java.sql.Connection;

public class Db {
    private static JSONObject config ;
    private static Connection connection ;

    public static Connection getConnection() {
        return connection;
    }

    public static boolean setConnection( JSONObject json ) {
        try {
            String dbms = json.getString( "dbms" ) ;
            if( dbms.equalsIgnoreCase( "Oracle") ) {

                config = json ;
                return true ;
            } else {
                System.err.println( "Db: Unsupported DBMS" ) ;
            }
        } catch ( Exception ex ) {
            System.err.println( "Db: " + ex.getMessage() ) ;
        }

        connection = null ;
        config = null ;
        return false ;
    }
}
