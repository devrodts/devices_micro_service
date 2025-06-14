CREATE TABLE IF NOT EXISTS devices (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_device_name UNIQUE (name)
);

COMMENT ON TABLE devices IS 'Tabela para armazenar informações de dispositivos';
COMMENT ON COLUMN devices.id IS 'Identificador único do dispositivo';
COMMENT ON COLUMN devices.name IS 'Nome do dispositivo (deve ser único)';
COMMENT ON COLUMN devices.type IS 'Tipo do dispositivo';
COMMENT ON COLUMN devices.status IS 'Status atual do dispositivo';
COMMENT ON COLUMN devices.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN devices.updated_at IS 'Data e hora da última atualização do registro';
