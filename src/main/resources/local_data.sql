INSERT INTO post_user(name, email, password, role)
SELECT
    'admin',
    'test@example.com',
    '$2a$10$JZQ8rcukybgpdpg0HufATehPDkoRzlPB0RDD30h3QD7s9CQnCpXhW',
    0
WHERE (SELECT COUNT(*) FROM post_user) = 0;