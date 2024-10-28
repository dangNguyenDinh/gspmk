USE master;

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'GSPMKManager')
BEGIN

    DROP DATABASE GSPMKManager;
END
GO

CREATE DATABASE GSPMKManager;
GO
USE GSPMKManager; 
GO
-- B?ng Users
CREATE TABLE Users (
    id INTEGER NOT NULL IDENTITY PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(50) NOT NULL,
    buyingSessionId INTEGER NULL,  -- Ng??i d�ng c� th? kh�ng tham gia phi�n n�o (NULL)
    moneyAccount INTEGER NOT NULL
);
GO

-- B?ng Products
CREATE TABLE Products (
    id INTEGER NOT NULL IDENTITY PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    type NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) NULL,
    readyState BIT DEFAULT 1  -- 1: S?n s�ng, 0: Kh�ng s?n s�ng
);
GO

-- B?ng BuyingSessions
CREATE TABLE BuyingSessions (
    id INTEGER NOT NULL IDENTITY PRIMARY KEY,
    startTime DATETIME NOT NULL,
    endTime DATETIME NULL  -- C� th? NULL khi phi�n ?ang di?n ra
);
GO

-- B?ng UsersInBuyingSessions - Li�n k?t nhi?u User v?i nhi?u BuyingSessions
CREATE TABLE UsersInBuyingSessions (
    UserID INTEGER NOT NULL,
    BuyingSessionsID INTEGER NOT NULL,
    PRIMARY KEY (UserID, BuyingSessionsID),
    FOREIGN KEY (UserID) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (BuyingSessionsID) REFERENCES BuyingSessions(id) ON DELETE CASCADE
);
GO

-- B?ng BuyingSessionsHasProducts - Li�n k?t gi?a c�c phi�n mua s?m v� s?n ph?m
CREATE TABLE BuyingSessionsHasProducts (
    SessionID INTEGER NOT NULL,
    ProductID INTEGER NOT NULL,
    PRIMARY KEY (SessionID, ProductID),
    FOREIGN KEY (SessionID) REFERENCES BuyingSessions(id) ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Products(id) ON DELETE CASCADE
);
GO

-- B?ng Cart - Gi? h�ng li�n k?t v?i c�c phi�n mua s?m
CREATE TABLE Cart (
    id INTEGER NOT NULL IDENTITY PRIMARY KEY,
    BuyingSessionsID INTEGER NOT NULL,
    base64String NVARCHAR(MAX) NOT NULL,  -- Chu?i base64 ?? l?u tr? h�nh ?nh ho?c d? li?u kh�c
    FOREIGN KEY (BuyingSessionsID) REFERENCES BuyingSessions(id) ON DELETE CASCADE
);
GO

CREATE TRIGGER check_buying_session_update
ON Users
INSTEAD OF UPDATE
AS
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM inserted i
        JOIN deleted d ON i.id = d.id
        WHERE d.buyingSessionId IS NOT NULL AND i.buyingSessionId IS NOT NULL
    )
    BEGIN
        RAISERROR ('Kh�ng th? c?p nh?t buyingSessionId v� ng??i d�ng ?� tham gia m?t phi�n kh�c', 16, 1);
        ROLLBACK TRANSACTION;  
    END
    ELSE
    BEGIN
        UPDATE Users
        SET username = i.username,
            password = i.password,
            buyingSessionId = i.buyingSessionId,
            moneyAccount = i.moneyAccount
        FROM inserted i
        WHERE Users.id = i.id;
    END
END;
GO
