import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "head2head1amthebe$t";

    // Add a new student
    public void addStudent(Student student) {
        String sql = "INSERT INTO students (student_name) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.executeUpdate();

            System.out.println("Student added to database: " + student.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get a student by ID
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("student_name")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getString("student_name")
                );
                list.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Search students by name
    public List<Student> searchByName(String name) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_name LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getString("student_name")
                );
                list.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
