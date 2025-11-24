begin;
  -- 20-feet Dry Container (Standard)
  INSERT INTO container_type (name, tare_weight, max_weight, max_volume, height, width, length, is_refrigerated, status)
  SELECT '20DC', 2300, 28180, 33.2, 2.39, 2.35, 5.9, false, 'ACTIVE'
  WHERE NOT EXISTS (SELECT 1 FROM container_type WHERE name = '20DC');

  -- 20-feet High Cube Container
  INSERT INTO container_type (name, tare_weight, max_weight, max_volume, height, width, length, is_refrigerated, status)
  SELECT '20HC', 2500, 28000, 37.6, 2.70, 2.35, 5.9, false, 'ACTIVE'
  WHERE NOT EXISTS (SELECT 1 FROM container_type WHERE name = '20HC');

  -- 20-feet Refrigerated container_type
  INSERT INTO container_type (name, tare_weight, max_weight, max_volume, height, width, length, is_refrigerated, status)
  SELECT '20RF', 3080, 27400, 28.3, 2.29, 2.29, 5.45, true, 'ACTIVE'
  WHERE NOT EXISTS (SELECT 1 FROM container_type WHERE name = '20RF');

  -- 40-feet Dry Container (Standard)
  INSERT INTO container_type (name, tare_weight, max_weight, max_volume, height, width, length, is_refrigerated, status)
  SELECT '40DC', 3750, 28750, 67.7, 2.39, 2.35, 12.03, false, 'ACTIVE'
  WHERE NOT EXISTS (SELECT 1 FROM container_type WHERE name = '40DC');

  -- 40-feet High Cube Container
  INSERT INTO container_type (name, tare_weight, max_weight, max_volume, height, width, length, is_refrigerated, status)
  SELECT '40HC', 3900, 28600, 76.4, 2.70, 2.35, 12.03, false, 'ACTIVE'
  WHERE NOT EXISTS (SELECT 1 FROM container_type WHERE name = '40HC');

  -- 40-feet Refrigerated Container
  INSERT INTO container_type (name, tare_weight, max_weight, max_volume, height, width, length, is_refrigerated, status)
  SELECT '40RF', 4480, 28000, 57.8, 2.29, 2.29, 11.59, true, 'ACTIVE'
  WHERE NOT EXISTS (SELECT 1 FROM container_type WHERE name = '40RF');

  -- 45-feet High Cube Container
  INSERT INTO container_type (name, tare_weight, max_weight, max_volume, height, width, length, is_refrigerated, status)
  SELECT '45HC', 4800, 27700, 86.1, 2.70, 2.35, 13.58, false, 'ACTIVE'
  WHERE NOT EXISTS (SELECT 1 FROM container_type WHERE name = '45HC');
  


  insert into good_kind (name, description, is_refrigerated, is_default)
  select 'Normal Goods', '', false, true
  where not exists (select 1 from good_kind where name = 'Normal Goods');

  insert into good_kind (name, description, is_refrigerated, is_default)
  select 'Refrigerated Goods', '', true, true
  where not exists (select 1 from good_kind where name = 'Refrigerated Goods');

  insert into good_kind (name, description, is_refrigerated, is_default)
  select 'Other Goods', '', false, true
  where not exists (select 1 from good_kind where name = 'Other Goods');

  insert into good_kind (name, description, is_refrigerated, is_default)
  select 'Dangerous Goods', '', false, true
  where not exists (select 1 from good_kind where name = 'Dangerous Goods');

commit;