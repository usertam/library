package library;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class auth {

    public static int uid = -1;

    public static void whoami() {
        System.out.printf("%s (%d)\n", database.query_user(), auth.uid);
    }

    public static void login() {

        // prompt for username
        String user = sc.prompt("Enter username: ");

        // prompt for password, do not echo password back to console
        System.out.printf("Enter password: ");
        String pw = new String(System.console().readPassword());

        // get uid from database
        int uid = database.query_uid(user);

        // check if the supplied user id exists
        if (!check_priv(uid, 1)) return;

        // get hash and salt from database
        String p[] = database.query_passwd(uid);

        // check the password
        if (p[0].equals(sha256Hex(pw + p[1]))) {
            setuid(uid);
            System.out.printf("[+] Welcome back, user %s!\n", database.query_user(uid));
        } else {
            System.out.printf("[-] Password does not match.\n");
        }
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
         * 1. Deny guests
         * 2. Deny users modifying others password, except admin
         * 3. Abort if invaild user id is supplied
         */
        
        if (!check_priv(auth.uid, 1)) {
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        } else if (!(uid == auth.uid) && !check_priv(auth.uid, 0)) { 
            System.out.printf("[-] You don't have permission to perform this action.\n");
            return;
        } else if (!check_uid(auth.uid)) {
            System.out.printf("[-] Invaild user.\n");
            return;
        }

        // print message
        if (uid == auth.uid) {
            System.out.printf("Enter new password: ");
        } else {
            System.out.printf("Enter new password for %s: ", database.query_user(uid));
        }

        // get password and salt
        String pw = new String(System.console().readPassword());
        String salt = get_salt();

        // write password
        String p[] = { sha256Hex(pw + salt), salt };
        database.write_passwd(uid, p);
    }

    public static void passwd(String user) {
        int uid = database.query_uid(user);
        passwd(uid);
    }

    public static boolean check_priv(int uid, int mode) {

        // admin: bypass all security checks
        if (uid == 0) {   
            return true;
        }

        // mode 1: allow all users, deny guests
        if ((mode == 1) && (uid >= 0)) {
            return true;
        }

        return false;
    }

    public static boolean check_uid(int uid) {
        if (uid < 0) return false;
        else return true;
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
