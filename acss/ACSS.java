package acss;

public class ACSS {

    public static User currentUser = null;
    public static Page1 firstPage;

    public static void main(String[] args) {
        System.out.println("ACSS System Started");
        DataIO.readFromFile();  // Load all data from text files
        firstPage = new Page1(); // Open login/register page
    }
}
