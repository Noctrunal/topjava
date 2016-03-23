DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 1, 'Breakfast', 500, '2015-05-30 10:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 2, 'Lunch', 1000, '2015-05-30 13:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 3, 'Dinner', 500, '2015-05-30 20:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 4, 'Breakfast', 500, '2015-05-31 10:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 5, 'Lunch', 1000, '2015-05-31 13:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 6, 'Dinner', 510, '2015-05-31 20:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 7, 'Admin Breakfast', 500, '2015-05-30 10:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 8, 'Admin Lunch', 1000, '2015-05-30 13:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 9, 'Admin Dinner', 500, '2015-05-30 20:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 10, 'Admin Breakfast', 500, '2015-05-31 10:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 11, 'Admin Lunch', 1000, '2015-05-31 13:00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 12, 'Admin Dinner', 510, '2015-05-31 20:00:00');