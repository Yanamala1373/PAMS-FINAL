🏥 Patient Appointment Management System

The Patient Appointment Management System is a comprehensive web-based platform designed to streamline hospital operations by enabling seamless interaction between patients, doctors, and administrators. It provides a centralized solution for managing registrations, logins, appointments, and role-based dashboards, ensuring a smooth and secure experience for all users.

This system is built to support multi-role access with tailored workflows for each user type, including patients booking appointments, doctors managing schedules, and admins overseeing operations. It emphasizes usability, security, and maintainability — making it suitable for real-world deployment in clinics, hospitals, or healthcare startups.

👥 User Roles

Patient: Can register, log in, book appointments, and view appointment history.

Doctor: Can register, log in, view assigned appointments, and update statuses.

Admin: Can register, log in, and manage hospital-wide operations and user oversight.


🎯 Key Features

Secure registration and login for all roles

Role-based dashboards with session-based access

Appointment booking and status tracking

Live dashboard updates for doctors

Validation for user inputs (e.g., gmail format, username and password authentication)

Clear navigation and user-friendly interface

Session and cache control for secure access

Professional documentation and annotation mapping


🛠️ Technologies Used

Java (Backend logic and service layer)

Spring Boot (Framework for building RESTful services and MVC architecture)

Spring Security (Authentication and role-based authorization)

Thymeleaf (Server-side HTML rendering)

HTML / CSS / Bootstrap (Frontend design and responsiveness)

MySQL (Relational database for storing user and appointment data)

JPA / Hibernate (ORM for database interaction)

JUnit / Mockito (Unit testing and service validation)


📂 Project Structure

The project follows a clean MVC architecture with separate layers for:

Controllers

Services

Repositories (DAO)

Models (Entities)

Configuration

Templates (HTML views)


 **Spring Security**

Spring Security stores your login in the HTTP session (backed by a session cookie like JSESSIONID).

      -->  In Chrome, all tabs in the same browser profile share the same cookies for a given site.
      -->  When you log in as a doctor in one tab, your session is tied to that doctor account.
      -->  When you then log in as a patient in another tab (same browser), you overwrite the session cookie with the patient’s login.
      -->  Now, when you go back to the doctor tab and try to do something (edit availability, cancel appointment, etc.), Spring Security sees your session as patient, not doctor →      
      -->  If you hit a /doctor/** URL, you don’t have ROLE_DOCTOR anymore → 403 Forbidden or Whitelabel error.
      -->  If you hit a /patient/** URL while logged in as doctor, same problem.

In short: you can’t be logged in as two different users in the same browser session at the same time.

How to fix / work around it

Option 1 — Use different browsers
      
      --> Log in as doctor in Chrome.
      
      --> Log in as patient in Firefox / Edge / another browser.
      
      --> Each browser keeps its own cookies and session.
