INSERT IGNORE INTO user (active, last_login, id, type, name, password, username)
VALUES (b'1', NULL, 1, 'adm', 'admin_1st', '$2a$12$Xfaog7ZIo3RfMpjm9dEk/Or9lqUpM1Tc6vM4zLpW62i0K79LKnYNK', 'admin');

INSERT IGNORE INTO genre (id, name) VALUES(1, 'action');
INSERT IGNORE INTO genre (id, name) VALUES(2, 'sci-fi');
INSERT IGNORE INTO genre (id, name) VALUES(3, 'thriller');
INSERT IGNORE INTO genre (id, name) VALUES(4, 'romance');
INSERT IGNORE INTO genre (id, name) VALUES(5, 'math');
INSERT IGNORE INTO genre (id, name) VALUES(6, 'art');
INSERT IGNORE INTO genre (id, name) VALUES(7, 'illustrative');
INSERT IGNORE INTO genre (id, name) VALUES(8, 'fantasy');