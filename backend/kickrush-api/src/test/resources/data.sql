INSERT INTO shoes (id, name, brand, model_number, price, description, image_url) VALUES
  (1, 'Air Jordan 1 Retro High OG ''Chicago Lost and Found''', 'Nike', 'DZ5485-612', 209000,
   '1985년 오리지널 시카고 컬러웨이를 재현한 에어 조던 1.', '/images/shoes/aj1-chicago.jpg'),
  (2, 'Nike Dunk Low ''Panda''', 'Nike', 'DD1391-100', 139000,
   '클래식한 블랙 앤 화이트 컬러웨이의 덩크 로우.', '/images/shoes/dunk-panda.jpg'),
  (3, 'New Balance 550 ''White Green''', 'New Balance', 'BB550WT1', 139000,
   '90년대 농구화에서 영감받은 레트로 디자인.', '/images/shoes/nb550-green.jpg');

INSERT INTO releases (id, shoe_id, release_date_time, end_date_time, total_stock) VALUES
  (1, 1, '2026-02-10T11:00:00', '2026-02-10T11:30:00', 100),
  (2, 2, '2026-02-08T10:00:00', '2026-02-08T10:30:00', 50),
  (3, 3, '2026-02-05T11:00:00', '2026-02-05T11:30:00', 0);

INSERT INTO release_sizes (id, release_id, size, stock, price) VALUES
  (1, 1, 250, 10, 209000),
  (2, 1, 255, 8, 209000),
  (3, 1, 260, 12, 209000),
  (4, 1, 265, 15, 209000),
  (5, 1, 270, 20, 209000),
  (6, 1, 275, 15, 209000),
  (7, 1, 280, 10, 209000),
  (8, 1, 285, 5, 209000),
  (9, 1, 290, 5, 209000),
  (10, 2, 250, 5, 139000),
  (11, 2, 255, 3, 139000),
  (12, 2, 260, 8, 139000),
  (13, 2, 265, 10, 139000),
  (14, 2, 270, 12, 139000),
  (15, 2, 275, 7, 139000),
  (16, 2, 280, 5, 139000),
  (17, 3, 250, 0, 139000),
  (18, 3, 255, 0, 139000),
  (19, 3, 260, 0, 139000),
  (20, 3, 265, 0, 139000),
  (21, 3, 270, 0, 139000);
