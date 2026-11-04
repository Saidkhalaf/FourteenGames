INSERT INTO tictactoe.Game_Character (Is_Npc, Id, Email, Icon, Name)
VALUES (false, 'b85182a8-68f8-4d42-b0d5-6166bf2e8284', 'james@example.invalid', 'https://img.freepik.com/free-photo/emotions-people-concept-headshot-serious-looking-handsome-man-with-beard-looking-confident-determined_1258-26730.jpg?semt=ais_hybrid&w=740&q=80', 'James')
ON CONFLICT (id) DO NOTHING;
INSERT INTO tictactoe.Game_Character (Is_Npc, Id, Email, Icon, Name)
VALUES (false, '7ad223bd-bf5e-4945-8284-3a9c81e4e7a9', 'sarah@example.invalid', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Sarah_Goldberg_at_the_2025_Sundance_Film_Festival_01_%28cropped%29.jpg/960px-Sarah_Goldberg_at_the_2025_Sundance_Film_Festival_01_%28cropped%29.jpg', 'Sarah')
ON CONFLICT (id) DO NOTHING;
INSERT INTO tictactoe.Game_Character (Is_Npc, Id, Email, Icon, Name)
VALUES (True, '0e5ce7dd-c39c-4475-a52c-c0b85edd2074', null, 'https://static.vecteezy.com/system/resources/previews/009/742/308/non_2x/robot-icon-isolated-contour-symbol-illustration-vector.jpg', 'AI')
ON CONFLICT (id) DO NOTHING;