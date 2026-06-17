# 📚 Library Management System (Java)

A simple but fully functional **Library Management System** built in Java.  
It manages **books**, **students**, **borrowing**, **returning**, and **persistent storage** using a text file.

This project demonstrates:
- Object‑oriented programming (OOP)
- File I/O (saving & loading data)
- Class relationships (Library → Student → Book)
- Data persistence across program restarts
- Real‑world logic such as availability tracking and borrowing rules

---

## 🚀 Features

### ✅ Book Management
- Add new books  
- Track availability  
- Display all books  

### 👨‍🎓 Student Management
- Register students  
- Track borrowed books  
- Display student details  

### 🔄 Borrowing & Returning
- Borrow a book (only if available)  
- Return a book  
- Prevent invalid operations  

### 💾 Persistent Storage
Data is saved to `library_data.txt`:
- Books (ID, title, author, availability)
- Students (ID, name)
- Borrowed book IDs (restored on load)

This ensures the system remembers everything even after closing the program.

---

## 📂 Project Structure
/LibraryManagementSystem
│── Book.java
│── Student.java
│── Library.java
│── Main.java
│── library_data.txt (auto‑generated)
│── README.md

---

## 🛠 How to Run

1. Compile all `.java` files:
javac *.java


2. Run the program:
java Main


---

## 💡 How Data Persistence Works

When saving:
- Each student’s borrowed books are stored as a list of book IDs.
- Books store their availability status.

When loading:
- Books are recreated first.
- Students are recreated next.
- Borrowed book IDs are mapped back to actual Book objects.

This ensures:
- Books and students stay in sync
- Returning books works correctly after restart

---

## 🧪 Example Output
=========📖Books📖=========
ID: 1
Title: The Hobbit
Author: J.R.R. Tolkien
Available: false

=========👩‍🎓Students👨‍🎓=========
Student ID: 101
Name: Alice
Borrowed Books: The Hobbit


---

## 📝 Future Improvements
- Due dates & late fees  
- Search system (by title, author, student)  
- JSON or database storage  
- GUI version (JavaFX or Swing)  

---

## 👤 Author
**Al‑Mustapha (Shankz)**  
Java Developer | Writer | Creator  

---

## ⭐ License
This project is open‑source. Feel free to fork, modify, and improve it.
