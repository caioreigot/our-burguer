CREATE TABLE user(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name varchar(100) NOT NULL,
  email varchar(100) NOT NULL UNIQUE,
  address varchar(100) NOT NULL,
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

CREATE TABLE forgotPasswordRoom(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  email varchar(100) NOT NULL,
  roomId varchar(100) NOT NULL UNIQUE
);

CREATE TABLE cart(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  idUser INTEGER NOT NULL,
  total DECIMAL(10, 2) NOT NULL,
  status varchar(20) NOT NULL,
  FOREIGN KEY(idUser) REFERENCES user(id)
);

CREATE TABLE cartItem(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  idCart INTEGER NOT NULL,
  itemName varchar(100) NOT NULL,
  amount INTEGER NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY(idCart) REFERENCES cart(id)
);

INSERT INTO product(imageUrl, title, kcal, price, description) VALUES('/assets/Comuna Burguer.png', 'Comuna Burger', 475, 10.00, 'Este hambúrguer é composto por um pão macio e fresquinho, uma suculenta carne bovina, queijo cheddar derretido, cebola caramelizada e um molho especial de maionese com mostarda. Todos os ingredientes são cuidadosamente selecionados para garantir o melhor sabor e qualidade.');
INSERT INTO product(imageUrl, title, kcal, price, description) VALUES('/assets/Sundae da Coletividade.png', 'Sundae da Coletividade', 325, 7.00, 'Este sundae representa a ideia de que juntos somos mais fortes. É feito com uma bola de sorvete de creme, fatias de banana fresca, calda de caramelo quente e chantilly. Todos os ingredientes são cuidadosamente selecionados para oferecer uma sobremesa deliciosa e compartilhável.');
INSERT INTO product(imageUrl, title, kcal, price, description) VALUES('/assets/Igualdade Burguer.png', 'Igualdade Burger', 530, 12.00, 'Este hambúrguer é um verdadeiro exemplo da colaboração entre todos os membros da comunidade. Ele é feito com um pão artesanal crocante, carne bovina suculenta, alface fresca, tomate maduro e um molho especial de maionese com ervas finas. Cada ingrediente é escolhido com cuidado e amor para oferecer um sabor incrível.');
INSERT INTO product(imageUrl, title, kcal, price, description) VALUES('/assets/Brownie da Solidariedade.png', 'Brownie da Solidariedade', 345, 9.00, 'Este brownie é feito com chocolate amargo de alta qualidade e servido quente com uma bola de sorvete de baunilha cremoso. É a combinação perfeita de texturas e sabores e representa a importância da solidariedade em nossa comunidade.');
INSERT INTO product(imageUrl, title, kcal, price, description) VALUES('/assets/Fraternidade Burger.png', 'Fraternidade Burger', 725, 14.00, 'Este hambúrguer tem um pão macio e fresquinho, uma generosa porção de carne bovina suculenta, queijo derretido, alface crocante, tomate maduro e um molho especial de maionese com alho. Cada ingrediente é escolhido com cuidado para oferecer um sabor incrível e criar uma experiência fraterna e acolhedora.');
INSERT INTO product(imageUrl, title, kcal, price, description) VALUES('/assets/Milkshake da União.png', 'Milkshake da União', 450, 12.00, 'Este milkshake é feito com morangos frescos, sorvete de baunilha e leite gelado. É uma bebida cremosa e refrescante que representa a união de todos os membros da comunidade. É servido com chantilly por cima para um toque extra de indulgência.');