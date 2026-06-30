import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Library {

    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private BorrowDAO borrowDAO;

    public Library() {
        bookDAO = new BookDAO();
        studentDAO = new StudentDAO();
        borrowDAO = new BorrowDAO();
    }

    // Add a new book
    public void addBook(Book book) {
        bookDAO.addBook(book);
        System.out.println("Book added: " + book.getTitle());
    }

    // Register a new student
    public void addStudent(Student student) {
        studentDAO.addStudent(student);
        System.out.println("Student registered: " + student.getName());
    }

    // Borrow a book
    public void borrowBook(int studentId, int bookId) {
        Student student = studentDAO.getStudentById(studentId);
        Book book = bookDAO.getBookById(bookId);

        if (student == null || book == null) {
            System.out.println("Invalid student ID or book ID.");
            return;
        }

        boolean success = borrowDAO.borrowBook(studentId, bookId);

        if (success) {
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(14);

            System.out.println(student.getName() + " borrowed " + book.getTitle() +
                " (Due: " + dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        }
    }

    // Return a book
    public void returnBook(int studentId, int bookId) {
        Integer borrowId = borrowDAO.getBorrowId(studentId, bookId);

        if (borrowId == null) {
            System.out.println("No active borrow record found for this book.");
            return;
        }

        borrowDAO.returnBook(borrowId);
    }

    // Display all books
    public void displayAllBooks() {
        System.out.println("=========📖Books📖=========");
        List<Book> books = bookDAO.getAllBooks();

        for (Book book : books) {
            book.displayDetails();
            System.out.println();
        }
    }

    // Display all students
    public void displayAllStudents() {
        System.out.println("=========👩‍🎓Students👨‍🎓=========");
        List<Student> students = studentDAO.getAllStudents();

        for (Student student : students) {
            student.displayDetails();
            System.out.println();
        }
    }

    // Display all borrowed books
    public void displayAllBorrowedBooks() {
        System.out.println("\n===== All Borrowed Books =====");

        List<BorrowRecord> records = borrowDAO.getActiveBorrowRecords();

        if (records.isEmpty()) {
            System.out.println("No books are currently borrowed.");
            return;
        }

        for (BorrowRecord record : records) {
            System.out.println("- Book ID: " + record.getBook().getBookID() +
                " | Borrowed by Student ID: " + record.getStudent().getStudentID() +
                " | Due: " + record.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    // Search book by title
    public void searchBookByTitle(String title) {
        System.out.println("\n===== Search Results (Title Contains) =====");
        List<Book> results = bookDAO.searchByTitle(title);

        if (results.isEmpty()) {
            System.out.println("No books found matching that title.");
            return;
        }

        for (Book book : results) {
            book.displayDetails();
            System.out.println();
        }
    }

    // Search student by name
    public void searchStudent(String name) {
        System.out.println("\n===== Student Search Results =====");
        List<Student> results = studentDAO.searchByName(name);

        if (results.isEmpty()) {
            System.out.println("No student found matching that name.");
            return;
        }

        for (Student student : results) {
            student.displayDetails();
            System.out.println();
        }
    }

    // Search book by ID
    public void searchBookById(int id) {
        System.out.println("\n===== Search Results (Book ID) =====");

        Book book = bookDAO.getBookById(id);

        if (book == null) {
            System.out.println("No book found with that ID.");
            return;
        }

        book.displayDetails();
    }
}
