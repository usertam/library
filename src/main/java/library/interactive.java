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
            case "userlist":
                interactive.userlist();
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
    
    public static void userlist() {
        
        System.out.println("[*] Listing all users.");
   
        if (auth.check_user(auth.uid(),0)) {
        List<String[]> list = database.query_users();
    
        for (String[] user : list)
        System.out.println(user[1]); 
   
        } else {
        System.out.println("[-] You don't have permission to perform this action.\n"); 
     } 
  }
    
    public static void help() {
        
        System.out.println("[*] Listing available commands.");
        System.out.println("=".repeat(80));

        // commands available for guests
        System.out.println("search\t<keywords>\t Search for a book using keywords.");
        System.out.println("\t\t\t   Keywords with spaces can be placed in a quote.");
        System.out.println("\t\t\t   e.g. search \"key words\" ");
        System.out.println("ls\t\t\t Display all library collections.");
        System.out.println("=".repeat(80));
        System.out.println("whoami\t\t\t Display the current user.");
        System.out.println("login\t<user>\t\t Login as an user, providing password.");
        System.out.println("exit\t\t\t Terminate the program.");
        System.out.println("=".repeat(80));

        // commands available for users
        if (auth.check_user(auth.uid(), 1)) {
            System.out.println("reserve\t<ISBN>\t\t Attempt to reserve a book.");
            System.out.println("borrow\t<ISBN>\t\t Attempt to borrow a book.");
            System.out.println("records\t\t\t Show your personal records.");
            System.out.println("passwd\t\t\t Change your user password.");
            System.out.println("logout\t\t\t Terminate your user session.");
            System.out.println("=".repeat(80));
        }

        // commands available for admin
        if (auth.check_user(auth.uid(), 0)) {
            System.out.println("su\t<user>\t\t Impersonate a specific user, without password.");
            System.out.println("\t\t\t   To return to the original user, use \"logout\". ");
            System.out.println("userlist\t\t\t\t\t Show list of users.");
            System.out.println("records\t<user>\t\t Show records of a specific user.");
            System.out.println("passwd\t<user>\t\t Change password of a specific user.");
            System.out.println("=".repeat(80));
            System.out.println("adduser\t<user>\t\t Add an user entry.");
            System.out.println("deluser\t<user>\t\t Delete an user entry.");
            System.out.println("addbook\t<ISBN>\t\t Add a book entry.");
            System.out.println("delbook\t<ISBN>\t\t Delete a book entry.");
            System.out.println("=".repeat(80));
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
