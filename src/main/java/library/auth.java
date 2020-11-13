package library;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class auth {

    public static int uid = -1;

    public static void whoami() {
        System.out.printf("%s (%d)\n", database.query_user(), auth.uid);
    }

    public static void login() {

        // prompt for username and password
        String user = sc.prompt("Enter username: ");
        String pw = sc.prompt("Enter password: ");

        // get uid from database
        int uid = database.query_uid(user);
        if (uid < 0) {
            System.out.printf("[-] Unable to find user %s.\n", user);
            return;
        }

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

    private static void setuid(int uid) {
        auth.uid = uid;
    }

    // TODO: Method for user to change password

}
