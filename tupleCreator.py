from imdb import imdb

globvarCount = 0

def insertIntoMovies(file, movie_id, title, release_year, country, rating, votes, runtime):
    file.write("INSERT INTO MOVIES VALUES(", movie_id, ",'", title, "',", release_year, ",'", country, "',", rating, ",", votes, ",", runtime, ");")
    global globvarCount 
    globvarCount = globvarCount + 1
    return

def insertIntoStars(file, star_id, name, sex, bday, age):
    file.write("INSERT INTO STARS VALUES(", star_id, ",'", name, "','", bday, "','", height, "');")
    global globvarCount
    globvarCount = globvarCount + 1
    print globvarCount
    return

def insertIntoDirectors(file, director_id, name, sex, bday, age):
    file.write("INSERT INTO DIRECTORS VALUES(", director_id, ",'", name, "','", bday, "','", height, "');")
    global globvarCount
    globvarCount = globvarCount + 1
    print globvarCount
    return

def insertIntoWriters(file, composer_id, name, sex, bday, age, rating):
    file.write("INSERT INTO WRITERS VALUES(", writer_id, ",'", name, "','", bday, "','", height, "');")
    global globvarCount
    globvarCount = globvarCount + 1
    print globvarCount
    return

def insertIntoDirects(file, movie_id, director_id):
    file.write("INSERT INTO DIRECTS VALUES(", movie_id, ",", director_id, ");")
    global globvarCount
    globvarCount = globvarCount + 1
    print globvarCount
    return

def insertIntoStars_in(file, movie_id, star_id):
    file.write("INSERT INTO STARS_IN VALUES(", movie_id, ",", star_id, ");")
    global globvarCount
    globvarCount = globvarCount + 1
    print globvarCount
    return

def insertIntoWrites_for(file, movie_id, writer_id):
    file.write("INSERT INTO WRITES_FOR VALUES(", movie_id, ",", writer_id, ");")
    global globvarCount
    globvarCount = globvarCount + 1
    print globvarCount
    return

if __name__ == "__main__":
    file = open("tupleRecords.txt", 'w')
    ia = imdb.IMDb(accessSystem = 'http', adultSearch = 0)
    ia.get_person_infoset()
    for i in range(133093, 9999999):
        movie = ia.get_movie(str(i))
        ia.update(movie)
        try:
            if int(movie['year']) >= 1999 and movie['kind'] == 'movie':
                #movie data initialization
                movie_id = "NULL"
                title = "NULL"
                release_year = "NULL"
                country = "NULL"
                rating = "NULL"
                votes = "NULL"
                runtime = "NULL"
                
                #star data initialization
                star_id = "NULL"
                starName = "NULL"
                starBday = "NULL"
                starHeight = "NULL"

                #director data initialization
                director_id = "NULL"
                directorName = "NULL"
                directorBday = "NULL"
                directorHeight = "NULL"

                #writer data initialization
                writer_id = "NULL"
                writerName = "NULL"
                writerBday = "NULL"
                writerHeight = "NULL"

                #movie data search
                movie_id = str(i)
                title = movie['title']
                release_year = movie['year']
                country = movie['country'][0]
                rating = movie['rating']
                votes = movie['votes']
                runtime = movie['runtime']

                #movie insert generation
                insertIntoMovies(file, movie_id, title, release_year, country, rating, votes, runtime)

                #star data search
                for i in movie['cast']:
                    star = ia.get_person(i.getID())
                    star_id = star.getID()
                    starName = star['name']
                    starBday = star['birth date']
                    starHeight = star['height']

                #star insert generation
                insertIntoStars(file, star_id, starName, starBday, starHeight)
                insertIntoStars_in(file, movie_id, star_id)

                #director data search
                for i in movie['director']:
                    director = ia.get_person(i.getID())
                    director_id = director.getID()
                    directorName = director['name']
                    directorBday = director['birth date']
                    directorHeight = director['height']
                
                #director insert generation
                insertIntoDirectors(file, director_id, directorName, directorBday, directorHeight)
                insertIntoDirects(file, movie_id, director_id)
                    
                #writer data search
                for i in movie['writer']:
                    writer = ia.get_person(i.getID())
                    writer_id = writer.getID()
                    writerName = writer['name']
                    writerBday = writer['birth date']
                    writerHeight = writer['height']

                #writer insert generators    
                insertIntoWriters(file, writer_id, writerName, writerBday, writerHeight)
                insertIntoWrites_for(file, movie_id, writer_id)
            
        except KeyError:
            pass
    file.close()