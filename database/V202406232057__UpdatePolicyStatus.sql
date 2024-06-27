CREATE OR REPLACE PROCEDURE update_policy_status(
    IN p_persona_id INT,
    IN p_status_name VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_status_id INT;
BEGIN
    SELECT "status_id" INTO v_status_id
    FROM "policy_status"
    WHERE "statusName" = p_status_name;

    UPDATE "policy"
    SET "status_id" = v_status_id
    WHERE "persona_id" = p_persona_id;
END $$;
