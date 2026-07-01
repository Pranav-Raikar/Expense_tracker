-- ══════════════════════════════════════════════════════════
--  EXPENSE TRACKER – MySQL Setup Commands
--  Run these in your MySQL CLI or Workbench
-- ══════════════════════════════════════════════════════════

USE expense_tracker;

-- 1. Add missing columns to 'users' table
--    (Run only if they don't exist yet)
ALTER TABLE users ADD COLUMN IF NOT EXISTS first_name VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_name  VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS email      VARCHAR(150);

-- 2. Verify the tables look correct
DESC users;
DESC transactions;
DESC balance;

-- 3. (Optional) Insert a test user to verify login works
--    Username: admin  Password: admin123
INSERT IGNORE INTO users (username, password, first_name, last_name, email)
VALUES ('admin', 'admin123', 'Admin', 'User', 'admin@example.com');

-- 4. Verify
SELECT * FROM users;
