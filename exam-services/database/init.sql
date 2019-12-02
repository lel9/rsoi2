DROP DATABASE exam_service;

CREATE DATABASE exam_service WITH TEMPLATE = template0 ENCODING = 'UTF8';

CREATE USER testadmin with encrypted password 'testadmin';

GRANT ALL PRIVILEGES on database exam_service to testadmin;

\connect exam_service testadmin

CREATE TABLE exams (
  id UUID PRIMARY KEY NOT NULL,
  name VARCHAR(100),
  description VARCHAR(800),
  pass_count INT NOT NULL,
  is_deleted BOOLEAN NOT NULL,
  created_at DATE,
  questions JSON
);

INSERT INTO exams (id, name, description, pass_count, is_deleted, created_at, questions)
    VALUES ('0596c2c0-a70a-47dd-81c8-31411a5b132a', 'main exam', 'description of exam 1', 8, FALSE, to_timestamp(1195333200), '[]'::json);

INSERT INTO exams (id, name, description, pass_count, is_deleted, created_at, questions)
    VALUES ('66bcd4a3-a3d5-409e-9a38-e0d7b029a020', 'exam number 2', 'description of exam 2', 10, FALSE, to_timestamp(1195333200), '[]'::json);

INSERT INTO exams (id, name, description, pass_count, is_deleted, created_at, questions)
    VALUES ('446ae2f3-eb60-44cb-b889-22f14ef06d82', 'exam', 'very long description', 3, FALSE, to_timestamp(1195333200), '[{"id":0,"questionText":"1+1","type":"SINGLE_ANSWER","variants":["1","2"],"correctVariantsId":[1],"correctInputAnswer":null}]'::json);

INSERT INTO exams (id, name, description, pass_count, is_deleted, created_at, questions)
    VALUES ('6c8d5358-4111-4d28-a12e-fecfa3c1ce78', 'exam of user number 2', 'long description', 10, FALSE, to_timestamp(1195333200), '[]'::json);
