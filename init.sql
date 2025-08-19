CREATE TABLE IF NOT EXISTS regions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    dimensions JSON,
    area DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS survey_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    region_id BIGINT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    survey_time DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'completed',
    FOREIGN KEY (region_id) REFERENCES regions(id)
);

CREATE TABLE IF NOT EXISTS attachments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    region_id BIGINT NOT NULL,
    ground_material VARCHAR(50),
    photos JSON,
    notes TEXT,
    FOREIGN KEY (region_id) REFERENCES regions(id)
);

INSERT INTO regions (project_id, type, dimensions, area) VALUES 
('PROJ001', 'rectangle', '{"length":5.2, "width":3.8}', 19.76),
('PROJ001', 'polygon', '{"points":[[0,0],[2.1,0],[2.1,3.5],[0,2.8]]}', 8.4);

INSERT INTO survey_records (region_id, user_id, survey_time) VALUES
(1, 'USER001', '2024-05-20 14:30:00'),
(2, 'USER001', '2024-05-20 15:15:00');

INSERT INTO attachments (region_id, ground_material, photos, notes) VALUES
(1, 'concrete', '["photo1.jpg", "photo2.jpg"]', '地面平整，适合施工'),
(2, 'asphalt', '["photo3.jpg"]', '局部有裂缝，需处理');