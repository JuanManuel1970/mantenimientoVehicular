CREATE TABLE vehiculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patente VARCHAR(20),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    kilometros INT,
    duracion_aceite INT,
    duracion_filtros INT,
    proximo_servicio_km INT,
    email VARCHAR(100)
);
