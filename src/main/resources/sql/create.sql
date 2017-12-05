CREATE TABLE IF NOT EXISTS "buyer" (
  "buyerId" SERIAL PRIMARY KEY,
  "buyerName" text NOT NULL,
  "buyerSurname" text NOT NULL,
  "age" smallint NOT NULL
);

CREATE TABLE IF NOT EXISTS "shop" (
  "shopId" SERIAL PRIMARY KEY,
  "shopName" text NOT NULL UNIQUE,
  "address" text NOT NULL
);

CREATE TABLE IF NOT EXISTS "plu" (
  "pluId" SERIAL PRIMARY KEY,
  "pluName" text NOT NULL UNIQUE,
  "price" numeric(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS "order" (
  "orderId" SERIAL PRIMARY KEY,
  "orderDate" date NOT NULL,
  "buyerId" integer NOT NULL,
  "shopId" integer NOT NULL,
  FOREIGN KEY ("shopId") REFERENCES "shop"("shopId"),
  FOREIGN KEY ("buyerId") REFERENCES "buyer"("buyerId")
  ON DELETE  CASCADE
);

CREATE TABLE IF NOT EXISTS "lineOrder" (
  "lineOrderId" SERIAL PRIMARY KEY,
  "orderId" integer NOT NULL,
  "pluId" integer NOT NULL,
  "quantity" numeric(10, 2) NOT NULL,
  "price" numeric(10, 2) NOT NULL,
  FOREIGN KEY ("pluId") REFERENCES "plu"("pluId"),
  FOREIGN KEY ("orderId") REFERENCES "order"("orderId")
  ON DELETE  CASCADE
);