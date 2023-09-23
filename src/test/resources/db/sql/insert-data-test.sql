DO
$$
    DECLARE
        counter  INTEGER   := 0;
        baseDate TIMESTAMP := '2022-09-23 08:00:00';
        title    VARCHAR(2);
    BEGIN
        -- Loop for the first character (A-Z, a-z)
        FOR i IN 65..122
            LOOP
                -- Exclude non-alphabet ASCII values
                IF i BETWEEN 91 AND 96 THEN
                    CONTINUE;
                END IF;

                -- Loop for the second character (A-Z, a-z)
                FOR j IN 65..122
                    LOOP
                        IF j BETWEEN 91 AND 96 THEN
                            CONTINUE;
                        END IF;

                        title := CHR(i) || CHR(j);
                        EXECUTE format(
                                'INSERT INTO notes (title, content, date, version) VALUES (''%s'', ''Content for note %s'', TIMESTAMP ''%s'', 1);',
                                title, title, baseDate + counter * INTERVAL '1 hour');
                        counter := counter + 1;

                    END LOOP;
            END LOOP;
    END
$$;
-- 52 * 52 = 2704 notes