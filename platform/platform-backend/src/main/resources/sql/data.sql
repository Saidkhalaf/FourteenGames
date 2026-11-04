INSERT INTO genres (name, description)
VALUES ('Strategy',
        'Games that emphasize planning, tactics, and decision-making to outmaneuver an opponent or solve complex scenarios.')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO genres (name, description)
VALUES (
        'Puzzle',
        'Games centered around solving spatial, logical, or pattern-based challenges, often requiring quick thinking or precision.')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO games (id, name,max_player_count, description, price, image, icon, genre_name, url, game_settings)
VALUES ('33333333-3333-3333-3333-333333333333',
        'Tetris',
        2,
        'A tile-matching puzzle game where players rotate pieces to clear lines.',
        14.99,
        'tetris.png',
        '',
        'Puzzle',
        '',
        '{}'
       )
    ON CONFLICT (id) DO NOTHING;