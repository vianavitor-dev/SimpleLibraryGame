# 📚 Gamified Reading Platform
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![HTML](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black) 
<br>

## 📖 Overview

This project is a **gamified reading platform** designed to increase young students' interest in books.

The system uses **gamification techniques** such as levels, reading streaks, and activity tracking to motivate students to read more frequently and engage with books in a structured and enjoyable way.

The platform also allows **teachers, librarians, and administrators** to monitor student activity and manage books and classes.

---

## 🎯 Project Goal

The main objective of the system is to **encourage reading among younger students**.

To achieve this, the platform introduces **game-like mechanics**, allowing students to track their progress, maintain reading streaks, and interact with books and reading groups.

---

# 👥 User Roles

The system supports four types of users:

- **Student**
- **Teacher**
- **Librarian**
- **Administrator**

Each role has specific responsibilities and permissions.

---

# 🚀 Features

## 👨‍🎓 Student

The **student** is the primary user of the platform and has access to most features.

- Register (using a class code)
- Login
- View personal statistics:
  - Level
  - Average reading time
  - Last book read
  - Reading streak
- Search for books
- Start a reading session
- Finish a reading session
- Submit a summary of the pages read
- Create reading groups
- Rename or remove groups
- Add books to a group

---

## 👨‍🏫 Teacher

Teachers are responsible for **monitoring students from their class**.

- Login
- View the list of students in their class
- Access student information such as:
  - Level
  - Average reading time
  - Last book read
  - Last submitted summary

---

## 📚 Librarian

The librarian is responsible for **book management**.

- Login
- Manage the book catalog:
  - Add books
  - Remove books
  - Update book information
  - List available books

---

## 🛠 Administrator

The administrator manages **users and classes** within the system.

- Register new users
- Login
- Manage classes:
  - Create classes
  - List classes
  - Update class information
  - View class details
- Generate **expiring registration links** for new users (teachers or librarians)

---

# 🧠 AI Integration

The platform uses **Artificial Intelligence** to classify the **difficulty level of books**.

The AI analyzes the book content and categorizes it into one of three levels:

- **Easy**
- **Medium**
- **Hard**

This classification helps students choose books appropriate for their reading level and allows teachers to better guide student reading development.

---

# 🧱 Tech Stack

| Layer | Technology |
|------|-------------|
| **Backend** | Java, Spring Framework |
| **Frontend** | HTML, CSS, JavaScript |
| **Database** | MySQL |
| **Infrastructure** | Docker |
