import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "head2head1amthebe$t";

    // Add a new book to the database
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, category, total_copies, available_copies) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, "Unknown"); // Your Book class has no category field yet
            stmt.setInt(4, 1);            // total copies default
            stmt.setInt(5, 1);            // available copies default

            stmt.executeUpdate();
            System.out.println("Book added to database: " + book.getTitle());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get a book by its ID
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author")
                );
                book.setAvailable(rs.getInt("available_copies") > 0);
                return book;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get all books
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author")
                );
                book.setAvailable(rs.getInt("available_copies") > 0);
                list.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Update availability (borrow)
    public void markBorrowed(int bookId) {
        String sql = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update availability (return)
    public void markReturned(int bookId) {
        String sql = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search by title
    public List<Book> searchByTitle(String title) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author")
                );
                book.setAvailable(rs.getInt("available_copies") > 0);
                list.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
