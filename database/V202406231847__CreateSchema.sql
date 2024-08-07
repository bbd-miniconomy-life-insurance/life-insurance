CREATE TABLE "policy_status" (
  "status_id" SERIAL PRIMARY KEY,
  "status_name" VARCHAR(100) NOT NULL
);

CREATE TABLE "policy" (
  "policy_id" SERIAL PRIMARY KEY,
  "persona_id" bigint NOT NULL,
  "status_id" bigint NOT NULL,
  "inception_date" timestamp NOT NULL
);

CREATE TABLE "debit_order" (
  "debit_order_id" SERIAL PRIMARY KEY,
  "policy_id" bigint NOT NULL,
  "debit_order_reference_number" varchar(100) NOT NULL
);

CREATE TABLE "transaction" (
  "transaction_id" SERIAL PRIMARY KEY,
  "policy_id" bigint NOT NULL,
  "transaction_reference_number" varchar(100) NOT NULL
);

CREATE TABLE "price" (
  "price_id" SERIAL PRIMARY KEY,
  "inception_date" timestamp NOT NULL,
  "price" BIGINT NOT NULL
);

CREATE TABLE "stocks" (
  "stocks_id" SERIAL PRIMARY KEY,
  "businessId" varchar(100) NOT NULL,
  "quantity" int NOT NULL
);

CREATE TABLE "constants" (
  "constants_id" SERIAL PRIMARY KEY,
  "name" varchar(100) NOT NULL,
  "id" varchar(100) NOT NULL
);

CREATE TABLE "transaction_history" (
  "transaction_history_id" SERIAL PRIMARY KEY,
  "amount" bigint NOT NULL,
  "date" timestamp NOT NULL,
  "reference" varchar(100) NOT NULL
);

ALTER TABLE "policy" ADD FOREIGN KEY ("status_id") REFERENCES "policy_status" ("status_id");

ALTER TABLE "transaction" ADD FOREIGN KEY ("policy_id") REFERENCES "policy" ("policy_id");

ALTER TABLE "debit_order" ADD FOREIGN KEY ("policy_id") REFERENCES "policy" ("policy_id");
