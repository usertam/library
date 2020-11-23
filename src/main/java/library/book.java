package library;

import java.util.List;

public class book {
    
    public static void search(String word) {

        // print message
        System.out.println("[*] Listing matching results.");

        // get list of matching books
        List<String[]> list = database.query_books(word);

        // print formatted output
        int i = 0;
        for (String[] b : list) {
            System.out.println("=".repeat(64));
            System.out.printf("%d.\n", ++i);
            System.out.printf("ISBN: %s\n", b[0]);
            System.out.printf("Title: %s\n", b[1]);
            System.out.printf("Author: %s\n", b[2]);
            System.out.printf("Publication date: %s\n", b[3]);
            System.out.printf("Status: %s\n", database.translate_status(b[4]));
            System.out.println("=".repeat(64));
        }
    }

    public static void reserve(String isbn) {
        Object status[] = { 1, auth.uid(), "" };
        int i = database.write_status(isbn, status);
        if (i > 0) {
            System.out.printf("[+] Success\n");
        } else {
            System.out.printf("[-] Failed\n");
        }
    }

    public static void query() {

        // print message
        System.out.println("[*] Listing all library collections.");

        // get list of book details (in separate array)
        List<String[]> list = database.query_books();

        // print formatted output
        System.out.println("=".repeat(64));
        System.out.println("ISBN\t\tTITLE");
        System.out.println("=".repeat(64));
        for (String[] b : list) System.out.printf("%s\t%s\n", b[0], b[1]);
        System.out.println("=".repeat(64));
    }

    public static void records(int uid) {

        /**
         * Access control
         * 1. Deny guest access
         * 2. Allow users to change own password only, except admin
         */

        if ( !auth.check_user(auth.uid(), 1) || ( !auth.check_user(auth.uid(), 0) && (uid != auth.uid()) ) ) { 
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        }

        // get user info
        String u[] = database.query_user(uid);

        // print message
        System.out.printf("[*] The records of user %s.\n", u[1]);

        // get list of book details (in separate array)
        List<String[]> list = database.query_records(uid);

        // print formatted output
        int i = 0;
        for (String[] b : list) {
            System.out.println("=".repeat(64));
            System.out.printf("[%d]\n", ++i);
            System.out.printf("ISBN: %s\n", b[0]);
            System.out.printf("Title: %s\n", b[1]);
            System.out.printf("Author: %s\n", b[2]);
            System.out.printf("Publication date: %s\n", b[3]);
            System.out.printf("Status: %s\n", database.translate_status(b[4]));
            System.out.printf("Due date: %s\n", b[6]);
            System.out.println("=".repeat(64));
        }
    }

    public static void records(String user) {
        int uid = database.query_uid(user);
        records(uid);
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
        int i = database.add_book(book);
        if (i > 0) {
            System.out.println("[+] New book entry added.");
        } else {
            System.out.println("[-] Failed to add a new book entry.");
        }
    }
    
    public static void new_user() {

        // get new user info
        String user[] = {
            sc.prompt("Enter the user id: "),
            sc.prompt("Enter the username: "),
            sc.prompt("Enter the full name: "),
        };

        // update database and print status
        int i = database.add_user(user);
        if (i > 0) {
            System.out.println("[+] New user entry added.");
        } else {
            System.out.println("[-] Failed to add a new user entry.");
        }
    }

    public static void del_book() {

        // get book isbn
        String isbn = sc.prompt("Enter the ISBN: ");

        // update database and print status
        int i = database.del_book(isbn);
        if (i > 0) {
            System.out.println("[+] Removed the book entry.");
        } else {
            System.out.println("[-] Failed to remove the book entry.");
        }
    }
    
    public static void del_user() {

        // get user id
        String uid = sc.prompt("Enter the user id: ");

        // update database and print status
        int i = database.del_user(uid);
        if (i > 0) {
            System.out.println("[+] Removed the user entry.");
        } else {
            System.out.println("[-] Failed to remove the user entry.");
        }
    }
}
