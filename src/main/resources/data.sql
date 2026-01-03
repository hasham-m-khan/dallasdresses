-- ============================================================================
-- USERS TABLE - More diverse users
-- ============================================================================
INSERT INTO users (EMAIL, FIRST_NAME, LAST_NAME, ROLE, LOCALE, EMAIL_VERIFIED, TELEPHONE, AVATAR) VALUES
    ('john.doe@xyzmail.com', 'John', 'Doe', 'USER', 'en', true, '+1-555-0101', 'https://i.pravatar.cc/150?img=12'),
    ('danielle.jackson@xyzmail.com', 'Danielle', 'Jackson', 'STAFF', 'en', true, '+1-555-0102', 'https://i.pravatar.cc/150?img=5'),
    ('jeremiah.daniels@xyzmail.com', 'Jeremiah', 'Daniels', 'USER', 'en', true, '+1-555-0103', 'https://i.pravatar.cc/150?img=33'),
    ('sarah.martinez@xyzmail.com', 'Sarah', 'Martinez', 'USER', 'es', true, '+1-555-0104', 'https://i.pravatar.cc/150?img=47'),
    ('michael.chen@xyzmail.com', 'Michael', 'Chen', 'USER', 'en', true, '+1-555-0105', 'https://i.pravatar.cc/150?img=15'),
    ('emily.brown@xyzmail.com', 'Emily', 'Brown', 'USER', 'en', true, '+1-555-0106', 'https://i.pravatar.cc/150?img=20'),
    ('admin@dallasdresses.com', 'Admin', 'User', 'ADMIN', 'en', true, '+1-555-0100', 'https://i.pravatar.cc/150?img=68'),
    ('jessica.wilson@xyzmail.com', 'Jessica', 'Wilson', 'USER', 'en', true, '+1-555-0107', 'https://i.pravatar.cc/150?img=23'),
    ('david.lee@xyzmail.com', 'David', 'Lee', 'USER', 'en', true, '+1-555-0108', 'https://i.pravatar.cc/150?img=52'),
    ('maria.garcia@xyzmail.com', 'Maria', 'Garcia', 'USER', 'es', true, '+1-555-0109', 'https://i.pravatar.cc/150?img=45');

-- ============================================================================
-- COUNTRIES TABLE
-- ============================================================================
INSERT INTO countries (name) VALUES
    ('USA'),
    ('Canada'),
    ('Mexico'),
    ('India'),
    ('Spain'),
    ('United Kingdom'),
    ('Australia'),
    ('France'),
    ('Germany'),
    ('Italy');

-- ============================================================================
-- ADDRESSES TABLE - Multiple addresses for various users
-- ============================================================================
-- John Doe's addresses
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES
    ('123 Main Street', 'Queens', 'NY', 1, '11234', 'RESIDENTIAL', 1),
    ('W 42nd Street', 'New York', 'NY', 1, '10036', 'MAIN', 1),
    ('1060 W Addison ST', 'Chicago', 'IL', 1, '60613', 'ALTERNATE', 1);

-- Danielle Jackson's addresses
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES
    ('456 Oak Avenue', 'Los Angeles', 'CA', 1, '90001', 'RESIDENTIAL', 2),
    ('789 Pine Road', 'San Diego', 'CA', 1, '92101', 'MAIN', 2);

-- Jeremiah Daniels' address
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES
    ('321 Elm Street', 'Dallas', 'TX', 1, '75201', 'RESIDENTIAL', 3);

-- Sarah Martinez's address
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES
    ('555 Maple Drive', 'Miami', 'FL', 1, '33101', 'RESIDENTIAL', 4),
    ('777 Beach Boulevard', 'Miami Beach', 'FL', 1, '33139', 'MAIN', 4);

-- Michael Chen's address
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES
    ('888 Tech Center', 'San Francisco', 'CA', 1, '94102', 'RESIDENTIAL', 5);

-- Emily Brown's address
INSERT INTO addresses (ADDRESS_LINE1, CITY, STATE, COUNTRY_ID, POSTAL_CODE, ADDRESS_TYPE, USER_ID)
VALUES
    ('999 Garden Lane', 'Seattle', 'WA', 1, '98101', 'RESIDENTIAL', 6);

-- ============================================================================
-- CATEGORIES TABLE
-- ============================================================================
INSERT INTO categories (NAME, SLUG) VALUES
    ('Women''s', 'womens'),
    ('Men''s', 'mens'),
    ('Kids', 'kids'),
    ('Frock', 'frock'),
    ('Suit', 'suit'),
    ('Luxury', 'luxury'),
    ('Angrakha', 'angrakha'),
    ('Dupatta', 'dupatta'),
    ('Chiffon', 'chiffon'),
    ('Cotton', 'cotton'),
    ('Organza', 'organza'),
    ('Chikankari', 'chikankari'),
    ('Gown', 'gown'),
    ('Sharara', 'sharara'),
    ('Summer Collection', 'summer-collection'),
    ('Winter Collection', 'winter-collection'),
    ('Wedding', 'wedding'),
    ('Casual', 'casual'),
    ('Formal', 'formal'),
    ('Party Wear', 'party-wear');

-- ============================================================================
-- ITEMS TABLE - More diverse items with various properties
-- ============================================================================
INSERT INTO items (NAME, DESCRIPTION, COLOR, SIZE, STOCK, PRICE, DISCOUNT_TYPE, DISCOUNT_VALUE, AVERAGE_RATING, TOTAL_RATINGS, PARENT_ID, IS_PARENT)
VALUES
    -- Item 1: Parent item with variants
    ('Zinc Shisha Work Frock', 'Elegant frock with intricate shisha work. Perfect for special occasions. Made with high-quality fabric and detailed embroidery.', 'Zinc', 'SM', 7, 67.00, 'NONE', 0, 4.5, 12, NULL, false),

    -- Item 2: Parent item (already has child #3)
    ('Peach Chiffon Suit', 'Beautiful lightweight chiffon suit with delicate embroidery. Comfortable and elegant for any occasion.', 'Peach', 'MD', 15, 59.00, 'PERCENTAGE', 10, 4.8, 25, NULL, true),

    -- Item 3: Child of Item 2
    ('Skin Chiffon Suit', 'Variant of the popular Peach Chiffon Suit in a stunning skin tone. Same quality and design.', 'Skin', 'MD', 17, 62.00, 'PERCENTAGE', 10, 4.7, 18, 2, false),

    -- Item 4: Standalone
    ('Peach Kundan Neck Suit', 'Exquisite suit with traditional Kundan work on the neckline. A perfect blend of tradition and elegance.', 'Peach', 'MD', 13, 52.00, 'NONE', 0, 4.3, 8, NULL, false),

    -- Item 5: Standalone
    ('Maroon Frock', 'Classic maroon frock with modern cut. Comfortable fit and vibrant color.', 'Maroon', 'LG', 6, 42.00, 'FIXED', 5, 4.0, 5, NULL, false),

    -- Additional items for variety
    ('Royal Blue Angrakha', 'Traditional Angrakha style in royal blue with gold embellishments. Perfect for festive occasions.', 'Royal Blue', 'LG', 10, 89.00, 'PERCENTAGE', 15, 4.9, 32, NULL, false),

    ('Emerald Green Gown', 'Stunning emerald green evening gown with flowing silhouette. Ideal for formal events and weddings.', 'Emerald Green', 'MD', 5, 125.00, 'NONE', 0, 4.6, 15, NULL, false),

    ('Ivory Wedding Sharara', 'Elegant ivory sharara set with intricate zari work. Perfect for the modern bride.', 'Ivory', 'SM', 8, 150.00, 'PERCENTAGE', 20, 5.0, 42, NULL, false),

    ('Black Cotton Suit', 'Comfortable cotton suit in classic black. Perfect for everyday wear with a touch of elegance.', 'Black', 'LG', 20, 45.00, 'NONE', 0, 4.2, 28, NULL, false),

    ('Pink Chikankari Kurta', 'Delicate Chikankari embroidery on soft pink fabric. Traditional craftsmanship meets modern style.', 'Pink', 'MD', 12, 55.00, 'FIXED', 8, 4.4, 19, NULL, false),

    -- Parent with variants (Organza Collection)
    ('Golden Organza Dupatta Set', 'Luxurious organza dupatta set with golden thread work. Complete outfit for special occasions.', 'Golden', 'MD', 9, 95.00, 'PERCENTAGE', 12, 4.7, 22, NULL, true),

    ('Silver Organza Dupatta Set', 'Variant of the Golden Organza set in stunning silver. Same luxury, different elegance.', 'Silver', 'MD', 11, 95.00, 'PERCENTAGE', 12, 4.6, 16, 11, false),

    ('Rose Gold Organza Dupatta Set', 'Modern rose gold variant of the popular Organza Dupatta Set. Trendy and sophisticated.', 'Rose Gold', 'MD', 8, 98.00, 'PERCENTAGE', 12, 4.8, 20, 11, false),

    -- More standalone items
    ('Navy Blue Formal Suit', 'Professional navy blue suit perfect for business meetings and formal events.', 'Navy Blue', 'LG', 15, 78.00, 'NONE', 0, 4.1, 11, NULL, false),

    ('Coral Summer Dress', 'Light and breezy coral dress perfect for summer days. Comfortable and stylish.', 'Coral', 'SM', 18, 38.00, 'PERCENTAGE', 5, 4.5, 30, NULL, false),

    ('Wine Velvet Gown', 'Rich wine-colored velvet gown for winter events. Warm, elegant, and luxurious.', 'Wine', 'MD', 6, 135.00, 'FIXED', 15, 4.9, 25, NULL, false),

    ('Mint Green Party Wear', 'Fresh mint green outfit perfect for parties and celebrations. Modern cut with traditional elements.', 'Mint Green', 'SM', 14, 68.00, 'PERCENTAGE', 8, 4.3, 17, NULL, false),

    ('Burgundy Bridal Lehenga', 'Stunning burgundy bridal lehenga with heavy embroidery and embellishments. Makes every bride feel special.', 'Burgundy', 'MD', 3, 250.00, 'NONE', 0, 5.0, 38, NULL, false),

    ('Cream Cotton Casual', 'Comfortable cream cotton outfit for daily wear. Simple, elegant, and practical.', 'Cream', 'LG', 25, 35.00, 'NONE', 0, 3.9, 12, NULL, false),

    ('Turquoise Beach Kaftan', 'Flowing turquoise kaftan perfect for beach vacations and summer getaways.', 'Turquoise', 'MD', 20, 42.00, 'FIXED', 5, 4.4, 23, NULL, false);

-- ============================================================================
-- ITEM_CATEGORY TABLE - Assign items to categories
-- ============================================================================
INSERT INTO item_category (ITEM_ID, CATEGORY_ID) VALUES
    -- Item 1: Zinc Shisha Work Frock
    (1, 1), (1, 4), (1, 6), (1, 20),

    -- Item 2: Peach Chiffon Suit
    (2, 1), (2, 5), (2, 9), (2, 15),

    -- Item 3: Skin Chiffon Suit
    (3, 1), (3, 5), (3, 9), (3, 15),

    -- Item 4: Peach Kundan Neck Suit
    (4, 1), (4, 5), (4, 6),

    -- Item 5: Maroon Frock
    (5, 1), (5, 4), (5, 18),

    -- Item 6: Royal Blue Angrakha
    (6, 1), (6, 7), (6, 6), (6, 20),

    -- Item 7: Emerald Green Gown
    (7, 1), (7, 13), (7, 19), (7, 20),

    -- Item 8: Ivory Wedding Sharara
    (8, 1), (8, 14), (8, 17), (8, 6),

    -- Item 9: Black Cotton Suit
    (9, 1), (9, 5), (9, 10), (9, 18),

    -- Item 10: Pink Chikankari Kurta
    (10, 1), (10, 12), (10, 18),

    -- Item 11-13: Organza Dupatta Sets
    (11, 1), (11, 8), (11, 11), (11, 6), (11, 20),
    (12, 1), (12, 8), (12, 11), (12, 6), (12, 20),
    (13, 1), (13, 8), (13, 11), (13, 6), (13, 20),

    -- Item 14: Navy Blue Formal Suit
    (14, 1), (14, 5), (14, 19),

    -- Item 15: Coral Summer Dress
    (15, 1), (15, 15), (15, 18),

    -- Item 16: Wine Velvet Gown
    (16, 1), (16, 13), (16, 16), (16, 19),

    -- Item 17: Mint Green Party Wear
    (17, 1), (17, 20), (17, 18),

    -- Item 18: Burgundy Bridal Lehenga
    (18, 1), (18, 17), (18, 6),

    -- Item 19: Cream Cotton Casual
    (19, 1), (19, 10), (19, 18),

    -- Item 20: Turquoise Beach Kaftan
    (20, 1), (20, 15), (20, 18);

-- ============================================================================
-- ITEM_IMAGES TABLE - Images for all items
-- ============================================================================
-- Item 1: Zinc Shisha Work Frock
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY) VALUES
    ('https://m.media-amazon.com/images/I/81u-H5hhV5L._AC_SY741_.jpg', 'Front view of Zinc Shisha Work Frock', 1, 1, true),
    ('https://m.media-amazon.com/images/I/71vhJI7YVCL._AC_SY741_.jpg', 'Side view of Zinc Shisha Work Frock', 1, 2, false),
    ('https://m.media-amazon.com/images/I/71d67WvWB2L._AC_SY741_.jpg', 'Detail view of embroidery', 1, 3, false),
    ('https://m.media-amazon.com/images/I/71TFhw86HoL._AC_SY741_.jpg', 'Back view of dress', 1, 4, false);

-- Item 2: Peach Chiffon Suit
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY) VALUES
    ('https://m.media-amazon.com/images/I/81QGplmEQxL._AC_SY741_.jpg', 'Front view of Peach Chiffon Suit', 2, 1, true),
    ('https://m.media-amazon.com/images/I/81EwExy2vzL._AC_SY741_.jpg', 'Side view of suit', 2, 2, false),
    ('https://m.media-amazon.com/images/I/91L7Ma+kMqL._AC_SY741_.jpg', 'Detail of chiffon fabric', 2, 3, false),
    ('https://m.media-amazon.com/images/I/816pGVl5mnL._AC_SY741_.jpg', 'Full outfit view', 2, 4, false);

-- Item 3: Skin Chiffon Suit
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY) VALUES
    ('https://m.media-amazon.com/images/I/81QGplmEQxL._AC_SY741_.jpg', 'Front view of Skin Chiffon Suit', 3, 1, true),
    ('https://m.media-amazon.com/images/I/81EwExy2vzL._AC_SY741_.jpg', 'Side view of suit', 3, 2, false),
    ('https://m.media-amazon.com/images/I/91L7Ma+kMqL._AC_SY741_.jpg', 'Detail view', 3, 3, false);

-- Item 4: Peach Kundan Neck Suit
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY) VALUES
    ('https://m.media-amazon.com/images/I/71d67WvWB2L._AC_SY741_.jpg', 'Front view with Kundan work detail', 4, 1, true),
    ('https://m.media-amazon.com/images/I/71TFhw86HoL._AC_SY741_.jpg', 'Close-up of neckline', 4, 2, false);

-- Item 5: Maroon Frock
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY) VALUES
    ('https://m.media-amazon.com/images/I/81u-H5hhV5L._AC_SY741_.jpg', 'Front view of Maroon Frock', 5, 1, true),
    ('https://m.media-amazon.com/images/I/71vhJI7YVCL._AC_SY741_.jpg', 'Side view', 5, 2, false);

-- Add at least one image for remaining items (6-20)
INSERT INTO item_image (URL, ALT_TEXT, ITEM_ID, DISPLAY_ORDER, IS_PRIMARY) VALUES
    ('https://m.media-amazon.com/images/I/816pGVl5mnL._AC_SY741_.jpg', 'Royal Blue Angrakha', 6, 1, true),
    ('https://m.media-amazon.com/images/I/81jWB80rLCL._AC_SY741_.jpg', 'Emerald Green Gown', 7, 1, true),
    ('https://m.media-amazon.com/images/I/81QGplmEQxL._AC_SY741_.jpg', 'Ivory Wedding Sharara', 8, 1, true),
    ('https://m.media-amazon.com/images/I/81EwExy2vzL._AC_SY741_.jpg', 'Black Cotton Suit', 9, 1, true),
    ('https://m.media-amazon.com/images/I/91L7Ma+kMqL._AC_SY741_.jpg', 'Pink Chikankari Kurta', 10, 1, true),
    ('https://m.media-amazon.com/images/I/816pGVl5mnL._AC_SY741_.jpg', 'Golden Organza Dupatta Set', 11, 1, true),
    ('https://m.media-amazon.com/images/I/81jWB80rLCL._AC_SY741_.jpg', 'Silver Organza Dupatta Set', 12, 1, true),
    ('https://m.media-amazon.com/images/I/81QGplmEQxL._AC_SY741_.jpg', 'Rose Gold Organza Dupatta Set', 13, 1, true),
    ('https://m.media-amazon.com/images/I/81EwExy2vzL._AC_SY741_.jpg', 'Navy Blue Formal Suit', 14, 1, true),
    ('https://m.media-amazon.com/images/I/91L7Ma+kMqL._AC_SY741_.jpg', 'Coral Summer Dress', 15, 1, true),
    ('https://m.media-amazon.com/images/I/816pGVl5mnL._AC_SY741_.jpg', 'Wine Velvet Gown', 16, 1, true),
    ('https://m.media-amazon.com/images/I/81jWB80rLCL._AC_SY741_.jpg', 'Mint Green Party Wear', 17, 1, true),
    ('https://m.media-amazon.com/images/I/81QGplmEQxL._AC_SY741_.jpg', 'Burgundy Bridal Lehenga', 18, 1, true),
    ('https://m.media-amazon.com/images/I/81EwExy2vzL._AC_SY741_.jpg', 'Cream Cotton Casual', 19, 1, true),
    ('https://m.media-amazon.com/images/I/91L7Ma+kMqL._AC_SY741_.jpg', 'Turquoise Beach Kaftan', 20, 1, true);

-- ============================================================================
-- ITEM_RATINGS TABLE - Realistic ratings and reviews
-- ============================================================================
INSERT INTO item_ratings (ITEM_ID, USER_ID, RATING, TITLE, REVIEW_TEXT, VERIFIED_PURCHASE, HELPFUL_VOTES) VALUES
    -- Item 1: Zinc Shisha Work Frock (avg 4.5, 12 ratings)
    (1, 1, 5, 'Absolutely stunning!', 'The shisha work is beautiful and the fabric quality is excellent. Got so many compliments!', true, 15),
    (1, 3, 5, 'Perfect for my event', 'Wore this to a wedding and felt like a princess. Highly recommend!', true, 8),
    (1, 4, 4, 'Beautiful but runs small', 'Gorgeous dress but I had to exchange for a larger size. Make sure to check measurements.', true, 12),
    (1, 5, 5, 'Worth every penny', 'The embroidery detail is incredible. This is now my favorite dress!', true, 6),
    (1, 6, 4, 'Great quality', 'Very well made. The only reason for 4 stars is the price is a bit high.', false, 3),
    (1, 8, 5, 'Love it!', 'Exceeded my expectations. The color is even better in person.', true, 9),
    (1, 9, 4, 'Nice dress', 'Good quality and pretty design. Delivery was fast too.', true, 4),
    (1, 10, 5, 'Simply beautiful', 'This dress is a work of art. Can''t wait to wear it again!', true, 11),
--     (1, 4, 4, 'Lovely', 'Beautiful dress, comfortable to wear all day.', true, 2),
--     (1, 5, 5, 'Perfect!', 'Everything about this dress is perfect!', true, 7),
--     (1, 6, 4, 'Very nice', 'Good purchase overall. Would buy again.', true, 1),
--     (1, 8, 5, 'Gorgeous', 'Absolutely gorgeous! Highly recommend!', true, 5),

    -- Item 2: Peach Chiffon Suit (avg 4.8, 25 ratings) - Adding some
    (2, 1, 5, 'Love the chiffon!', 'So light and comfortable. Perfect for summer events.', true, 20),
    (2, 3, 5, 'Best purchase ever', 'I''ve bought three different colors now. That''s how much I love it!', true, 18),
    (2, 4, 5, 'Elegant and comfortable', 'Rare to find something that''s both stylish and comfortable. This is it!', true, 14),
    (2, 6, 5, 'Highly recommend', 'Beautiful fabric and the embroidery is delicate and pretty.', true, 10),
    (2, 8, 4, 'Great suit', 'Very nice quality. The discount made it even better!', true, 7),
    (2, 9, 5, 'Perfect fit', 'Fits like a glove. The peach color is exactly as shown.', true, 12),
    (2, 10, 5, 'Outstanding', 'This exceeded all my expectations. Will be ordering more!', true, 16),

    -- Item 8: Ivory Wedding Sharara (avg 5.0, 42 ratings) - Adding some
    (8, 1, 5, 'Dream wedding outfit!', 'I wore this on my wedding day and felt absolutely beautiful. The zari work is exquisite!', true, 45),
    (8, 3, 5, 'Perfect for bride', 'Bought this for my daughter''s wedding. She looked stunning!', true, 38),
    (8, 4, 5, 'Breathtaking', 'The ivory color is perfect and the quality is top-notch. Worth every dollar!', true, 42),
    (8, 6, 5, 'Flawless', 'Everything about this is perfect. The fit, the fabric, the embroidery - all flawless!', true, 35),
    (8, 8, 5, 'Most beautiful outfit', 'I''ve never owned anything this beautiful. Made my special day even more special.', true, 50),

    -- Item 7: Emerald Green Gown (avg 4.6, 15 ratings)
    (7, 1, 5, 'Stunning gown', 'Wore this to a gala and received endless compliments. The emerald green is rich and elegant.', true, 12),
    (7, 3, 4, 'Beautiful but long', 'Gorgeous gown but I had to get it hemmed. The color is amazing though!', true, 8),
    (7, 5, 5, 'Perfect for formal events', 'This gown is everything I hoped for. Elegant, classy, and comfortable.', true, 10),
    (7, 9, 5, 'Love it!', 'The flowing silhouette is so flattering. Feel like a movie star!', true, 6),
    (7, 10, 4, 'Very nice', 'Great quality gown. Would have given 5 stars if it came with a matching dupatta.', true, 4),

    -- Item 15: Coral Summer Dress (avg 4.5, 30 ratings)
    (15, 1, 5, 'Summer favorite!', 'This is my go-to summer dress now. So comfortable and the coral color is vibrant!', true, 22),
    (15, 3, 5, 'Love it', 'Perfect for hot days. Light fabric and beautiful color.', true, 18),
    (15, 4, 4, 'Nice dress', 'Good quality for the price. Comfortable and pretty.', true, 10),
    (15, 6, 5, 'Great buy', 'Amazing value! The dress is well-made and fits perfectly.', true, 15),
    (15, 8, 4, 'Pretty color', 'The coral shade is lovely. Dress is comfortable for all-day wear.', true, 8),
    (15, 9, 5, 'Highly recommend', 'Bought two more in different colors. That says it all!', true, 20),

    -- Item 18: Burgundy Bridal Lehenga (avg 5.0, 38 ratings)
    (18, 4, 5, 'Dream bridal outfit', 'This lehenga made me feel like royalty on my wedding day. The embroidery is phenomenal!', true, 55),
    (18, 5, 5, 'Absolutely perfect', 'Every detail is perfect. The burgundy color is regal and the work is intricate.', true, 48),
    (18, 6, 5, 'Best bridal wear', 'I''ve never seen such beautiful craftsmanship. Worth every penny!', true, 52),
    (18, 10, 5, 'Stunning!', 'This is the most beautiful thing I''ve ever owned. Made my wedding unforgettable.', true, 60);
