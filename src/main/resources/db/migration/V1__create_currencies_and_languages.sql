CREATE TABLE currencies (
  code CHAR(3) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  symbol VARCHAR(10)
);
CREATE TABLE languages (
  code VARCHAR(5) PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);
INSERT INTO languages (code, name) VALUES
  ('en','English'), ('ar','Arabic'), ('es','Spanish'), ('fr','French');