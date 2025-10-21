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
INSERT INTO categories (NAME, SLUG) VALUES ('Jackets', 'jackets');
INSERT INTO categories (NAME, SLUG) VALUES ('Jeans', 'jeans');
INSERT INTO categories (NAME, SLUG) VALUES ('Mens', 'mens');
INSERT INTO categories (NAME, SLUG) VALUES ('Womens', 'womens');