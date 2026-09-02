package library;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = Library.getInstance();

        // For simplicity, hardcode a logged-in student (id=1 from schema.sql sample data)
        User currentUser = UserFactory.createUser("STUDENT", 1, "Nuer");
        currentUser.viewDashboard();

        boolean running = true;
        while (running) {
            System.out.println("\nEnter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter title keyword: ");
                    String keyword = sc.nextLine();
                    List<Book> results = library.searchByTitle(keyword);
                    if (results.isEmpty()) {
                        System.out.println("No books found.");
                    } else {
                        results.forEach(System.out::println);
                    }
                }
                case 2 -> {
                    System.out.print("Enter ISBN to issue: ");
                    String isbn = sc.nextLine();
                    library.issueBook(isbn, currentUser.getId());
                }
                case 3 -> {
                    System.out.print("Enter ISBN to return: ");
                    String isbn = sc.nextLine();
                    library.returnBook(isbn, currentUser.getId());
                }
                case 4 -> {
                    System.out.println("All books in catalog:");
                    library.getAllBooks().forEach(System.out::println);
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye!");
        sc.close();
    }
}
