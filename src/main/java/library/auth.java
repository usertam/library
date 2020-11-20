package library;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class auth {

    public static int uid = -1;

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
        if (!check_user(uid, 1)) {
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

        // set user as nobody
        setuid(-1);

        // print message
        System.out.printf("[+] Logged out.\n");
    }

    public static void passwd(int uid) {

        /**
         * Security policies for passwd() method
         * 1. Deny guest access
         * 2. Allow users to change own password only, except admin
         */

        if ( !check_user(auth.uid, 1) || ( !check_user(auth.uid, 0) && (uid != auth.uid) ) ) { 
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        }

        if ( !check_user(uid, 1) ) {
            System.out.printf("[-] Unknown user.\n");
            return;
        }

        // format prompt message
        String s;
        if (uid == auth.uid) {
            s = "Enter new password: ";
        } else {
            String user[] = database.query_user(uid);
            s = String.format("Enter new password for %s (%s): ", user[1], user[0]);
        }

        // prompt for password
        String pw = sc.prompt_pw(s);
        if (pw == null) {
            System.out.printf("[*] Aborted.\n");
            return;
        }

        // generate salt and write password in hashed form
        String salt = get_salt();
        String p[] = { sha256Hex(pw + salt), salt };
        database.write_passwd(uid, p);
    }

    public static void passwd(String user) {
        int uid = database.query_uid(user);
        passwd(uid);
    }

    public static boolean check_user(int uid, int mode) {

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

    private static String get_salt() {
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
}
