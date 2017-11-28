CREATE TABLE IF NOT EXISTS "shop" (
  "id"      SERIAL PRIMARY KEY,
  "name"    VARCHAR(255)        NOT NULL,
  "address" VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "customer" (
  "id"      SERIAL PRIMARY KEY,
  "name"    VARCHAR(255) NOT NULL,
  "surname" VARCHAR(255) NOT NULL,
  "age"     INT2         NOT NULL,
  "shop_id" INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS "purchase" (
  "id"          SERIAL PRIMARY KEY,
  "date"        TIMESTAMP NOT NULL,
  "customer_id" INT       NOT NULL,
  UNIQUE ("date","customer_id")
);

CREATE TABLE "product" (
  "id"          SERIAL PRIMARY KEY,
  "name"        VARCHAR(255) NOT NULL,
  "price"       MONEY        NOT NULL
);

CREATE TABLE "purchase_detail" (
  "id"          SERIAL PRIMARY KEY,
  "quantity"    INT2  NOT NULL,
  "price"       MONEY NOT NULL,
  "shop_id"     INT   NOT NULL,
  "purchase_id" INT   NOT NULL,
  "product_id"  INT   NOT NULL
);

ALTER TABLE "customer"
  ADD FOREIGN KEY ("shop_id") REFERENCES "shop" ("id");

ALTER TABLE "purchase"
  ADD FOREIGN KEY ("customer_id") REFERENCES "customer" ("id");

ALTER TABLE "purchase_detail"
  ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id"),
  ADD FOREIGN KEY ("purchase_id") REFERENCES "purchase" ("id"),
  ADD FOREIGN KEY ("shop_id") REFERENCES "shop" ("id");