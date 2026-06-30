📚 Library Management System (LBMS 3.0 – Java + MySQL)
A fully upgraded Library Management System built in Java, now powered by a robust MySQL database backend.
LBMS 3.0 introduces a complete architectural overhaul, replacing text‑file storage with persistent relational data, enabling real‑world scalability and reliability.

This project demonstrates:

Object‑oriented programming (OOP)

DAO (Data Access Object) architecture

MySQL database integration

Real‑world library logic (borrowing, returning, availability, late fees)

Data persistence across program restarts

Clean separation of concerns (UI → Logic → Database)

LBMS has evolved from a simple file‑based system into a structured, database‑driven application.

🚀 New in LBMS 3.0
🗄️ MySQL Database Integration
LBMS now stores all data in MySQL tables:

books

students

borrowed_books

system

This upgrade provides:

reliable persistence

structured relational data

easier querying

safer updates

scalable storage

🧩 DAO Architecture
All database operations are handled through dedicated DAO classes:

BookDAO.java

StudentDAO.java

BorrowDAO.java

This ensures:

cleaner code

easier maintenance

reusable database logic

proper separation of concerns

💸 Late Fee System
LBMS now supports:

due dates

late fee calculation

late fee storage in the database

late fee display during returns

A new column late_fee was added to borrowed_books to support this feature.

🔄 Borrow & Return Tracking (Database Version)
Borrowing and returning books now updates:

availability in books

borrow status in borrowed_books

return dates

late fees

All operations are fully persistent.

📥 Migration Support
A migration script was created to import old LBMS 2.2 data into MySQL:

students

books

borrowed records

availability states

This ensures continuity between versions.

🔍 Improved Search System
Search now works across:

book titles

authors

student names

borrowed records

All powered by SQL queries.

🧼 Fixed Menu Logic
The main loop now exits correctly when selecting “Exit,” resolving the previous infinite loop issue.

✨ Features
📘 Book Management
Add new books

Auto‑generated book IDs (MySQL)

Track total & available copies

Display all books

👨‍🎓 Student Management
Register students

Auto‑generated student IDs

View all students

🔄 Borrowing & Returning
Borrow books (only if available)

Return books

Late fee calculation

Automatic availability updates

📊 Borrowed Book Records
View all borrowed books

Track borrow dates

Track return dates

Track late fees

🗄️ Persistent Storage (MySQL)
All data is stored in MySQL tables, ensuring:

durability

consistency

structured relationships

safe updates

📂 Project Structure
Code
/LibraryManagementSystem
│── Book.java
│── Student.java
│── BorrowRecord.java
│── Library.java
│── Main.java
│── dao/
│   ├── BookDAO.java
│   ├── StudentDAO.java
│   └── BorrowDAO.java
│── sql/
│   ├── schema.sql
│   └── migration.sql
│── README.md
🛠 How to Run
1. Set up MySQL
Import the schema:

sql
source schema.sql;
(Optional) import migration data:

sql
source migration.sql;
2. Compile Java files
bash
javac *.java dao/*.java
3. Run the program
bash
java Main
🧠 How Database Persistence Works
When borrowing:
A row is added to borrowed_books

available_copies decreases

Status = Not Returned

When returning:
return_date is stored

Late fee is calculated

Status becomes Returned

available_copies increases

When restarting:
All data is loaded from MySQL

No more text files

No more manual syncing

🧪 Example Output
Code
=========📖 Books 📖=========
ID: 110
Title: A Gentleman in Moscow
Author: Amor Towles
Available: false

=========👩‍🎓 Students 👨‍🎓=========
Student ID: 16
Name: Marie
Borrowed Books: Recursion, A Gentleman in Moscow
Late Fees: 0
📝 Future Improvements
Category filtering

ISBN support

Overdue book notifications

Admin login system

Web dashboard (Spring Boot)

GUI version (JavaFX)

👤 Author
Al‑Mustapha (Shankz)  
Java Developer | Writer | Creator

⭐ License
This project is open‑source.
Feel free to fork, modify, and improve it.
