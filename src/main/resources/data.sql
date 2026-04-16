REPLACE INTO user (active, last_login, id, type, name, password, username)
VALUES (b'1', NULL, 1, 'adm', 'admin_1st', '$2a$12$Xfaog7ZIo3RfMpjm9dEk/Or9lqUpM1Tc6vM4zLpW62i0K79LKnYNK', 'admin');

REPLACE INTO genre (id, name) VALUES(1, 'action');
REPLACE INTO genre (id, name) VALUES(2, 'sci-fi');
REPLACE INTO genre (id, name) VALUES(3, 'thriller');
REPLACE INTO genre (id, name) VALUES(4, 'romance');
REPLACE INTO genre (id, name) VALUES(5, 'math');
REPLACE INTO genre (id, name) VALUES(6, 'art');
REPLACE INTO genre (id, name) VALUES(7, 'illustrative');
REPLACE INTO genre (id, name) VALUES(8, 'fantasy');