package library;

public class app {
    public static void main(String[] args) {
        // create interactive object
        interactive interactive = new interactive();
        
        // pass args as list to wrapper
        // if there is no args, call interactive shell
        if (args.length!=0)
            interactive.wrapper(args);
        else
            interactive.start();
    }
}
