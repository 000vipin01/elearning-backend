-- Seed data for E-Learning Management System (PostgreSQL)
-- Passwords are BCrypt encoded: 'password123'

-- Insert Users (using ON CONFLICT to avoid duplicates)
INSERT INTO users (id, name, email, password, role, created_at) VALUES
(1, 'Alex Johnson', 'alex@example.com', '$2a$10$d7c98gW3QBqj/moPDgH93OlYNuG8M9f1VT0anrAzWiuFC0gN0Ibzm', 'STUDENT', NOW()),
(2, 'Dr. Sarah Chen', 'sarah@example.com', '$2a$10$d7c98gW3QBqj/moPDgH93OlYNuG8M9f1VT0anrAzWiuFC0gN0Ibzm', 'INSTRUCTOR', NOW()),
(3, 'Admin User', 'admin@example.com', '$2a$10$d7c98gW3QBqj/moPDgH93OlYNuG8M9f1VT0anrAzWiuFC0gN0Ibzm', 'ADMIN', NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Courses
INSERT INTO courses (id, title, description, category, instructor_id, created_at) VALUES
(1, 'Introduction to Web Development', 'Learn the fundamentals of web development including HTML, CSS, and JavaScript. This comprehensive course takes you from beginner to building your own web pages from scratch.', 'Web Development', 2, NOW()),
(2, 'Data Structures & Algorithms', 'Master computer science fundamentals with this comprehensive course on data structures and algorithms. Learn arrays, linked lists, trees, graphs, sorting, and searching.', 'Computer Science', 2, NOW()),
(3, 'UI/UX Design Fundamentals', 'Discover the principles of user interface and user experience design. Learn wireframing, prototyping, user research, and design thinking.', 'Design', 2, NOW()),
(4, 'Python for Data Science', 'Explore data science with Python. Learn NumPy, Pandas, Matplotlib, and machine learning basics. Work with real datasets and build predictive models.', 'Data Science', 2, NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Lessons for Course 1
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(1, 1, 'HTML Basics', 'HTML (HyperText Markup Language) is the standard markup language for creating web pages. It describes the structure of a web page using a series of elements.', 1, 15),
(2, 1, 'CSS Fundamentals', 'CSS (Cascading Style Sheets) is used to style and layout web pages. It controls colors, fonts, spacing, and positioning of elements.', 2, 20),
(3, 1, 'JavaScript Essentials', 'JavaScript is a programming language that adds interactivity to web pages. It runs in the browser and can manipulate HTML and CSS dynamically.', 3, 25),
(4, 1, 'Building a Web Page', 'Putting it all together! Build a complete web page using HTML, CSS, and JavaScript.', 4, 30)
ON CONFLICT (id) DO NOTHING;

-- Insert Lessons for Course 2
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(5, 2, 'Arrays and Linked Lists', 'Arrays and linked lists are fundamental data structures.', 1, 20),
(6, 2, 'Stacks and Queues', 'Stacks and queues are linear data structures with specific insertion and deletion rules.', 2, 15),
(7, 2, 'Trees and Graphs', 'Trees and graphs are non-linear data structures.', 3, 25)
ON CONFLICT (id) DO NOTHING;

-- Insert Lessons for Course 3
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(8, 3, 'Design Thinking', 'Design thinking is a human-centered approach to innovation and problem-solving.', 1, 15),
(9, 3, 'Wireframing', 'Wireframing is the process of creating a visual guide that represents the skeletal framework of a design.', 2, 20)
ON CONFLICT (id) DO NOTHING;

-- Insert Lessons for Course 4
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes) VALUES
(10, 4, 'Python Basics', 'Python is a versatile programming language widely used in data science.', 1, 20),
(11, 4, 'NumPy Fundamentals', 'NumPy is the fundamental package for scientific computing in Python.', 2, 25),
(12, 4, 'Pandas DataFrame', 'Pandas provides the DataFrame object, a powerful data structure for data manipulation and analysis.', 3, 30)
ON CONFLICT (id) DO NOTHING;

-- Insert Enrollments
INSERT INTO enrollments (id, student_id, course_id, progress, enrolled_at) VALUES
(1, 1, 1, 75, NOW()),
(2, 1, 2, 45, NOW()),
(3, 1, 4, 20, NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Quizzes (PostgreSQL syntax)
CREATE TABLE IF NOT EXISTS quizzes (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INT DEFAULT 30,
    total_questions INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE IF NOT EXISTS quiz_questions (
    id BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_option CHAR(1) NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Insert sample quizzes for each course
INSERT INTO quizzes (id, course_id, title, description, duration_minutes, total_questions) VALUES
(1, 1, 'HTML & CSS Basics Quiz', 'Test your knowledge of HTML and CSS fundamentals', 20, 5),
(2, 1, 'JavaScript Fundamentals Quiz', 'Test your JavaScript skills', 30, 5),
(3, 2, 'Data Structures Quiz', 'Test your understanding of data structures', 30, 5),
(4, 3, 'Design Principles Quiz', 'Test your UI/UX knowledge', 20, 5),
(5, 4, 'Python Basics Quiz', 'Test your Python fundamentals', 25, 5)
ON CONFLICT (id) DO NOTHING;

-- Insert quiz questions for Quiz 1 (HTML & CSS)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
(1, 1, 'What does HTML stand for?', 'Hyper Text Markup Language', 'High Tech Modern Language', 'Hyper Transfer Markup Language', 'Home Tool Markup Language', 'A'),
(2, 1, 'Which tag is used for the largest heading?', '<h6>', '<h1>', '<head>', '<heading>', 'B'),
(3, 1, 'Which CSS property changes text color?', 'font-color', 'text-color', 'color', 'foreground', 'C'),
(4, 1, 'Which HTML attribute specifies an image source?', 'href', 'src', 'link', 'url', 'B'),
(5, 1, 'Which CSS property controls spacing between elements?', 'spacing', 'margin', 'padding', 'border', 'B')
ON CONFLICT (id) DO NOTHING;

-- Insert quiz questions for Quiz 2 (JavaScript)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
(6, 2, 'Which keyword declares a variable in JavaScript?', 'var', 'let', 'const', 'All of the above', 'D'),
(7, 2, 'What is the result of typeof null?', 'null', 'undefined', 'object', 'number', 'C'),
(8, 2, 'Which method adds an element to the end of an array?', 'shift()', 'unshift()', 'push()', 'pop()', 'C'),
(9, 2, 'What does DOM stand for?', 'Document Object Model', 'Data Object Model', 'Document Oriented Model', 'Data Oriented Model', 'A'),
(10, 2, 'Which operator is used for strict equality?', '==', '===', '=', '!==', 'B')
ON CONFLICT (id) DO NOTHING;

-- Insert quiz questions for Quiz 3 (Data Structures)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
(11, 3, 'Which data structure uses FIFO?', 'Stack', 'Queue', 'Tree', 'Graph', 'B'),
(12, 3, 'What is the time complexity of binary search?', 'O(n)', 'O(log n)', 'O(n log n)', 'O(1)', 'B'),
(13, 3, 'Which data structure uses LIFO?', 'Queue', 'Stack', 'Array', 'Linked List', 'B'),
(14, 3, 'What is the worst case of quicksort?', 'O(n log n)', 'O(n)', 'O(n^2)', 'O(log n)', 'C'),
(15, 3, 'Which traversal visits root first?', 'Inorder', 'Preorder', 'Postorder', 'Level order', 'B')
ON CONFLICT (id) DO NOTHING;

-- Insert quiz questions for Quiz 4 (Design)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
(16, 4, 'What does UX stand for?', 'User Experience', 'User Extra', 'Universal Experience', 'User Extension', 'A'),
(17, 4, 'What is a wireframe?', 'A finished design', 'A skeletal framework', 'A color palette', 'A font selection', 'B'),
(18, 4, 'What is the primary goal of UI design?', 'Beauty', 'Usability', 'Speed', 'Complexity', 'B'),
(19, 4, 'What does prototyping help with?', 'Testing ideas', 'Writing code', 'Database design', 'Server setup', 'A'),
(20, 4, 'What is user research used for?', 'Understanding users', 'Writing code', 'Designing logos', 'Creating databases', 'A')
ON CONFLICT (id) DO NOTHING;

-- Insert quiz questions for Quiz 5 (Python)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
(21, 5, 'What is the output of print(type([]))?', 'list', 'tuple', 'dict', 'set', 'A'),
(22, 5, 'Which keyword defines a function?', 'function', 'def', 'func', 'define', 'B'),
(23, 5, 'What is a Python dictionary?', 'Ordered key-value pairs', 'Unordered key-value pairs', 'A list of tuples', 'A set of strings', 'B'),
(24, 5, 'Which library is used for data manipulation?', 'NumPy', 'Pandas', 'Matplotlib', 'Scikit-learn', 'B'),
(25, 5, 'What does len() return for a string?', 'First character', 'Last character', 'Length of string', 'Type of string', 'C')
ON CONFLICT (id) DO NOTHING;
