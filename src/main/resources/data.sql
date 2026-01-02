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


-- Countries Table
INSERT INTO countries (name) VALUES ('USA');
INSERT INTO countries (name) VALUES ('INDIA');
INSERT INTO countries (name) VALUES ('SPAIN');


-- Addresses Table
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
    VALUES ('123 Main Street', 'Queens', 'NY', 1, '11234',
            'RESIDENTIAL', 1);

INSERT INTO addresses (ADDRESS_LINE1, ADDRESS_LINE2, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES ('W 42nd Street', 'Building #434', 'New York', 'NY', 1,
        '10036', 'ALTERNATE', 1);

INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES ('1060 W Addison ST', 'Chicago', 'IL', 1, '60613',
        'RESIDENTIAL', 1);


-- Categories Table
INSERT INTO categories (NAME, SLUG) VALUES ('Women''s', 'womens');
INSERT INTO categories (NAME, SLUG) VALUES ('Men''s', 'mens');
INSERT INTO categories (NAME, SLUG) VALUES ('Kids', 'kids');
INSERT INTO categories (NAME, SLUG) VALUES ('Frock', 'frock');
INSERT INTO categories (NAME, SLUG) VALUES ('Suit', 'suit');
INSERT INTO categories (NAME, SLUG) VALUES ('Luxury', 'luxury');
INSERT INTO categories (NAME, SLUG) VALUES ('Angrakha', 'angrakha');
INSERT INTO categories (NAME, SLUG) VALUES ('Dupatta', 'dupatta');
INSERT INTO categories (NAME, SLUG) VALUES ('Chiffon', 'chiffon');
INSERT INTO categories (NAME, SLUG) VALUES ('Cotton', 'cotton');
INSERT INTO categories (NAME, SLUG) VALUES ('Organza', 'organza');
INSERT INTO categories (NAME, SLUG) VALUES ('Chikankari', 'chikankari');
INSERT INTO categories (NAME, SLUG) VALUES ('Gown', 'gown');
INSERT INTO categories (NAME, SLUG) VALUES ('Sharara', 'sharara');


-- Items Table
INSERT INTO items (NAME, DESCRIPTION, COLOR, SIZE, STOCK, PRICE, DISCOUNT_TYPE, DISCOUNT_VALUE)
VALUES('Zinc Shisha Work Frock', 'Some description', 'Zinc', 'SM',
       7, 67.00, 'NONE', 0);
INSERT INTO items (NAME, DESCRIPTION, COLOR, SIZE, STOCK, PRICE, DISCOUNT_TYPE, DISCOUNT_VALUE)
VALUES('Peach Chiffon Suit', 'Some description', 'Peach', 'MD',
       15, 59.00, 'NONE', 0);
INSERT INTO items (NAME, DESCRIPTION, COLOR, SIZE, STOCK, PRICE, DISCOUNT_TYPE, DISCOUNT_VALUE, PARENT_ID)
VALUES('Skin Chiffon Suit', 'Some description', 'Skin', 'MD',
       17, 62.00, 'NONE', 0, 2);
INSERT INTO items (NAME, DESCRIPTION, COLOR, SIZE, STOCK, PRICE, DISCOUNT_TYPE, DISCOUNT_VALUE)
VALUES('Peach Kundan Neck Suit', 'Some description', 'Peach', 'MD',
       13, 52.00, 'NONE', 0);
INSERT INTO items (NAME, DESCRIPTION, COLOR, SIZE, STOCK, PRICE, DISCOUNT_TYPE, DISCOUNT_VALUE)
VALUES('Maroon Frock', 'Some description', 'Maroon', 'LG',
       6, 12, 'NONE', 0);


-- ItemCategory Table
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(1, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(2, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(3, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(4, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(5, 1);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(1, 4);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(2, 5);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(3, 5);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(4, 5);
INSERT INTO item_category (ITEM_ID, CATEGORY_ID)
VALUES(5, 4);

INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
       'https://m.media-amazon.com/images/I/81u-H5hhV5L._AC_SY741_.jpg',
       'A lady wearing a dress',
       1, 1, true);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/71vhJI7YVCL._AC_SY741_.jpg',
          'A lady wearing a dress',
          1, 2, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/71d67WvWB2L._AC_SY741_.jpg',
          'A lady wearing a dress',
          1, 3, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/71TFhw86HoL._AC_SY741_.jpg',
          'A lady wearing a dress',
          1, 4, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/71d67WvWB2L._AC_SY741_.jpg',
          'A lady wearing a dress',
          1, 5, false);

INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/81QGplmEQxL._AC_SY741_.jpg',
          'A lady wearing a dress',
          2, 1, true);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/81EwExy2vzL._AC_SY741_.jpg',
          'A lady wearing a dress',
          2, 2, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/91L7Ma+kMqL._AC_SY741_.jpg',
          'A lady wearing a dress',
          2, 3, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/816pGVl5mnL._AC_SY741_.jpg',
          'A lady wearing a dress',
          2, 4, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/81jWB80rLCL._AC_SY741_.jpg',
          'A lady wearing a dress',
          2, 5, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/81QGplmEQxL._AC_SY741_.jpg',
          'A lady wearing a dress',
          3, 1, true);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/81EwExy2vzL._AC_SY741_.jpg',
          'A lady wearing a dress',
          3, 2, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/91L7Ma+kMqL._AC_SY741_.jpg',
          'A lady wearing a dress',
          3, 3, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/816pGVl5mnL._AC_SY741_.jpg',
          'A lady wearing a dress',
          3, 4, false);
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY)
VALUES(
          'https://m.media-amazon.com/images/I/81jWB80rLCL._AC_SY741_.jpg',
          'A lady wearing a dress',
          3, 5, false);