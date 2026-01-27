CREATE TABLE customer (
      id INT PRIMARY KEY AUTO_INCREMENT ,
      firstname VARCHAR(100) NOT NULL,
      lastname VARCHAR(100) NOT NULL,
      mobilenumber VARCHAR(10) NOT NULL,
      dob DATE NOT NULL,
      gender VARCHAR(20) NOT NULL,
      password VARCHAR(100),
      email VARCHAR(255) UNIQUE NOT NULL,
      role VARCHAR(50) ,
      tenant_id VARCHAR(100)
);

CREATE TABLE customer_address (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        address VARCHAR(255) NOT NULL,
        addresstype VARCHAR(50) NOT NULL,
        cutomer_id INT NOT NULL,
        CONSTRAINT fk_customer_address
          FOREIGN KEY (cutomer_id)
              REFERENCES customer(id)
              ON DELETE CASCADE
);
