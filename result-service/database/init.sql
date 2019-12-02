DROP DATABASE result_service;

CREATE DATABASE result_service WITH TEMPLATE = template0 ENCODING = 'UTF8';

CREATE USER testadmin with encrypted password 'testadmin';

GRANT ALL PRIVILEGES on database result_service to testadmin;

\connect result_service testadmin

CREATE TABLE exam_results (
  id UUID PRIMARY KEY NOT NULL,
  user_id UUID NOT NULL,
  test_id UUID NOT NULL,
  result VARCHAR(50),
  passed_at DATE
);

INSERT INTO exam_results  (id, user_id, test_id, result, passed_at)
    VALUES ('18c4f984-22bd-4edd-ae66-6fb157328337', '12412cdb-398f-4def-9cec-325b11968b56', '0596c2c0-a70a-47dd-81c8-31411a5b132a', '11', to_timestamp(1195333200));

INSERT INTO exam_results  (id, user_id, test_id, result, passed_at)
    VALUES ('9d7c4fc9-56bb-4e39-b359-f23d56c3bfe2', '7c803c41-ca5f-4e66-9483-7e361db72917', '66bcd4a3-a3d5-409e-9a38-e0d7b029a020', '12', to_timestamp(1195333200));
INSERT INTO exam_results  (id, user_id, test_id, result, passed_at)
    VALUES ('5f4f4ac2-bd8f-4193-847c-7fef7bb4c7ab', 'be7b8ae6-5368-40f1-9b07-b33027acf43f', '66bcd4a3-a3d5-409e-9a38-e0d7b029a020', '1/2', to_timestamp(1195333200));

INSERT INTO exam_results  (id, user_id, test_id, result, passed_at)
    VALUES ('74f7a1b8-5bbd-411f-82a3-9c2aa1e9ab3e', 'ecc4f8af-c758-434a-84c1-6df04cbb0936', '446ae2f3-eb60-44cb-b889-22f14ef06d82', '1/3', to_timestamp(1195333200));

INSERT INTO exam_results  (id, user_id, test_id, result, passed_at)
    VALUES ('073b22c6-4deb-40cf-99d5-4b36c1e41478', '0d984722-ba7f-4b57-9afb-3078191f2a01', '6c8d5358-4111-4d28-a12e-fecfa3c1ce78', '3/3', to_timestamp(1195333200));
