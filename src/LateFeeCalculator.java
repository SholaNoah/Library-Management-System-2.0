import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** This is the brain behind the return date calculation and fees being incurred.
 * This class calculates the late fee for a book based on the due date and the return date.
 * The late fee is calculated as £2 per day for each day the book is returned after
 * the due date. If the book is returned on or before the due date, no late fee is charged.
 * @author Shankz
 */

public class LateFeeCalculator{
    public static int calculateLateFee(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            return (int) daysLate * 2;
        }
        return 0;
    }
}
 
    

