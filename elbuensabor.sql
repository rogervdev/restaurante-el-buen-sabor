-- =====================================================
-- BASE DE DATOS: RESTAURANTE EL BUEN SABOR (v8 - SIN DUPLICADOS)
-- =====================================================
DROP DATABASE IF EXISTS restaurante_db;
CREATE DATABASE IF NOT EXISTS restaurante_db;
USE restaurante_db;

-- =====================================================
-- 1. CONFIGURACIÓN GENERAL
-- =====================================================
CREATE TABLE configuracion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    precio_menu DECIMAL(10,2) NOT NULL,
    costo_descartable DECIMAL(10,2) DEFAULT 2.00,
    costo_delivery DECIMAL(10,2) DEFAULT 2.00,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================================================
-- 2. CATÁLOGO DE PLATOS DEL MENÚ
-- =====================================================
CREATE TABLE catalogo_plato_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo_menu VARCHAR(50) NOT NULL, -- ENTRADA, SEGUNDO, BEBIDA
    permite_cambio_presa BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- 3. PRESAS
-- =====================================================
CREATE TABLE presa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio_adicional DECIMAL(10,2) DEFAULT 0.00,
    activo BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- 4. RELACIÓN PLATO-PRESA
-- =====================================================
CREATE TABLE plato_menu_presa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    catalogo_plato_menu_id BIGINT NOT NULL,
    presa_id BIGINT NOT NULL,
    presa_por_defecto BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (catalogo_plato_menu_id) REFERENCES catalogo_plato_menu(id) ON DELETE CASCADE,
    FOREIGN KEY (presa_id) REFERENCES presa(id) ON DELETE CASCADE,
    UNIQUE(catalogo_plato_menu_id, presa_id)
);

-- =====================================================
-- 5. CATÁLOGO DE PLATOS A LA CARTA (SOLO PLATOS PRINCIPALES)
-- =====================================================
CREATE TABLE catalogo_plato_carta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL, -- PLATO_FONDO, ENTRADA_CARTA, POSTRE
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- 6. STOCK DIARIO DE PLATOS DEL MENÚ
-- =====================================================
CREATE TABLE plato_menu_dia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    catalogo_plato_menu_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    disponible BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (catalogo_plato_menu_id) REFERENCES catalogo_plato_menu(id),
    UNIQUE(catalogo_plato_menu_id, fecha)
);

-- =====================================================
-- 7. STOCK DIARIO DE PRESAS
-- =====================================================
CREATE TABLE presa_dia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    presa_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    FOREIGN KEY (presa_id) REFERENCES presa(id),
    UNIQUE(presa_id, fecha)
);

-- =====================================================
-- 8. CLIENTES FRECUENTES
-- =====================================================
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    telefono VARCHAR(15) UNIQUE NOT NULL,
    nombre VARCHAR(100),
    notas VARCHAR(500)
);

-- =====================================================
-- 9. PEDIDOS
-- =====================================================
CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT,
    cliente_nombre VARCHAR(100) NOT NULL,
    cliente_telefono VARCHAR(15),
    tipo VARCHAR(20) NOT NULL,
    tipo_venta VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    total DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(20) NOT NULL DEFAULT 'EFECTIVO',
    monto_recibido DECIMAL(10,2),
    vuelto DECIMAL(10,2) DEFAULT 0.00,
    costo_descartable DECIMAL(10,2) DEFAULT 0.00,
    requiere_delivery BOOLEAN DEFAULT FALSE,
    costo_delivery DECIMAL(10,2) DEFAULT 0.00,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- =====================================================
-- 10. DETALLE DEL PEDIDO
-- =====================================================
CREATE TABLE pedido_detalle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    catalogo_plato_menu_id BIGINT,
    catalogo_plato_carta_id BIGINT,
    presa_id BIGINT,
    entrada_id BIGINT,
    bebida_id BIGINT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    observacion VARCHAR(200),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (catalogo_plato_menu_id) REFERENCES catalogo_plato_menu(id),
    FOREIGN KEY (catalogo_plato_carta_id) REFERENCES catalogo_plato_carta(id),
    FOREIGN KEY (presa_id) REFERENCES presa(id),
    FOREIGN KEY (entrada_id) REFERENCES catalogo_plato_menu(id),
    FOREIGN KEY (bebida_id) REFERENCES catalogo_plato_menu(id)
);

-- =====================================================
-- DATOS DE PRUEBA LIMPIOS (SIN DUPLICADOS)
-- =====================================================

-- 1. Configuración
INSERT INTO configuracion (precio_menu, costo_descartable, costo_delivery) 
VALUES (10.00, 2.00, 2.00);

-- 2. Platos del MENÚ
INSERT INTO catalogo_plato_menu (nombre, tipo_menu, permite_cambio_presa) VALUES
('Sopa de pollo',      'ENTRADA', FALSE),  -- 1
('Sopa de res',        'ENTRADA', FALSE),  -- 2
('Ocopa arequipeña',   'ENTRADA', FALSE),  -- 3
('Ensalada rusa',      'ENTRADA', FALSE),  -- 4
('Frejoles',           'SEGUNDO', TRUE),   -- 5
('Tacu tacu',          'SEGUNDO', TRUE),   -- 6
('Lomo saltado',       'SEGUNDO', FALSE),  -- 7
('Arroz con pollo',    'SEGUNDO', TRUE),   -- 8
('Chicha morada',      'BEBIDA',  FALSE),  -- 9
('Limonada',           'BEBIDA',  FALSE),  -- 10
('Carambola',          'BEBIDA',  FALSE);  -- 11

-- 3. Presas (solo 4)
INSERT INTO presa (nombre, precio_adicional) VALUES
('Seco de pollo', 0.00),   -- 1
('Pollo frito',   3.00),   -- 2
('Pescado',       5.00),   -- 3
('Bistec',        4.00);   -- 4

-- 4. Asociaciones plato-presa
INSERT INTO plato_menu_presa (catalogo_plato_menu_id, presa_id, presa_por_defecto) VALUES
(5, 1, TRUE), (5, 2, FALSE), (5, 3, FALSE), (5, 4, FALSE),
(6, 1, TRUE), (6, 2, FALSE), (6, 3, FALSE),
(8, 1, TRUE), (8, 2, FALSE), (8, 3, FALSE), (8, 4, FALSE);

-- 5. Platos a la CARTA
INSERT INTO catalogo_plato_carta (nombre, categoria, precio, stock) VALUES
('Lomo saltado a la carta', 'PLATO_FONDO', 22.00, 30),
('Pollo a la brasa',        'PLATO_FONDO', 25.00, 25),
('Pescado frito',           'PLATO_FONDO', 28.00, 20),
('Chicharrón de pollo',     'PLATO_FONDO', 20.00, 25),
('Tallarín saltado',        'PLATO_FONDO', 18.00, 30);

-- 6. Cliente de ejemplo
INSERT INTO cliente (telefono, nombre, notas) VALUES
('999888777', 'Sra. María', 'Siempre pide sin ají, le gusta el pescado');

-- 7. Stock de platos del menú para hoy (UNA SOLA VEZ, todos los platos)
INSERT INTO plato_menu_dia (catalogo_plato_menu_id, fecha, stock, disponible) VALUES
(1,  CURDATE(), 20, TRUE),   -- Sopa de pollo
(2,  CURDATE(), 15, TRUE),   -- Sopa de res
(3,  CURDATE(), 10, TRUE),   -- Ocopa arequipeña
(4,  CURDATE(), 12, TRUE),   -- Ensalada rusa
(5,  CURDATE(), 30, TRUE),   -- Frejoles
(6,  CURDATE(), 25, TRUE),   -- Tacu tacu
(7,  CURDATE(), 20, TRUE),   -- Lomo saltado
(8,  CURDATE(), 18, TRUE),   -- Arroz con pollo
(9,  CURDATE(), 40, TRUE),   -- Chicha morada
(10, CURDATE(), 35, TRUE),   -- Limonada
(11, CURDATE(), 30, TRUE);   -- Carambola

-- 8. Stock de presas para hoy (UNA SOLA VEZ, solo las 4 presas existentes)
INSERT INTO presa_dia (presa_id, fecha, stock) VALUES
(1, CURDATE(), 30),  -- Seco de pollo
(2, CURDATE(), 20),  -- Pollo frito
(3, CURDATE(), 15),  -- Pescado
(4, CURDATE(), 10);  -- Bistec

-- =====================================================
-- VERIFICACIONES (limpias, sin repetir)
-- =====================================================
SELECT '✅ CONFIGURACIÓN' AS info;
SELECT * FROM configuracion;

SELECT '✅ PLATOS DEL MENÚ' AS info;
SELECT id, nombre, tipo_menu, permite_cambio_presa 
FROM catalogo_plato_menu 
ORDER BY tipo_menu, nombre;

SELECT '✅ PLATOS A LA CARTA' AS info;
SELECT id, nombre, categoria, precio, stock 
FROM catalogo_plato_carta 
WHERE activo = TRUE 
ORDER BY categoria, nombre;

SELECT '✅ STOCK DE HOY (menú)' AS info;
SELECT cp.id, cp.nombre, cp.tipo_menu, pmd.stock, pmd.disponible
FROM plato_menu_dia pmd
JOIN catalogo_plato_menu cp ON pmd.catalogo_plato_menu_id = cp.id
WHERE pmd.fecha = CURDATE()
ORDER BY cp.tipo_menu, cp.nombre;

SELECT '✅ STOCK DE HOY (presas)' AS info;
SELECT p.id, p.nombre, pd.stock
FROM presa_dia pd
JOIN presa p ON pd.presa_id = p.id
WHERE pd.fecha = CURDATE()
ORDER BY p.id;