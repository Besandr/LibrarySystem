INSERT INTO library_system.author (first_name, last_name) VALUES 
('Bryan', 'Basham'),
('Kathy', 'Sierra'),
('Bert', 'Bates'),
('Brian', 'Goetz'),
('Joshua', 'Bloch'),
('Harry', 'Harrison'),
('Александр', 'Пушкин'),
('Михайло', 'Коцюбинський');

INSERT INTO library_system.book (title, year, description) VALUES
('Servlets&JSP', 2008, "This book will get you way up to speed on the technology you'll know it so well, in fact, that you can pass the brand new J2EE 1.5 exam. If that's what you want to do, that is. Maybe you don't care about the exam, but need to use servlets and JSPs in your next project. You're working on a deadline. You're over the legal limit for caffeine. You can't waste your time with a book that makes sense only AFTER you're an expert (or worse, one that puts you to sleep)."),
('Java Concurrency in Practice', 2010, "Threads are a fundamental part of the Java platform. As multicore processors become the norm, using concurrency effectively becomes essential for building high-performance applications. Java SE 5 and 6 are a huge step forward for the development of concurrent applications, with improvements to the Java Virtual Machine to support high-performance, highly scalable concurrent classes and a rich set of new concurrency building blocks."),
('Effective Java', 2018, "The Definitive Guide to Java Platform Best Practices–Updated for Java 7, 8, and 9"),
('The Stainless Steel Rat', 1994, "In the vastness of space, the crimes just get bigger and Slippery Jim diGriz, the Stainless Steel Rat, is the biggest criminal of them all. He can con humans, aliens and any number of robots time after time. Jim is so slippery that all the inter-galactic cops can do is make him one of their own"),
('Капитанская дочка', 2002, '"Капитанская дочка" - роман, в котором удивительным образом сплелись исторические факты, фольклор, предания и рассказы очевидцев пугачевского восстания и авторский вымысел. Знакомство с героями романа - путь к познанию русского национального характера - его силы и слабости, наивности и величественности, вспыльчивости и великодушия.'),
('Тіні забутих предків', 2012, '“Тіні забутих предків” Михайла Коцюбинського (1864-1913) - шедевр української літератури. Серед таємничої природи Карпат розгортається історія кохання юних Івана та Марічки. Ні ворожість сімей, ні життєві випробування, ні навіть смерть не змогли стати на перешкоді цьому почуттю.');

INSERT INTO library_system.author_book (author_id, book_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 2),
(5, 2),
(5, 3),
(6, 4),
(7, 5),
(8, 6);

INSERT INTO library_system.keyword (word) VALUES
('IT'),
('Java'),
('English'),
('Русский'),
('Українська');

INSERT INTO library_system.book_keyword (book_id, keyword_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(1, 2),
(2, 2),
(3, 2),
(1, 3),
(2, 3),
(3, 3),
(4, 3),
(5, 4),
(6, 5);

INSERT INTO library_system.bookcase (shelf_quantity, cell_quantity) value (2, 4);

INSERT INTO library_system.location (bookcase_id, shelf_number, cell_number) VALUES
(1, 1, 1),
(1, 1, 2),
(1, 1, 3),
(1, 1, 4),
(1, 2, 1),
(1, 2, 2),
(1, 2, 3),
(1, 2, 4);

UPDATE library_system.location SET book_id = 1, is_occupied = true WHERE location_id = 1;
UPDATE library_system.location SET book_id = 1, is_occupied = false WHERE location_id = 2;
UPDATE library_system.location SET book_id = 2, is_occupied = true WHERE location_id = 3;
UPDATE library_system.location SET book_id = 2, is_occupied = true WHERE location_id = 4;
UPDATE library_system.location SET book_id = 3, is_occupied = true WHERE location_id = 5;
UPDATE library_system.location SET book_id = 4, is_occupied = true WHERE location_id = 6;
UPDATE library_system.location SET book_id = 5, is_occupied = true WHERE location_id = 7;
UPDATE library_system.location SET book_id = 6, is_occupied = true WHERE location_id = 8;

INSERT INTO library_system.role (name) VALUES ('Administrator'), ('Borrower');

/*Passwords are hashed. Admin password is "asdf", users password is "123"*/
INSERT INTO library_system.user (email, password, phone, first_name, last_name, role_id) VALUES
('guru@gmail.com', 'tO��IX���Wس�(�f�L�h��y�{�83�+�m.!�Gj�]ȟ)��X�7�[q��#��', '+49257367367', 'Linus', 'Torvalds', '1'),
('jekie@gmail.com', 'm��=/箠s�yI��ɾ�MѦ��F)��!��%�:�f��?|�nUއUqrgu��Z��ď�-w�', '+380501112233', 'Evgeniy', 'Kovalchuk', 2),
('evil_user@mail.ru', 'm��=/箠s�yI��ɾ�MѦ��F)��!��%�:�f��?|�nUއUqrgu��Z��ď�-w�', '+380631234567', 'Tomas', 'Smith', 2),
('sweetie@yahoo.com', 'm��=/箠s�yI��ɾ�MѦ��F)��!��%�:�f��?|�nUއUqrgu��Z��ď�-w�', '+380675557777', 'Nastya', 'Stremilova', 2);

UPDATE library_system.user SET karma = 2 WHERE user_id = 3;

INSERT INTO library_system.loan (book_id, user_id, apply_date, loan_date, expired_date, return_date) VALUES
(3, 4, '2019-03-07', '2019-03-07', '2019-04-07', '2019-04-03'),
(6, 3, '2019-02-14', '2019-02-14', '2019-03-14', '2019-04-14'),
(1, 3, SUBDATE(CURDATE(), INTERVAL 10 DAY), SUBDATE(CURDATE(), INTERVAL 10 DAY), ADDDATE(CURDATE(), INTERVAL 20 DAY), NULL),
(1, 2, curdate(), NULL, NULL, NULL),
(2, 4, curdate(), NULL, NULL, NULL);
