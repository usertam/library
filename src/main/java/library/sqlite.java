package library;

import java.sql.*;

public class sqlite {

    public static Connection conn;

    public static void connect() {
        try {
            String url = "jdbc:sqlite:library.db";
            conn = DriverManager.getConnection(url);
            // System.out.printf("[*] SQLite: Connection established.\n");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            app.exit(1);
        }
    }

    public static void kill(){
        try {
            if (conn != null) {
                conn.close();
                // System.out.printf("[*] SQLite: Connection closed.\n");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
