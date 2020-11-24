package library;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class book {

    public static void list() {
        System.out.println("[*] Listing all library collections.");
        ls_simple(database.query_books());
    }

    public static void search(String word) {
        System.out.println("[*] Listing matching results.");
        ls_detail(database.query_books(word));
    }

    public static void records(int uid) {

        /**
         * Access control
         * 1. Deny guest access
         * 2. Allow users to change own records only, except admin
         */

        if ( !auth.check_user(auth.uid(), 1) || ( !auth.check_user(auth.uid(), 0) && (uid != auth.uid()) ) ) { 
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        }

        // print message
        String u[] = database.query_user(uid);
        List<String[]> list = database.query_records(uid);

        if (list.size() > 0) {
            System.out.printf("[*] The records of user %s.\n", u[1]);
            ls_detail(list);
        } else {
            System.out.printf("[*] No records of user %s found.\n", u[1]);
        }
    }

    public static void records(String user) {
        int uid = database.query_uid(user);
        records(uid);
    }

    public static void reserve(String isbn) {

        // access control: deny guests
        if (!auth.check_user(auth.uid(), 1)) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        }

        // craft status payload
        Object status[] = {
            1, auth.uid(), null
        };

        // execute and print status message
        if (database.write_status(isbn, status) > 0) {
            System.out.println("[+] Book reserved.");
        } else {
            System.out.println("[-] Failed to reserve the book.");
        }
    }

    public static void borrow(String isbn) {

        // access control: deny guests
        if (!auth.check_user(auth.uid(), 1)) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        }

        // craft status payload
        Object status[] = {
            2, auth.uid(), get_expiry()
        };

        // execute and print status message
        if (database.write_status(isbn, status) > 0) {
            System.out.println("[+] Book borrowed.");
        } else {
            System.out.println("[-] Failed to borrow the book.");
        }
    }

    public static void new_book() {

        // get new book info
        String book[] = {
            sc.prompt("Enter the ISBN: "),
            sc.prompt("Enter the title: "),
            sc.prompt("Enter the author: "),
            sc.prompt("Enter the publication date (YYYY-MM-DD): ")
        };

        // update database and print status
        if (database.add_book(book) > 0) {
            System.out.println("[+] New book entry added.");
        } else {
            System.out.println("[-] Failed to add a new book entry.");
        }
    }

    public static void del_book() {

        // get book isbn
        String isbn = sc.prompt("Enter the ISBN: ");

        // update database and print status
        if (database.del_book(isbn) > 0) {
            System.out.println("[+] Removed the book entry.");
        } else {
            System.out.println("[-] Failed to remove the book entry.");
        }
    }

    private static void ls_simple(List<String[]> list) {

        // variables
        int i = 0;

        // print divider
        print_hr(80);

        // for every book in the list, print info
        for (String b[] : list) {
            System.out.printf("%d. \t%s \t%s \n", ++i, b[0], b[1]);
        }

        // print divider
        print_hr(80);
    }

    private static void ls_detail(List<String[]> list) {

        // variables
        int i = 0;

        // print divider
        print_hr(60);

        // for every book in the list, print info
        for (String b[] : list) {

            // print item number
            System.out.printf("%d. \n", ++i);

            // print general details
            System.out.println("ISBN: " + b[0]);
            System.out.println("Title: " + b[1]);
            System.out.println("Author: " + b[2]);
            System.out.println("Publication date: " + b[3]);

            // get book status (text)
            String status = status_text(b[4]);

            // print book status
            if (auth.check_user(auth.uid(), 0) && !b[4].equals("0")) {
                // user is admin and book is not free, print verbose user info
                int uid = Integer.parseInt(b[5]);
                String[] u = database.query_user(uid);
                System.out.printf("Status: %s by %s\n", status, u[1]);
            } else {
                // user is not admin or book is free, print just status info
                System.out.println("Status: " + status);
            }

            // only print due date if it exists
            if (b[6] != null) {
                System.out.println("Due date: " + b[6]);
            }

            // print divider
            print_hr(60);
        }
    }

    private static void print_hr(int i){
        System.out.println("=".repeat(i));
    }

    private static String status_text(String status){
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

    private static String get_expiry(){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime due = now.plusDays(7);
        return f.format(due);
    }
}
