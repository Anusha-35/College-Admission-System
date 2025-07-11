import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();
        AdminService adminService = new AdminService();

        while (true) {
            System.out.println("\n🎓 College Admission Management System");
            System.out.println("======================================");
            System.out.println("1. Register Student");
            System.out.println("2. Login and Apply for Course");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    studentService.registerStudent();
                    break;

                case 2:
                    int studentId = studentService.studentLogin();
                    if (studentId != -1) {
                        studentService.applyForCourseByName(studentId);

                        System.out.println("🔄 Processing application...");
                        adminService.processApplications();

                        System.out.println("📄 Generating CSV and PDF...");
                        try {
                            adminService.exportToCSV();
                        } catch (IOException e) {
                            System.out.println("❌ CSV export failed.");
                            e.printStackTrace();
                        }

                        adminService.exportToPDF();

                        System.out.println("✅ All steps completed successfully!");
                    }
                    break;

                case 3:
                    System.out.println("👋 Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
}
