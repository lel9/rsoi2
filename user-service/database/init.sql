DROP DATABASE user_service;

CREATE DATABASE user_service WITH TEMPLATE = template0 ENCODING = 'UTF8';

CREATE USER testadmin with encrypted password 'testadmin';

GRANT ALL PRIVILEGES on database user_service to testadmin;

\connect user_service testadmin

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE person_data (
  id UUID PRIMARY KEY NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  birthday DATE,
  avatar VARCHAR(150)
);

INSERT INTO person_data (id, first_name, last_name) VALUES ('e22e6ce8-406e-4618-9d9e-44114ac13697', 'admin', 'admin');
INSERT INTO person_data (id, first_name, last_name) VALUES ('833f9d04-9d81-4983-9dae-b69a89d9efe7', 'admin2', 'admin2');
INSERT INTO person_data (id, first_name, last_name) VALUES ('b040df7e-5d36-4c08-9252-521a5461485c', 'admin3', 'admin3');
INSERT INTO person_data (id, first_name, last_name) VALUES ('9a932257-bd92-4e87-ade0-046006fe134f', 'user', 'user');
INSERT INTO person_data (id, first_name, last_name) VALUES ('7be76d3f-ccac-42f8-9ded-e09ad7fef5d7', 'user2', 'user2');
INSERT INTO person_data (id, first_name, last_name) VALUES ('74657632-ea59-4183-932d-2e57bbd4e9e9', 'user3', 'user3');


CREATE TABLE users (
  id UUID PRIMARY KEY NOT NULL,
  person_id UUID NOT NULL REFERENCES person_data(id),
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL
);

INSERT INTO users (id, person_id, username, email) VALUES ('12412cdb-398f-4def-9cec-325b11968b56', 'e22e6ce8-406e-4618-9d9e-44114ac13697', 'admin', 'admin');
INSERT INTO users (id, person_id, username, email) VALUES ('7c803c41-ca5f-4e66-9483-7e361db72917', '833f9d04-9d81-4983-9dae-b69a89d9efe7', 'admin2', 'admin2');
INSERT INTO users (id, person_id, username, email) VALUES ('a4c7eb02-1c79-48df-a1a7-7701bb500dcf', 'b040df7e-5d36-4c08-9252-521a5461485c', 'admin3', 'admin3');
INSERT INTO users (id, person_id, username, email) VALUES ('be7b8ae6-5368-40f1-9b07-b33027acf43f', '9a932257-bd92-4e87-ade0-046006fe134f', 'user', 'user');
INSERT INTO users (id, person_id, username, email) VALUES ('ecc4f8af-c758-434a-84c1-6df04cbb0936', '7be76d3f-ccac-42f8-9ded-e09ad7fef5d7', 'user2', 'user2');
INSERT INTO users (id, person_id, username, email) VALUES ('0d984722-ba7f-4b57-9afb-3078191f2a01', '74657632-ea59-4183-932d-2e57bbd4e9e9', 'user3', 'user3');
