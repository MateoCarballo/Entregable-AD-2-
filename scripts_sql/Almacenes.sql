/*
-- Base de datos: almacenes

*/
-- Eliminar la base de datos si ya existe
DROP DATABASE IF EXISTS almacenes;

-- Crear la base de datos
CREATE DATABASE almacenes
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Galician_Spain.1252'
    LC_CTYPE = 'Galician_Spain.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;



-- En clase me pide la de

-- 'Galician_Spain.1252'
-- LC_CTYPE = 'Galician_Spain.1252'

-- En casa me pide esta
    -- Crear la base de datos
 --    CREATE DATABASE almacenes
 --        WITH
 --        OWNER = postgres
 --        ENCODING = 'UTF8'
 --        LC_COLLATE = 'Spanish_Spain.1252'
 --        LC_CTYPE = 'Spanish_Spain.1252'
 --        LOCALE_PROVIDER = 'libc'
 --        TABLESPACE = pg_default
 --        CONNECTION LIMIT = -1
 --        IS_TEMPLATE = False;


-- Crear tablas y tipo compuesto
CREATE TABLE almacenes (
    id_almacen       SERIAL PRIMARY KEY,  
    nombre_almacen   VARCHAR(100),
    ubicacion        VARCHAR(100)
);

CREATE TABLE categorias (
    id_categoria     SERIAL PRIMARY KEY,  
    nombre_categoria VARCHAR(100)
);

-- Crear tipo compuesto para contacto
CREATE TYPE type_contacto AS (
    nombre_contacto  VARCHAR(100),
    nif              VARCHAR(9),
    telefono         VARCHAR(9),
    email            VARCHAR(75)
);

-- Crear tabla de proveedores
CREATE TABLE proveedores (
    id_proveedor     SERIAL PRIMARY KEY,  
    nombre_proveedor VARCHAR(100),
    contacto         type_contacto,
    -- Sincronizar nombre_proveedor con nombre_contacto al insertar o actualizar
    CONSTRAINT chk_nombre_proveedor CHECK (nombre_proveedor = (contacto).nombre_contacto)
);

CREATE TABLE productos (
    id_producto      INTEGER PRIMARY KEY,  -- No es serial porque este ID viene dado de otra DB MySQL
    id_proveedor     INTEGER,
    id_categoria     INTEGER,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE almacenes_productos (
    id_almacen       INTEGER NOT NULL,
    id_producto      INTEGER NOT NULL,
    cantidad         INTEGER NOT NULL,
    PRIMARY KEY (id_almacen, id_producto),
    FOREIGN KEY (id_almacen) REFERENCES almacenes(id_almacen) ON DELETE SET NULL,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE SET NULL
);

-- Insertar datos en la tabla almacenes
INSERT INTO almacenes (nombre_almacen, ubicacion)
VALUES 
    ('Tech Galicia - Almacén A Coruña', 'Avenida de los Castros, 123, A Coruña'),
    ('Tech Galicia - Almacén Santiago', 'Rúa da República Argentina, 45, Santiago de Compostela'),
    ('Tech Galicia - Almacén Vigo', 'Avenida de Madrid, 22, Vigo'),
    ('Tech Galicia - Almacén Ourense', 'Rúa do Progreso, 67, Ourense'),
    ('Tech Galicia - Almacén Lugo', 'Rúa San Roque, 10, Lugo');

-- Insertar datos en la tabla categorias
INSERT INTO categorias (nombre_categoria)
VALUES 
    ('PC de sobremesa'),
    ('Laptop'),
    ('Monitor'),
    ('Disco duro'),
    ('Impresora'),
    ('Accesorio'),
    ('Tablets'),
    ('Auriculares'),
    ('Cámaras'),
    ('Tarjetas Gráficas'),
    ('Procesadores');

-- Insertar datos en la tabla proveedores
INSERT INTO proveedores (nombre_proveedor, contacto)
VALUES
    ('Carlos Rodríguez', ('Carlos Rodríguez', '23456789A', '555-3456', 'c.rodriguez@lenovo.com')),
    ('Ana García', ('Ana García', '34567890A', '555-4567', 'ana.garcia@hp.com')),
    ('Luis Pérez', ('Luis Pérez', '45678901A', '555-5678', 'luis.perez@seagate.com')),
    ('Marta López', ('Marta López', '56789012A', '555-6789', 'marta.lopez@samsung.com')),
    ('José Álvarez', ('José Álvarez', '67890123A', '555-7890', 'jose.alvarez@canon.com')),
    ('Raúl Sánchez', ('Raúl Sánchez', '78901234A', '555-9012', 'raul.sanchez@nvidia.com')),
    ('Carmen López', ('Carmen López', '89012345A', '555-0123', 'carmen.lopez@amd.com')),
    ('Javier Fernández', ('Javier Fernández', '90123456A', '555-1234', 'javier.fernandez@bose.com')),
    ('Lucía Martínez', ('Lucía Martínez', '01234567A', '555-2345', 'lucia.martinez@samsung.com'));

-- Insertar datos en la tabla productos
INSERT INTO productos (id_producto, id_proveedor, id_categoria)
VALUES 
    (1, 1, 1),  -- Lenovo: PC de sobremesa
    (2, 1, 2),  -- Lenovo: Laptop
    (3, 2, 1),  -- HP: PC de sobremesa
    (4, 2, 2),  -- HP: Laptop
    (5, 3, 4),  -- Seagate: Disco duro HDD
    (6, 4, 4),  -- Samsung: Disco duro SSD
    (7, 5, 3),  -- Canon: Impresora
    (8, 4, 3),  -- Samsung: Monitor 24"
    (9, 2, 3),  -- HP: Monitor 27"
    (10, 1, 6), -- Lenovo: Accesorio teclado mecánico
    (11, 4, 7),  -- Tablet Samsung
    (12, 3, 8),  -- Auriculares Bose
    (13, 5, 9),  -- Cámara Nikon DSLR
    (14, 6, 10), -- Tarjeta gráfica NVIDIA GTX 3060
    (15, 6, 11); -- Procesador AMD Ryzen 5

-- Insertar datos en la tabla almacenes_productos
INSERT INTO almacenes_productos (id_almacen, id_producto, cantidad)
VALUES
    (1, 1, 100),    -- Almacén A Coruña: 100 unidades de PC de sobremesa
    (1, 2, 50),     -- Almacén A Coruña: 50 unidades de Laptop Lenovo
    (2, 3, 120),    -- Almacén Santiago: 120 unidades de HP PC de sobremesa
    (2, 4, 60),     -- Almacén Santiago: 60 unidades de HP Laptop
    (3, 5, 200),    -- Almacén Vigo: 200 unidades de Seagate HDD
    (3, 6, 150),    -- Almacén Vigo: 150 unidades de Samsung SSD
    (4, 7, 80),     -- Almacén Ourense: 80 unidades de Canon Impresora
    (4, 8, 110),    -- Almacén Ourense: 110 unidades de Samsung Monitor 24"
    (1, 9, 30),     -- Almacén A Coruña: 30 unidades de Lenovo Teclado mecánico
    (5, 11, 40),    -- Almacén Lugo: 40 unidades de Tablet Samsung
    (5, 12, 80),    -- Almacén Lugo: 80 unidades de Auriculares Bose
    (5, 13, 20),    -- Almacén Lugo: 20 unidades de Cámara Nikon DSLR
    (5, 14, 15),    -- Almacén Lugo: 15 unidades de Tarjeta gráfica NVIDIA GTX 3060
    (5, 15, 25);    -- Almacén Lugo: 25 unidades de Procesador AMD Ryzen 5
