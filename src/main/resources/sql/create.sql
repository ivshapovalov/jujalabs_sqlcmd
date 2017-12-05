CREATE TABLE "shop" (
  "id"      SERIAL PRIMARY KEY,
  "name"    VARCHAR(255)        NOT NULL,
  "address" VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE "customer" (
  "id"      SERIAL PRIMARY KEY,
  "name"    VARCHAR(255) NOT NULL,
  "surname" VARCHAR(255) NOT NULL,
  "age"     SMALLINT     NOT NULL
);

CREATE TABLE "purchase" (
  "id"          SERIAL PRIMARY KEY,
  "date"        TIMESTAMP NOT NULL,
  "customer_id" INTEGER   NOT NULL,
  "shop_id"     INTEGER   NOT NULL,
  FOREIGN KEY ("shop_id") REFERENCES "shop" ("id"),
  FOREIGN KEY ("customer_id") REFERENCES "customer" ("id")
);

CREATE TABLE "product" (
  "id"    SERIAL PRIMARY KEY,
  "name"  VARCHAR(255)   NOT NULL,
  "price" NUMERIC(19, 2) NOT NULL
);

CREATE TABLE "purchase_detail" (
  "purchase_id" INTEGER        NOT NULL,
  "product_id"  INTEGER        NOT NULL,
  "quantity"    SMALLINT       NOT NULL,
  "sell_price"  NUMERIC(19, 2) NOT NULL,
  FOREIGN KEY ("purchase_id") REFERENCES purchase ("id"),
  FOREIGN KEY ("product_id") REFERENCES product ("id"),
  PRIMARY KEY ("purchase_id", "product_id")
);