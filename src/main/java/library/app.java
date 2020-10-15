package library;

import java.util.*;

public class app {
    public static void main(String[] args) {
        // create interactive object
        interactive interactive = new interactive();
        
        // pass args as list to wrapper
        // if there is no args, call interactive shell
        if (args.length!=0)
            interactive.wrapper(Arrays.asList(args));
        else
            interactive.start();
    }
}
