package library;

public class Librarian extends User {

    public Librarian(int id, String name) {
        super(id, name);
    }

    @Override
    public void viewDashboard() {
        System.out.println("Librarian Dashboard - Welcome, " + name);
        System.out.println("1. Add Book  2. Remove Book  3. View All Transactions");
    }
}
