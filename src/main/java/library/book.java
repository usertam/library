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
            System.out.printf("Status: %s\n", b[4]);
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
}
