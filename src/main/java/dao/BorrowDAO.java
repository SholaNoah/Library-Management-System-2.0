package dao;

import models.Book;
import models.Student;
import models.BorrowRecord;
import models.LateFeeCalculator;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true";
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
   public int returnBook(int borrowId) {
    String returnSql = "UPDATE borrowed_books SET return_date = CURRENT_DATE, status = 'Returned', late_fee = ? WHERE borrow_id = ?";
    String getBookSql = "SELECT book_id, borrow_date FROM borrowed_books WHERE borrow_id = ?";
    String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";

    int lateFee = 0;

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

        PreparedStatement getStmt = conn.prepareStatement(getBookSql);
        getStmt.setInt(1, borrowId);
        ResultSet rs = getStmt.executeQuery();

        if (!rs.next()) {
            System.out.println("Borrow record not found.");
            return 0;
        }

        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate dueDate = borrowDate.plusDays(14);
        LocalDate returnDate = LocalDate.now();

        lateFee = LateFeeCalculator.calculateLateFee(dueDate, returnDate);

        PreparedStatement returnStmt = conn.prepareStatement(returnSql);
        returnStmt.setInt(1, lateFee);
        returnStmt.setInt(2, borrowId);
        returnStmt.executeUpdate();

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

    return lateFee;
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

   public List<FeeRecord> getFeeHistory() {
    List<FeeRecord> list = new ArrayList<>();
    String sql = """
        SELECT s.student_name, b.title, bb.borrow_date, bb.late_fee, bb.return_date
        FROM borrowed_books bb
        JOIN students s ON bb.student_id = s.student_id
        JOIN books b ON bb.book_id = b.book_id
        WHERE bb.late_fee > 0
        ORDER BY bb.return_date DESC
    """;

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            list.add(new FeeRecord(
                rs.getString("student_name"),
                rs.getString("title"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getInt("late_fee"),
                rs.getDate("return_date").toLocalDate()
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    // Small helper class just to carry fee history rows
  public static class FeeRecord {
    private final String studentName;
    private final String bookTitle;
    private final int lateFee;
    private final java.time.LocalDate borrowDate;
    private final java.time.LocalDate returnDate;

    public FeeRecord(String studentName, String bookTitle, java.time.LocalDate borrowDate, int lateFee, java.time.LocalDate returnDate) {
        this.studentName = studentName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.lateFee = lateFee;
        this.returnDate = returnDate;
    }

    public String getStudentName() { return studentName; }
    public String getBookTitle() { return bookTitle; }
    public int getLateFee() { return lateFee; }

    public String getBorrowDate() {
        return borrowDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getReturnDate() {
        return returnDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}

    public List<BorrowHistoryRecord> getBorrowHistory() {
    List<BorrowHistoryRecord> list = new ArrayList<>();
    String sql = """
        SELECT s.student_name, b.title, bb.borrow_date, bb.return_date, bb.status, bb.late_fee
        FROM borrowed_books bb
        JOIN students s ON bb.student_id = s.student_id
        JOIN books b ON bb.book_id = b.book_id
        ORDER BY bb.borrow_date DESC
    """;

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            java.sql.Date returnDateRaw = rs.getDate("return_date");
            String returnDateStr = returnDateRaw == null ? "—" :
                returnDateRaw.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            list.add(new BorrowHistoryRecord(
                rs.getString("student_name"),
                rs.getString("title"),
                rs.getDate("borrow_date").toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                returnDateStr,
                rs.getString("status"),
                rs.getInt("late_fee")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public static class BorrowHistoryRecord {
        private final String studentName;
        private final String bookTitle;
        private final String borrowDate;
        private final String returnDate;
        private final String status;
        private final int lateFee;

        public BorrowHistoryRecord(String studentName, String bookTitle, String borrowDate, String returnDate, String status, int lateFee) {
            this.studentName = studentName;
            this.bookTitle = bookTitle;
            this.borrowDate = borrowDate;
            this.returnDate = returnDate;
            this.status = status;
            this.lateFee = lateFee;
        }

        public String getStudentName() { return studentName; }
        public String getBookTitle() { return bookTitle; }
        public String getBorrowDate() { return borrowDate; }
        public String getReturnDate() { return returnDate; }
        public String getStatus() { return status; }
        public int getLateFee() { return lateFee; }
    }

    public List<OverdueRecord> getOverdueBooks() {
    List<OverdueRecord> list = new ArrayList<>();
    String sql = """
        SELECT s.student_name, b.title, bb.borrow_date
        FROM borrowed_books bb
        JOIN students s ON bb.student_id = s.student_id
        JOIN books b ON bb.book_id = b.book_id
        WHERE bb.status = 'Not Returned'
    """;

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
            LocalDate dueDate = borrowDate.plusDays(14);
            LocalDate today = LocalDate.now();

            if (today.isAfter(dueDate)) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
                int estimatedFee = (int) daysLate * 2;

                list.add(new OverdueRecord(
                    rs.getString("student_name"),
                    rs.getString("title"),
                    dueDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    (int) daysLate,
                    estimatedFee
                ));
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public static class OverdueRecord {
        private final String studentName;
        private final String bookTitle;
        private final String dueDate;
        private final int daysLate;
        private final int estimatedFee;

        public OverdueRecord(String studentName, String bookTitle, String dueDate, int daysLate, int estimatedFee) {
            this.studentName = studentName;
            this.bookTitle = bookTitle;
            this.dueDate = dueDate;
            this.daysLate = daysLate;
            this.estimatedFee = estimatedFee;
        }

        public String getStudentName() { return studentName; }
        public String getBookTitle() { return bookTitle; }
        public String getDueDate() { return dueDate; }
        public int getDaysLate() { return daysLate; }
        public int getEstimatedFee() { return estimatedFee; }
    }
}
