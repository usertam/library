package library;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class interactive {

    public static void start() {

        // interactive cycles
        while (true) {
            String line = sc.prompt("> ");
            String[] cmd = parser.spilt(line);
            eval(cmd);
        }
    }

    public static void eval(String[] cmd) {

        // return if cmd is empty
        if (cmd.length == 0) return;

        // call requested methods here
        switch (cmd[0]) {
            // test methods may be changed later
            case "addbook":
                book.new_book();
                break;
            case "adduser":
                book.new_user();
                break;
            case "records":
                if (cmd.length > 1) book.records(cmd[1]);
                else book.records(auth.uid());
                break;
            case "search":
                if (cmd.length > 1) book.search(cmd[1]);
                else System.out.printf("[-] No keyword supplied.\n");
                break;
            case "reserve":
                if (cmd.length > 1) book.reserve(cmd[1]);
                else System.out.printf("[-] No ISBN supplied.\n");
                break;
            case "help":
                interactive.help();
                break;       
            case "ls":
                book.query();
                break;
            case "whoami":
                auth.whoami();
                break;
            case "login":
                if (cmd.length > 1) auth.login(cmd[1]);
                else auth.login();
                break;
            case "logout":
                auth.logout();
                break;
            case "passwd":
                if (cmd.length > 1) auth.passwd(cmd[1]);
                else auth.passwd(auth.uid());
                break;
            case "exit":
                app.exit(0);
                break;
            default:
                System.out.printf("[*] Unknown command: %s\n", Arrays.toString(cmd));
        }
    }

    private static class parser {

        private static String[] spilt(String line) {
    
            // create objects from classes
            parser p = new parser();
            parser.buf buf = p.new buf();
            parser.del del = p.new del();
    
            // command parsing logic
            char a[] = line.toCharArray();
            for (char c : a) {
                if (c == del.current) {
                    buf.reset();
                    del.reset();
                }
                else if (c == del.quote) {
                    buf.reset();
                    del.quote();
                }
                else {
                    buf.append(c);
                }
            }
    
            // return a string array
            return buf.get();
        }
    
        private class buf {
    
            // define array list and string buffer
            ArrayList<String> cmd = new ArrayList<>();
            StringBuffer buf = new StringBuffer();
    
            // method to append char to buffer
            void append(char c) {
                buf.append(c);
            }
    
            // method to append the string to arraylist, then clear the buffer
            void reset() {
                if (buf.length() > 0) cmd.add(buf.toString());
                buf.delete(0, buf.length());
            }
    
            // method to return a string array
            String[] get() {
                reset();
                return cmd.stream().toArray(String[]::new);
            }
        }
    
        private class del {
    
            // define delimiter characters
            char space = ' ', quote = '"';
            char current = space;
    
            // methods to change current delimiter
            void quote() { current = quote; }
            void reset() { current = space; }
        }
    }
    
    public static void help() {
        
        //Commands available for guests
        System.out.println("[*} Listing all help commands.");
        System.out.println("Search <keywords> Searches for a book using keywords.");
        System.out.println("Ls\t\t Displays all library collections.");
        System.out.println("Info <ISBN>\t Displays detailed book information.");
        System.out.println("Login");
        System.out.println("Logout");

         //Commands available for user
         if (auth.check_user(auth.uid(),1)) {
         System.out.println("Reserve <ISBN>\t Attempts to reserve a book.");
         System.out.println("Records <user>\t Shows user records.");
        }

        //Commands available for admin
        if (auth.check_user(auth.uid(),0)) {
         System.out.println("Records <UID>\t Shows a specific user's records.");    
         System.out.println("Student <user>\t Reserves books for borrowing.");
         System.out.println("Admin <user>\t Changes user’s password.");
      // System.out.println("Admin <user?>\t Changes user's username.");
      // System.out.println("Add <user>\t Adds user.");
      // System.out.println("Delete <user>\t Deletes user.");
        }     
    }
}

class sc {

    // declare scanner
    static Scanner sc = new Scanner(System.in);

    // method to prompt for input
    static String prompt(String s) {
        try {
            System.out.printf(s);
            String input = sc.nextLine();
            return input;
        }
        catch (java.util.NoSuchElementException e) {
            System.out.printf("\n");
            System.out.printf("[*] EOF detected, exiting.\n");
            app.exit(0);
            return null;
        }
    }

    // method to prompt for password (hide input)
    static String prompt_pw(String s) {
        if (System.console() != null) {
            System.out.printf(s);
            try {
                String pw = new String(System.console().readPassword());
                return pw;
            } catch (java.lang.NullPointerException e) {
                return null;
            }
        } else {
            System.out.printf("\n");
            System.out.printf("[!] Unable to access the console.\n");
            System.out.printf("  -  This might be caused by running the program from an IDE.\n");
            System.out.printf("  -  Run me in an interactive command line to fix this.\n");
            System.out.printf("[*] Fall back to default prompting method.\n");
            System.out.printf("[!] The password will be displayed in plain text.\n");
            return prompt(s);
        }
    }

    // method to close the scanner
    static void kill() {
        sc.close();
    }
}
