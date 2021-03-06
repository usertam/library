package library;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Base64.Encoder;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class auth {

    private static int uid = -1;
    private static int suid = -1;

    public static void whoami() {
        String user[] = database.query_user(auth.uid);
        System.out.printf("[*] %s, user %s (%s)\n", user[2], user[1], user[0]);
    }

    public static void login(String user) {

        // prompt for password
        String pw = sc.prompt_pw("Enter password: ");

        // get uid from database
        int uid = database.query_uid(user);

        // check if the supplied user id exists
        if (!check(uid, 1)) {
            System.out.printf("[-] Unknown user.\n");
            return;
        }

        // get hash and salt from database
        String p[] = database.query_passwd(uid);

        // check the password
        if (p[0].equals(sha256Hex(pw + p[1]))) {
            setuid(uid);
            String u[] = database.query_user(uid);
            System.out.printf("[+] Welcome back, %s!\n", u[2]);
        } else {
            System.out.printf("[-] Password does not match.\n");
        }
    }

    public static void login() {

        // prompt for user
        String user = sc.prompt("Enter username: ");

        // continue login
        login(user);
    }

    public static void logout() {
        if (auth.suid >= 0) {
            setuid(auth.suid);
            setsuid(-1);
            System.out.printf("[+] Returned to the original user.\n");
        } else {
            setuid(-1);
            System.out.printf("[+] Logged out.\n");
        }
    }

    public static void su(String user) {

        /** Method to switch user without entering password, for admin. */

        // access control: allow admin only
        if (!auth.check(auth.uid(), 0)) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        }

        // set uid and suid
        int uid = database.query_uid(user);
        setsuid(auth.uid);
        setuid(uid);

        // print message
        String u[] = database.query_user(uid);
        System.out.printf("[+] Switched user to %s (%s)\n", u[1], u[0]);
    }

    public static void passwd(int uid) {

        /**
         * Access control (check auth.uid)
         * 1. Deny guest access
         * 2. Allow users to change own password only, except admin
         */

        if ( !check(auth.uid, 1) || ( !check(auth.uid, 0) && (uid != auth.uid) ) ) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        }

        // check the supplied uid (check uid)
        if (!check(uid, 1)) {
            System.out.printf("[-] Unknown user.\n");
            return;
        }

        // format prompt message
        String prompt;
        if (uid == auth.uid) {
            prompt = "Enter new password: ";
        } else {
            String u[] = database.query_user(uid);
            prompt = String.format("Enter new password for %s (%s): ", u[1], u[0]);
        }

        // prompt for password
        String pw = sc.prompt_pw(prompt);
        if (pw == null) {
            System.out.printf("[*] Aborted.\n");
            return;
        }

        // generate salt and write password in hashed form
        String salt = salt();
        String p[] = { sha256Hex(pw + salt), salt };
        int code = database.write_passwd(uid, p);

        // print status message
        if (code > 0) {
            System.out.printf("[+] Password changed.\n");
        } else {
            System.out.printf("[-] Failed to change password.\n");
        }
    }

    public static void passwd(String user) {
        int uid = database.query_uid(user);
        passwd(uid);
    }

    public static void insert() {
        
        // access control: allow admin only
        if (!auth.check(auth.uid(), 0)) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return; 
        }
            
        // get new user info
        String user[] = {
            sc.prompt("Enter the user id: "),
            sc.prompt("Enter the username: "),
            sc.prompt("Enter the full name: "),
        };

        // update database and print status
        int i = database.add_user(user);
        if (i > 0) {
            System.out.println("[+] New user entry added.");
        } else {
            System.out.println("[-] Failed to add a new user entry.");
        }
    }

    public static void delete() {
        
        // access control: allow admin only
        if (!auth.check(auth.uid(), 0)) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return; 
        }

        // get user id
        String uid = sc.prompt("Enter the user id: ");

        // update database and print status
        int i = database.del_user(uid);
        if (i > 0) {
            System.out.println("[+] Removed the user entry.");
        } else {
            System.out.println("[-] Failed to remove the user entry.");
        }
    }

    public static void list(){

        // access control: allow admin only
        if (!auth.check(auth.uid(), 0)) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return; 
        }

        // get users from database
        List<String[]> users = database.query_users();

        // print divider
        System.out.println("=".repeat(40));

        // print user details
        System.out.printf("%s\t%s\t%s\n", "UID", "USER", "NAME");
        for (String u[] : users) {
            System.out.printf("%s\t%s\t%s\n", u[0], u[1], u[2]);
        }

        // print divider
        System.out.println("=".repeat(40));
    }

    public static int uid() {
        return auth.uid;
    }

    public static boolean check(int uid, int mode) {

        /**
         * User checking method
         * Admin only: mode = 0
         * Users only: mode = 1
         * 
         * if (!check_user(auth.uid, mode)) {
         *     System.out.printf("[-] You don't have permission to perform this action.\n");
         *     return;
         * }
         */

        if ((mode == 0) && (uid == 0)) {   
            return true;
        } else if ((mode == 1) && (uid >= 0)) {
            return true;
        }

        return false;
    }

    private static String salt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        Encoder ec = Base64.getEncoder().withoutPadding();
        String salt = ec.encodeToString(bytes);
        return salt;
    }

    private static void setuid(int uid) {
        auth.uid = uid;
    }

    private static void setsuid(int uid) {
        auth.suid = uid;
    }
}
