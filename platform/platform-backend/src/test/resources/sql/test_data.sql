INSERT INTO genres(description, name)
VALUES ('Games that emphasize planning, tactics and decision making.',
        'Strategy');

INSERT INTO games(id, name,ai_game_start_endpoint, game_start_endpoint,max_player_count,game_settings, description, price, genre_name, icon, image, url)
VALUES ('11111111-1111-1111-1111-111111111111',
        'Tic Tac Toe',
        'testhost/start/ai',
        'testhost/start',
        2,
        '{}' FORMAT JSON,
        'Tic Tac Toe is een game waarin X en O geplaatst wordt om een rij van 3 te vormen.',
        19.99,
        'Strategy',
        'myicon.png',
        'myImage.png',
        'localhost:8080');


INSERT INTO platform_user (id,
                           user_name,
                           biography,
                           profile_picture_url,
                           banner_url,
                           credits)
VALUES ('11111111-1111-1111-1234-111111111111',
        'test-host',
        '',
        '',
        '',
        0.0);

INSERT INTO platform_user (id,
                           user_name,
                           biography,
                           profile_picture_url,
                           banner_url,
                           credits)
VALUES ('11111111-1111-1111-aaaa-111111111111',
        'test-user-1',
        '',
        '',
        '',
        0.0);

INSERT INTO platform_user (id,
                           user_name,
                           biography,
                           profile_picture_url,
                           banner_url,
                           credits)
VALUES ('11111111-1111-1111-aabb-111111111111',
        'test-user-2',
        '',
        '',
        '',
        0.0);

INSERT INTO platform_user (id,
                           user_name,
                           biography,
                           profile_picture_url,
                           banner_url,
                           credits)
VALUES ('11111111-1111-1111-aacc-111111111111',
        'test-user-3',
        '',
        '',
        '',
        0.0);

INSERT INTO owned_copies (id,
                          game_id,
                          is_favorite,
                          user_id)
VALUES ('22222222-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111',
        false,
        '11111111-1111-1111-aaaa-111111111111');



INSERT INTO achievements (id, name, description, game_id)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479',
        'King of the Hill',
        'Win a game from starting in the middle',
        '11111111-1111-1111-1111-111111111111');

INSERT INTO lobbies (id, game_id, creation_date, max_player_count)
VALUES ('11111111-ffff-1111-1111-111111111111',
        '11111111-1111-1111-1111-111111111111',
        now(),
        2);

INSERT INTO players(lobby_id, user_id)
VALUES ( '11111111-ffff-1111-1111-111111111111'
       , '11111111-1111-1111-aabb-111111111111');

INSERT INTO lobbies (id, game_id, creation_date, max_player_count, current_game_session_id)
VALUES ('11111111-ffee-1111-1111-111111111111',
        '11111111-1111-1111-1111-111111111111',
        now(),
        1,
        '99999999-9999-9999-9999-999999999999'
        );

INSERT INTO players(lobby_id, user_id)
VALUES ( '11111111-ffee-1111-1111-111111111111'
       , '11111111-1111-1111-aaaa-111111111111');

INSERT INTO players(lobby_id, user_id)
VALUES ( '11111111-ffee-1111-1111-111111111111'
       , '11111111-1111-1111-aacc-111111111111');