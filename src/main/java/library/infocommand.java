package library;
import java.util.Scanner;

public class infocommand {
public static void main(String[] args) {
        
        System.out.println("Enter the book name:");
        Scanner sc = new Scanner(System.in);
        String input = sc.next(); 
        
        switch (input) {
        case "Computer Organization and Design, Fifth Edition":
            /*A. List<String[]> list = query.list_books();
             * for (String[] b : list) {
             *     System.out.println(b); ?
             */ 
            //B. Book1(); 
            //C.
            System.out.println(
            "Name: Computer Organization and Design, Fifth Edition"
          + "Author: David Patterson, John L. Hennessy"
          + "ISBN: 9780124077263"
          + "Genre: Computer & Techonolgy"
          + "Availability: Borrowed"
          + "Publisher: Morgan Kaufmann Publishers Inc."
          + "Year of Publication: 2013");
            break;
            
        case "Computer Organization and Embedded Sytems":
            System.out.println("........");
            break;
            
        case "C Prgramming Language, 2nd Edition":
            System.out.println("........");
            break;
            
        case "Network Fundamentals, CCNA Exploration Companion Guide":
            System.out.println("........");
            break;
        
        case "The C++ Programming Language, 4th Edition":
            System.out.println("........");
            break;
            
        default:
            System.out.println("Sorry! Seems like we don't have that in our library."
                     + "Press [1] to try again."
                     + "Press [0] to take a look at our book list.");
        }
    }
    
     /*public static void Book1() {
        System.out.println(
            "Name: Computer Organization and Design, Fifth Edition"
          + "Author: David Patterson, John L. Hennessy"
          + "ISBN: 9780124077263"
          + "Genre: Computer & Techonolgy"
          + "Availability: Borrowed"
             want to add a see more 
             print: wanna see more? press x yes xx no go menu
             if (equals yes)
             print: blabalb
             else (go back to menu)
          + "Publisher: Morgan Kaufmann Publishers Inc."
          + "Year of Publication: 2013"
          + "Pages: 800"
          + "Rating: 3/5"
          + "Hardcover"); //hardcover or softcover 
       }*/
          
     
    
}
