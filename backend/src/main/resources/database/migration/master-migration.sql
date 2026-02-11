CREATE PROCEDURE migrate_existing_customer_data()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE v_db VARCHAR(255);

    DECLARE cur CURSOR FOR
SELECT db_name
FROM tenants
WHERE status='ACTIVE'
  AND db_name IS NOT NULL
  AND db_name <> '';

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

OPEN cur;

read_loop: LOOP
        FETCH cur INTO v_db;
        IF done THEN
            LEAVE read_loop;
END IF;

        SET @sql = CONCAT(
            'UPDATE tenants SET ',
            'email = (SELECT email FROM `', v_db, '`.customer LIMIT 1), ',
            'mobilenumber = (SELECT mobilenumber FROM `', v_db, '`.customer LIMIT 1) ',
            'WHERE db_name = ''', v_db, ''''
        );

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

END LOOP;

CLOSE cur;
END;

