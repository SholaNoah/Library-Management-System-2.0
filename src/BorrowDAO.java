import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "head2head1amthebe$t";

    // Borrow a book
    public boolean borrowBook(int studentId, int bookId) {
        String borrowSql = "INSERT INTO borrowed_books (student_id, book_id, borrow_date, status) VALUES (?, ?, CURRENT_DATE, 'Not Returned')";
        String updateBookSql = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Update book availability
            PreparedStatement updateStmt = conn.prepareStatement(updateBookSql);
            updateStmt.setInt(1, bookId);
            int updated = updateStmt.executeUpdate();

            if (updated == 0) {
                System.out.println("No available copies for this book.");
                return false;
            }

            // Insert borrow record
            PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);
            borrowStmt.setInt(1, studentId);
            borrowStmt.setInt(2, bookId);
            borrowStmt.executeUpdate();

            System.out.println("Book borrowed successfully.");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return a book
    public void returnBook(int borrowId) {
        String returnSql = "UPDATE borrowed_books SET return_date = CURRENT_DATE, status = 'Returned' WHERE borrow_id = ?";
        String getBookSql = "SELECT book_id, borrow_date FROM borrowed_books WHERE borrow_id = ?";
        String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Get book ID and borrow date
            PreparedStatement getStmt = conn.prepareStatement(getBookSql);
            getStmt.setInt(1, borrowId);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Borrow record not found.");
                return;
            }

            int bookId = rs.getInt("book_id");
            LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
            LocalDate dueDate = borrowDate.plusDays(14);
            LocalDate returnDate = LocalDate.now();

            int lateFee = LateFeeCalculator.calculateLateFee(dueDate, returnDate);

            // Update borrow record
            PreparedStatement returnStmt = conn.prepareStatement(returnSql);
            returnStmt.setInt(1, borrowId);
            returnStmt.executeUpdate();

            // Update book availability
            PreparedStatement updateStmt = conn.prepareStatement(updateBookSql);
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();

            System.out.println("Book returned successfully.");
            if (lateFee > 0) {
                System.out.println("Late fee: £" + lateFee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get all active borrow records
    public List<BorrowRecord> getActiveBorrowRecords() {
        List<BorrowRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM borrowed_books WHERE status = 'Not Returned'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                int bookId = rs.getInt("book_id");

                LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();

                BorrowRecord record = new BorrowRecord(
                    new Student(studentId, ""),   // Name will be fetched later
                    new Book(bookId, "", ""),     // Title/author fetched later
                    borrowDate
                );

                list.add(record);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Get borrow ID for a student/book pair
    public Integer getBorrowId(int studentId, int bookId) {
        String sql = "SELECT borrow_id FROM borrowed_books WHERE student_id = ? AND book_id = ? AND status = 'Not Returned'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, bookId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("borrow_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
