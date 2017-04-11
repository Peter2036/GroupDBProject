/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author peter
 */

import java.io.*;  
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.sql.DataSource;

public class pageMovieLinker extends HttpServlet {
    
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter(); 
        String id = request.getParameter("id");
        RequestDispatcher rd
                = request.getRequestDispatcher("/movie_search.jsp");
        rd.include(request, response);
        
        try {
            generateMovieData(out, id);
        } catch (Exception ex) {
            Logger.getLogger(pageMovieLinker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generateMovieData(PrintWriter out, String id) throws Exception {
        
        InitialContext initialContext = new InitialContext();
        DataSource dataSource 
                = (DataSource) initialContext.lookup("jdbc/group");
        Connection connection 
                = dataSource.getConnection();
        
        String query = "SELECT * FROM Movies WHERE movie_ID = " + id;
        PreparedStatement statement
                = connection.prepareStatement(query);
        
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<h4 style = color:orange > About this movie </h1>");
        out.print("<h4 style = color:orange> ID: " + resultSet.getString("movie_ID") + "</h1>");
        out.print("<h4 style = color:orange> Title: " + resultSet.getString("title") + "</h1>");
        out.print("<h4 style = color:orange> Release year: " + resultSet.getString("release_year") + "</h1>");
        out.print("<h4 style = color:orange> Country: " + resultSet.getString("Country") + "</h1>");
        out.print("<h4 style = color:orange> IMDB rating: " + resultSet.getString("rating") + "</h1>");  
        out.print("<h4 style = color:orange> Votes: " + resultSet.getString("votes") + "</h1>");
        out.print("<h4 style = color:orange> Runtime: " + resultSet.getString("runtime") + "</h1>"); 
        
        query = "select movie_ID, Rank " 
                + "from (select Rownum as Rank, a.* from (select movie_ID, title, nvl(rating, 0), nvl(votes, 0) " 
                + "from movies " 
                + "order by rating desc, votes desc) a) " 
                + "where movie_ID = " + id;
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        out.print("<h4 style = color:orange> Ranking: " + resultSet.getString("Rank") + "</h1>"); 
        
        query = "select MW.movie_ID, D, W, S\n" +
                "from\n" +
                "((select count(*) as D, m.movie_ID\n" +
                "from directs d, movies m\n" +
                "where d.movie_ID = m.movie_ID\n" +
                "group by m.movie_ID)\n" +
                "union\n" +
                "(select 0 as D, b.movie_ID\n" +
                "from movies b\n" +
                "where not exists (select count(*) as D, m.movie_ID\n" +
                "from directs d, movies m\n" +
                "where d.movie_ID = m.movie_ID\n" +
                "and b.movie_ID = m.movie_ID\n" +
                "group by m.movie_ID))) MD,\n" +
                "((select count(*) as W, m.movie_ID\n" +
                "from writes_for w, movies m\n" +
                "where w.movie_ID = m.movie_ID\n" +
                "group by m.movie_ID)\n" +
                "union\n" +
                "(select 0 as W, b.movie_ID\n" +
                "from movies b\n" +
                "where not exists (select count(*) as W, m.movie_ID\n" +
                "from writes_for w, movies m\n" +
                "where w.movie_ID = m.movie_ID\n" +
                "and b.movie_ID = m.movie_ID\n" +
                "group by m.movie_ID))) MW,\n" +
                "((select count(*) as S, m.movie_ID\n" +
                "from movies m, stars_in s\n" +
                "where m.movie_ID = s.movie_ID\n" +
                "group by m.movie_ID)\n" +
                "union\n" +
                "(select 0 as S, b.movie_ID\n" +
                "from movies b\n" +
                "where not exists (select count(*) as S, m.movie_ID\n" +
                "from movies m, stars_in s\n" +
                "where m.movie_ID = s.movie_ID\n" +
                "and b.movie_ID = m.movie_ID\n" +
                "group by m.movie_ID))) MS,\n" +
                "movies m\n" +
                "where MD.movie_ID = MW.movie_ID\n" +
                "and MW.movie_ID = MS.movie_ID\n" +
                "and MW.movie_ID = m.movie_ID\n" +
                "and m.movie_ID = " + id;
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        out.print("<h4 style = color:orange> Amount of stars in movie: " + resultSet.getString("S") + "</h1>");
        out.print("<h4 style = color:orange> Amount of writers in movie: " + resultSet.getString("W") + "</h1>");
        out.print("<h4 style = color:orange> Amount of directors in movie: " + resultSet.getString("D") + "</h1>");

        out.print("</div>");
        
        out.print("<form name =\"movieListStars\" action =\"movieListStars\" method =\"GET\" enctype =\"text/plain\">");
        out.print("<div style = \"margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange\" align = \"center\">");
        out.print("<font color = \"blue\"><h3 style = \"margin-top:10px\">Get list of stars in movie</h3></font>");
        out.print("<input type = \"submit\" style =\"margin-bottom:10px;background-color:blue;color:orange\" value = \"Show\">");
        out.print("<input type = \"hidden\" name = \"id\" value = " + id + " \" >");
        out.print("</div>");
        out.print("</form>");
        
        out.print("<form name =\"movieListWriters\" action =\"movieListWriters\" method =\"GET\" enctype =\"text/plain\">");
        out.print("<div style = \"margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange\" align = \"center\">");
        out.print("<font color = \"blue\"><h3 style = \"margin-top:10px\">Get list of writers in movie</h3></font>");
        out.print("<input type = \"submit\" style =\"margin-bottom:10px;background-color:blue;color:orange\" value = \"Show\">");
        out.print("<input type = \"hidden\" name = \"id\" value = " + id + " \" >");
        out.print("</div>");
        out.print("</form>");
        
        out.print("<form name =\"movieListDirectors\" action =\"movieListDirectors\" method =\"GET\" enctype =\"text/plain\">");
        out.print("<div style = \"margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange\" align = \"center\">");
        out.print("<font color = \"blue\"><h3 style = \"margin-top:10px\">Get list of directors in movie</h3></font>");
        out.print("<input type = \"submit\" style =\"margin-bottom:10px;background-color:blue;color:orange\" value = \"Show\">");
        out.print("<input type = \"hidden\" name = \"id\" value = " + id + " \" >");
        out.print("</div>");
        out.print("</form>");
        
        out.print("<form name =\"debut\" action =\"debut\" method =\"GET\" enctype =\"text/plain\">");
        out.print("<div style = \"margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange\" align = \"center\">");
        out.print("<font color = \"blue\"><h3 style = \"margin-top:10px\">Get list of movies that debut in same year and country</h3></font>");
        out.print("<input type = \"submit\" style =\"margin-bottom:10px;background-color:blue;color:orange\" value = \"Show\">");
        out.print("<input type = \"hidden\" name = \"id\" value = " + id + " \" >");
        out.print("</div>");
        out.print("</form>");
        
        out.print("</div>");
        
        connection.close();  
        statement.close();
        resultSet.close();
    }
    
}
