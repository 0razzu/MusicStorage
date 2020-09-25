DROP DATABASE IF EXISTS FcONqOaHRl;
CREATE DATABASE FcONqOaHRl;
USE FcONqOaHRl;


CREATE TABLE musician (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE track (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(127) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE musician_track (
    id INT NOT NULL AUTO_INCREMENT,
    musician_id INT,
    track_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY musician_track (musician_id, track_id),
    KEY musician_id (musician_id),
    KEY track_id (track_id),
    FOREIGN KEY (musician_id) REFERENCES musician (id) ON DELETE CASCADE,
    FOREIGN KEY (track_id) REFERENCES track (id) ON DELETE CASCADE
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;