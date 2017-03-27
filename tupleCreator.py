from imdb import imdb
import re

globvarCount = 0

def insertIntoMovies(file, movie_id, title, release_year, country, rating, votes, runtime):
    file.write("INSERT INTO MOVIES VALUES(" + str(movie_id) + ",'" + str(title) + "'," + str(release_year) + ",'" + str(country) + "'," + str(rating) + "," + str(votes) + "," + str(runtime) + ");\n")
    global globvarCount 
    globvarCount = globvarCount + 1
    print "Record Count: " + str(globvarCount)
    return

def insertIntoStars(file, star_id, name, bday, height):
    file.write("INSERT INTO STARS VALUES(" + str(star_id) + ",'" + str(name) + "','" + str(bday) + "','" + str(height) + "');\n")
    global globvarCount
    globvarCount = globvarCount + 1
    print "Record Count: " + str(globvarCount)
    return

def insertIntoDirectors(file, director_id, name, bday, height):
    file.write("INSERT INTO DIRECTORS VALUES(" + str(director_id) + ",'" + str(name) + "','" + str(bday) + "','" + str(height) + "');\n")
    global globvarCount
    globvarCount = globvarCount + 1
    print "Record Count: " + str(globvarCount)
    return

def insertIntoWriters(file, writer_id, name, bday, height):
    file.write("INSERT INTO WRITERS VALUES(" + str(writer_id) + ",'" + str(name) + "','" + str(bday) + "','" + str(height) + "');\n")
    global globvarCount
    globvarCount = globvarCount + 1
    print "Record Count: " + str(globvarCount)
    return

def insertIntoDirects(file, movie_id, director_id):
    file.write("INSERT INTO DIRECTS VALUES(" + str(movie_id) + "," + str(director_id) + ");\n")
    global globvarCount
    globvarCount = globvarCount + 1
    print "Record Count: " + str(globvarCount)
    return

def insertIntoStars_in(file, movie_id, star_id):
    file.write("INSERT INTO STARS_IN VALUES(" + str(movie_id) + "," + str(star_id) + ");\n")
    global globvarCount
    globvarCount = globvarCount + 1
    print "Record Count: " + str(globvarCount)
    return

def insertIntoWrites_for(file, movie_id, writer_id):
    file.write("INSERT INTO WRITES_FOR VALUES(" + str(movie_id) + "," + str(writer_id) + ");\n")
    global globvarCount
    globvarCount = globvarCount + 1
    print "Record Count: " + str(globvarCount)
    return

if __name__ == "__main__":
    file = open("tupleRecords.txt", 'w')
    ia = imdb.IMDb(accessSystem = 'http', adultSearch = 0)
    for i in range(25, 9999999):
        movie = ia.get_movie(str(i))
        ia.update(movie)
        print "movieID: " + str(i)
        try:
            if int(movie['year']) >= 2010 and movie['kind'] == 'movie':
                #movie data initialization
                movie_id = "NULL"
                title = "NULL"
                release_year = "NULL"
                country = "NULL"
                rating = 0
                votes = 0
                runtime = 0
                
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
                title = movie['title'].encode('utf-8')
                try:
                    release_year = movie['year']
                except KeyError:
                    pass
                try:
                    country = movie['country'][0].encode('utf-8')
                except KeyError:
                    pass
                try:
                    rating = movie['rating']
                except KeyError:
                    pass
                try:
                    votes = movie['votes']
                except KeyError:
                    pass
                try:
                    runtime = movie['runtime'][0]
                    runtime = re.findall('\d+', runtime)[0].encode('utf-8')
                except KeyError:
                    pass
                #movie insert generation
                insertIntoMovies(file, movie_id, title, release_year, country, rating, votes, runtime)
                file.flush()

                #star data search
                for i in movie['cast']:
                    star = ia.get_person(i.getID())
                    star_id = star.getID()
                    starName = star['name'].encode('utf-8')
                    try:
                        starBday = star['birth date'].encode('utf-8')
                    except KeyError:
                        starBday = "NULL"
                        pass
                    try:
                        starHeight = star['height'].encode('utf-8')
                        if "'" in starHeight:
                            index = starHeight.index("'")
                            starHeight = starHeight[:index] + "'" + starHeight[index:]
                    except KeyError:
                        starHeight = "NULL"
                        pass
                    #star insert generation
                    insertIntoStars(file, star_id, starName, starBday, starHeight)
                    insertIntoStars_in(file, movie_id, star_id)
                    file.flush()

                #director data search
                for i in movie['director']:
                    director = ia.get_person(i.getID())
                    director_id = director.getID()
                    directorName = director['name'].encode('utf-8')
                    try:
                        directorBday = director['birth date'].encode('utf-8')
                    except KeyError:
                        directorBday = "NULL"
                        pass
                    try:
                        directorHeight = director['height'][0].encode('utf-8')
                    except KeyError:
                        directorHeight = "NULL"
                        pass
                    #director insert generation
                    insertIntoDirectors(file, director_id, directorName, directorBday, directorHeight)
                    insertIntoDirects(file, movie_id, director_id)
                    file.flush()
                    
                #writer data search
                for i in movie['writer']:
                    writer = ia.get_person(i.getID())
                    writer_id = writer.getID()
                    writerName = writer['name'].encode('utf-8')
                    try:
                        writerBday = writer['birth date'].encode('utf-8')
                    except KeyError:
                        writerBday = "NULL"
                        pass
                    try:
                        writerHeight = writer['height'].encode('utf-8')
                    except KeyError:
                        writerHeight = "NULL"
                        pass
                    #writer insert generators    
                    insertIntoWriters(file, writer_id, writerName, writerBday, writerHeight)
                    insertIntoWrites_for(file, movie_id, writer_id)
                    file.flush()
            
        except KeyError:
            pass
        except KeyboardInterrupt:
            file.flush()
            file.close()
    file.close()