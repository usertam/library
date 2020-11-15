package library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The List<String[]> methods will return a Java list with multiple String arrays.
 * Each array consists of elements, which are pieces of information about a book or an user. 
 * 
 * // For example, to print out the all book titles, do: 
 * List<String[]> list = query.list_books();
 * for (String[] b : list) {
 *     System.out.println(b[1]);        // b[1] is the title of current book
 * }
 * 
 * // Or to print out the ISBN of the first book in the list: 
 * List<String[]> list = query.list_books();
 * String b[] = list.get(0);
 * System.out.println(b[0]);            // b[0] is the ISBN of book
 */

/**
 * The String[] methods will return a String array. 
 * The array consists pieces of information requested, as elements. 
 * 
 * // You can do something like: 
 * String[] p = query.get_passwd(0);    // get passwd of admin (user id 0)
 * System.out.println(p[0] + p[1]);     // p[0] is passwd_hash, p[1] is passwd_salt
 */

public class database {

    public static List<String[]> query_books() {

        /** 
         * This method will return a list of arrays, which contain book details. 
         * 
         * list<books[]>─┬─book1[]
         *               ├─book2[]
         *               └─...
         * 
         * book1[]─┬─book1[0] => ISBN
         *         ├─book1[1] => title
         *         ├─book1[2] => author
         *         ├─book1[3] => publication date
         *         ├─book1[4] => status
         *         ├─book1[5] => user id of user borrowing the book
         *         └─book1[6] => due date
         */

        // sql query command
        String cmd = "SELECT * FROM books";

        // query from database, return result
        try (Statement stmt = sqlite.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(cmd);
            List<String[]> list = new ArrayList<>();
            while (rs.next()) {
                String book[] = {
                    rs.getString("isbn"), 
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("pub"),
                    rs.getString("status"),
                    rs.getString("borw_uid"),
                    rs.getString("borw_due"),
                };
                list.add(book);
            }
            return list;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static List<String[]> query_users() {

        /** 
         * This method will return a list of arrays, which contain user details. 
         * 
         * list<users[]>─┬─user1[]
         *               ├─user2[]
         *               └─...
         * 
         * user1[]─┬─user1[0] => uid
         *         ├─user1[1] => user
         *         └─user1[2] => name
         */

        // sql query command
        String cmd = "SELECT uid, user, name FROM users";

        // query from database, return result
        try (Statement stmt = sqlite.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(cmd);
            List<String[]> list = new ArrayList<>();
            while (rs.next()) {
                String user[] = {
                    rs.getString("uid"), 
                    rs.getString("user"),
                    rs.getString("name")
                };
                list.add(user);
            }
            return list;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String[] query_user(int uid) {

        /** 
         * This method will return a string array of user's user id, username and full name. 
         * 
         * user[]─┬─user[0] => uid
         *        ├─user[1] => user
         *        └─user[2] => name
         */

        // sql query command
        String cmd = "SELECT uid, user, name FROM users WHERE uid = " + uid;

        // query from database, return result
        try (Statement stmt = sqlite.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(cmd);
            String user[] = {
                rs.getString("uid"), 
                rs.getString("user"),
                rs.getString("name")
            };
            return user;
        }
        catch (SQLException e) {
            String nobody[] = {"-1", "nobody", "Nobody"};
            return nobody;
        }
    }

    public static String query_user(int uid, int i) {
        String user[] = query_user(uid);
        return user[i];
    }

    public static int query_uid(String user) {

        /** This method will return an user id. */

        // sql query command (prepare statement)
        String cmd = "SELECT uid FROM users WHERE user = ?";

        // query from database, return result
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            int uid = rs.getInt("uid");
            return uid;
        }
        catch (SQLException e) {
            // unable to find user, return user id of nobody
            return -1;
        }
    }

    public static String[] query_passwd(int uid) {

        /** 
         * This method will return a string array of user's password hash and salt. 
         * 
         * p[]─┬─p[0] => passwd_hash
         *     └─p[1] => passwd_salt
         */

        // sql query command
        String cmd = "SELECT passwd_hash, passwd_salt FROM users WHERE uid = " + uid;

        // query from database, return result
        try (Statement stmt = sqlite.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(cmd);
            String p[] = {
                rs.getString("passwd_hash"), 
                rs.getString("passwd_salt")
            };
            return p;
        }
        catch (SQLException e) {
            // this should never be reached, user checking should be performed beforehand
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void write_passwd(int uid, String[] p) {

        // sql query command (prepare statement)
        String cmd = "UPDATE users SET passwd_hash = ?, passwd_salt = ? WHERE uid = ?";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, p[0]);
            pstmt.setString(2, p[1]);
            pstmt.setInt(3, uid);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            // unable to update, print error message
            System.out.println(e.getMessage());
        }
    }

    public static void write_status(int isbn, Object[] status) {

        // sql query command (prepare statement)
        String cmd = "UPDATE books SET status = ?, brow_uid = ?, brow_due = ? WHERE isbn = ?";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setInt(1, (int) status[0]);
            pstmt.setInt(2, (int) status[1]);
            pstmt.setString(2, (String) status[2]);
            pstmt.setInt(3, isbn);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            // unable to update, print error message
            System.out.println(e.getMessage());
        }
    }

    public static void query_books_example() {

        // print message
        System.out.println("[*] Listing all library collections.");

        // get list of book details (in separate array)
        List<String[]> list = database.query_books();

        // print formatted output
        System.out.println("=".repeat(64));
        System.out.println("ISBN\t\tTITLE");
        System.out.println("=".repeat(64));
        for (String[] b : list) {
            System.out.printf("%s\t%s\n", b[0], b[1]);
        }
        System.out.println("=".repeat(64));
    }
}
