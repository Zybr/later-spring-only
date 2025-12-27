INSERT INTO users (email, name) VALUES ('user1@example.com', 'User One');
INSERT INTO users (email, name) VALUES ('user2@example.com', 'User Two');

INSERT INTO items (user_id, url, resolved_url, mime_type, title, has_image, has_video, date_resolved) VALUES (1, 'https://example.com/item1', 'https://example.com/item1', 'text/html', 'Example Item 1', false, false, '2024-01-01');
INSERT INTO items (user_id, url, resolved_url, mime_type, title, has_image, has_video, date_resolved) VALUES (1, 'https://example.com/item2', 'https://example.com/item2', 'text/html', 'Example Item 2', false, false, '2024-01-02');
INSERT INTO items (user_id, url, resolved_url, mime_type, title, has_image, has_video, date_resolved) VALUES (2, 'https://example.com/item3', 'https://example.com/item3', 'text/html', 'Example Item 3', false, false, '2024-01-03');