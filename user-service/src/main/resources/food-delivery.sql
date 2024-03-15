CREATE TYPE "user_level" AS ENUM (
  'normal',
  'upper',
  'high_upper',
  'high_end',
  'ultra'
);

CREATE TYPE "order_status" AS ENUM (
  'placed',
  'preparing',
  'on_way',
  'delivered'
);

CREATE TABLE "users" (
  "id" SERIAL PRIMARY KEY,
  "firstname" VARCHAR(50) NOT NULL,
  "lastname" VARCHAR(50) NOT NULL,
  "email" VARCHAR(255) UNIQUE NOT NULL,
  "password_hash" TEXT NOT NULL,
  "phone_number" VARCHAR(20),
  "address" TEXT,
  "user_level" user_level NOT NULL,
  "country_code" INTEGER,
  "role" VARCHAR(50) NOT NULL,
  "account_non_expired" BOOLEAN NOT NULL DEFAULT TRUE,
  "account_non_locked" BOOLEAN NOT NULL DEFAULT TRUE,
  "credentials_non_expired" BOOLEAN NOT NULL DEFAULT TRUE,
  "enabled" BOOLEAN NOT NULL DEFAULT TRUE,
  "last_password_reset_date" TIMESTAMP,
  "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "countries" (
  "code" INTEGER PRIMARY KEY,
  "name" VARCHAR(100),
  "continent_name" VARCHAR(50)
);

CREATE TABLE "restaurants" (
  "id" SERIAL PRIMARY KEY,
  "name" VARCHAR(100),
  "address" TEXT,
  "country_code" INTEGER,
  "phone_number" VARCHAR(20),
  "rating" DECIMAL(3,2),
  "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "food_items" (
  "id" SERIAL PRIMARY KEY,
  "restaurant_id" INTEGER,
  "name" VARCHAR(100),
  "description" TEXT,
  "price" DECIMAL(10,2),
  "currency" VARCHAR(10),
  "available" BOOLEAN DEFAULT TRUE,
  "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "orders" (
  "id" SERIAL PRIMARY KEY,
  "user_id" INTEGER,
  "total_price" DECIMAL(10,2),
  "currency" VARCHAR(10),
  "order_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "delivery_address" TEXT,
  "status" order_status
);

CREATE TABLE "order_details" (
  "id" SERIAL PRIMARY KEY,
  "order_id" INTEGER,
  "food_item_id" INTEGER,
  "quantity" INTEGER,
  "price" DECIMAL(10,2)
);

CREATE TABLE "industries" (
  "id" SERIAL PRIMARY KEY,
  "name" VARCHAR(100),
  "description" TEXT
);

CREATE TABLE "user_industries" (
  "user_id" INTEGER,
  "industry_id" INTEGER,
  "occupation" VARCHAR(100)
);

CREATE TABLE "tokens" (
  "id" SERIAL PRIMARY KEY,
  "user_id" INTEGER,
  "token" TEXT NOT NULL,
  "expiration_date" TIMESTAMP NOT NULL,
  "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "users" IS 'Stores user data, including authentication and personal details.';
COMMENT ON TABLE "user_industries" IS 'Links users to their respective industries and occupations.';

-- Foreign Key Constraints
ALTER TABLE "users" ADD FOREIGN KEY ("country_code") REFERENCES "countries" ("code");
ALTER TABLE "restaurants" ADD FOREIGN KEY ("country_code") REFERENCES "countries" ("code");
ALTER TABLE "food_items" ADD FOREIGN KEY ("restaurant_id") REFERENCES "restaurants" ("id");
ALTER TABLE "orders" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
ALTER TABLE "order_details" ADD FOREIGN KEY ("order_id") REFERENCES "orders" ("id");
ALTER TABLE "order_details" ADD FOREIGN KEY ("food_item_id") REFERENCES "food_items" ("id");
ALTER TABLE "user_industries" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
ALTER TABLE "user_industries" ADD FOREIGN KEY ("industry_id") REFERENCES "industries" ("id");
ALTER TABLE "tokens" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_food_items_restaurant_id ON food_items(restaurant_id);
CREATE INDEX idx_order_details_order_id_food_item_id ON order_details(order_id, food_item_id);
CREATE INDEX idx_food_items_available ON food_items(id) WHERE available = TRUE;
CREATE INDEX idx_restaurants_active ON restaurants(id) WHERE rating > 4.0;
CREATE INDEX idx_orders_user_id_status ON orders(user_id, status);