-- Limpiar datos previos (dev only)
TRUNCATE TABLE recipe_ingredients, recipe_tags, ingredient_alias, recipes, ingredient_alias RESTART IDENTITY CASCADE;

-- ====== INGREDIENTS ======
-- UUIDs fijos para referencias
INSERT INTO ingredients (id, name, created_at, updated_at) VALUES
  ('11111111-1111-1111-1111-111111111111', 'tomate', now(), now()),
  ('22222222-2222-2222-2222-222222222222', 'huevo', now(), now()),
  ('33333333-3333-3333-3333-333333333333', 'pan', now(), now()),
  ('44444444-4444-4444-4444-444444444444', 'aceite de oliva', now(), now()),
  ('55555555-5555-5555-5555-555555555555', 'sal', now(), now()),
  ('66666666-6666-6666-6666-666666666666', 'ajo', now(), now()),
  ('77777777-7777-7777-7777-777777777777', 'cebolla', now(), now()),
  ('88888888-8888-8888-8888-888888888888', 'pasta', now(), now());

-- Aliases ejemplo
INSERT INTO ingredient_alias (ingredient_id, alias) VALUES
  ('11111111-1111-1111-1111-111111111111', 'jitomate'),
  ('33333333-3333-3333-3333-333333333333', 'tostada');

-- ====== RECIPES ======
INSERT INTO recipes (id, title, summary, steps, created_at, updated_at) VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
   'Tostada de tomate y huevo',
   'Desayuno rápido en 10 minutos',
   '1) Tuesta el pan\n2) Fríe el huevo al gusto\n3) Unta tomate rallado y chorrito de aceite\n4) Monta y sala',
   now(), now()
  ),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
   'Pasta ajo y aceite (Aglio e olio)',
   'Pasta básica, sabrosa y barata',
   '1) Cuece la pasta al dente\n2) Saltea ajo laminado en aceite (fuego bajo)\n3) Mezcla con la pasta y un poco de agua de cocción\n4) Ajusta de sal',
   now(), now()
  ),
  ('cccccccc-cccc-cccc-cccc-cccccccccccc',
   'Huevos al plato con tomate',
   'Plato único sencillo al horno',
   '1) Sofríe cebolla y ajo\n2) Añade tomate triturado y reduce\n3) Casca los huevos encima y hornea hasta cuajar claras\n4) Termina con sal',
   now(), now()
  ),
  ('dddddddd-dddd-dddd-dddd-dddddddddddd',
   'Pan tumaca (pa amb tomàquet)',
   'Clásico catalán para cualquier momento',
   '1) Tuesta pan\n2) Frota ajo (opcional)\n3) Unta tomate maduro\n4) Aceite de oliva y sal',
   now(), now()
  ),
  ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
   'Revueltos de cebolla y tomate',
   'Cena exprés con pocos ingredientes',
   '1) Pocha cebolla\n2) Añade tomate en dados\n3) Incorpora huevo batido y cuaja suave\n4) Ajusta de sal',
   now(), now()
  );

-- Tags
INSERT INTO recipe_tags (recipe_id, tag) VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'rápida'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'desayuno'),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'pasta'),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'barata'),
  ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'horno'),
  ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'clásico'),
  ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'cena');

-- ====== RECIPE_INGREDIENTS ======
-- Tostada de tomate y huevo
INSERT INTO recipe_ingredients (id, recipe_id, ingredient_id, quantity, created_at, updated_at) VALUES
  ('a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '33333333-3333-3333-3333-333333333333', '2 rebanadas', now(), now()),
  ('a2a2a2a2-a2a2-a2a2-a2a2-a2a2a2a2a2a2', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222', '1 ud', now(), now()),
  ('a3a3a3a3-a3a3-a3a3-a3a3-a3a3a3a3a3a3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', '1 ud', now(), now()),
  ('a4a4a4a4-a4a4-a4a4-a4a4-a4a4a4a4a4a4', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '44444444-4444-4444-4444-444444444444', 'chorrito', now(), now()),
  ('a5a5a5a5-a5a5-a5a5-a5a5-a5a5a5a5a5a5', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '55555555-5555-5555-5555-555555555555', 'al gusto', now(), now());

-- Pasta ajo y aceite
INSERT INTO recipe_ingredients (id, recipe_id, ingredient_id, quantity, created_at, updated_at) VALUES
  ('b1b1b1b1-b1b1-b1b1-b1b1-b1b1b1b1b1b1', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '88888888-8888-8888-8888-888888888888', '200 g', now(), now()),
  ('b2b2b2b2-b2b2-b2b2-b2b2-b2b2b2b2b2b2', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '66666666-6666-6666-6666-666666666666', '2 dientes', now(), now()),
  ('b3b3b3b3-b3b3-b3b3-b3b3-b3b3b3b3b3b3', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '44444444-4444-4444-4444-444444444444', '2 cdas', now(), now()),
  ('b4b4b4b4-b4b4-b4b4-b4b4-b4b4b4b4b4b4', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '55555555-5555-5555-5555-555555555555', 'al gusto', now(), now());

-- Huevos al plato con tomate
INSERT INTO recipe_ingredients (id, recipe_id, ingredient_id, quantity, created_at, updated_at) VALUES
  ('c1c1c1c1-c1c1-c1c1-c1c1-c1c1c1c1c1c1', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '77777777-7777-7777-7777-777777777777', '1 ud', now(), now()),
  ('c2c2c2c2-c2c2-c2c2-c2c2-c2c2c2c2c2c2', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '66666666-6666-6666-6666-666666666666', '1 diente', now(), now()),
  ('c3c3c3c3-c3c3-c3c3-c3c3-c3c3c3c3c3c3', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '11111111-1111-1111-1111-111111111111', '300 g (triturado)', now(), now()),
  ('c4c4c4c4-c4c4-c4c4-c4c4-c4c4c4c4c4c4', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '22222222-2222-2222-2222-222222222222', '2 uds', now(), now()),
  ('c5c5c5c5-c5c5-c5c5-c5c5-c5c5c5c5c5c5', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '55555555-5555-5555-5555-555555555555', 'al gusto', now(), now());

-- Pan tumaca
INSERT INTO recipe_ingredients (id, recipe_id, ingredient_id, quantity, created_at, updated_at) VALUES
  ('d1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '33333333-3333-3333-3333-333333333333', '2 rebanadas', now(), now()),
  ('d2d2d2d2-d2d2-d2d2-d2d2-d2d2d2d2d2d2', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '66666666-6666-6666-6666-666666666666', '1/2 diente (opcional)', now(), now()),
  ('d3d3d3d3-d3d3-d3d3-d3d3-d3d3d3d3d3d3', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '11111111-1111-1111-1111-111111111111', '1 ud maduro', now(), now()),
  ('d4d4d4d4-d4d4-d4d4-d4d4-d4d4d4d4d4d4', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '44444444-4444-4444-4444-444444444444', 'chorro', now(), now()),
  ('d5d5d5d5-d5d5-d5d5-d5d5-d5d5d5d5d5d5', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '55555555-5555-5555-5555-555555555555', 'al gusto', now(), now());

-- Revueltos de cebolla y tomate
INSERT INTO recipe_ingredients (id, recipe_id, ingredient_id, quantity, created_at, updated_at) VALUES
  ('e1e1e1e1-e1e1-e1e1-e1e1-e1e1e1e1e1e1', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '77777777-7777-7777-7777-777777777777', '1 ud', now(), now()),
  ('e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '11111111-1111-1111-1111-111111111111', '1 ud', now(), now()),
  ('e3e3e3e3-e3e3-e3e3-e3e3-e3e3e3e3e3e3', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '22222222-2222-2222-2222-222222222222', '3 uds', now(), now()),
  ('e4e4e4e4-e4e4-e4e4-e4e4-e4e4e4e4e4e4', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '55555555-5555-5555-5555-555555555555', 'al gusto', now(), now());
