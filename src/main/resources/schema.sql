CREATE TABLE IF NOT EXISTS app_user (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  login varchar,
  name varchar,
  email varchar,
  birthday date
);

CREATE TABLE IF NOT EXISTS friendship (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  user_id INTEGER REFERENCES app_user (id),
  friend_id INTEGER REFERENCES app_user (id),
  status boolean
);

CREATE TABLE IF NOT EXISTS rating (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS film (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar,
  description text,
  rating_id INTEGER REFERENCES rating (id),
  release_date date,
  duration INTEGER
);

COMMENT ON COLUMN film.duration IS 'in_minutes';

CREATE TABLE IF NOT EXISTS film_like (
  user_id INTEGER REFERENCES app_user (id),
  film_id INTEGER REFERENCES film (id),
  PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS genre (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id INTEGER REFERENCES film (id),
  genre_id INTEGER REFERENCES genre (id),
  PRIMARY KEY (film_id, genre_id)
);