package library;

public class app {
    public static void main(String[] args) {

        // process arguments, or start interactive cycles
        if (args.length != 0) {
            interactive.eval(args);
        } else {
            interactive.start();
        }
    }

    public static void exit(int code) {

        // stop running resources
        sc.kill();

        // terminate the program
        System.exit(code);
    }
}
