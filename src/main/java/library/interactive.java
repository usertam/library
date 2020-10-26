package library;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class interactive {
    public static void start() {
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
            System.out.printf("[*] Proceeds to exit.\n");
            System.exit(0);
        }
    }

    public static String[] parse(String cmdline) {

        // create objects from classes
        interactive i = new interactive();
        interactive.buf buf = i.new buf();
        interactive.del del = i.new del();

        char[] chars = cmdline.toCharArray();

        for (char c : chars) {
            if (c == del.current) {
                buf.reset();
                del.reset();
            }
            else if (c == del.quote) {
                buf.reset();
                del.quote();
            }
            else
                buf.append(c);
        }

        buf.reset();
        return buf.get();
    }

    public static void wrapper(String[] cmd) {
        if (cmd.length == 0)
            return;
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
        ArrayList<String> cmd = new ArrayList<>();
        StringBuffer buf = new StringBuffer();
        
        void append(char c) {
            buf.append(c);
        }
    
        void reset() {
            if (buf.length() > 0)
                cmd.add(buf.toString());
            buf.delete(0, buf.length());
        }
    
        String[] get() {
            return cmd.stream().toArray(String[]::new);
        }
    }
    
    // parse() delimiter class
    private class del {
        char space = ' ', quote = '"';
        char current = space;
        void quote() { current = quote; }
        void reset() { current = space; }
    }
}

class sc {

    // declare scanner
    static Scanner sc = new Scanner(System.in);

    // method to prompt user for input
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
