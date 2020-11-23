package library;

public class app {

    public static void main(String[] args) {

        // connect to database
        sqlite.connect();

        // process arguments, or start interactive cycles
        if (args.length != 0) {
            interactive.eval(args);
            app.exit(0);
        } else {
            interactive.greet();
            interactive.start();
        }
    }

    public static void exit(int code) {

        // stop running resources
        sc.kill();
        sqlite.kill();

        // terminate the program
        System.exit(code);
    }
}
