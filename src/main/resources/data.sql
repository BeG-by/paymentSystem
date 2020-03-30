INSERT INTO address
VALUES (null, 'Minsk', '12a', 'Nemiga');
INSERT INTO address
VALUES (null, 'Grodno', '111', 'Nevski');
INSERT INTO address
VALUES (null, 'Brest', '13a', 'Voronyma');
INSERT INTO address
VALUES (null, 'Minsk', '4a', 'Lenina');

INSERT INTO user
VALUES (null, '1990-03-10', 'something@gmail.com', 'firstName', 'lastName', '2020-01-01', 'PP111111111', 'password',
        'ACTIVE',
        'ADMIN', 1);
INSERT INTO user
VALUES (null, '1991-04-11', 'something1@gmail.com', 'firstName', 'lastName', '2020-01-01', 'PP222222222', 'password',
        'ACTIVE',
        'ADMIN', 2);
INSERT INTO user
VALUES (null, '1992-05-12', 'something2@gmail.com', 'firstName', 'lastName', '2020-01-01', 'PP333333333', 'password',
        'ACTIVE',
        'USER', 3);
INSERT INTO user
VALUES (null, '1993-06-13', 'something3@gmail.com', 'firstName', 'lastName', '2020-01-01', 'PP444444444', 'password',
        'ACTIVE',
        'USER', 4);

INSERT INTO token VALUE (null, '2020-10-10', 'test', 1);
INSERT INTO token VALUE (null, '2020-10-10', 'test1', 2);
INSERT INTO token VALUE (null, '2020-10-10', '3', 3);
INSERT INTO token VALUE (null, '2019-10-10', '4', 4);

INSERT INTO wallet VALUE (null, 1000, '2020-01-01', 'USD', 'USD111111111', 1);
INSERT INTO wallet VALUE (null, 1000, '2020-01-01', 'EUR', 'EUR222222222', 1);
INSERT INTO wallet VALUE (null, 1000, '2020-01-01', 'USD', 'USD333333333', 2);
INSERT INTO wallet VALUE (null, 1000, '2020-01-01', 'EUR', 'EUR444444444', 2);
INSERT INTO wallet VALUE (null, 1000, '2020-01-01', 'USD', 'USD555555555', 3);
INSERT INTO wallet VALUE (null, 10000, '2020-01-01', 'BYN', 'BYN666666666', 3);
INSERT INTO wallet VALUE (null, 1000, '2020-01-01', 'USD', 'USD777777777', 4);
INSERT INTO wallet VALUE (null, 10000, '2020-01-01', 'RUB', 'RUB888888888', 4);

INSERT INTO deposit VALUE (null, 'USD', true, 'USD240', 240, 2.5, 'AVAILABLE');
INSERT INTO deposit VALUE (null, 'USD', true, 'USD90', 90, 1.7, 'AVAILABLE');
INSERT INTO deposit VALUE (null, 'BYN', true, 'BYN360', 360, 11.2, 'AVAILABLE');
INSERT INTO deposit VALUE (null, 'EUR', true, 'EUR180', 180, 2.1, 'AVAILABLE');
INSERT INTO deposit VALUE (null, 'RUB', true, 'RUB240', 240, 7.4, 'AVAILABLE');
INSERT INTO deposit VALUE (null, 'USD', true, 'USD360', 360, 0.1, 'DELETED');
INSERT INTO deposit VALUE (null, 'RUB', true, 'RUB360', 360, 0.1, 'DELETED');

INSERT INTO transfer_detail VALUE (null, 'USD', '2011-01-01', 1, 1, 'USD111111111', 1);
INSERT INTO transfer_detail VALUE (null, 'EUR', '2014-01-01', 1, 1, 'EUR222222222', 1);
INSERT INTO transfer_detail VALUE (null, 'BYN', '2017-01-01', 1, 1, 'BYN666666666', 2);
INSERT INTO transfer_detail VALUE (null, 'RUB', '2018-01-01', 1, 1, 'USD111111111', 2);
INSERT INTO transfer_detail VALUE (null, 'USD', '2020-01-01', 100, 100, 'USD555555555', 3);
INSERT INTO transfer_detail VALUE (null, 'USD', '2020-03-13', 100, 100, 'USD555555555', 3);
INSERT INTO transfer_detail VALUE (null, 'BYN', '2011-01-01', 100, 100, 'BYN666666666', 4);

INSERT INTO credit VALUE (null, 'USD', 'USD360', 360, 7, 'AVAILABLE');
INSERT INTO credit VALUE (null, 'BYN', 'BYN360', 360, 15, 'AVAILABLE');
INSERT INTO credit VALUE (null, 'EUR', 'EUR360', 360, 6, 'DELETED');
INSERT INTO credit VALUE (null, 'RUB', 'RUB720', 720, 10.1, 'AVAILABLE');

INSERT INTO deposit_detail VALUE (null, 100, 'AVAILABLE', '2022-01-01', 120, '2019-01-01', 1, 1);
INSERT INTO deposit_detail VALUE (null, 100, 'UNAVAILABLE', '2015-01-01', 120, '2013-01-01', 1, 1);
INSERT INTO deposit_detail VALUE (null, 1000, 'AVAILABLE', '2024-01-01', 1300, '2017-01-01', 2, 3);
INSERT INTO deposit_detail VALUE (null, 500, 'UNAVAILABLE', '2013-01-01', 545, '2011-01-01', 6, 3);
INSERT INTO deposit_detail VALUE (null, 100, 'AVAILABLE', '2021-01-01', 120, '2018-01-01', 4, 4);

INSERT INTO credit_detail VALUE (null, 'OPEN', 200, '2025-02-02', 220, '2020-03-29', '2020-03-29', 180, 1, 1);
INSERT INTO credit_detail VALUE (null, 'OPEN', 300, '2025-02-02', 350, '2020-03-29', '2020-03-29', 180, 2, 2);
INSERT INTO credit_detail VALUE (null, 'OPEN', 400, '2025-02-02', 460, '2020-03-29', '2020-03-29', 180, 3, 3);
INSERT INTO credit_detail VALUE (null, 'CLOSED', 500, '2025-02-02', 557, '2020-03-29', '2020-03-29', 180, 4, 4);

