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

public class pagePersonLinker extends HttpServlet {
    
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html"); 
        PrintWriter out = response.getWriter(); 
        String id = request.getParameter("id");
        String type = request.getParameter("type");
  
        if(type.equals("stars")){
             RequestDispatcher rd
                = request.getRequestDispatcher("/star_search.jsp");
            rd.include(request, response);
            try {
                doStarStuff(out, id, type);
            } catch (Exception ex) {
                Logger.getLogger(pagePersonLinker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(type.equals("directors")){
             RequestDispatcher rd
                = request.getRequestDispatcher("/director_search.jsp");
            rd.include(request, response);
            try {
                doDirectorStuff(out, id, type);
            } catch (Exception ex) {
                Logger.getLogger(pagePersonLinker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(type.equals("writers")){
            RequestDispatcher rd
                = request.getRequestDispatcher("/writer_search.jsp");
            rd.include(request, response);
            try {
                doWriterStuff(out, id, type);
            } catch (Exception ex) {
                Logger.getLogger(pagePersonLinker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
          
    }

    private void doWriterStuff(PrintWriter out, String id, String type) throws Exception{
        
        InitialContext initialContext = new InitialContext();
        DataSource dataSource 
                = (DataSource) initialContext.lookup("jdbc/group");
        Connection connection 
                = dataSource.getConnection();
        
        String query = "SELECT * FROM Writers WHERE writer_ID = " + id;
        PreparedStatement statement
                = connection.prepareStatement(query);
        
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<h4 style = color:orange > About this writer </h1>");
        out.print("<h4 style = color:orange> ID: " + resultSet.getString("writer_ID") + "</h1>");
        out.print("<h4 style = color:orange> Name: " + resultSet.getString("name") + "</h1>");
        out.print("<h4 style = color:orange> Birthday: " + resultSet.getString("bday") + "</h1>");
        out.print("<h4 style = color:orange> Height: " + resultSet.getString("height") + "</h1>");
        
        query = "select SW.writer_ID, fellowD, fellowW, fellowS " 
                +"from " 
                +"((select count(*) as fellowD, w.writer_ID " 
                +"from directs d, writes_for w " 
                +"where d.movie_ID = w.movie_ID " 
                +"group by w.writer_ID) " 
                +"union " 
                +"(select 0 as fellowD, writer_ID " 
                +"from writes_for b " 
                +"where not exists (select count(*) as fellowD, w.writer_ID " 
                +"from directs d, writes_for w " 
                +"where d.movie_ID = w.movie_ID " 
                +"and b.writer_ID = w.writer_ID " 
                +"group by w.writer_ID))) SD, " 
                +"((select count(*) as fellowW, w1.writer_ID " 
                +"from writes_for w1, writes_for w2 " 
                +"where w1.movie_ID = w2.movie_ID " 
                +"and w1.writer_ID <> w2.writer_ID " 
                +"group by w1.writer_ID) " 
                +"union " 
                +"(select 0 as fellowW, writer_ID " 
                +"from writes_for b " 
                +"where not exists (select count(*) as fellowW, w1.writer_ID " 
                +"from writes_for w1, writes_for w2 " 
                +"where w1.movie_ID = w2.movie_ID " 
                +"and w1.writer_ID <> w2.writer_ID " 
                +"and b.writer_ID = w1.writer_ID " 
                +"group by w1.writer_ID))) SW, " 
                +"((select count(*) as fellowS, w.writer_ID " 
                +"from stars_in s, writes_for w " 
                +"where s.movie_ID = w.movie_ID " 
                +"group by w.writer_ID) " 
                +"union " 
                +"(select 0 as fellowS, writer_ID " 
                +"from writes_for b " 
                +"where not exists (select count(*) as fellowS, w.writer_ID " 
                +"from stars_in s, writes_for w " 
                +"where s.movie_ID = w.movie_ID " 
                +"and b.writer_ID = w.writer_ID " 
                +"group by writer_ID))) SS, " 
                +"writers w " 
                +"where SD.writer_ID = SW.writer_ID " 
                +"and SW.writer_ID = SS.writer_ID " 
                +"and SW.writer_ID = w.writer_ID " 
                +"and w.writer_ID = " + id;
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<h4 style = color:orange> Amount of stars worked with: " + resultSet.getString("fellowS") + "</h1>");
        out.print("<h4 style = color:orange> Amount of other writers worked with: " + resultSet.getString("fellowW") + "</h1>");
        out.print("<h4 style = color:orange> Amount of directors worked with: " + resultSet.getString("fellowD") + "</h1>");
        
        query = "select * from ( "
                +"select writer_ID, count(*) as Total "
                +"from movies m, writes_for w "
                +"where m.movie_ID = w.movie_ID "
                +"group by writer_ID) "
                +"where writer_ID = " + id;
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<h4 style = color:orange> Amount of movies worked in: " + resultSet.getString("total") + "</h1>");
        
        out.print("</div>");

        out.print("<form name =\"percentDistributionGetter\" action =\"percentDistribution\" method =\"GET\" enctype =\"text/plain\">");
        out.print("<div style = \"margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange\" align = \"center\">");
        out.print("<font color = \"blue\"><h3 style = \"margin-top:10px\">Get percentage distribution of rating for movies starred in</h3></font>");
        out.print("<input type = \"submit\" style =\"margin-bottom:10px;background-color:blue;color:orange\" value = \"Show\">");
        out.print("<input type = \"hidden\" name = \"id\" value = " + id + " \" >");
        out.print("<input type = \"hidden\" name = \"type\" value = " + type + " \" >");
        out.print("</div>");
        out.print("</form>");
        
        connection.close();  
        statement.close();
        resultSet.close();
        
    }

    private void doDirectorStuff(PrintWriter out, String id, String type) throws Exception {
        
        InitialContext initialContext = new InitialContext();
        DataSource dataSource 
                = (DataSource) initialContext.lookup("jdbc/group");
        Connection connection 
                = dataSource.getConnection();
        
        String query = "SELECT * FROM Directors WHERE director_ID = " + id;
        PreparedStatement statement
                = connection.prepareStatement(query);
        
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<h4 style = color:orange > About this director </h1>");
        out.print("<h4 style = color:orange> ID: " + resultSet.getString("director_ID") + "</h1>");
        out.print("<h4 style = color:orange> Name: " + resultSet.getString("name") + "</h1>");
        out.print("<h4 style = color:orange> Birthday: " + resultSet.getString("bday") + "</h1>");
        out.print("<h4 style = color:orange> Height: " + resultSet.getString("height") + "</h1>");
        
        query = "select SW.director_ID, fellowD, fellowW, fellowS " 
                +"from " 
                +"((select count(*) as fellowD, d.director_ID " 
                +"from directs d, directs d2 " 
                +"where d.movie_ID = d2.movie_ID " 
                +"and d.director_ID <> d2.director_ID " 
                +"group by d.director_ID) " 
                +"union " 
                +"(select 0 as fellowD, director_ID " 
                +"from directs b " 
                +"where not exists (select count(*) as fellowD, d.director_ID " 
                +"from directs d, directs d2 " 
                +"where d.movie_ID = d2.movie_ID " 
                +"and d.director_ID <> d2.director_ID " 
                +"and b.director_ID = d.director_ID " 
                +"group by d.director_ID))) SD, " 
                +"((select count(*) as fellowW, director_ID " 
                +"from writes_for w, directs d " 
                +"where w.movie_ID = d.movie_ID " 
                +"group by director_ID) " 
                +"union " 
                +"(select 0 as fellowW, director_ID " 
                +"from directs b " 
                +"where not exists (select count(*) as fellowW, director_ID " 
                +"from writes_for w, directs d " 
                +"where w.movie_ID = d.movie_ID " 
                +"and b.director_ID = d.director_ID " 
                +"group by director_ID))) SW, " 
                +"((select count(*) as fellowS, director_ID " 
                +"from stars_in s, directs d " 
                +"where s.movie_ID = d.movie_ID " 
                +"group by director_ID) " 
                +"union " 
                +"(select 0 as fellowS, director_ID " 
                +"from directs b " 
                +"where not exists (select count(*) as fellowS, director_ID " 
                +"from stars_in s, directs d " 
                +"where s.movie_ID = d.movie_ID " 
                +"and b.director_ID = d.director_ID " 
                +"group by director_ID))) SS, " 
                +"directors d " 
                +"where SD.director_ID = SW.director_ID " 
                +"and SW.director_ID = SS.director_ID " 
                +"and SW.director_ID = d.director_ID " 
                +"and d.director_ID = " + id;
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<h4 style = color:orange> Amount of stars worked with: " + resultSet.getString("fellowS") + "</h1>");
        out.print("<h4 style = color:orange> Amount of writers worked with: " + resultSet.getString("fellowW") + "</h1>");
        out.print("<h4 style = color:orange> Amount of other directors worked with: " + resultSet.getString("fellowD") + "</h1>");
        
        query = "select * from ( "
                +"select director_ID, count(*) as Total "
                +"from movies m, directs d "
                +"where m.movie_ID = d.movie_ID "
                +"group by director_ID) "
                +"where director_ID = " + id;
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<h4 style = color:orange> Amount of movies worked in: " + resultSet.getString("total") + "</h1>");
        
        out.print("</div>");
        
        out.print("<form name =\"percentDistribution\" action =\"percentDistribution\" method =\"GET\" enctype =\"text/plain\">");
        out.print("<div style = \"margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange\" align = \"center\">");
        out.print("<font color = \"blue\"><h3 style = \"margin-top:10px\">Get percentage distribution of rating for movies directed</h3></font>");
        out.print("<input type = \"submit\" style =\"margin-bottom:10px;background-color:blue;color:orange\" value = \"Show\">");
        out.print("<input type = \"hidden\" name = \"id\" value = " + id + " \" >");
        out.print("<input type = \"hidden\" name = \"type\" value = " + type + " \" >");
        out.print("</div>");
        out.print("</form>");
        
        connection.close();  
        statement.close();
        resultSet.close();
    }

    private void doStarStuff(PrintWriter out, String id, String type) throws Exception{
          
        InitialContext initialContext = new InitialContext();
        DataSource dataSource 
                = (DataSource) initialContext.lookup("jdbc/group");
        Connection connection 
                = dataSource.getConnection();
        
        String query = "SELECT * FROM Stars WHERE star_ID = " + id;
        PreparedStatement statement
                = connection.prepareStatement(query);
        
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<h4 style = color:orange > About this star </h1>");
        out.print("<h4 style = color:orange> ID: " + resultSet.getString("star_ID") + "</h1>");
        out.print("<h4 style = color:orange> Name: " + resultSet.getString("name") + "</h1>");
        out.print("<h4 style = color:orange> Birthday: " + resultSet.getString("bday") + "</h1>");
        out.print("<h4 style = color:orange> Height: " + resultSet.getString("height") + "</h1>");
        
        query = "select SW.star_ID, fellowD, fellowW, fellowS " 
                +"from " 
                +"((select count(*) as fellowD, star_ID " 
                +"from directs d, stars_in s " 
                +"where d.movie_ID = s.movie_ID " 
                +"group by star_ID) " 
                +"union " 
                +"(select 0 as fellowD, star_ID " 
                +"from stars_in b " 
                +"where not exists (select count(*) as fellowD, star_ID " 
                +"from directs d, stars_in s " 
                +"where d.movie_ID = s.movie_ID " 
                +"and b.star_ID = s.star_ID " 
                +"group by star_ID))) SD, " 
                +"((select count(*) as fellowW, star_ID " 
                +"from writes_for w, stars_in s " 
                +"where w.movie_ID = s.movie_ID " 
                +"group by star_ID) " 
                +"union " 
                +"(select 0 as fellowW, star_ID " 
                +"from stars_in b " 
                +"where not exists (select count(*) as fellowW, star_ID " 
                +"from writes_for w, stars_in s " 
                +"where w.movie_ID = s.movie_ID " 
                +"and b.star_ID = s.star_ID " 
                +"group by star_ID))) SW, " 
                +"((select count(*) as fellowS, s1.star_ID " 
                +"from stars_in s1, stars_in s2 " 
                +"where s1.movie_ID = s2.movie_ID " 
                +"and s1.star_ID <> s2.star_ID " 
                +"group by s1.star_ID) " 
                +"union " 
                +"(select 0 as fellowS, b.star_ID " 
                +"from stars_in b " 
                +"where not exists (select count(*) as fellowS, s1.star_ID " 
                +"from stars_in s1, stars_in s2 " 
                +"where s1.movie_ID = s2.movie_ID " 
                +"and s1.star_ID <> s2.star_ID " 
                +"and b.star_ID = s1.star_ID " 
                +"group by s1.star_ID))) SS, " 
                +"stars s " 
                +"where SD.star_ID = SW.star_ID " 
                +"and SW.star_ID = SS.star_ID " 
                +"and SW.star_ID = s.star_ID " 
                +"and s.star_ID = " + id;
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<h4 style = color:orange> Amount of other stars worked with: " + resultSet.getString("fellowS") + "</h1>");
        out.print("<h4 style = color:orange> Amount of writers worked with: " + resultSet.getString("fellowW") + "</h1>");
        out.print("<h4 style = color:orange> Amount of directors worked with: " + resultSet.getString("fellowD") + "</h1>");
        
        query = "select * from ( "
                +"select star_ID, count(*) as Total "
                +"from movies m, stars_in s "
                +"where m.movie_ID = s.movie_ID "
                +"group by star_ID) "
                +"where star_ID = " + id;

        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        resultSet.next();
        
        out.print("<h4 style = color:orange> Amount of movies worked in: " + resultSet.getString("total") + "</h1>");
        
       
        out.print("</div>");
        
        out.print("<form name =\"percentDistribution\" action =\"percentDistribution\" method =\"GET\" enctype =\"text/plain\">");
        out.print("<div style = \"margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange\" align = \"center\">");
        out.print("<font color = \"blue\"><h3 style = \"margin-top:10px\">Get percentage distribution of rating for movies written</h3></font>");
        out.print("<input type = \"submit\" style =\"margin-bottom:10px;background-color:blue;color:orange\" value = \"Show\">");
        out.print("<input type = \"hidden\" name = \"id\" value = " + id + " \" >");
        out.print("<input type = \"hidden\" name = \"type\" value = " + type + " \" >");
        out.print("</div>");
        out.print("</form>");
        
        connection.close();  
        statement.close();
        resultSet.close();
    }
    
}
