CREATE TYPE user_role AS ENUM ('USER');

CREATE TABLE users (
  id UUID PRIMARY KEY NOT NULL,
  username VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role user_role NOT NULL
);

INSERT INTO users(id, username, password_hash, user_role)
VALUES('12412cdb-398f-4def-9cec-325b11968b56', 'olga', '$2a$10$bO0R953eHWoIFMBU8c.fpeAd9hFvyEI/nqXUBylhRciam01q6XSX2', 'USER');