package library;

public class UserFactory {

    public static User createUser(String role, int id, String name) {
        if (role.equalsIgnoreCase("STUDENT")) {
            return new Student(id, name);
        } else if (role.equalsIgnoreCase("LIBRARIAN")) {
            return new Librarian(id, name);
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
