-- Users Table
INSERT INTO users (EMAIL, FIRST_NAME, LAST_NAME, ROLE, LOCALE, EMAIL_VERIFIED) VALUES (
    'john.doe@xyzmail.com', 'John', 'Doe',
    'USER', 'en', true);

INSERT INTO users (EMAIL, FIRST_NAME, LAST_NAME, ROLE, LOCALE, EMAIL_VERIFIED) VALUES (
      'danille.jackson@xyzmail.com', 'Danielle', 'Jackson',
      'STAFF', 'en', true);

INSERT INTO users (EMAIL, FIRST_NAME, LAST_NAME, ROLE, LOCALE, EMAIL_VERIFIED) VALUES (
      'Jeremiah.Daniels@xyzmail.com', 'Jeremiah', 'Daniels',
      'USER', 'en', true);


-- Addresses Table
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
    VALUES ('123 Main Street', 'Queens', 'NY', 'USA', '11234',
            'RESIDENTIAL', 1);

INSERT INTO addresses (ADDRESS_LINE1, ADDRESS_LINE2, CITY, STATE, COUNTRY, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES ('W 42nd Street', 'Building #434', 'New York', 'NY', 'USA',
        '10036', 'ALTERNATE', 1);

INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES ('1060 W Addison ST', 'Chicago', 'IL', 'USA', '60613',
        'RESIDENTIAL', 2);


-- Categories Table
INSERT INTO categories (NAME, SLUG) VALUES ('Keyboard', 'keyboard');
INSERT INTO categories (NAME, SLUG) VALUES ('Computer Peripherals', 'peripherals');
INSERT INTO categories (NAME, SLUG) VALUES ('Office', 'office');
INSERT INTO categories (NAME, SLUG) VALUES ('Stationary', 'stationary');


-- Items Table
INSERT INTO items (NAME, DESCRIPTION, SUMMARY, PRICE, DISCOUNT_TYPE, DISCOUNT_AMOUNT)
VALUES('Sharara Khada dupatta - 8 count', 'Some description', 'Some summary', 11.99,
       'NONE', 0.0);
INSERT INTO items (NAME, DESCRIPTION, SUMMARY, PRICE, DISCOUNT_TYPE, DISCOUNT_AMOUNT)
VALUES('Anarkali Cream and White', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vel.',
       'Some summary', 39.99, 'NONE', 0.0);
INSERT INTO items (NAME, DESCRIPTION, SUMMARY, PRICE, DISCOUNT_TYPE, DISCOUNT_AMOUNT)
VALUES('Corsair Sabre v2 Pro Wireless Gaming Mouse - 33,000 DPI', 'Suspendisse potenti. Nulla sodales congue dolor.',
       'Some summary', 99.99, 'NONE', 0.0);
INSERT INTO items (NAME, DESCRIPTION, SUMMARY, PRICE, DISCOUNT_TYPE, DISCOUNT_AMOUNT)
VALUES('EPOMAKER Aula F108 Wireless Gaming Keyboard, Full Size', 'Nulla non dictum augue, eleifend rutrum sapien.',
       'Some summary', 99.99, 'NONE', 0.0);
INSERT INTO items (NAME, DESCRIPTION, SUMMARY, PRICE, DISCOUNT_TYPE, DISCOUNT_AMOUNT)
VALUES('Redragon K630 60% Wired RGB Gaming Keyboard - Blue Switches', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vel.',
       'Some summary', 49.99, 'NONE', 0.0);

-- ItemCategory Table
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(1, 3);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(1, 4);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(2, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(2, 2);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(3, 2);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(4, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(4, 2);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(5, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(5, 2);

INSERT INTO item_variants (ITEM_ID, COLOR, SIZE, PRICE, STOCK)
VALUES (1, 'red', 'MD', 29.99, 10);
INSERT INTO item_variants (ITEM_ID, COLOR, SIZE, PRICE, STOCK)
VALUES (1, 'blue', 'SM', 29.99, 7);
INSERT INTO item_variants (ITEM_ID, COLOR, SIZE, PRICE, STOCK)
VALUES (1, 'teal', 'LG', 29.99, 15);
INSERT INTO item_variants (ITEM_ID, COLOR, SIZE, PRICE, STOCK)
VALUES (2, 'green', 'MD', 64.99, 7);
INSERT INTO item_variants (ITEM_ID, COLOR, SIZE, PRICE, STOCK)
VALUES (2, 'red', 'XL', 115.99, 12);