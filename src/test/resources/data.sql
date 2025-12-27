INSERT INTO users (email, name) VALUES ('user1@example.com', 'User 1');
INSERT INTO users (email, name) VALUES ('user2@example.com', 'User 2');

INSERT INTO items (user_id, url, title, unread, date_resolved) VALUES (1, 'https://example.com/item1', 'Item 1', true, '2023-01-01');
INSERT INTO items (user_id, url, title, unread, date_resolved) VALUES (1, 'https://example.com/item2', 'Item 2', true, '2023-01-02');
INSERT INTO items (user_id, url, title, unread, date_resolved) VALUES (2, 'https://example.com/item3', 'Item 3', true, '2023-01-03');
