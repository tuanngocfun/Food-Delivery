-- Users table email lookups
CREATE INDEX idx_users_email ON users(email);

-- Orders table by user_id for faster retrieval of user orders
CREATE INDEX idx_orders_user_id ON orders(user_id);

-- Food_items table by restaurant_id for faster access to a restaurant's menu
CREATE INDEX idx_food_items_restaurant_id ON food_items(restaurant_id);

-- Order_details by order_id and food_item_id for quick access to order specifics
CREATE INDEX idx_order_details_order_id_food_item_id ON order_details(order_id, food_item_id);

CREATE INDEX idx_food_items_available ON food_items(id) WHERE available = TRUE;
CREATE INDEX idx_restaurants_active ON restaurants(id) WHERE rating > 4.0; -- Assuming active restaurants have a rating above 4.0

-- If there's a frequent query joining orders and users based on user_id and filtering on status
CREATE INDEX idx_orders_user_id_status ON orders(user_id, status);

-- Assuming PostgreSQL 10+ for declarative partitioning
CREATE TABLE orders_partioned PARTITION OF orders
FOR VALUES IN ('placed', 'preparing', 'on_way', 'delivered');
