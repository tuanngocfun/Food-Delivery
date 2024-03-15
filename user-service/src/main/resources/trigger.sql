-- PostgreSQL Trigger Use in Microservices with Kafka & Socket.IO:
-- 1. Complexity Concern: Avoid SQL triggers to reduce hidden logic & maintain simplicity.
-- 2. Decoupling Services: Prefer keeping business logic in services, not in DB triggers, for loose coupling.
-- 3. Scalability: SQL triggers can impact DB performance; use Kafka for scalable, inter-service communication.
-- 4. Real-Time Communication: Use Socket.IO for client updates; keep real-time updates separate from DB logic.
-- 5. Data Consistency: Be cautious with triggers affecting distributed transactions; ensure consistency at service level.
-- 6. Maintainability: Keeping business logic outside of DB enhances visibility & ease of management.
-- 7. Debugging: SQL triggers can obscure flow, making debugging more challenging. Keep logic in services for transparency.
-- REMEMBER: Use Kafka for asynchronous communication between services & Socket.IO for client-side real-time updates. Avoid embedding complex logic in SQL triggers to foster a more maintainable, scalable microservices architecture.

CREATE OR REPLACE FUNCTION update_user_level()
RETURNS TRIGGER AS $$
BEGIN
    -- Assuming the existence of a column in 'users' that counts completed orders
    IF (SELECT count(*) FROM orders WHERE user_id = NEW.user_id AND status = 'delivered') >= 20 THEN
        UPDATE users SET user_level = 'high_upper' WHERE id = NEW.user_id;
    ELSIF (SELECT count(*) FROM orders WHERE user_id = NEW.user_id AND status = 'delivered') >= 10 THEN
        UPDATE users SET user_level = 'upper' WHERE id = NEW.user_id;
    -- Add more conditions as needed
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_user_level
AFTER INSERT ON orders
FOR EACH ROW
WHEN (NEW.status = 'delivered')
EXECUTE FUNCTION update_user_level();

CREATE OR REPLACE FUNCTION log_order_status_change()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO order_status_logs(order_id, previous_status, new_status, log_time)
    VALUES (NEW.id, OLD.status, NEW.status, now());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_order_status_change
AFTER UPDATE OF status ON orders
FOR EACH ROW
EXECUTE FUNCTION log_order_status_change();

CREATE OR REPLACE FUNCTION cleanup_on_user_deletion()
RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM orders WHERE user_id = OLD.id;
    DELETE FROM user_industries WHERE user_id = OLD.id;
    -- Add more cleanup queries as needed
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_cleanup_on_user_deletion
BEFORE DELETE ON users
FOR EACH ROW
EXECUTE FUNCTION cleanup_on_user_deletion();
