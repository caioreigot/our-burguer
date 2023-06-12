CREATE TABLE user(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name varchar(100) NOT NULL,
  email varchar(100) NOT NULL,
  cpf varchar(50),
  phone varchar(50),
  encryptedPassword TEXT NOT NULL
);

CREATE TABLE product(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  imageUrl TEXT NOT NULL,
  title varchar(100) NOT NULL,
  kcal INTEGER NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  description TEXT
);