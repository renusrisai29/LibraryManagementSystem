package library;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton pattern: only one Library instance manages the catalog.
 */
public class Library {

    private static Library instance;
    private FineStrategy fineStrategy;

    private Library() {
        this.fineStrategy = new FlatRateFineStrategy(); // swap strategies here if needed
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    // ---------- Search ----------
    public List<Book> searchByTitle(String keyword) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }
        return results;
    }

    public List<Book> getAllBooks() {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            System.out.println("Fetch failed: " + e.getMessage());
        }
        return results;
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("isbn"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );
    }

    // ---------- Issue ----------
    public boolean issueBook(String isbn, int userId) {
        String checkSql = "SELECT available_copies FROM books WHERE isbn = ?";
        String updateBookSql = "UPDATE books SET available_copies = available_copies - 1 WHERE isbn = ?";
        String insertTxnSql = "INSERT INTO transactions (book_isbn, user_id, issue_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, isbn);
                ResultSet rs = checkPs.executeQuery();
                if (!rs.next() || rs.getInt("available_copies") <= 0) {
                    System.out.println("No copies available for ISBN: " + isbn);
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement updatePs = conn.prepareStatement(updateBookSql)) {
                updatePs.setString(1, isbn);
                updatePs.executeUpdate();
            }

            LocalDate issueDate = LocalDate.now();
            LocalDate dueDate = issueDate.plusDays(14); // 2-week loan period

            try (PreparedStatement insertPs = conn.prepareStatement(insertTxnSql)) {
                insertPs.setString(1, isbn);
                insertPs.setInt(2, userId);
                insertPs.setDate(3, Date.valueOf(issueDate));
                insertPs.setDate(4, Date.valueOf(dueDate));
                insertPs.executeUpdate();
            }

            conn.commit();
            System.out.println("Book issued successfully. Due date: " + dueDate);
            return true;

        } catch (SQLException e) {
            System.out.println("Issue failed: " + e.getMessage());
            return false;
        }
    }

    // ---------- Return ----------
    public boolean returnBook(String isbn, int userId) {
        String findTxnSql = "SELECT id, due_date FROM transactions " +
                "WHERE book_isbn = ? AND user_id = ? AND return_date IS NULL " +
                "ORDER BY issue_date DESC LIMIT 1";
        String updateTxnSql = "UPDATE transactions SET return_date = ?, fine = ? WHERE id = ?";
        String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 WHERE isbn = ?";

        try (Connection conn = DBConnection.getConnection()) {
            int txnId;
            LocalDate dueDate;

            try (PreparedStatement findPs = conn.prepareStatement(findTxnSql)) {
                findPs.setString(1, isbn);
                findPs.setInt(2, userId);
                ResultSet rs = findPs.executeQuery();
                if (!rs.next()) {
                    System.out.println("No active loan found for this book/user.");
                    return false;
                }
                txnId = rs.getInt("id");
                dueDate = rs.getDate("due_date").toLocalDate();
            }

            LocalDate returnDate = LocalDate.now();
            double fine = fineStrategy.calculateFine(dueDate, returnDate);

            try (PreparedStatement updateTxnPs = conn.prepareStatement(updateTxnSql)) {
                updateTxnPs.setDate(1, Date.valueOf(returnDate));
                updateTxnPs.setDouble(2, fine);
                updateTxnPs.setInt(3, txnId);
                updateTxnPs.executeUpdate();
            }

            try (PreparedStatement updateBookPs = conn.prepareStatement(updateBookSql)) {
                updateBookPs.setString(1, isbn);
                updateBookPs.executeUpdate();
            }

            System.out.println("Book returned. Fine: Rs. " + fine);
            return true;

        } catch (SQLException e) {
            System.out.println("Return failed: " + e.getMessage());
            return false;
        }
    }
}
