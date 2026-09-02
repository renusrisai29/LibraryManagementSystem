package library;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private String bookIsbn;
    private int userId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public Transaction(String bookIsbn, int userId, LocalDate issueDate, LocalDate dueDate) {
        this.bookIsbn = bookIsbn;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public String getBookIsbn() { return bookIsbn; }
    public int getUserId() { return userId; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}
