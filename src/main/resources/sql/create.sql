CREATE TABLE IF NOT EXISTS "buyer" (
  "buyerId" SERIAL PRIMARY KEY,
  "buyerName" TEXT NOT NULL,
  "buyerSurname" TEXT NOT NULL,
  "age" SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS "shop" (
  "shopId" SERIAL PRIMARY KEY,
  "shopName" TEXT NOT NULL UNIQUE,
  "address" TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "plu" (
  "pluId" SERIAL PRIMARY KEY,
  "pluName" TEXT NOT NULL UNIQUE,
  "price" NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS "order" (
  "orderId" SERIAL PRIMARY KEY,
  "orderDate" DATE NOT NULL,
  "buyerId" INTEGER NOT NULL,
  "shopId" INTEGER NOT NULL,
  FOREIGN KEY ("shopId") REFERENCES "shop"("shopId"),
  FOREIGN KEY ("buyerId") REFERENCES "buyer"("buyerId")
  ON DELETE  CASCADE
);

CREATE TABLE IF NOT EXISTS "lineOrder" (
  "lineOrderId" SERIAL PRIMARY KEY,
  "orderId" INTEGER NOT NULL,
  "pluId" INTEGER NOT NULL,
  "quantity" NUMERIC(10, 2) NOT NULL,
  "price" NUMERIC(10, 2) NOT NULL,
  FOREIGN KEY ("pluId") REFERENCES "plu"("pluId"),
  FOREIGN KEY ("orderId") REFERENCES "order"("orderId")
  ON DELETE  CASCADE
);