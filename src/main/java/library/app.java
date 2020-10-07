package library;

import java.util.*;

public class app {
    public static void main(String[] args) {
        // convert args to string list
        List<String> cmd = Arrays.asList(args);
        
        // pass args list to wrapper
        // if there is no args, call interactive shell
        if (!cmd.isEmpty()) wrapper_cmd(cmd);
        else interactive();
    }

    static void interactive() {
        Scanner sc = new Scanner(System.in);
        print_prompt();
        while (true) {
            if (!sc.hasNextLine()) continue;
            String cmdline = sc.nextLine();
            List<String> cmd = parse_cmd(cmdline);
            wrapper_cmd(cmd);
            print_prompt();
        }
    }

    static void print_prompt() {
        System.out.print("> ");
    }

    static List<String> parse_cmd(String cmdline) {
        List<String> cmd = new ArrayList<String>();
        StringBuffer buf = new StringBuffer();
        char[] chars = cmdline.toCharArray();
        char space = ' ', quote = '"';
        char delimiter = space;

        for (char ch:chars) {
            if (ch==delimiter) {
                if (buf.length() > 0) cmd.add(buf.toString());
                buf.delete(0, buf.length());
                delimiter = space;
            }
            else if (ch==quote) delimiter = quote;
            else buf.append(ch);
        }

        if (buf.length() > 0) cmd.add(buf.toString());
        buf.delete(0, buf.length());
        return cmd;
    }

    static void wrapper_cmd(List<String> cmd) {
        if (cmd.isEmpty()) return;
        switch (cmd.get(0)) {
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.printf("[*] COMMAND: %s\n", cmd);
        }
    }
}
