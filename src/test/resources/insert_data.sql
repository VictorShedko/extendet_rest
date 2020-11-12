
INSERT INTO hibernate_sequence (next_val) VALUES (16);

INSERT INTO tag (id, name) VALUES (9, 'spa');
INSERT INTO tag (id, name) VALUES (10, 'massage');
INSERT INTO tag (id, name) VALUES (11, 'sauna');
INSERT INTO tag (id, name) VALUES (12, 'hamam');
INSERT INTO tag (id, name) VALUES (13, 'rest');
INSERT INTO tag (id, name) VALUES (14, 'perfume');
INSERT INTO tag (id, name) VALUES (15, 'cruise');

INSERT INTO user (user_id, name) VALUES (3, 'Borya');
INSERT INTO user (user_id, name) VALUES (8, 'Kirill');
INSERT INTO user (user_id, name) VALUES (4, 'Liza');
INSERT INTO user (user_id, name) VALUES (1, 'Vasya');

INSERT INTO certificate (id, creation_time, description, duration, name, price, update_time, user_id) VALUES (2, null, 'certificate holder can get sale', 50, 'parfume by gucci', 25, null, 1);
INSERT INTO certificate (id, creation_time, description, duration, name, price, update_time, user_id) VALUES (5, null, 'certificate holder can get sale', 50, 'parfume by dolce', 25, null, 4);
INSERT INTO certificate (id, creation_time, description, duration, name, price, update_time, user_id) VALUES (6, null, 'certificate holder can get sale', 50, 'spa massage', 25, null, 4);
INSERT INTO certificate (id, creation_time, description, duration, name, price, update_time, user_id) VALUES (7, null, 'certificate holder can get sale', 50, 'spa sayna', 25, null, 4);

INSERT INTO certificate_tags (certificate_id, tags_id) VALUES (5, 9);
INSERT INTO certificate_tags (certificate_id, tags_id) VALUES (5, 10);
INSERT INTO certificate_tags (certificate_id, tags_id) VALUES (2, 14);
INSERT INTO certificate_tags (certificate_id, tags_id) VALUES (6, 10);