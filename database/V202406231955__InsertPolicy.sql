CREATE OR REPLACE PROCEDURE insert_policy_active(
    IN p_persona_id INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_status_id INT;
    v_policy_id INT;
BEGIN
    SELECT "status_id" INTO v_status_id
    FROM "policy_status"
    WHERE "status_name" = 'Active';

    INSERT INTO "policy" ("persona_id", "status_id", "inception_date")
    VALUES (p_persona_id, v_status_id, CURRENT_DATE);
END $$;
