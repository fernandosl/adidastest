CREATE TABLE IF NOT EXISTS SUBS_SUBSCRIPTIONS (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    surname1 VARCHAR(250),
    surname2 VARCHAR(250),
    gender VARCHAR(20),
    email VARCHAR(250) NOT NULL,
    consent_flag CHAR(1) NOT NULL,
    newsletter_id INT(10) NOT NULL,
    birth_date DATE,
    send_email_id INT(10),
    creation_date DATE,
    cancel_date DATE
);

commit;
