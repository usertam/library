package library;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class interactive {

    public static void start() {

        // interactive cycles
        while (true) {
            String line = sc.prompt("> ");
            String[] cmd = split(line);
            eval(cmd);
        }
    }

    public static String[] split(String line) {

        // use regex to split the line
        List<String> cmd = new ArrayList<>();
        Pattern p = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher m = p.matcher(line);
        while (m.find()) cmd.add(m.group().replaceAll("[\"']", ""));
        return cmd.toArray(new String[0]);
    }

    public static void eval(String[] cmd) {

        // return if cmd is empty
        if (cmd.length == 0) return;

        // call requested methods here
        switch (cmd[0].toLowerCase()) {
            // test methods may be changed later
            case "listusers":
                auth.list();
                break;
            case "delbook":
                book.delete();
                break;
            case "deluser":
                auth.delete();
                break;
            case "addbook":
                book.insert();
                break;
            case "adduser":
                auth.insert();
                break;
            case "records":
                if (cmd.length > 1) book.records(cmd[1]);
                else book.records(auth.uid());
                break;
            case "search":
                if (cmd.length > 1) book.search(cmd[1]);
                else System.out.printf("[-] No keyword supplied.\n");
                break;
            case "borrow":
                if (cmd.length > 1) book.borrow(cmd[1]);
                else System.out.printf("[-] No ISBN supplied.\n");
                break;
            case "reserve":
                if (cmd.length > 1) book.reserve(cmd[1]);
                else System.out.printf("[-] No ISBN supplied.\n");
                break;
            case "help":
                interactive.help();
                break;       
            case "listbooks":
                book.index();
                break;
            case "whoami":
                auth.whoami();
                break;
            case "su":
                if (cmd.length > 1) auth.su(cmd[1]);
                else System.out.println("[-] No user supplied.");
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
                System.out.println("[*] Time of exit: " + interactive.time());
                app.exit(0);
                break;
            default:
                System.out.println("[*] Unknown command: " + Arrays.toString(cmd));
        }
    }

    public static void greet() {

        // print message
        System.out.println("=".repeat(64));
        System.out.println("A heartful welcome to our library management system!");
        System.out.println("Please feel free to enter 'help' to see available commands.");
        System.out.println("=".repeat(64));
    }
    
    public static String time() {
        
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return f.format(now);
    }
    
    public static void help() {
        
        System.out.println("[*] Listing available commands.");
        System.out.println("=".repeat(80));

        // commands available for guests
        System.out.println("search\t<keywords>\t Search for a book using keywords.");
        System.out.println("\t\t\t   Keywords with spaces can be placed in a quote.");
        System.out.println("\t\t\t   e.g. search \"key words\" ");
        System.out.println("listbooks\t\t Display all library collections.");
        System.out.println("=".repeat(80));
        System.out.println("whoami\t\t\t Display the current user.");
        System.out.println("login\t<user>\t\t Login as an user, providing password.");
        System.out.println("exit\t\t\t Terminate the program.");
        System.out.println("=".repeat(80));

        // commands available for users
        if (auth.check(auth.uid(), 1)) {
            System.out.println("reserve\t<ISBN>\t\t Attempt to reserve a book.");
            System.out.println("borrow\t<ISBN>\t\t Attempt to borrow a book.");
            System.out.println("records\t\t\t Show your personal records.");
            System.out.println("passwd\t\t\t Change your user password.");
            System.out.println("logout\t\t\t Terminate your user session.");
            System.out.println("=".repeat(80));
        }

        // commands available for admin
        if (auth.check(auth.uid(), 0)) {
            System.out.println("su\t<user>\t\t Impersonate a specific user, without password.");
            System.out.println("\t\t\t   To return to the original user, use \"logout\". ");
            System.out.println("records\t<user>\t\t Show records of a specific user.");
            System.out.println("listusers\t\t Display all user entires.");
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
            System.out.println();
            System.out.println("[*] EOF detected, exiting.");
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
            System.out.println();
            System.out.println("[!] Unable to access the console.");
            System.out.println("  -  This might be caused by running the program from an IDE.");
            System.out.println("  -  Run me in an interactive command line to fix this.");
            System.out.println();
            System.out.println("[*] Fall back to default prompting method.");
            System.out.println("[!] The password will be displayed in plain text.");
            return prompt(s);
        }
    }

    // method to close the scanner
    static void kill() {
        sc.close();
    }
}
