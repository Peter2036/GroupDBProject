create table movies(
movie_ID	number not null,
title		varchar(100) not null,
release_year integer,
Country varchar(25),
rating		number,
votes number,
runtime number,
primary key(movie_ID));

create table stars(
star_ID		number not null,
name		varchar(100) not null,
bday		varchar(25),
height varchar(25),
primary key(star_ID));

create table directors(
director_ID number not null,
name		varchar(100) not null,
bday		varchar(25),
height varchar(25),
primary key(director_ID));

create table writers(
writer_id number not null,
name		varchar(100) not null,
bday		varchar(25),
height varchar(25),
primary key(writer_ID));

create table stars_in(
movie_ID	number not null,
star_ID		number not null,
primary key(movie_ID, star_ID),
foreign key(movie_ID) references movies(movie_ID),
foreign key(star_ID) references stars(star_ID));

create table directs(
movie_ID	number not null,
director_ID	number not null,
primary key(movie_ID, director_ID),
foreign key(movie_ID) references movies(movie_ID),
foreign key(director_ID) references directors(director_ID));

create table writes_for(
movie_ID	number not null,
writer_ID	number not null,
primary key(movie_ID, writer_ID),
foreign key(movie_ID) references movies(movie_ID),
foreign key(writer_ID) references writers(writer_ID));
