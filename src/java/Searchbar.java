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

public class Searchbar extends HttpServlet {
    
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();        
        
        String radioVal = (String)request.getParameter("Selection");
        String searchText = (String)request.getParameter("Search bar");

        if(searchText.equals("") && radioVal != null){
            RequestDispatcher rd
                = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);
            out.println(
                    "<font color =\"red\"><center><p> Please enter a phrase to search </p></center></font>");
        }
        else if(radioVal == null && !searchText.equals("")){
            RequestDispatcher rd
                = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);
            out.println(
                    "<font color =\"red\"><center><p> Please select an entity to search. </p></center></font>");
        }
        else if(radioVal == null && searchText.equals("")){
            RequestDispatcher rd
                = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);
            out.println(
                    "<font color =\"red\"><center><p> Please select an entity to search. </p></center></font>");
            out.println(
                    "<font color =\"red\"><center><p> Please enter a phrase to search </p></center></font>");
        }
        else{
            if(radioVal.equals("0")){
             
                RequestDispatcher rd
                    = request.getRequestDispatcher("/index.jsp");
                rd.include(request, response);
                try {
                    generateSearchQueryHTML(searchText, "movies", out);
                } catch (NamingException | SQLException ex) {
                    Logger.getLogger(Searchbar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(radioVal.equals("1")){
                
                RequestDispatcher rd
                    = request.getRequestDispatcher("/index.jsp");
                rd.include(request, response);
                try {
                    generateSearchQueryHTML(searchText, "stars", out);
                } catch (NamingException | SQLException ex) {
                    Logger.getLogger(Searchbar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(radioVal.equals("2")){
                
                RequestDispatcher rd
                    = request.getRequestDispatcher("/index.jsp");
                rd.include(request, response);
                try {
                    generateSearchQueryHTML(searchText, "directors", out);
                } catch (NamingException | SQLException ex) {
                    Logger.getLogger(Searchbar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(radioVal.equals("3")){
                
                RequestDispatcher rd
                    = request.getRequestDispatcher("/index.jsp");
                rd.include(request, response);
                try {
                    generateSearchQueryHTML(searchText, "writers", out);
                } catch (NamingException | SQLException ex) {
                    Logger.getLogger(Searchbar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }   
    }
    
    private void generateSearchQueryHTML(String searchText, String entity, PrintWriter out) throws NamingException, SQLException{
        InitialContext initialContext = new InitialContext();
        DataSource dataSource 
                = (DataSource) initialContext.lookup("jdbc/group");
        Connection connection 
                = dataSource.getConnection();
        
        String query;
        if(entity.equals("movies")){
            query = "SELECT * FROM "
               + "movies "
               + "WHERE upper(title) LIKE upper(\'%%" 
               + searchText + "%%\')" 
               + " AND ROWNUM <= 50";
        }
        else if(entity.equals("stars")){
            query = "SELECT * FROM "
               + "stars "
               + "WHERE upper(name) LIKE upper(\'%%"
               + searchText + "%%\')" 
               + " AND ROWNUM <= 50";
        }
        else if(entity.equals("directors")){
            query = "SELECT * FROM "
               + "directors "
               + "WHERE upper(name) LIKE upper(\'%%"
               + searchText + "%%\')" 
               + " AND ROWNUM <= 50";
        }
        else{
            query = "SELECT * FROM "
               + "writers "
               + "WHERE upper(name) LIKE upper(\'%%"
               + searchText + "%%\')" 
               + " AND ROWNUM <= 50";
        }
        
        PreparedStatement statement
                = connection.prepareStatement(query);
        
        ResultSet resultSet = statement.executeQuery();
        
        if(entity.equals("movies")){
            out.print("<h2 align = \"center\" style = color:orange> Search Results </h2>)");
            out.print("<div align = \"center\" style = \"background-color:blue\">");
            out.print("<html><body><table border = \"3\">");
            while(resultSet.next()){
                out.print("<tr><td style = color:orange;font-size:20px>");
                
                 out.print("<form name =\"routeToMoviePage\" action =\"pageMovieLinker\" method =\"GET\" enctype =\" text/plain\">\n" +
"<input type = \"submit\" style =\"margin-bottom:10px;background-color:orange;color:blue;float:right\" value = \"More Info\">"
                         + "<input type = \"hidden\" name = \"id\" value = " + resultSet.getString("movie_ID") + " \" ></form>");
                out.print(resultSet.getString("title"));
                out.print("</td></tr>");
            }
            out.print("</table></body></html>");
            out.print("</div>");
        }
        else{
            out.print("<h2 align = \"center\" style = color:orange> Search Results </h2>)");
            out.print("<div align = \"center\" style = \"background-color:blue\">");
            out.print("<html><body><table border = \"3\">");
            while(resultSet.next()){
                out.print("<tr><td style = color:orange;font-size:20px>");
                
                if(entity.equals("stars")){
                     out.print("<form name =\"routeToPersonPage\" action =\"pagePersonLinker\" method =\"GET\" enctype =\" text/plain\">\n" +
"<input type = \"submit\" style =\"margin-bottom:10px;background-color:orange;color:blue;float:right\" value = \"More Info\">"
                    + "<input type = \"hidden\" name = \"id\" value = " + resultSet.getString("star_ID") + " \" >"
                    + "<input type = \"hidden\" name = \"type\" value = \"stars\" ></form>" );
                }
                else if(entity.equals("directors")){
                     out.print("<form name =\"routeToPersonPage\" action =\"pagePersonLinker\" method =\"GET\" enctype =\" text/plain\">\n" +
"<input type = \"submit\" style =\"margin-bottom:10px;background-color:orange;color:blue;float:right\" value = \"More Info\">"
                    + "<input type = \"hidden\" name = \"id\" value = " + resultSet.getString("director_ID") + " \" >"
                    + "<input type = \"hidden\" name = \"type\" value = \"directors\" ></form>" );
                    
                }
                else if(entity.equals("writers")){
                     out.print("<form name =\"routeToPersonPage\" action =\"pagePersonLinker\" method =\"GET\" enctype =\" text/plain\">\n" +
"<input type = \"submit\" style =\"margin-bottom:10px;background-color:orange;color:blue;float:right\" value = \"More Info\">"
                    + "<input type = \"hidden\" name = \"id\" value = " + resultSet.getString("writer_id") + " \" >"
                    + "<input type = \"hidden\" name = \"type\" value = \"writers\" ></form>" );
                    
                }
               
                out.print(resultSet.getString("name"));
                out.print("</td><tr>");
            }
            out.print("</table></body></html>");
            out.print("</div>");
        }
        connection.close();  
        statement.close();
        resultSet.close();
    }
}