import java.sql.*;
import java.io.*;
import java.util.stream.Stream;

import com.opencsv.CSVWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class AdminService {

    public void processApplications() {
        String sql = "SELECT a.application_id, s.marks, c.cut_off_marks FROM Applications a " +
                     "JOIN Students s ON a.student_id = s.student_id " +
                     "JOIN Courses c ON a.course_id = c.course_id " +
                     "WHERE a.status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int appId = rs.getInt("application_id");
                float marks = rs.getFloat("marks");
                float cutOff = rs.getFloat("cut_off_marks");

                String status = marks >= cutOff ? "APPROVED" : "REJECTED";

                String update = "UPDATE Applications SET status = ? WHERE application_id = ?";
                try (PreparedStatement ups = conn.prepareStatement(update)) {
                    ups.setString(1, status);
                    ups.setInt(2, appId);
                    ups.executeUpdate();
                }
            }

            System.out.println("✅ Applications processed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exportToCSV() throws IOException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT s.name, s.email, s.phone, s.marks, s.category, c.course_name, a.status " +
                 "FROM Applications a " +
                 "JOIN Students s ON a.student_id = s.student_id " +
                 "JOIN Courses c ON a.course_id = c.course_id " +
                 "WHERE a.status IS NOT NULL");
             CSVWriter writer = new CSVWriter(new FileWriter("admission_list.csv", false))) {

            writer.writeNext(new String[]{"Name", "Email", "Phone", "Marks", "Category", "Course", "Result"});

            int rowCount = 0;

            while (rs.next()) {
                rowCount++;
                String[] row = {
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    String.valueOf(rs.getFloat("marks")),
                    rs.getString("category"),
                    rs.getString("course_name"),
                    rs.getString("status")
                };

                writer.writeNext(row);
            }

            if (rowCount > 0) {
                System.out.println("✅ Total rows exported to CSV: " + rowCount);
                System.out.println("✅ CSV file generated: admission_list.csv");
            } else {
                System.out.println("⚠️ No applications found. CSV not updated.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exportToPDF() {
        String fileName = "admission_list.pdf";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT s.name, s.email, s.phone, s.marks, s.category, c.course_name, a.status " +
                 "FROM Applications a " +
                 "JOIN Students s ON a.student_id = s.student_id " +
                 "JOIN Courses c ON a.course_id = c.course_id " +
                 "WHERE a.status IS NOT NULL")) {

            if (!rs.isBeforeFirst()) {
                System.out.println("⚠️ No applications found. PDF not created.");
                return;
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Admission List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setWidths(new float[]{2.2f, 3.5f, 3.0f, 1.5f, 2.2f, 4.2f, 2.3f});

            // Header row
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Stream.of("Name", "Email", "Phone", "Marks", "Category", "Course", "Result")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setNoWrap(true);
                    cell.setPadding(5f);
                    table.addCell(cell);
                });

            // Data rows
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 9);
            int rowCount = 0;

            while (rs.next()) {
                rowCount++;

                Stream.of(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    String.valueOf(rs.getFloat("marks")),
                    rs.getString("category"),
                    rs.getString("course_name")
                ).forEach(value -> {
                    PdfPCell cell = new PdfPCell(new Phrase(value, dataFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cell);
                });

                // Add result cell
                PdfPCell resultCell = new PdfPCell(new Phrase(rs.getString("status"), dataFont));
                resultCell.setNoWrap(true);
                resultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                resultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(resultCell);
            }

            document.add(table);
            document.close();

            if (rowCount > 0) {
                System.out.println("✅ Total rows exported to PDF: " + rowCount);
                System.out.println("✅ PDF file generated: admission_list.pdf");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
