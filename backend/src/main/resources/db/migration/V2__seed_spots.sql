-- Seed data: study spots collected by hand across Scarborough and North York.
-- Amenity observations are inserted as reports rather than columns on spots,
-- so the initial data ages through the same decay path as user submissions.

INSERT INTO spots (name, address, latitude, longitude, chain, map_link, active) VALUES
('Tim Hortons - O''Connor Dr & Victoria Park Ave', '1900 O''Connor Dr, North York, ON M4A 1X2', 43.723537, -79.302571, 'Tim Hortons', 'https://maps.app.goo.gl/gvV4coKwQrdxQCPu6', TRUE),
('Tim Hortons - Laureleaf Rd & Steeles Ave E', '1567 Steeles Ave E, North York, ON M2M 2Y3', 43.806021, -79.384106, 'Tim Hortons', 'https://maps.app.goo.gl/pQKsCVFGTN4GwZQYA', TRUE),
('Tim Hortons - Pharmacy & Lawrence', '1108 Pharmacy Ave, Scarborough, ON M1R 2H3', 43.743282, -79.304325, 'Tim Hortons', 'https://maps.app.goo.gl/GoNkHHNnUDAmBeYg8', TRUE),
('Tim Hortons - Wexford Park', '960 Warden Ave., Scarborough, ON M1L 4C9', 43.734362, -79.290006, 'Tim Hortons', 'https://maps.app.goo.gl/XSitBLaXK4zGc6ek8', FALSE),
('Tim Hortons - Eglinton Square', '1 Eglinton Square Unit 121, Scarborough, ON M1L 2K1', 43.723719, -79.298875, 'Tim Hortons', 'https://maps.app.goo.gl/tsDUZzxbhD8N5RZ89', TRUE),
('Tim Hortons - Parkway Mall', '75 Ellesmere Rd #4b, Scarborough, ON M1R 4B7', 43.758424, -79.31171, 'Tim Hortons', 'https://maps.app.goo.gl/yPRzs6pzwwKPtrnE9', TRUE),
('Tim Hortons - Eglinton Town Centre', '4 Lebovic Ave, Scarborough, ON M1L 2L6', 43.726408, -79.291722, 'Tim Hortons', 'https://maps.app.goo.gl/ApVXjP5krhcrXNRD6', TRUE),
('Chachee''s Chai Cafe - Scarborough', 'D-101, 69 Lebovic Ave, Scarborough, ON M1L 4T7', 43.723354, -79.288098, 'Chachee''s Chai Cafe', 'https://maps.app.goo.gl/32c4mGDMWcph4eCt8', TRUE),
('Craves Bites & Cafe', '2171 Lawrence Ave E, Scarborough, ON M1P 0E9', 43.747465, -79.283788, NULL, 'https://maps.app.goo.gl/aCEZRNjuic16Fp1i7', TRUE),
('Starbucks - 1900 Eglinton Ave E', '1900 Eglinton Ave E, Scarborough, ON M1L 2L9', 43.726922, -79.293239, 'Starbucks', 'https://maps.app.goo.gl/bSLamVaUzNzEEJ9C6', TRUE),
('Wendy''s - Eglinton Town Centre', '4 Lebovic Ave, Scarborough, ON M1L 4V9', 43.72653, -79.291491, 'Wendy''s', 'https://maps.app.goo.gl/u9in42UEtyXJzQ7dA', TRUE),
('Original Shawarma', '1795 Victoria Park Ave, Scarborough, ON M1R 1T2', 43.741907, -79.309292, 'Original Shawarma', 'https://maps.app.goo.gl/QYVHrrG19iAZYQvZ8', TRUE),
('McDonald''s - Warden Ave & Eglinton Ave E', '1966 Eglinton Ave E, Scarborough, ON M1L 2M6', 43.728808, -79.285929, 'McDonald''s', 'https://maps.app.goo.gl/yE7j2kxNtyzhn5m4A', TRUE);

-- Initial observations, all from a single reporter (the collector).
-- Spots with nothing observed yet get no report at all.

INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'FEW', 'QUIET', FALSE
  FROM spots WHERE name = 'Tim Hortons - O''Connor Dr & Victoria Park Ave';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'NONE', 'MODERATE', TRUE
  FROM spots WHERE name = 'Tim Hortons - Laureleaf Rd & Steeles Ave E';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'FEW', 'MODERATE', TRUE
  FROM spots WHERE name = 'Tim Hortons - Pharmacy & Lawrence';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'FEW', 'MODERATE', TRUE
  FROM spots WHERE name = 'Tim Hortons - Wexford Park';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'NONE', 'LOUD', FALSE
  FROM spots WHERE name = 'Tim Hortons - Eglinton Square';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, NULL, 'MODERATE', TRUE
  FROM spots WHERE name = 'Tim Hortons - Parkway Mall';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'FEW', 'QUIET', TRUE
  FROM spots WHERE name = 'Chachee''s Chai Cafe - Scarborough';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'MANY', 'LOUD', TRUE
  FROM spots WHERE name = 'Craves Bites & Cafe';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'FEW', 'MODERATE', TRUE
  FROM spots WHERE name = 'Starbucks - 1900 Eglinton Ave E';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'NONE', 'LOUD', FALSE
  FROM spots WHERE name = 'Original Shawarma';
INSERT INTO reports (reporter_id, spot_id, outlets, noise, has_wifi)
SELECT '8f14e45f-ea6c-4d1b-9f2a-3c7b8e5a1d40', spot_id, 'FEW', 'LOUD', TRUE
  FROM spots WHERE name = 'McDonald''s - Warden Ave & Eglinton Ave E';