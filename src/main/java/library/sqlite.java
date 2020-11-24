package library;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqlite {

    public static Connection conn;

    public static void connect() {

        // check if the database file exists
        File db = new File("library.db");
        if (!db.exists()) {
            System.out.printf("[-] Unable to find the database file.\n");
            app.exit(1);
        }

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
