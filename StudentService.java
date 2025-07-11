import java.sql.*;
import java.util.Scanner;

public class StudentService {
   public void registerStudent() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Name: ");
    String name = sc.nextLine();

    System.out.print("Enter Email: ");
    String email = sc.nextLine();

    System.out.print("Enter Phone: ");
    String phone = sc.nextLine();

    System.out.print("Enter Marks: ");
    float marks = sc.nextFloat();
    sc.nextLine(); // consume newline

    System.out.print("Enter Category: ");
    String category = sc.nextLine();

    System.out.print("Create Password: ");
    String password = sc.nextLine();

    String query = "INSERT INTO Students (name, email, phone, marks, category, password) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.setFloat(4, marks);
        ps.setString(5, category);
        ps.setString(6, password);

        ps.executeUpdate();
        System.out.println("✅ Student registered successfully!");

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


   public int studentLogin() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter your Email: ");
    String email = sc.nextLine();

    System.out.print("Enter your Password: ");
    String password = sc.nextLine();

    String sql = "SELECT student_id, name FROM Students WHERE email = ? AND password = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, email);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("✅ Welcome, " + rs.getString("name"));
            return rs.getInt("student_id");
        } else {
            System.out.println("❌ Login failed. Incorrect email or password.");
            return -1;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return -1;
    }
}



    public void applyForCourseByName(int studentId) {
    Scanner sc = new Scanner(System.in);
    showCourses();

    System.out.print("Enter course name to apply: ");
    String courseName = sc.nextLine();

    String courseQuery = "SELECT course_id FROM Courses WHERE course_name = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(courseQuery)) {

        ps.setString(1, courseName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int courseId = rs.getInt("course_id");

            // Proceed with application
            String insertApp = "INSERT INTO Applications (student_id, course_id) VALUES (?, ?)";
            try (PreparedStatement insertPs = conn.prepareStatement(insertApp)) {
                insertPs.setInt(1, studentId);
                insertPs.setInt(2, courseId);
                insertPs.executeUpdate();
                System.out.println("✅ Application submitted.");
            }
        } else {
            System.out.println("❌ Course not found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    public void showCourses() {
    String sql = "SELECT course_id, course_name, cut_off_marks FROM Courses";
    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        System.out.println("\n📚 Available Courses:");
        System.out.println("-------------------------------------");
        while (rs.next()) {
            System.out.printf("ID: %d | Name: %s | Cut-Off: %.2f\n",
                rs.getInt("course_id"),
                rs.getString("course_name"),
                rs.getFloat("cut_off_marks"));
        }
        System.out.println("-------------------------------------");

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
