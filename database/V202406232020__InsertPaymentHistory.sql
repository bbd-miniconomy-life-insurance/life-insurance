CREATE OR REPLACE PROCEDURE insert_payment_history(
    IN p_persona_id INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_policy_id INT;
    v_latest_price NUMERIC(12,2);
BEGIN
    SELECT "policy_id" INTO v_policy_id
    FROM "policy"
    WHERE "persona_id" = p_persona_id;

    SELECT "price"
    INTO v_latest_price
    FROM "price"
    WHERE "inception_date" = (
        SELECT MAX("inception_date") FROM "price"
    );

    INSERT INTO "payment_history" ("policy_id", "time", "amount")
    VALUES (v_policy_id, CURRENT_TIMESTAMP, v_latest_price);
END $$;
