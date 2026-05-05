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
- **Teacher (Professor)**
- **Librarian**
- **Administrator**

Each role has specific responsibilities and permissions.

---

# 🚀 Features

## 👨‍🎓 Student

The **student** is the primary user of the platform.

- **Registration** – Students can register using a classroom code and select their favorite genres.
- **Login** – Secure authentication using JWT.
- **Personal statistics** – View level, current XP, reading streak, average reading time, and number of books read.
- **Book browsing** – Search for books by title, author, or genre. Each book displays its cover image, rating, and availability.
- **Reading session** – Start a timed reading session (with pause/cancel). When finished, the student enters the last page read and submits a summary. XP is awarded based on the number of lines in the summary (max 30 XP per session).
- **Book rating** – Rate books from 1 to 5 stars.
- **Reading groups** – Create groups, view your groups, and add books to them.
- **View summaries** – See all summaries you have submitted.
- **Reading history** – Track your past reading sessions.

---

## 👨‍🏫 Teacher (Professor)

Teachers can monitor the students in their assigned classrooms.

- **Login** – Role-based access.
- **Classroom overview** – See all classrooms the teacher belongs to, with a collapsible list of students in each.
- **Student information** – For each student, view:
  - Last login date
  - Current book being read
  - Last submitted summary (popup modal)
- **Other tools** – Search summaries by book ID and view individual student stats by ID.

---

## 📚 Librarian

The librarian manages the book catalog.

- **Login** – Role-based access.
- **Add books** – Enter title, synopsis, pages, release date, authors, and genres. Confirmation option to bypass duplicate title warnings.
- **Upload book covers** – After adding a book, the librarian can upload a cover image. Images are rendered via a dedicated image endpoint.
- **Update availability** – Toggle a book’s availability (available/unavailable) directly from a list.
- **List all books** – View the entire collection with cover thumbnails, titles, availability, and quantity.
- **Author management** – Add new authors and list existing ones.

---

## 🛠 Administrator

The administrator manages users and classrooms.

- **Login** – Role-based access.
- **Register users** – Create new professors, librarians, and administrators (with optional classroom code for professors).
- **Classroom management**:
  - Create classrooms (auto‑generates a unique public code).
  - List all classrooms with options to show users, edit name, delete, and copy the public code.
  - Replace the set of users in a classroom (bulk assign/unassign).
- **No AI invite links yet** – Future feature.

---

# 🧠 AI Integration (Planned)

The platform will use **Artificial Intelligence** to classify the **difficulty level of books** (Easy, Medium, Hard). This feature is not yet implemented but is a priority for future releases.

---

# 🧱 Tech Stack

| Layer | Technology |
|------|-------------|
| **Backend** | Java 21, Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate |
| **Frontend** | HTML5, Bootstrap 5, JavaScript + jQuery (JWT decoding, dynamic tabs, modals) |
| **Database** | MySQL |
| **Infrastructure** | Docker (containerized deployment) |

---

# 🔮 Future Features

- **AI book difficulty classification** – Use NLP to automatically analyse book content and suggest a difficulty level.
- **Expiring registration links** – Allow administrators to generate time‑limited links for teachers or librarians to self‑register.
- **Leaderboards** – Compare students’ reading streaks and XP within a classroom.
- **Rewards and achievements** – Unlock badges for reaching reading milestones.
- **Mobile‑friendly version** – Optimize the frontend for smartphones and tablets.
- **Detailed analytics** – Graphs and reports for teachers to track class progress over time.
- **Book recommendations** – Based on favourite genres and past reading.

---

## 📦 Running the Project

The entire stack (backend, frontend, MySQL) can be run inside Docker containers. The frontend is served as static HTML from the Spring Boot application, and all API endpoints are secured with JWT.

For development, you can run the Spring Boot application locally and access the dashboard at `http://localhost:8080/dashboard.html`.
