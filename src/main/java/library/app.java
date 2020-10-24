package library;

public class app {
    public static void main(String[] args) {
        // pass args as list to wrapper
        // if there is no args, start interactive cycles
        if (args.length != 0)
            interactive.wrapper(args);
        else
            interactive.start();
    }
}
