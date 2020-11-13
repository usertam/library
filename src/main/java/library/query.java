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

public class query {

    public static List<String[]> list_books() {
        String cmd = "SELECT * FROM books";
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

    public static List<String[]> list_users() {
        String cmd = "SELECT uid, user, name FROM users";
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

    public static String[] get_passwd(int uid) {
        String cmd = "SELECT passwd_hash, passwd_salt FROM users WHERE uid = " + uid;
        try (Statement stmt = sqlite.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(cmd);
            String passwd[] = {
                rs.getString("passwd_hash"), 
                rs.getString("passwd_salt")
            };
            return passwd;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void print_books_basic() {

        // print message
        System.out.println("[*] Listing all library collections.");

        // get list of book details (in separate array)
        List<String[]> list = query.list_books();

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
