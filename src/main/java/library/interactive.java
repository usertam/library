package library;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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
        switch (cmd[0]) {
            // test methods may be changed later
            case "su":
                if (cmd.length > 1) auth.su(cmd[1]);
                else System.out.printf("[-] No user supplied.\n");
                break;
            case "reserve":
                if (cmd.length > 1) database.reserve_example(cmd[1]);
                else System.out.printf("[-] No ISBN supplied.\n");
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
            case "ls":
                database.query_books_example();
                break;
            case "exit":
                app.exit(0);
                break;
            default:
                System.out.printf("[*] Unknown command: %s\n", Arrays.toString(cmd));
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
