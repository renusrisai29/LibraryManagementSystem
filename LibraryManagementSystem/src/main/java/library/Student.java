package library;

public class Student extends User {

    public Student(int id, String name) {
        super(id, name);
    }

    @Override
    public void viewDashboard() {
        System.out.println("Student Dashboard - Welcome, " + name);
        System.out.println("1. Search Book  2. Issue Book  3. Return Book  4. View My Fines");
    }
}
