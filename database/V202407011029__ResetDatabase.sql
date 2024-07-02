CREATE OR REPLACE FUNCTION reset_database()
    RETURNS boolean  -- Change return type to boolean
AS $$
DECLARE
    success boolean := true;  -- Assume success unless an error occurs
BEGIN
    BEGIN
        -- Delete records from tables
        DELETE FROM "debit_order";
        DELETE FROM "transaction";
        DELETE FROM "policy";
        DELETE FROM "policy_status";
        DELETE FROM "price";

        -- Reset sequences
        ALTER SEQUENCE "policy_status_status_id_seq" RESTART WITH 1;
        ALTER SEQUENCE "policy_policy_id_seq" RESTART WITH 1;
        ALTER SEQUENCE "debit_order_debit_order_id_seq" RESTART WITH 1;
        ALTER SEQUENCE "transaction_transaction_id_seq" RESTART WITH 1;
        ALTER SEQUENCE "price_price_id_seq" RESTART WITH 1;

        -- Update tables' ID columns
        UPDATE "policy_status" SET status_id = DEFAULT;
        UPDATE "policy" SET policy_id = DEFAULT;
        UPDATE "debit_order" SET debit_order_id = DEFAULT;
        UPDATE "transaction" SET transaction_id = DEFAULT;
        UPDATE "price" SET price_id = DEFAULT;
    EXCEPTION
        WHEN others THEN
            success := false;  -- Set success to false on any error
    END;

    RETURN success;  -- Return success status
END;
$$ LANGUAGE plpgsql;
