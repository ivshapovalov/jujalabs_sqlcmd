CREATE TABLE IF NOT EXISTS "shop" (
  "id"      SERIAL PRIMARY KEY,
  "name"    VARCHAR(255)        NOT NULL,
  "address" VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "customer" (
  "id"      SERIAL PRIMARY KEY,
  "name"    VARCHAR(255) NOT NULL,
  "surname" VARCHAR(255) NOT NULL,
  "age"     SMALLINT     NOT NULL,
  "shop_id" INTEGER      NOT NULL,
  FOREIGN KEY ("shop_id") REFERENCES "shop" ("id")
);

CREATE TABLE IF NOT EXISTS "purchase" (
  "id"          SERIAL PRIMARY KEY,
  "date"        TIMESTAMP NOT NULL,
  "customer_id" INTEGER   NOT NULL,
  UNIQUE ("date", "customer_id"),
  FOREIGN KEY ("customer_id") REFERENCES "customer" ("id")
);

CREATE TABLE IF NOT EXISTS "product" (
  "id"    SERIAL PRIMARY KEY,
  "name"  VARCHAR(255) NOT NULL,
  "price" MONEY        NOT NULL
);

CREATE TABLE IF NOT EXISTS "purchase_detail" (
  "id"          SERIAL PRIMARY KEY,
  "quantity"    INT2    NOT NULL,
  "price"       MONEY   NOT NULL,
  "shop_id"     INTEGER NOT NULL,
  "purchase_id" INTEGER NOT NULL,
  "product_id"  INTEGER NOT NULL,
  FOREIGN KEY ("product_id") REFERENCES "product" ("id"),
  FOREIGN KEY ("purchase_id") REFERENCES "purchase" ("id"),
  FOREIGN KEY ("shop_id") REFERENCES "shop" ("id")
);