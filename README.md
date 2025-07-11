# 🎓 College Admission Management System

A Java-based console application to manage student registrations, course applications, merit-based selections, and generate admission lists in CSV and PDF formats.

---

## 📌 Features

- 📝 **Student Registration** (Name, Email, Phone, Marks, Category, Password)
- 🔐 **Student Login** using Email & Password
- 📚 **Course Application** (choose from available courses)
- ✅ **Merit-based Admission Processing** (based on cut-off marks)
- 📄 **Admission List Export**:
  - CSV (`admission_list.csv`)
  - PDF (`admission_list.pdf`)
- 💡 Automatically shows results (`APPROVED` / `REJECTED`)

---

## 🛠️ Technologies Used

- Java (JDK 8+)
- JDBC
- MySQL
- OpenCSV (`opencsv-5.7.1.jar`)
- iText PDF (`itextpdf-5.5.13.3.jar`)

---

## 🗃️ Database Schema

**Database:** `admission_db`

```sql
CREATE TABLE Students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    marks FLOAT,
    category VARCHAR(20),
    password VARCHAR(50)
);

CREATE TABLE Courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100),
    total_seats INT,
    cut_off_marks FLOAT
);

CREATE TABLE Applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    course_id INT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);


🧰 Tools and Techniques Used
Category	Tools / Techniques
💻 Programming	Java (Core Java, OOP concepts, Exception Handling)
🗃️ Database	MySQL (Relational DB, SQL Joins, Foreign Keys, Auto Increment)
🔌 Connectivity	JDBC (Java Database Connectivity for SQL operations)
🧾 File Handling	OpenCSV (for CSV export), iTextPDF (for PDF export)
📋 UI/UX	Console-based User Interface using Java Scanner
🧠 Logic	Merit calculation using cut-off marks, status update logic
🗂️ Data Structure	Arrays, Streams (Java 8+)
📦 Build & Run	Command-line (javac, java), external JAR support via lib/*
🧪 Testing	Manual test data through MySQL CLI and console input
🔐 Security	Basic login authentication using email and password

📦 External Libraries Used
Library	Purpose	Version
opencsv	Exporting data to CSV format	5.7.1 (or latest)
itextpdf	Creating structured PDF documents	5.5.13.3
