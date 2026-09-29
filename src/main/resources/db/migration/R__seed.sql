-- R__seed: Idempotent seed data. Uses INSERT ... SELECT ... WHERE NOT EXISTS
-- so it is safe to re-run and does not conflict with old production data.
-- All foreign keys are resolved by natural key (email, title, code) — no hardcoded IDs.

-- ============ USERS ============

-- Admin (upsert by email)
INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Platform Admin', 'admin@example.com', '$2b$10$SF4MN4HwiC5hNV8jaj4pKuUXJGR.jbkXILQeKZsRzMKwyWYNzO96S', 'ADMIN', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

-- Instructors
INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Dr. Sarah Chen', 'instructor1@example.com', '$2b$10$XFfK0JeWHVo5Ef5VbYgg5Oi/5oljbPUqTOEHVBhhQvr40UYgjxkaW', 'INSTRUCTOR', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Prof. James Miller', 'instructor2@example.com', '$2b$10$XFfK0JeWHVo5Ef5VbYgg5Oi/5oljbPUqTOEHVBhhQvr40UYgjxkaW', 'INSTRUCTOR', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Dr. Priya Sharma', 'instructor3@example.com', '$2b$10$XFfK0JeWHVo5Ef5VbYgg5Oi/5oljbPUqTOEHVBhhQvr40UYgjxkaW', 'INSTRUCTOR', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

-- Students
INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Alex Johnson', 'student1@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Maria Garcia', 'student2@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'David Kim', 'student3@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Emma Wilson', 'student4@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Liam Brown', 'student5@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Olivia Davis', 'student6@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Noah Martinez', 'student7@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Ava Anderson', 'student8@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Ethan Taylor', 'student9@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, password, role, created_at, email_verified)
SELECT 'Sophia Thomas', 'student10@example.com', '$2b$10$aaWGhSTD4BC2ixA0SxIg8OTBOQ6xwNAkJumm6dcD.1CTkhwLiU7VC', 'STUDENT', NOW(), TRUE
ON CONFLICT (email) DO NOTHING;

-- ============ COURSES ============

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'Introduction to Web Development', 'Learn HTML, CSS, and JavaScript from scratch. Build real projects and master the fundamentals of web development.', 'Web Development',
       (SELECT id FROM users WHERE email = 'instructor1@example.com' LIMIT 1),
       NOW(), 0, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Introduction to Web Development')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor1@example.com');

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'Data Structures & Algorithms', 'Master computer science fundamentals. Learn arrays, linked lists, trees, graphs, sorting, and searching with practical implementations.', 'Computer Science',
       (SELECT id FROM users WHERE email = 'instructor1@example.com' LIMIT 1),
       NOW(), 4999, 'PUBLISHED', 'INTERMEDIATE'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Data Structures & Algorithms')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor1@example.com');

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'UI/UX Design Fundamentals', 'Discover the principles of user interface and user experience design. Learn wireframing, prototyping, user research, and design thinking.', 'Design',
       (SELECT id FROM users WHERE email = 'instructor3@example.com' LIMIT 1),
       NOW(), 3999, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'UI/UX Design Fundamentals')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor3@example.com');

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'Python for Data Science', 'Explore data science with Python. Learn NumPy, Pandas, Matplotlib, and machine learning basics with real datasets.', 'Data Science',
       (SELECT id FROM users WHERE email = 'instructor3@example.com' LIMIT 1),
       NOW(), 5999, 'PUBLISHED', 'INTERMEDIATE'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Python for Data Science')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor3@example.com');

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'Machine Learning Basics', 'Introduction to machine learning concepts, algorithms, and practical applications using Python and scikit-learn.', 'Data Science',
       (SELECT id FROM users WHERE email = 'instructor1@example.com' LIMIT 1),
       NOW(), 6999, 'PUBLISHED', 'ADVANCED'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Machine Learning Basics')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor1@example.com');

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'Cloud Computing Essentials', 'Learn cloud computing fundamentals with AWS, Azure, and GCP. Understand IaaS, PaaS, SaaS, and deployment strategies.', 'Cloud',
       (SELECT id FROM users WHERE email = 'instructor3@example.com' LIMIT 1),
       NOW(), 0, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Cloud Computing Essentials')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor3@example.com');

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'Mobile App Development', 'Build mobile apps with React Native. Learn components, navigation, state management, and deployment to app stores.', 'Mobile Development',
       (SELECT id FROM users WHERE email = 'instructor1@example.com' LIMIT 1),
       NOW(), 4999, 'PUBLISHED', 'INTERMEDIATE'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Mobile App Development')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor1@example.com');

INSERT INTO courses (title, description, category, instructor_id, created_at, price, status, level)
SELECT 'Cybersecurity Fundamentals', 'Learn cybersecurity principles, threat analysis, encryption, and security best practices for modern applications.', 'Security',
       (SELECT id FROM users WHERE email = 'instructor3@example.com' LIMIT 1),
       NOW(), 5499, 'PUBLISHED', 'BEGINNER'
WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Cybersecurity Fundamentals')
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor3@example.com');

-- ============ LESSONS ============

-- Course 1: Web Development (free)
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'HTML Basics', 'HTML (HyperText Markup Language) is the standard markup language for creating web pages. It describes the structure of a web page using a series of elements.', 1, 15, TRUE, 'VIDEO', 'media/lesson1.mp4', 900, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Introduction to Web Development'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'HTML Basics' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'CSS Fundamentals', 'CSS (Cascading Style Sheets) is used to style and layout web pages. It controls colors, fonts, spacing, and positioning of elements.', 2, 20, FALSE, 'VIDEO', 'media/lesson2.mp4', 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Introduction to Web Development'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'CSS Fundamentals' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'JavaScript Essentials', 'JavaScript is a programming language that adds interactivity to web pages. It runs in the browser and can manipulate HTML and CSS dynamically.', 3, 25, FALSE, 'VIDEO', 'media/lesson3.mp4', 1500, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Introduction to Web Development'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'JavaScript Essentials' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Building a Web Page', 'Putting it all together! Build a complete web page using HTML, CSS, and JavaScript.', 4, 30, FALSE, 'VIDEO', 'media/lesson4.mp4', 1800, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Introduction to Web Development'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Building a Web Page' AND l.course_id = c.id);

-- Course 2: Data Structures
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Arrays and Linked Lists', 'Arrays and linked lists are fundamental data structures. Learn their properties, operations, and use cases.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Data Structures & Algorithms'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Arrays and Linked Lists' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Stacks and Queues', 'Stacks and queues are linear data structures with specific insertion and deletion rules.', 2, 15, FALSE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Data Structures & Algorithms'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Stacks and Queues' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Trees and Graphs', 'Trees and graphs are non-linear data structures used in many applications.', 3, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Data Structures & Algorithms'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Trees and Graphs' AND l.course_id = c.id);

-- Course 3: UI/UX Design
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Design Thinking', 'Design thinking is a human-centered approach to innovation and problem-solving.', 1, 15, TRUE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'UI/UX Design Fundamentals'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Design Thinking' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Wireframing', 'Wireframing is the process of creating a visual guide that represents the skeletal framework of a design.', 2, 20, FALSE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'UI/UX Design Fundamentals'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Wireframing' AND l.course_id = c.id);

-- Course 4: Python for Data Science
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Python Basics', 'Python is a versatile programming language widely used in data science.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Python for Data Science'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Python Basics' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'NumPy Fundamentals', 'NumPy is the fundamental package for scientific computing in Python.', 2, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Python for Data Science'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'NumPy Fundamentals' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Pandas DataFrame', 'Pandas provides the DataFrame object, a powerful data structure for data manipulation and analysis.', 3, 30, FALSE, 'VIDEO', NULL, 1800, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Python for Data Science'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Pandas DataFrame' AND l.course_id = c.id);

-- Course 5: Machine Learning
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Introduction to ML', 'What is machine learning? Supervised vs unsupervised learning, and real-world applications.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Machine Learning Basics'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Introduction to ML' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Linear Regression', 'Linear regression is a fundamental algorithm in machine learning for predicting continuous values.', 2, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Machine Learning Basics'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Linear Regression' AND l.course_id = c.id);

-- Course 6: Cloud Computing (free)
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Cloud Fundamentals', 'What is cloud computing? IaaS, PaaS, SaaS explained with real examples.', 1, 15, TRUE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Cloud Computing Essentials'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Cloud Fundamentals' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'AWS Core Services', 'Explore core AWS services: EC2, S3, Lambda, and more.', 2, 20, FALSE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Cloud Computing Essentials'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'AWS Core Services' AND l.course_id = c.id);

-- Course 7: Mobile Development
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'React Native Basics', 'Introduction to React Native for building cross-platform mobile apps.', 1, 20, TRUE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Mobile App Development'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'React Native Basics' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Navigation and State', 'Learn about navigation patterns and state management in React Native.', 2, 25, FALSE, 'VIDEO', NULL, 1500, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Mobile App Development'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Navigation and State' AND l.course_id = c.id);

-- Course 8: Cybersecurity
INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Security Fundamentals', 'Core cybersecurity principles: CIA triad, threat modeling, and risk assessment.', 1, 15, TRUE, 'VIDEO', NULL, 900, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Cybersecurity Fundamentals'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Security Fundamentals' AND l.course_id = c.id);

INSERT INTO lessons (course_id, title, content, order_index, duration_minutes, is_free_preview, media_type, media_path, media_duration, media_size, media_mime)
SELECT c.id, 'Encryption Basics', 'Symmetric and asymmetric encryption, hashing, and digital signatures.', 2, 20, FALSE, 'VIDEO', NULL, 1200, 1048576, 'video/mp4'
FROM courses c WHERE c.title = 'Cybersecurity Fundamentals'
AND NOT EXISTS (SELECT 1 FROM lessons l WHERE l.title = 'Encryption Basics' AND l.course_id = c.id);

-- ============ QUIZZES ============

INSERT INTO quizzes (course_id, title, description, duration_minutes, total_questions, pass_score, max_attempts, created_at)
SELECT c.id, 'HTML & CSS Basics Quiz', 'Test your knowledge of HTML and CSS fundamentals', 20, 5, 60, 0, NOW()
FROM courses c WHERE c.title = 'Introduction to Web Development'
AND NOT EXISTS (SELECT 1 FROM quizzes q WHERE q.title = 'HTML & CSS Basics Quiz' AND q.course_id = c.id);

INSERT INTO quizzes (course_id, title, description, duration_minutes, total_questions, pass_score, max_attempts, created_at)
SELECT c.id, 'Data Structures Quiz', 'Test your understanding of data structures', 30, 5, 60, 0, NOW()
FROM courses c WHERE c.title = 'Data Structures & Algorithms'
AND NOT EXISTS (SELECT 1 FROM quizzes q WHERE q.title = 'Data Structures Quiz' AND q.course_id = c.id);

INSERT INTO quizzes (course_id, title, description, duration_minutes, total_questions, pass_score, max_attempts, created_at)
SELECT c.id, 'Python Basics Quiz', 'Test your Python fundamentals', 25, 5, 60, 0, NOW()
FROM courses c WHERE c.title = 'Python for Data Science'
AND NOT EXISTS (SELECT 1 FROM quizzes q WHERE q.title = 'Python Basics Quiz' AND q.course_id = c.id);

-- Quiz questions for Quiz 1 (HTML & CSS)
INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'What does HTML stand for?', 'Hyper Text Markup Language', 'High Tech Modern Language', 'Hyper Transfer Markup Language', 'Home Tool Markup Language', 'A'
FROM quizzes q WHERE q.title = 'HTML & CSS Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'What does HTML stand for?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which tag is used for the largest heading?', '<h6>', '<h1>', '<head>', '<heading>', 'B'
FROM quizzes q WHERE q.title = 'HTML & CSS Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which tag is used for the largest heading?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which CSS property changes text color?', 'font-color', 'text-color', 'color', 'foreground', 'C'
FROM quizzes q WHERE q.title = 'HTML & CSS Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which CSS property changes text color?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which HTML attribute specifies an image source?', 'href', 'src', 'link', 'url', 'B'
FROM quizzes q WHERE q.title = 'HTML & CSS Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which HTML attribute specifies an image source?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which CSS property controls spacing between elements?', 'spacing', 'margin', 'padding', 'border', 'B'
FROM quizzes q WHERE q.title = 'HTML & CSS Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which CSS property controls spacing between elements?' AND qq.quiz_id = q.id);

-- Quiz questions for Quiz 2 (Data Structures)
INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which data structure uses FIFO?', 'Stack', 'Queue', 'Tree', 'Graph', 'B'
FROM quizzes q WHERE q.title = 'Data Structures Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which data structure uses FIFO?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'What is the time complexity of binary search?', 'O(n)', 'O(log n)', 'O(n log n)', 'O(1)', 'B'
FROM quizzes q WHERE q.title = 'Data Structures Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'What is the time complexity of binary search?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which data structure uses LIFO?', 'Queue', 'Stack', 'Array', 'Linked List', 'B'
FROM quizzes q WHERE q.title = 'Data Structures Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which data structure uses LIFO?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'What is the worst case of quicksort?', 'O(n log n)', 'O(n)', 'O(n^2)', 'O(log n)', 'C'
FROM quizzes q WHERE q.title = 'Data Structures Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'What is the worst case of quicksort?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which traversal visits root first?', 'Inorder', 'Preorder', 'Postorder', 'Level order', 'B'
FROM quizzes q WHERE q.title = 'Data Structures Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which traversal visits root first?' AND qq.quiz_id = q.id);

-- Quiz questions for Quiz 3 (Python)
INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'What is the output of print(type([]))?', 'list', 'tuple', 'dict', 'set', 'A'
FROM quizzes q WHERE q.title = 'Python Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'What is the output of print(type([]))?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which keyword defines a function?', 'function', 'def', 'func', 'define', 'B'
FROM quizzes q WHERE q.title = 'Python Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which keyword defines a function?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'What is a Python dictionary?', 'Ordered key-value pairs', 'Unordered key-value pairs', 'A list of tuples', 'A set of strings', 'B'
FROM quizzes q WHERE q.title = 'Python Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'What is a Python dictionary?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'Which library is used for data manipulation?', 'NumPy', 'Pandas', 'Matplotlib', 'Scikit-learn', 'B'
FROM quizzes q WHERE q.title = 'Python Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'Which library is used for data manipulation?' AND qq.quiz_id = q.id);

INSERT INTO quiz_questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
SELECT q.id, 'What does len() return for a string?', 'First character', 'Last character', 'Length of string', 'Type of string', 'C'
FROM quizzes q WHERE q.title = 'Python Basics Quiz'
AND NOT EXISTS (SELECT 1 FROM quiz_questions qq WHERE qq.question_text = 'What does len() return for a string?' AND qq.quiz_id = q.id);

-- ============ COUPONS ============

INSERT INTO coupons (code, description, discount_type, discount_value, max_uses, max_uses_per_user, min_order_amount, starts_at, expires_at, is_active, created_by, created_at)
SELECT 'WELCOME20', '20% off for new students', 'PERCENTAGE', 20, 100, 1, 0, NOW(), NOW() + INTERVAL '30 days', TRUE,
       (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
       NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'WELCOME20')
AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');

INSERT INTO coupons (code, description, discount_type, discount_value, max_uses, max_uses_per_user, min_order_amount, starts_at, expires_at, is_active, created_by, created_at)
SELECT 'FLAT100', 'Flat 100 off on orders above 500', 'FIXED', 100, 50, 1, 500, NOW(), NOW() + INTERVAL '60 days', TRUE,
       (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
       NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'FLAT100')
AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');

INSERT INTO coupons (code, description, discount_type, discount_value, max_uses, max_uses_per_user, min_order_amount, starts_at, expires_at, is_active, created_by, created_at)
SELECT 'SUMMER50', '50% off summer sale', 'PERCENTAGE', 50, 200, 2, 0, NOW(), NOW() + INTERVAL '15 days', TRUE,
       (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
       NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'SUMMER50')
AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');

-- ============ OFFERS ============

INSERT INTO offers (course_id, title, description, discount_percentage, starts_at, ends_at, status, created_by, created_at)
SELECT c.id, 'DSA Early Bird', 'Early bird discount for DSA course', 20, NOW(), NOW() + INTERVAL '14 days', 'APPROVED',
       (SELECT id FROM users WHERE email = 'instructor1@example.com' LIMIT 1),
       NOW()
FROM courses c WHERE c.title = 'Data Structures & Algorithms'
AND NOT EXISTS (SELECT 1 FROM offers o WHERE o.title = 'DSA Early Bird' AND o.course_id = c.id)
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor1@example.com');

INSERT INTO offers (course_id, title, description, discount_percentage, starts_at, ends_at, status, created_by, created_at)
SELECT c.id, 'Python Launch Offer', 'Special launch pricing for Python course', 15, NOW(), NOW() + INTERVAL '7 days', 'APPROVED',
       (SELECT id FROM users WHERE email = 'instructor3@example.com' LIMIT 1),
       NOW()
FROM courses c WHERE c.title = 'Python for Data Science'
AND NOT EXISTS (SELECT 1 FROM offers o WHERE o.title = 'Python Launch Offer' AND o.course_id = c.id)
AND EXISTS (SELECT 1 FROM users WHERE email = 'instructor3@example.com');

-- ============ ADS ============

INSERT INTO ads (title, image_url, target_url, placement_slot, target_role, starts_at, ends_at, is_active, created_by, created_at)
SELECT 'Summer Sale - 50% Off', NULL, '/courses', 'DASHBOARD_HERO', NULL, NOW(), NOW() + INTERVAL '30 days', TRUE,
       (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
       NOW()
WHERE NOT EXISTS (SELECT 1 FROM ads WHERE title = 'Summer Sale - 50% Off')
AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');

INSERT INTO ads (title, image_url, target_url, placement_slot, target_role, starts_at, ends_at, is_active, created_by, created_at)
SELECT 'New ML Course', NULL, '/courses/1005', 'CATALOG_INLINE', NULL, NOW(), NOW() + INTERVAL '14 days', TRUE,
       (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
       NOW()
WHERE NOT EXISTS (SELECT 1 FROM ads WHERE title = 'New ML Course')
AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');

INSERT INTO ads (title, image_url, target_url, placement_slot, target_role, starts_at, ends_at, is_active, created_by, created_at)
SELECT 'Instructor Program', NULL, '/signup', 'SIDEBAR', 'STUDENT', NOW(), NOW() + INTERVAL '60 days', TRUE,
       (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
       NOW()
WHERE NOT EXISTS (SELECT 1 FROM ads WHERE title = 'Instructor Program')
AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');

-- ============ NOTIFICATIONS ============

INSERT INTO notifications (user_id, type, title, body, link, is_read, created_at)
SELECT u.id, 'ANNOUNCEMENT', 'Welcome to E-Learning', 'Explore our new platform features and courses.', '/courses', FALSE, NOW()
FROM users u WHERE u.email = 'admin@example.com'
AND NOT EXISTS (SELECT 1 FROM notifications n WHERE n.title = 'Welcome to E-Learning' AND n.user_id = u.id);

INSERT INTO notifications (user_id, type, title, body, link, is_read, created_at)
SELECT u.id, 'ENROLLMENT', 'Welcome to Web Development', 'You are enrolled in Introduction to Web Development.', '/courses', FALSE, NOW()
FROM users u WHERE u.email = 'student1@example.com'
AND NOT EXISTS (SELECT 1 FROM notifications n WHERE n.title = 'Welcome to Web Development' AND n.user_id = u.id);

-- ============ ANNOUNCEMENTS ============

INSERT INTO announcements (title, body, target_role, created_by, created_at)
SELECT 'Platform Update', 'We have new courses and features available. Check them out!', NULL,
       (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
       NOW()
WHERE NOT EXISTS (SELECT 1 FROM announcements WHERE title = 'Platform Update')
AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');

-- ============ NOTIFICATION PREFERENCES ============

INSERT INTO notification_preferences (user_id, email_on_enrollment, email_on_payment, email_on_course_update, email_on_announcement, push_enabled)
SELECT u.id, TRUE, TRUE, TRUE, TRUE, TRUE FROM users u
WHERE NOT EXISTS (SELECT 1 FROM notification_preferences np WHERE np.user_id = u.id);
