package library;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The List<String[]> methods will return a list (dynamic array) with multiple string arrays.
 * Each array consists of elements, which are pieces of information about a book or an user. 
 * 
 * // For example, to print out the all book titles, do: 
 * List<String[]> list = query.query_books();
 * for (String[] book : list) {
 *     System.out.println(book[1]);     // b[1] is the title of current book
 * }
 * 
 * // Or to print out the ISBN of the first book in the list: 
 * String book[] = list.get(0);
 * System.out.println(book[0]);         // b[0] is the ISBN of book
 */

/**
 * The String[] methods will return a string array. 
 * The array consists pieces of information requested, as elements. 
 * 
 * // You can do something like: 
 * String[] p = query.get_passwd(0);    // get passwd of admin (uid 0)
 * System.out.println(p[0] + p[1]);     // p[0] is passwd_hash, p[1] is passwd_salt
 */

public class database {

    public static List<String[]> query_books() {

        /** 
         * Return a list of arrays, which contain book details. 
         * 
         * list<books[]>─┬─book1[]
         *               ├─book2[]
         *               └─...
         * 
         * book[]─┬─book[0] => ISBN
         *        ├─book[1] => title
         *        ├─book[2] => author
         *        ├─book[3] => publication date
         *        ├─book[4] => status
         *        ├─book[5] => user id of user borrowing the book
         *        └─book[6] => due date
         */

        // sql query statement
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
                    rs.getString("status_uid"),
                    rs.getString("status_due"),
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

    public static List<String[]> query_books(String word) {

        /** 
         * Return a list of arrays, which contain book details. 
         * 
         * list<books[]>─┬─book1[]
         *               ├─book2[]
         *               └─...
         * 
         * book[]─┬─book[0] => ISBN
         *        ├─book[1] => title
         *        ├─book[2] => author
         *        ├─book[3] => publication date
         *        ├─book[4] => status
         *        ├─book[5] => user id of user borrowing the book
         *        └─book[6] => due date
         */

        // sql query prepare statement
        String cmd = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";

        // query from database, return result
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, String.format("%%%s%%", word));
            pstmt.setString(2, String.format("%%%s%%", word));
            ResultSet rs = pstmt.executeQuery();
            List<String[]> list = new ArrayList<>();
            while (rs.next()) {
                String book[] = {
                    rs.getString("isbn"), 
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("pub"),
                    rs.getString("status"),
                    rs.getString("status_uid"),
                    rs.getString("status_due"),
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

    public static List<String[]> query_records(int uid) {

        /** 
         * Return a list of arrays, which contain book details. 
         * 
         * list<books[]>─┬─book1[]
         *               ├─book2[]
         *               └─...
         * 
         * book[]─┬─book[0] => ISBN
         *        ├─book[1] => title
         *        ├─book[2] => author
         *        ├─book[3] => publication date
         *        ├─book[4] => status
         *        ├─book[5] => user id of user borrowing the book
         *        └─book[6] => due date
         */

        // sql query prepare statement
        String cmd = "SELECT * FROM books WHERE status_uid = ?";

        // query from database, return result
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setInt(1, uid);
            ResultSet rs = pstmt.executeQuery();
            List<String[]> list = new ArrayList<>();
            while (rs.next()) {
                String book[] = {
                    rs.getString("isbn"), 
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("pub"),
                    rs.getString("status"),
                    rs.getString("status_uid"),
                    rs.getString("status_due"),
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
         * Return a list of arrays, which contains user details. 
         * However, password hash and salt should never be loaded in this method. 
         * 
         * list<users[]>─┬─user1[]
         *               ├─user2[]
         *               └─...
         * 
         * user[]─┬─user[0] => uid / user id
         *        ├─user[1] => user
         *        └─user[2] => name
         */

        // sql query statement
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

    public static String[] query_book(String isbn) {

        /** 
         * Return an array, which contains book details. 
         * 
         * book[]─┬─book[0] => ISBN
         *        ├─book[1] => title
         *        ├─book[2] => author
         *        ├─book[3] => publication date
         *        ├─book[4] => status
         *        ├─book[5] => user id of user borrowing the book
         *        └─book[6] => due date
         */

        // sql query prepare statement
        String cmd = "SELECT * FROM books WHERE isbn = ?";

        // query from database, return result
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            String book[] = {
                rs.getString("isbn"), 
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("pub"),
                rs.getString("status"),
                rs.getString("status_uid"),
                rs.getString("status_due"),
            };
            return book;
        }
        catch (SQLException e) {
            // unable to find book, return null
            String book[] = null;
            return book;
        }
    }

    public static String[] query_user(int uid) {

        /** 
         * Return an array, which contains details of an user. 
         * 
         * user[]─┬─user[0] => uid / user id
         *        ├─user[1] => user
         *        └─user[2] => name
         */

        // sql query prepare statement
        String cmd = "SELECT uid, user, name FROM users WHERE uid = ?";

        // query from database, return result
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setInt(1, uid);
            ResultSet rs = pstmt.executeQuery();
            String user[] = {
                rs.getString("uid"), 
                rs.getString("user"),
                rs.getString("name")
            };
            return user;
        }
        catch (SQLException e) {
            // unable to find user, return user info of nobody
            String nobody[] = {"-1", "nobody", "Nobody"};
            return nobody;
        }
    }

    public static String[] query_passwd(int uid) {

        /** 
         * Return an array, which contains password details of an user. 
         * 
         * p[]─┬─p[0] => passwd_hash
         *     └─p[1] => passwd_salt
         */

        // sql query prepare statement
        String cmd = "SELECT passwd_hash, passwd_salt FROM users WHERE uid = ?";

        // query from database, return result
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setInt(1, uid);
            ResultSet rs = pstmt.executeQuery();
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

    public static int query_uid(String user) {

        // sql query prepare statement
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

    public static int write_passwd(int uid, String[] p) {

        // sql query prepare statement
        String cmd = "UPDATE users SET passwd_hash = ?, passwd_salt = ? WHERE uid = ?";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, p[0]);
            pstmt.setString(2, p[1]);
            pstmt.setInt(3, uid);
            int i = pstmt.executeUpdate();
            return i;
        }
        catch (SQLException e) {
            // unable to update, print error message and return 0
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static int write_status(String isbn, Object[] status) {

        /** 
         * First, take string and an object array as argument. 
         * 
         * isbn => isbn of the specified book
         * The ISBN is treated as string, because it's pointless and too long to be an integer.
         * 
         * status[]─┬─status[0] (int) => book status (0 = free, 1 = reserved, 2 = borrowed)
         *          ├─status[1] (int) => the uid / user id who reserved/borrowed the book
         *          └─status[2] (String) => due date of the borrowed book
         * 
         * Finally, return an integer as write status. 
         * 
         * 0 => nothing is updated, failed
         * 1 => one entry updated, success
         */

        // sql query prepare statement
        String cmd = "UPDATE books SET status = ?, status_uid = ?, status_due = ? WHERE isbn = ?";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setInt(1, (int) status[0]);
            pstmt.setInt(2, (int) status[1]);
            pstmt.setString(3, (String) status[2]);
            pstmt.setString(4, isbn);
            int i = pstmt.executeUpdate();
            return i;
        }
        catch (SQLException e) {
            // unable to update, print error message and return 0
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static int add_book(String[] book) {

        /** 
         * Take string array as argument. 
         * 
         * book[]─┬─book[0] => ISBN
         *        ├─book[1] => title
         *        ├─book[2] => author
         *        └─book[3] => publication date
         * 
         * Return an integer as write status. 
         * 
         * 0 => nothing is updated, failed
         * 1 => one entry updated, success
         */

        // sql query prepare statement
        String cmd = "INSERT INTO books (isbn, title, author, pub, status) VALUES (?, ?, ?, ?, 0)";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, book[0]);
            pstmt.setString(2, book[1]);
            pstmt.setString(3, book[2]);
            pstmt.setString(4, book[3]);
            int i = pstmt.executeUpdate();
            return i;
        }
        catch (SQLException e) {
            // unable to update, print error message and return 0
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static int add_user(String[] user) {

        /** 
         * Take string array as argument. 
         * 
         * user[]─┬─user[0] (int) => uid / user id
         *        ├─user[1] (String) => user
         *        └─user[2] (String) => name
         * 
         * Return an integer as write status. 
         * 
         * 0 => nothing is updated, failed
         * 1 => one entry updated, success
         */

        // sql query prepare statement
        String cmd = "INSERT INTO users (uid, user, name) VALUES (?, ?, ?)";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, user[0]);
            pstmt.setString(2, user[1]);
            pstmt.setString(3, user[2]);
            int i = pstmt.executeUpdate();
            return i;
        }
        catch (SQLException e) {
            // unable to update, print error message and return 0
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static int del_book(String isbn) {

        /** 
         * Return an integer as write status. 
         * 
         * 0 => nothing is updated, failed
         * 1 => one entry updated, success
         */

        // sql query prepare statement
        String cmd = "DELETE FROM books WHERE isbn = ?";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, isbn);
            int i = pstmt.executeUpdate();
            return i;
        }
        catch (SQLException e) {
            // unable to update, print error message and return 0
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static int del_user(String uid) {

        /** 
         * Return an integer as write status. 
         * 
         * 0 => nothing is updated, failed
         * 1 => one entry updated, success
         */

        // sql query prepare statement
        String cmd = "DELETE FROM users WHERE uid = ?";

        // update database
        try (PreparedStatement pstmt = sqlite.conn.prepareStatement(cmd)) {
            pstmt.setString(1, uid);
            int i = pstmt.executeUpdate();
            return i;
        }
        catch (SQLException e) {
            // unable to update, print error message and return 0
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public static String translate_status(String status){
        switch (status) {
            case "0":
                return "free";
            case "1":
                return "reserved";
            case "2":
                return "borrowed";
            default:
                return "unknown";
        } 
    }

    public static String due_date(){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime due = now.plusDays(7);
        return f.format(due);
    }
}
