package dao;

import java.sql.*;

public class DashboardStatsDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "head2head1amthebe$t";

    public int getTotalBooks() {
        return runCountQuery("SELECT COUNT(*) FROM books");
    }

    public int getTotalStudents() {
        return runCountQuery("SELECT COUNT(*) FROM students");
    }

    public int getActiveBorrowsCount() {
        return runCountQuery("SELECT COUNT(*) FROM borrowed_books WHERE status = 'Not Returned'");
    }

    public String getMostBorrowedBook() {
        String sql = """
            SELECT b.title, COUNT(*) AS borrow_count
            FROM borrowed_books bb
            JOIN books b ON bb.book_id = b.book_id
            GROUP BY b.book_id, b.title
            ORDER BY borrow_count DESC
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("title") + " (" + rs.getInt("borrow_count") + " borrows)";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "No data yet";
    }

    public String getMostActiveStudent() {
        String sql = """
            SELECT s.student_name, COUNT(*) AS borrow_count
            FROM borrowed_books bb
            JOIN students s ON bb.student_id = s.student_id
            GROUP BY s.student_id, s.student_name
            ORDER BY borrow_count DESC
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("student_name") + " (" + rs.getInt("borrow_count") + " borrows)";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "No data yet";
    }

    private int runCountQuery(String sql) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getOverdueCount() {
    String sql = """
        SELECT COUNT(*) FROM borrowed_books
        WHERE status = 'Not Returned'
        AND DATE_ADD(borrow_date, INTERVAL 14 DAY) < CURRENT_DATE
    """;
    return runCountQuery(sql);
    }
}