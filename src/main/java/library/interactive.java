package library;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class interactive {
    public void start() {
        while (true) {
            print_prompt();
            String cmdline = sc.sc.nextLine();
            List<String> cmd = parse(cmdline);
            wrapper(cmd);
        }
    }

    public void print_prompt() {
        System.out.print("> ");
    }

    public List<String> parse(String cmdline) {
        parse_buffer buffer = new parse_buffer();
        parse_delimiter delimiter = new parse_delimiter();
        char[] chars = cmdline.toCharArray();

        for (char ch:chars) {
            if (ch==delimiter.current) {
                buffer.reset();
                delimiter.reset();
            }
            else if (ch==delimiter.quote)
                delimiter.quote();
            else
                buffer.append(ch);
        }

        buffer.reset();
        return buffer.cmd;
    }

    public void wrapper(List<String> cmd) {
        if (cmd.isEmpty()) return;
        switch (cmd.get(0)) {
            case "exit":
                sc.sc.close();
                System.exit(0);
                break;
            default:
                System.out.printf("[*] COMMAND: %s\n", cmd);
        }
    }
}

class parse_buffer {
    ArrayList<String> cmd = new ArrayList<String>();
    StringBuffer buf = new StringBuffer();
    
    void append(char ch) {
        buf.append(ch);
    }

    void reset() {
        if (buf.length() > 0) cmd.add(buf.toString());
        buf.delete(0, buf.length());
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