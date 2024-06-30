CREATE TABLE "policy_status" (
  "status_id" SERIAL PRIMARY KEY,
  "status_name" VARCHAR(100) NOT NULL
);

CREATE TABLE "policy" (
  "policy_id" SERIAL PRIMARY KEY,
  "persona_id" BIGINT UNIQUE NOT NULL,
  "status_id" INT NOT NULL,
  "inception_date" TIMESTAMP NOT NULL,
  FOREIGN KEY ("status_id") REFERENCES "policy_status" ("status_id")
);

CREATE TABLE "price" (
  "price_id" SERIAL PRIMARY KEY,
  "inception_date" TIMESTAMP NOT NULL,
  "price" BIGINT NOT NULL
);

