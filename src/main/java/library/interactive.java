package library;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class interactive {

    public static void start() {

        // interactive cycles
        try {
            while (true) {
                String cmdline = sc.prompt("> ");
                String[] cmd = parse(cmdline);
                wrapper(cmd);
            } 
        }
        catch (java.util.NoSuchElementException e) {
            System.out.printf("\n");
            System.out.printf("[-] The stardard input stream is empty.\n");
            System.out.printf("[-] Exiting.\n");
            app.exit(1);
        }
    }

    public static String[] parse(String line) {

        // create objects from classes
        interactive i = new interactive();
        interactive.buf buf = i.new buf();
        interactive.del del = i.new del();

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

    public static void wrapper(String[] cmd) {

        // return if cmd is empty
        if (cmd.length == 0) return;

        // call requested methods here
        switch (cmd[0]) {
            case "exit":
                app.exit(0);
                break;
            default:
                System.out.printf("[*] Unknown command: %s\n", Arrays.toString(cmd));
        }
    }

    // parse() buffer class 
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
    
    // parse() delimiter class
    private class del {

        // define delimiter characters
        char space = ' ', quote = '"';
        char current = space;

        // methods to change current delimiter
        void quote() { current = quote; }
        void reset() { current = space; }
    }
}

class sc {

    // declare scanner
    static Scanner sc = new Scanner(System.in);

    // method to prompt for input
    static String prompt(String s){
        System.out.printf(s);
        String input = sc.nextLine();
        return input;
    }

    // method to close the scanner
    static void kill(){
        sc.close();
    }
}
