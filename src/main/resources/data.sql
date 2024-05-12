INSERT INTO offline_shop (shop_id, address, shop_name) VALUES
                                                           (1, '123 Main St', 'ABC Mart'),
                                                           (2, '456 Oak St', 'XYZ Store'),
                                                           (3, '789 Pine St', '123 Electronics'),
                                                           (4, '101 Maple St', 'Fashion Trends'),
                                                           (5, '222 Cedar St', 'Tech Haven');

INSERT INTO product (product_id, description, product_name) VALUES
                                                                (1, 'Electronics', 'Laptop'),
                                                                (2, 'Clothing', 'T-Shirt'),
                                                                (3, 'Electronics', 'Smartphone'),
                                                                (4, 'Accessories', 'Headphones'),
                                                                (5, 'Clothing', 'Jeans');

INSERT INTO availability (price, quantity, offer_id, product_offer, shop_offer) VALUES
                                                                                    (999.99, 10, 1, 1, 1),
                                                                                    (888, 0, 6, 1, 2),
                                                                                    (19.99, 50, 2, 2, 2),
                                                                                    (799.99, 8, 3, 1, 3),
                                                                                    (49.99, 0, 4, 4, 4),
                                                                                    (129.99, 15, 5, 3, 5);

