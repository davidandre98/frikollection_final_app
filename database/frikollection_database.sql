-- Creació de la base de dades
CREATE DATABASE Frikollection;
GO

USE Frikollection;
GO

-- Usuaris
CREATE TABLE [User] (
    user_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    username NVARCHAR(50) NOT NULL UNIQUE,
    avatar NVARCHAR(MAX),
    nickname NVARCHAR(50),
    first_name NVARCHAR(50),
    last_name NVARCHAR(50),
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone_number NVARCHAR(20),
    birthday DATE,
    password NVARCHAR(MAX) NOT NULL,
    biography NVARCHAR(MAX),
    register_date DATETIME DEFAULT GETDATE(),
    last_login DATETIME
);

-- Col·leccions de l'usuari
CREATE TABLE [Collection] (
    collection_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    user_id UNIQUEIDENTIFIER NOT NULL,
    name NVARCHAR(100) NOT NULL,
    private BIT DEFAULT 0,
    creation_date DATE DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES [User](user_id) ON DELETE CASCADE
);

-- Col·leccions de seguiment
CREATE TABLE User_Follow_Collection (
    user_id UNIQUEIDENTIFIER NOT NULL,
    collection_id UNIQUEIDENTIFIER NOT NULL,
    follow_date DATETIME DEFAULT GETDATE(),
    notifications_enabled BIT DEFAULT 1,
    PRIMARY KEY (user_id, collection_id),
    FOREIGN KEY (user_id) REFERENCES [User](user_id) ON DELETE CASCADE,
    FOREIGN KEY (collection_id) REFERENCES [Collection](collection_id)
);

-- Notificacions
CREATE TABLE Notification (
    notification_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    recipient_user_id UNIQUEIDENTIFIER NOT NULL,
    follower_user_id UNIQUEIDENTIFIER NOT NULL,
    collection_id UNIQUEIDENTIFIER NOT NULL,
    message NVARCHAR(300) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    is_read BIT NOT NULL DEFAULT 0,

    CONSTRAINT FK_Notification_Recipient FOREIGN KEY (recipient_user_id)
        REFERENCES [User](user_id) ON DELETE CASCADE,

    CONSTRAINT FK_Notification_Follower FOREIGN KEY (follower_user_id)
        REFERENCES [User](user_id) ON DELETE NO ACTION,

    CONSTRAINT FK_Notification_Collection FOREIGN KEY (collection_id)
        REFERENCES [Collection](collection_id) ON DELETE NO ACTION
);

-- Tipus de producte
CREATE TABLE Product_Type (
    product_type_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    type_name NVARCHAR(100) NOT NULL,
    hasExtension BIT DEFAULT 0
);

-- Extensions de productes
CREATE TABLE Product_Extension (
    product_extension_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    package NVARCHAR(100),
    expansion NVARCHAR(100),
	hp INT,
    pokemon_types NVARCHAR(100),
    evolves_from NVARCHAR(100),
    abilities NVARCHAR(MAX),
    attacks NVARCHAR(MAX),
    converted_retreat_cost INT
);

-- Productes
CREATE TABLE Product (
    product_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    name NVARCHAR(100) NOT NULL,
    type NVARCHAR(100),
    subtype NVARCHAR(100),
    release DATE,
    status NVARCHAR(50),
    item_number NVARCHAR(50),
    license NVARCHAR(100),
    width FLOAT,
    height FLOAT,
    value DECIMAL(10,2),
    small_picture NVARCHAR(MAX),
    big_picture NVARCHAR(MAX),
    product_type_id UNIQUEIDENTIFIER,
    product_extension_id UNIQUEIDENTIFIER,
    FOREIGN KEY (product_type_id) REFERENCES Product_Type(product_type_id),
    FOREIGN KEY (product_extension_id) REFERENCES Product_Extension(product_extension_id)
);

-- Assignació de productes a col·leccions
CREATE TABLE Collection_Product (
    collection_id UNIQUEIDENTIFIER NOT NULL,
    product_id UNIQUEIDENTIFIER NOT NULL,
    added_date DATE DEFAULT GETDATE(),
    PRIMARY KEY (collection_id, product_id),
    FOREIGN KEY (collection_id) REFERENCES Collection(collection_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

-- Etiquetes (tags)
CREATE TABLE Tag (
    tag_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    name NVARCHAR(50) NOT NULL,
    tag_image NVARCHAR(MAX)
);

-- Assignació de tags a productes (relació many-to-many)
CREATE TABLE Product_Tag (
    product_id UNIQUEIDENTIFIER NOT NULL,
    tag_id UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY (product_id, tag_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tag(tag_id) ON DELETE CASCADE
);