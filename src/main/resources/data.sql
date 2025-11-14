-- Datos iniciales para la base de datos

-- Insertar libros (basados en la API externa)
-- INSERT INTO books (external_id, title, first_publish_year, edition_count, has_fulltext, price, stock_quantity, available_quantity)
-- VALUES 
--     (258027, 'The Lord of the Rings', 1954, 120, true, 15.99, 10, 8),
--     (140081, 'The Hitchhiker''s Guide to the Galaxy', 1979, 85, false, 20.99, 15, 12),
--     (90150, 'One Hundred Years of Solitude', 1967, 250, true, 22.99, 8, 6),
--     (50012, 'Pride and Prejudice', 1813, 75, true, 12.99, 20, 18);

-- -- Insertar autores de los libros
-- INSERT INTO book_authors (book_id, author_name)
-- VALUES 
--     (258027, 'J. R. R. Tolkien'),
--     (140081, 'Douglas Adams'),
--     (90150, 'Gabriel García Márquez'),
--     (50012, 'Jane Austen');

-- Insertar usuarios de ejemplo
INSERT INTO users (name, email, phone_number, created_at)
VALUES 
    ('Juan Pérez', 'juan.perez@example.com', '123456789', CURRENT_TIMESTAMP),
    ('María García', 'maria.garcia@example.com', '987654321', CURRENT_TIMESTAMP),
    ('Carlos Rodríguez', 'carlos.rodriguez@example.com', '555123456', CURRENT_TIMESTAMP),
    ('Ana Martínez', 'ana.martinez@example.com', '555987654', CURRENT_TIMESTAMP),
    ('Luis Fernández', 'luis.fernandez@example.com', '555456789', CURRENT_TIMESTAMP);

-- -- Insertar reservas de ejemplo
-- -- Nota: Las fechas se calculan dinámicamente usando DATEADD de H2
-- INSERT INTO reservations (user_id, book_external_id, rental_days, start_date, expected_return_date, actual_return_date, daily_rate, total_fee, late_fee, status, created_at)
-- VALUES 
--     -- Reserva activa de Juan (7 días, empezó hace 2 días)
--     (1, 258027, 7, DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 5, CURRENT_DATE), NULL, 15.99, 111.93, 0.00, 'ACTIVE', DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
    
--     -- Reserva activa de María (14 días, empezó hace 5 días)
--     (2, 140081, 14, DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 9, CURRENT_DATE), NULL, 20.99, 293.86, 0.00, 'ACTIVE', DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
    
--     -- Reserva devuelta a tiempo de Carlos (devuelta el día esperado)
--     (3, 90150, 10, DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), 22.99, 229.90, 0.00, 'RETURNED', DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
    
--     -- Reserva con demora de Ana (3 días de demora, devuelta hace 1 día)
--     (4, 50012, 5, DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), 12.99, 64.95, 5.85, 'OVERDUE', DATEADD('DAY', -10, CURRENT_TIMESTAMP));

-- -- Actualizar la cantidad disponible de libros según las reservas activas
-- -- The Lord of the Rings: 10 stock - 1 reserva activa = 9 disponible (pero ya pusimos 8, así que está bien)
-- -- The Hitchhiker's Guide: 15 stock - 1 reserva activa = 14 disponible (pero pusimos 12, así que hay 2 más reservadas)
-- -- One Hundred Years: 8 stock - 0 reservas activas = 8 disponible (pero pusimos 6, así que hay 2 reservadas)
-- -- Pride and Prejudice: 20 stock - 0 reservas activas = 20 disponible (pero pusimos 18, así que hay 2 reservadas)

