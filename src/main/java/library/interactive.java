package library;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class interactive {
    public static void start() {
        while (true) {
            String cmdline = prompt();
            String[] cmd = parse(cmdline);
            wrapper(cmd);
        }
    }

    public static String prompt() {
        System.out.print("> ");
        String cmdline = sc.sc.nextLine();
        return cmdline;
    }

    public static String[] parse(String cmdline) {
        parse_buffer buffer = new parse_buffer();
        parse_delimiter delimiter = new parse_delimiter();
        char[] chars = cmdline.toCharArray();

        for (char ch:chars) {
            if (ch == delimiter.current) {
                buffer.reset();
                delimiter.reset();
            }
            else if (ch == delimiter.quote) {
                buffer.reset();
                delimiter.quote();
            }
            else
                buffer.append(ch);
        }

        buffer.reset();
        return buffer.get();
    }

    public static void wrapper(String[] cmd) {
        if (cmd.length == 0)
            return;
        switch (cmd[0]) {
            case "exit":
                sc.sc.close();
                System.exit(0);
                break;
            default:
                System.out.printf("[*] COMMAND: %s\n", Arrays.toString(cmd));
        }
    }
}

class parse_buffer {
    ArrayList<String> cmd = new ArrayList<>();
    StringBuffer buf = new StringBuffer();
    
    void append(char ch) {
        buf.append(ch);
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

class parse_delimiter {
    char space = ' ', quote = '"';
    char current = space;

    void quote() {
        current = quote;
    }

    void reset() {
        current = space;
    }
}

class sc {
    static Scanner sc = new Scanner(System.in);
}
