-- Insert movies into Movie table
INSERT INTO Movie (imdbID, title, release_year, description)
VALUES ('tt0111161', 'The Shawshank Redemption', 1994, 'Two imprisoned men bond over a number of years...'),
       ('tt0068646', 'The Godfather', 1972, 'The aging patriarch of an organized crime dynasty...'),
       ('tt1375666', 'Inception', 2010,
        'A thief who steals corporate secrets through the use of dream-sharing technology...');

-- Insert pictures into movie_pictures table
INSERT INTO movie_pictures (imdbID, pictures)
VALUES ('tt0111161', 'http://example.com/shawshankredemption.jpg'),
       ('tt0111161', 'http://example.com/shawshank_cast.jpg'),
       ('tt0068646', 'http://example.com/thegodfather.jpg'),
       ('tt0068646', 'http://example.com/godfather_cast.jpg'),
       ('tt1375666', 'http://example.com/inception.jpg'),
       ('tt1375666', 'http://example.com/inception_cast.jpg');