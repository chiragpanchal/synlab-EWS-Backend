-- Check the table structure for SC_SCHEDULE_RULES
DESCRIBE SC_SCHEDULE_RULES;

-- Or use this alternative query
SELECT column_name, data_type, nullable, data_length 
FROM user_tab_columns 
WHERE table_name = 'SC_SCHEDULE_RULES' 
ORDER BY column_id;