INSERT INTO "policy_status" ("status_name") VALUES
('Active'),
('PaidOut'),
('Pending'),
('Lapsed');

INSERT INTO "policy" ("persona_id", "status_id", "inception_date") VALUES
(1, (SELECT "status_id" FROM "policy_status" WHERE "status_name" = 'Active'), '2023-01-01'),
(2, (SELECT "status_id" FROM "policy_status" WHERE "status_name" = 'PaidOut'), '2022-12-15'),
(3, (SELECT "status_id" FROM "policy_status" WHERE "status_name" = 'Lapsed'), '2023-02-28');

INSERT INTO "payment_history" ("policy_id", "time", "amount") VALUES
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 1), '2023-01-10', 100.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 1), '2023-02-10', 100.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 1), '2023-03-10', 100.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 2), '2023-01-15', 150.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 2), '2023-02-15', 150.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 2), '2023-03-15', 150.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 3), '2023-01-28', 80.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 3), '2023-02-28', 80.00),
((SELECT "policy_id" FROM "policy" WHERE "persona_id" = 3), '2023-03-28', 80.00);

INSERT INTO "price" ("inception_date", "price") VALUES
('2023-01-01', 500),
('2023-02-01', 600);
