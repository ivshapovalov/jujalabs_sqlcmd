CREATE TABLE IF NOT EXISTS "buyer" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL,
  "surname" TEXT NOT NULL,
  "age" SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS "shop" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL UNIQUE,
  "address" TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "products" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL UNIQUE,
  "price" NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS "order" (
  "id" SERIAL PRIMARY KEY,
  "date" DATE NOT NULL,
  "buyer_id" INTEGER NOT NULL,
  "shop_id" INTEGER NOT NULL,
  FOREIGN KEY ("shop_id") REFERENCES "shop"("id"),
  FOREIGN KEY ("buyer_id") REFERENCES "buyer"("id")
  ON DELETE  CASCADE
);

CREATE TABLE IF NOT EXISTS "line_order" (
  "id" SERIAL PRIMARY KEY,
  "order_id" INTEGER NOT NULL,
  "goods_id" INTEGER NOT NULL,
  "quantity" NUMERIC(10, 2) NOT NULL,
  "price" NUMERIC(10, 2) NOT NULL,
  FOREIGN KEY ("goods_id") REFERENCES "products"("id"),
  FOREIGN KEY ("order_id") REFERENCES "order"("id")
  ON DELETE  CASCADE
);