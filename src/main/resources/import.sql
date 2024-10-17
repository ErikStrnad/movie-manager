-- Insert Actors
INSERT INTO Actor (id, name, birthdate)
VALUES (1, 'Tim Robbins', '1958-10-16'),
       (2, 'Morgan Freeman', '1937-06-01'),
       (3, 'Leonardo DiCaprio', '1974-11-11'),
       (4, 'Liam Neeson', '1952-06-07'),
       (5, 'Viggo Mortensen', '1958-10-20'),
       (6, 'Mark Hamill', '1951-09-25'),
       (7, 'Brad Pitt', '1963-12-18'),
       (8, 'Elijah Wood', '1981-01-28'),
       (9, 'Tom Hanks', '1956-07-09');
ALTER TABLE Actor
    ALTER COLUMN id RESTART WITH 10;

-- Insert Movies
INSERT INTO Movie (imdbID, title, releaseYear, description)
VALUES ('tt0111161', 'The Shawshank Redemption', 1994, 'Two imprisoned men bond over a number of years...'),
       ('tt0068646', 'The Godfather', 1972, 'The aging patriarch of an organized crime dynasty...'),
       ('tt1375666', 'Inception', 2010,
        'A thief who steals corporate secrets through the use of dream-sharing technology...'),
       ('tt0108052', 'Schindler''s List', 1993, 'In German-occupied Poland during World War II...'),
       ('tt0167260', 'The Lord of the Rings: The Return of the King', 2003,
        'Gandalf and Aragorn lead the World of Men against Sauron''s army...'),
       ('tt0080684', 'Star Wars: Episode V - The Empire Strikes Back', 1980,
        'After the Rebels are brutally overpowered by the Empire on the ice planet Hoth...'),
       ('tt0137523', 'Fight Club', 1999,
        'An insomniac office worker and a devil-may-care soap maker form an underground fight club...'),
       ('tt0120737', 'The Lord of the Rings: The Fellowship of the Ring', 2001,
        'A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring...');

-- Insert Movie Pictures
INSERT INTO movie_pictures (imdbID, pictures)
VALUES ('tt0111161', 'http://example.com/shawshankredemption.jpg'),
       ('tt0111161', 'http://example.com/shawshank_cast.jpg'),
       ('tt0068646', 'http://example.com/thegodfather.jpg'),
       ('tt0068646', 'http://example.com/godfather_cast.jpg'),
       ('tt1375666', 'http://example.com/inception.jpg'),
       ('tt1375666', 'http://example.com/inception_cast.jpg'),
       ('tt0108052', 'http://example.com/schindlerlist.jpg'),
       ('tt0108052', 'http://example.com/schindlerlist_cast.jpg'),
       ('tt0167260', 'http://example.com/lotr_returnking.jpg'),
       ('tt0167260', 'http://example.com/lotr_returnking_cast.jpg'),
       ('tt0080684', 'http://example.com/empirestrikesback.jpg'),
       ('tt0080684', 'http://example.com/empirestrikesback_cast.jpg'),
       ('tt0137523', 'http://example.com/fightclub.jpg'),
       ('tt0137523', 'http://example.com/fightclub_cast.jpg'),
       ('tt0120737', 'http://example.com/lotr_fellowship.jpg'),
       ('tt0120737', 'http://example.com/lotr_fellowship_cast.jpg');

-- Insert Movie Cast
INSERT INTO movie_cast (movie_imdbID, actor_id)
VALUES ('tt0111161', 1),
       ('tt0111161', 2),
       ('tt0068646', 3),
       ('tt0068646', 4),
       ('tt1375666', 3),
       ('tt0108052', 4),
       ('tt0167260', 5),
       ('tt0080684', 6),
       ('tt0137523', 7),
       ('tt0120737', 8);
