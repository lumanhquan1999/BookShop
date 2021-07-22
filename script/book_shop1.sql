CREATE SCHEMA IF NOT EXISTS `book_shop1`;
USE `book_shop1` ;

-- -----------------------------------------------------
-- Table `book_shop`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `address` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(45) NULL DEFAULT NULL,
  `street` VARCHAR(45) NULL DEFAULT NULL,
  `ward` VARCHAR(45) NULL DEFAULT NULL,
  `district` VARCHAR(45) NULL DEFAULT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `author` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `date_of_birth` DATETIME NULL DEFAULT NULL,
  `gender` VARCHAR(45) NULL DEFAULT NULL,
  `address_id` INT NULL DEFAULT NULL,
  `telephone_number` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `NAME_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `FK_ADDRESS_idx` (`address_id` ASC) VISIBLE,
  CONSTRAINT `FK_ADDRESS_AUTHOR`
    FOREIGN KEY (`address_id`)
    REFERENCES `address` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `date_of_birth` DATETIME NULL DEFAULT NULL,
  `gender` VARCHAR(45) NULL DEFAULT NULL,
  `telephone_number` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(45) NULL DEFAULT NULL,
  `enable` boolean default null,
  `username` varchar(45) not null,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;




-- -----------------------------------------------------
-- Table `book_shop`.`bill`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bill` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NULL DEFAULT NULL,
  `user_id` INT NULL DEFAULT NULL,
  `address_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_USER_idx` (`user_id` ASC) VISIBLE,
  INDEX `FK_ADDRESS_idx` (`address_id` ASC) VISIBLE,
  CONSTRAINT `FK_ADDRESS_BILL`
    FOREIGN KEY (`address_id`)
    REFERENCES `address` (`id`),
  CONSTRAINT `FK_USER_BILL`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `NAME_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`publisher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `publisher` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `address_id` INT NULL DEFAULT NULL,
  `phone_number` VARCHAR(45) NULL DEFAULT NULL,
  `district` VARCHAR(45) NULL DEFAULT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `NAME_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `FK_ADDRESS_idx` (`address_id` ASC) VISIBLE,
  CONSTRAINT `FK_ADDRESS_PUBLISHER`
    FOREIGN KEY (`address_id`)
    REFERENCES `address` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `category_id` INT NULL DEFAULT NULL,
  `publisher_id` INT NULL DEFAULT NULL,
  `author` VARCHAR(45) NULL DEFAULT NULL,
  `price` VARCHAR(45) NULL DEFAULT NULL,
  `quantity` INT NULL DEFAULT NULL,
  `release_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_CATEGORY_idx` (`category_id` ASC) VISIBLE,
  INDEX `FK_PUBLISHER_idx` (`publisher_id` ASC) VISIBLE,
  CONSTRAINT `FK_CATEGORY`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`),
  CONSTRAINT `FK_PUBLISHER`
    FOREIGN KEY (`publisher_id`)
    REFERENCES `publisher` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`bill_book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bill_book` (
  `bill_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  PRIMARY KEY (`bill_id`, `book_id`),
  INDEX `FK_BILL_idx` (`bill_id` ASC) VISIBLE,
  INDEX `FK_BOOK_BILL_BOOK` (`book_id` ASC) VISIBLE,
  CONSTRAINT `FK_BILL`
    FOREIGN KEY (`bill_id`)
    REFERENCES `bill` (`id`),
  CONSTRAINT `FK_BOOK_BILL_BOOK`
    FOREIGN KEY (`book_id`)
    REFERENCES `book` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`book_author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_author` (
  `book_id` INT NOT NULL,
  `author_id` INT NOT NULL,
  PRIMARY KEY (`book_id`, `author_id`),
  INDEX `FK_BOOK_idx` (`book_id` ASC) VISIBLE,
  INDEX `FK_AUTHOR` (`author_id` ASC) VISIBLE,
  CONSTRAINT `FK_AUTHOR`
    FOREIGN KEY (`author_id`)
    REFERENCES `author` (`id`),
  CONSTRAINT `FK_BOOK`
    FOREIGN KEY (`book_id`)
    REFERENCES `book` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`shopping_cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopping_cart` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `total_value` DOUBLE NULL DEFAULT NULL,
  `user_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_USER_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK_USER_SHOPPINGCART`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`shopping_cart_book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopping_cart_book` (
  `shopping_cart_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  PRIMARY KEY (`shopping_cart_id`, `book_id`),
  INDEX `FK_SHOPPING_CART_idx` (`shopping_cart_id` ASC) VISIBLE,
  INDEX `FK_BOOK_SHOPPING_CART_BOOK` (`book_id` ASC) VISIBLE,
  CONSTRAINT `FK_BOOK_SHOPPING_CART_BOOK`
    FOREIGN KEY (`book_id`)
    REFERENCES `book` (`id`),
  CONSTRAINT `FK_SHOPPING_CART`
    FOREIGN KEY (`shopping_cart_id`)
    REFERENCES `shopping_cart` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `book_shop`.`user_address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_address` (
  `user_id` INT NOT NULL,
  `address_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `address_id`),
  INDEX `FK_USER_idx` (`user_id` ASC) VISIBLE,
  INDEX `FK_ADDRESS` (`address_id` ASC) VISIBLE,
  CONSTRAINT `FK_ADDRESS`
    FOREIGN KEY (`address_id`)
    REFERENCES `address` (`id`),
  CONSTRAINT `FK_USER`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

create table if not exists `role` (
	`id` int(14) not null auto_increment,
    `name` varchar(45) not null,
    primary key (`id`)
)Engine=InnoDB, default character set = latin1;

CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX `FK_USER_idx` (`user_id` ASC) VISIBLE,
  INDEX `FK_ROLE` (`role_id` ASC) VISIBLE,
  CONSTRAINT `FK_ROLE`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`),
  CONSTRAINT `FK_USER_ROLE`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;
