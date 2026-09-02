package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public interface FineStrategy {
    double calculateFine(LocalDate dueDate, LocalDate returnDate);
}

/**
 * Flat rate: Rs. 5 per day late. Swap this class out (e.g. SlabFineStrategy)
 * without touching any other code - that's the point of the Strategy pattern.
 */
class FlatRateFineStrategy implements FineStrategy {
    private static final double RATE_PER_DAY = 5.0;

    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        long lateDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return lateDays > 0 ? lateDays * RATE_PER_DAY : 0;
    }
}
