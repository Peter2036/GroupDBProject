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
   
        out.print("</div>");
        
        query = "select distinct m1.movie_ID as T1, m2.title as T2, m1.country, m1.release_year "
                + "from movies m1, movies m2 "
                + "where m1.movie_ID <> m2.movie_ID "
                + "and m1.country = m2.country " 
                + "and m1.release_year = m2.release_year "
                + "and rownum <= 50 "
                + "and m1.movie_ID = " + id;
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<h4 style = color:orange > Movies filmed in same country and debuted in same year </h1>");
        int i = 0;
        out.print("<select size = \"10\">");
        while(resultSet.next()){
            out.print("<option value = \"" + i + "\"> + "
                    + resultSet.getString("T2") + "</option>" );
            i++;
        }
        out.print("</div>");
        
        connection.close();  
        statement.close();
        resultSet.close();
    }
    
}
