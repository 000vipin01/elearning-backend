-- Seed data for E-Learning Management System
-- Passwords are BCrypt encoded: 'password123'

-- Insert Users (using INSERT IGNORE to avoid duplicates)
INSERT IGNORE INTO users (id, name, email, password, role, created_at) VALUES
(1, 'Alex Johnson', 'alex@example.com', '$2a$10$d7c98gW3QBqj/moPDgH93OlYNuG8M9f1VT0anrAzWiuFC0gN0Ibzm', 'STUDENT', NOW()),
(2, 'Dr. Sarah Chen', 'sarah@example.com', '$2a$10$d7c98gW3QBqj/moPDgH93OlYNuG8M9f1VT0anrAzWiuFC0gN0Ibzm', 'INSTRUCTOR', NOW()),
(3, 'Admin User', 'admin@example.com', '$2a$10$d7c98gW3QBqj/moPDgH93OlYNuG8M9f1VT0anrAzWiuFC0gN0Ibzm', 'ADMIN', NOW());

-- Insert Courses
INSERT IGNORE INTO courses (id, title, description, category, instructor_id, created_at) VALUES
(1, 'Introduction to Web Development', 'Learn the fundamentals of web development including HTML, CSS, and JavaScript. This comprehensive course takes you from beginner to building your own web pages from scratch.', 'Web Development', 2, NOW()),
(2, 'Data Structures & Algorithms', 'Master computer science fundamentals with this comprehensive course on data structures and algorithms. Learn arrays, linked lists, trees, graphs, sorting, and searching.', 'Computer Science', 2, NOW()),
(3, 'UI/UX Design Fundamentals', 'Discover the principles of user interface and user experience design. Learn wireframing, prototyping, user research, and design thinking.', 'Design', 2, NOW()),
(4, 'Python for Data Science', 'Explore data science with Python. Learn NumPy, Pandas, Matplotlib, and machine learning basics. Work with real datasets and build predictive models.', 'Data Science', 2, NOW());

-- Insert Lessons for Course 1
INSERT IGNORE INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(1, 1, 'HTML Basics', 'HTML (HyperText Markup Language) is the standard markup language for creating web pages. It describes the structure of a web page using a series of elements.', 1, 15),
(2, 1, 'CSS Fundamentals', 'CSS (Cascading Style Sheets) is used to style and layout web pages. It controls colors, fonts, spacing, and positioning of elements.', 2, 20),
(3, 1, 'JavaScript Essentials', 'JavaScript is a programming language that adds interactivity to web pages. It runs in the browser and can manipulate HTML and CSS dynamically.', 3, 25),
(4, 1, 'Building a Web Page', 'Putting it all together! Build a complete web page using HTML, CSS, and JavaScript.', 4, 30);

-- Insert Lessons for Course 2
INSERT IGNORE INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(5, 2, 'Arrays and Linked Lists', 'Arrays and linked lists are fundamental data structures.', 1, 20),
(6, 2, 'Stacks and Queues', 'Stacks and queues are linear data structures with specific insertion and deletion rules.', 2, 15),
(7, 2, 'Trees and Graphs', 'Trees and graphs are non-linear data structures.', 3, 25);

-- Insert Lessons for Course 3
INSERT IGNORE INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(8, 3, 'Design Thinking', 'Design thinking is a human-centered approach to innovation and problem-solving.', 1, 15),
(9, 3, 'Wireframing', 'Wireframing is the process of creating a visual guide that represents the skeletal framework of a design.', 2, 20);

-- Insert Lessons for Course 4
INSERT IGNORE INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(10, 4, 'Python Basics', 'Python is a versatile programming language widely used in data science.', 1, 20),
(11, 4, 'NumPy Fundamentals', 'NumPy is the fundamental package for scientific computing in Python.', 2, 25),
(12, 4, 'Pandas DataFrame', 'Pandas provides the DataFrame object, a powerful data structure for data manipulation and analysis.', 3, 30);

-- Insert Enrollments
INSERT IGNORE INTO enrollments (id, student_id, course_id, progress, enrolled_at) VALUES
(1, 1, 1, 75, NOW()),
(2, 1, 2, 45, NOW()),
(3, 1, 4, 20, NOW());
