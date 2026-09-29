-- R__seed: Idempotent seed data. Uses INSERT ... SELECT ... WHERE NOT EXISTS
-- so it is safe to re-run and does not conflict with old production data.
-- New seed IDs start at 1000 to avoid clashing with old IDs (1-25).

-- ============ USERS ============

-- Admin (upsert by email)
INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1001, 'Platform Admin', 'admin@example.com', '$2b$10$SF4MN4HwiC5hNV8jaj4pKuUXJGR.jbkXILQeKZsRzMKwyWYNzO96S', 'ADMIN', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');

-- Instructors
INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1002, 'Dr. Sarah Chen', 'instructor1@example.com', '$2b$10$XFfK0JeWHVo5Ef5VbYgg5Oi/5oljbPUqTOEHVBhhQvr40UYgjxkaW', 'INSTRUCTOR', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor1@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1003, 'Prof. James Miller', 'instructor2@example.com', '$2b$10$XFfK0JeWHVo5Ef5VbYgg5Oi/5oljbPUqTOEHVBhhQvr40UYgjxkaW', 'INSTRUCTOR', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor2@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1004, 'Dr. Priya Sharma', 'instructor3@example.com', '$2b$10$XFfK0JeWHVo5Ef5VbYgg5Oi/5oljbPUqTOEHVBhhQvr40UYgjxkaW', 'INSTRUCTOR', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor3@example.com');

-- Students
INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1011, 'Alex Johnson', 'student1@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student1@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1012, 'Maria Garcia', 'student2@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student2@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1013, 'David Kim', 'student3@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student3@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1014, 'Emma Wilson', 'student4@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student4@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1015, 'Liam Brown', 'student5@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student5@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1016, 'Olivia Davis', 'student6@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student6@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1017, 'Noah Martinez', 'student7@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student7@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1018, 'Ava Anderson', 'student8@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student8@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1019, 'Ethan Taylor', 'student9@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student9@example.com');

INSERT INTO users (id, name, email, password, role, created_at, email_verified)
SELECT 1020, 'Sophia Thomas', 'student10@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student10@example.com');

-- ============ COURSES ============

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1001, 'Introduction to Web Development', 'Learn HTML, CSS, and JavaScript from scratch. Build real projects and master the fundamentals of web development.', 'Web Development', 1002, NOW(), 0, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1001);

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1002, 'Data Structures & Algorithms', 'Master computer science fundamentals. Learn arrays, linked lists, trees, graphs, sorting, and searching with practical implementations.', 'Computer Science', 1002, NOW(), 4999, 'PUBLISHED', 'INTERMEDIATE'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1002);

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1003, 'UI/UX Design Fundamentals', 'Discover the principles of user interface and user experience design. Learn wireframing, prototyping, user research, and design thinking.', 'Design', 1003, NOW(), 3999, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1003);

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1004, 'Python for Data Science', 'Explore data science with Python. Learn NumPy, Pandas, Matplotlib, and machine learning basics with real datasets.', 'Data Science', 1003, NOW(), 5999, 'PUBLISHED', 'INTERMEDIATE'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1004);

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1005, 'Machine Learning Basics', 'Introduction to machine learning concepts, algorithms, and practical applications using Python and scikit-learn.', 'Data Science', 1002, NOW(), 6999, 'PUBLISHED', 'ADVANCED'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1005);

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1006, 'Cloud Computing Essentials', 'Learn cloud computing fundamentals with AWS, Azure, and GCP. Understand IaaS, PaaS, SaaS, and deployment strategies.', 'Cloud', 1003, NOW(), 0, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1006);

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1007, 'Mobile App Development', 'Build mobile apps with React Native. Learn components, navigation, state management, and deployment to app stores.', 'Mobile Development', 1002, NOW(), 4999, 'PUBLISHED', 'INTERMEDIATE'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1007);

INSERT INTO courses (id, title, description, category, instructor_id, created_at, price, status, level)
SELECT 1008, 'Cybersecurity Fundamentals', 'Learn cybersecurity principles, threat analysis, encryption, and security best practices for modern applications.', 'Security', 1003, NOW(), 5499, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE id = 1008);

-- ============ LESSONS ============

-- Course 1: Web Development (free)
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1001, 1001, 'HTML Basics', 'HTML (HyperText Markup Language) is the standard markup language for creating web pages. It describes the structure of a web page using a series of elements.', 1, 15, TRUE, 'VIDEO', 'media/lesson1.mp4', 900, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1001);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1002, 1001, 'CSS Fundamentals', 'CSS (Cascading Style Sheets) is used to style and layout web pages. It controls colors, fonts, spacing, and positioning of elements.', 2, 20, FALSE, 'VIDEO', 'media/lesson2.mp4', 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1002);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1003, 1001, 'JavaScript Essentials', 'JavaScript is a programming language that adds interactivity to web pages. It runs in the browser and can manipulate HTML and CSS dynamically.', 3, 25, FALSE, 'VIDEO', 'media/lesson3.mp4', 1500, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1003);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1004, 1001, 'Building a Web Page', 'Putting it all together! Build a complete web page using HTML, CSS, and JavaScript.', 4, 30, FALSE, 'VIDEO', 'media/lesson4.mp4', 1800, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1004);

-- Course 2: Data Structures
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1005, 1002, 'Arrays and Linked Lists', 'Arrays and linked lists are fundamental data structures. Learn their properties, operations, and use cases.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1005);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1006, 1002, 'Stacks and Queues', 'Stacks and queues are linear data structures with specific insertion and deletion rules.', 2, 15, FALSE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1006);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1007, 1002, 'Trees and Graphs', 'Trees and graphs are non-linear data structures used in many applications.', 3, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1007);

-- Course 3: UI/UX Design
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1008, 1003, 'Design Thinking', 'Design thinking is a human-centered approach to innovation and problem-solving.', 1, 15, TRUE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1008);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1009, 1003, 'Wireframing', 'Wireframing is the process of creating a visual guide that represents the skeletal framework of a design.', 2, 20, FALSE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1009);

-- Course 4: Python for Data Science
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1010, 1004, 'Python Basics', 'Python is a versatile programming language widely used in data science.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1010);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1011, 1004, 'NumPy Fundamentals', 'NumPy is the fundamental package for scientific computing in Python.', 2, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1011);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1012, 1004, 'Pandas DataFrame', 'Pandas provides the DataFrame object, a powerful data structure for data manipulation and analysis.', 3, 30, FALSE, 'VIDEO', NULL, 1800, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1012);

-- Course 5: Machine Learning
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1013, 1005, 'Introduction to ML', 'What is machine learning? Supervised vs unsupervised learning, and real-world applications.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1013);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1014, 1005, 'Linear Regression', 'Linear regression is a fundamental algorithm in machine learning for predicting continuous values.', 2, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1014);

-- Course 6: Cloud Computing (free)
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1015, 1006, 'Cloud Fundamentals', 'What is cloud computing? IaaS, PaaS, SaaS explained with real examples.', 1, 15, TRUE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1015);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1016, 1006, 'AWS Core Services', 'Explore core AWS services: EC2, S3, Lambda, and more.', 2, 20, FALSE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1016);

-- Course 7: Mobile Development
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1017, 1007, 'React Native Basics', 'Introduction to React Native for building cross-platform mobile apps.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1017);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1018, 1007, 'Navigation and State', 'Learn about navigation patterns and state management in React Native.', 2, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1018);

-- Course 8: Cybersecurity
INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1019, 1008, 'Security Fundamentals', 'Core cybersecurity principles: CIA triad, threat modeling, and risk assessment.', 1, 15, TRUE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1019);

INSERT INTO lessons (id, course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT 1020, 1008, 'Encryption Basics', 'Symmetric and asymmetric encryption, hashing, and digital signatures.', 2, 20, FALSE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE id = 1020);

-- ============ QUIZZES ============

INSERT INTO quizzes (id, course_id, title, description, duration_minutes, total_questions, pass_score, max_attempts, created_at)
SELECT 1001, 1001, 'HTML & CSS Basics Quiz', 'Test your knowledge of HTML and CSS fundamentals', 20, 5, 60, 0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM quizzes WHERE id = 1001);

INSERT INTO quizzes (id, course_id, title, description, duration_minutes, total_questions, pass_score, max_attempts, created_at)
SELECT 1002, 1002, 'Data Structures Quiz', 'Test your understanding of data structures', 30, 5, 60, 0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM quizzes WHERE id = 1002);

INSERT INTO quizzes (id, course_id, title, description, duration_minutes, total_questions, pass_score, max_attempts, created_at)
SELECT 1003, 1004, 'Python Basics Quiz', 'Test your Python fundamentals', 25, 5, 60, 0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM quizzes WHERE id = 1003);

-- Quiz questions for Quiz 1 (HTML & CSS)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1001, 1001, 'What does HTML stand for?', 'Hyper Text Markup Language', 'High Tech Modern Language', 'Hyper Transfer Markup Language', 'Home Tool Markup Language', 'A'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1001);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1002, 1001, 'Which tag is used for the largest heading?', '<h6>', '<h1>', '<head>', '<heading>', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1002);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1003, 1001, 'Which CSS property changes text color?', 'font-color', 'text-color', 'color', 'foreground', 'C'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1003);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1004, 1001, 'Which HTML attribute specifies an image source?', 'href', 'src', 'link', 'url', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1004);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1005, 1001, 'Which CSS property controls spacing between elements?', 'spacing', 'margin', 'padding', 'border', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1005);

-- Quiz questions for Quiz 2 (Data Structures)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1006, 1002, 'Which data structure uses FIFO?', 'Stack', 'Queue', 'Tree', 'Graph', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1006);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1007, 1002, 'What is the time complexity of binary search?', 'O(n)', 'O(log n)', 'O(n log n)', 'O(1)', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1007);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1008, 1002, 'Which data structure uses LIFO?', 'Queue', 'Stack', 'Array', 'Linked List', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1008);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1009, 1002, 'What is the worst case of quicksort?', 'O(n log n)', 'O(n)', 'O(n^2)', 'O(log n)', 'C'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1009);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1010, 1002, 'Which traversal visits root first?', 'Inorder', 'Preorder', 'Postorder', 'Level order', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1010);

-- Quiz questions for Quiz 3 (Python)
INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1011, 1003, 'What is the output of print(type([]))?', 'list', 'tuple', 'dict', 'set', 'A'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1011);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1012, 1003, 'Which keyword defines a function?', 'function', 'def', 'func', 'define', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1012);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1013, 1003, 'What is a Python dictionary?', 'Ordered key-value pairs', 'Unordered key-value pairs', 'A list of tuples', 'A set of strings', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1013);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1014, 1003, 'Which library is used for data manipulation?', 'NumPy', 'Pandas', 'Matplotlib', 'Scikit-learn', 'B'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1014);

INSERT INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT 1015, 1003, 'What does len() return for a string?', 'First character', 'Last character', 'Length of string', 'Type of string', 'C'
WHERE NOT EXISTS (SELECT 1 FROM quiz_questions WHERE id = 1015);

-- ============ COUPONS ============

INSERT INTO coupons (id, code, description, discount_type, discount_value, max_uses, max_uses_per_user, min_order_amount, starts_at, expires_at, is_active, created_by, created_at)
SELECT 1001, 'WELCOME20', '20% off for new students', 'PERCENTAGE', 20, 100, 1, 0, NOW(), NOW() + INTERVAL '30 days', TRUE, 1001, NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'WELCOME20');

INSERT INTO coupons (id, code, description, discount_type, discount_value, max_uses, max_uses_per_user, min_order_amount, starts_at, expires_at, is_active, created_by, created_at)
SELECT 1002, 'FLAT100', 'Flat 100 off on orders above 500', 'FIXED', 100, 50, 1, 500, NOW(), NOW() + INTERVAL '60 days', TRUE, 1001, NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'FLAT100');

INSERT INTO coupons (id, code, description, discount_type, discount_value, max_uses, max_uses_per_user, min_order_amount, starts_at, expires_at, is_active, created_by, created_at)
SELECT 1003, 'SUMMER50', '50% off summer sale', 'PERCENTAGE', 50, 200, 2, 0, NOW(), NOW() + INTERVAL '15 days', TRUE, 1001, NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'SUMMER50');

-- ============ OFFERS ============

INSERT INTO offers (id, course_id, title, description, discount_percentage, starts_at, ends_at, status, created_by, created_at)
SELECT 1001, 1002, 'DSA Early Bird', 'Early bird discount for DSA course', 20, NOW(), NOW() + INTERVAL '14 days', 'APPROVED', 1002, NOW()
WHERE NOT EXISTS (SELECT 1 FROM offers WHERE id = 1001);

INSERT INTO offers (id, course_id, title, description, discount_percentage, starts_at, ends_at, status, created_by, created_at)
SELECT 1002, 1004, 'Python Launch Offer', 'Special launch pricing for Python course', 15, NOW(), NOW() + INTERVAL '7 days', 'APPROVED', 1003, NOW()
WHERE NOT EXISTS (SELECT 1 FROM offers WHERE id = 1002);

-- ============ ADS ============

INSERT INTO ads (id, title, image_url, target_url, placement_slot, target_role, starts_at, ends_at, is_active, created_by, created_at)
SELECT 1001, 'Summer Sale - 50% Off', NULL, '/courses', 'DASHBOARD_HERO', NULL, NOW(), NOW() + INTERVAL '30 days', TRUE, 1001, NOW()
WHERE NOT EXISTS (SELECT 1 FROM ads WHERE id = 1001);

INSERT INTO ads (id, title, image_url, target_url, placement_slot, target_role, starts_at, ends_at, is_active, created_by, created_at)
SELECT 1002, 'New ML Course', NULL, '/courses/1005', 'CATALOG_INLINE', NULL, NOW(), NOW() + INTERVAL '14 days', TRUE, 1001, NOW()
WHERE NOT EXISTS (SELECT 1 FROM ads WHERE id = 1002);

INSERT INTO ads (id, title, image_url, target_url, placement_slot, target_role, starts_at, ends_at, is_active, created_by, created_at)
SELECT 1003, 'Instructor Program', NULL, '/signup', 'SIDEBAR', 'STUDENT', NOW(), NOW() + INTERVAL '60 days', TRUE, 1001, NOW()
WHERE NOT EXISTS (SELECT 1 FROM ads WHERE id = 1003);

-- ============ NOTIFICATIONS ============

INSERT INTO notifications (id, user_id, type, title, body, link, is_read, created_at)
SELECT 1001, 1001, 'ANNOUNCEMENT', 'Welcome to E-Learning', 'Explore our new platform features and courses.', '/courses', FALSE, NOW()
WHERE NOT EXISTS (SELECT 1 FROM notifications WHERE id = 1001);

INSERT INTO notifications (id, user_id, type, title, body, link, is_read, created_at)
SELECT 1002, 1011, 'ENROLLMENT', 'Welcome to Web Development', 'You are enrolled in Introduction to Web Development.', '/courses/1001', FALSE, NOW()
WHERE NOT EXISTS (SELECT 1 FROM notifications WHERE id = 1002);

-- ============ ANNOUNCEMENTS ============

INSERT INTO announcements (id, title, body, target_role, created_by, created_at)
SELECT 1001, 'Platform Update', 'We have new courses and features available. Check them out!', NULL, 1001, NOW()
WHERE NOT EXISTS (SELECT 1 FROM announcements WHERE id = 1001);

-- ============ NOTIFICATION PREFERENCES ============

INSERT INTO notification_preferences (user_id, email_on_enrollment, email_on_payment, email_on_course_update, email_on_announcement, push_enabled)
SELECT u.id, TRUE, TRUE, TRUE, TRUE, TRUE FROM users u
WHERE u.id >= 1001
AND NOT EXISTS (SELECT 1 FROM notification_preferences np WHERE np.user_id = u.id);
