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

public class top50participation extends HttpServlet{
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        RequestDispatcher rd
                = request.getRequestDispatcher("/stats.jsp");
        rd.include(request, response);
        PrintWriter out = response.getWriter();  
        try {
            query(out);
        } catch (SQLException ex) {
           
        }
    }
    
     private void query(PrintWriter out) throws SQLException{
        InitialContext initialContext = null;
        try {
            initialContext = new InitialContext();
        } catch (NamingException ex) {
           
        }
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) initialContext.lookup("jdbc/group");
        } catch (NamingException ex) {
            
        }
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException ex) {
           
        }
        
        String query = "SELECT * FROM (\n" +
                        "\n" +
                        "SELECT COUNT(*) as NumberOfMovies, stars.name FROM stars, stars_in, movies\n" +
                        "where stars_in.movie_id = movies.movie_id\n" +
                        "and stars_in.star_id = stars.star_id\n" +
                        "group by stars.name\n" +
                        "order by count(*) desc\n" +
                        ")\n" +
                        "where rownum <= 50";
        
        ResultSet resultSet = null;
        PreparedStatement statement = connection.prepareStatement(query); 
        resultSet = statement.executeQuery(query);
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<select size = \"10\">");
        int i = 0;
        while(resultSet.next()){
            out.print("<option value = \"" + i + "\">"
                    + resultSet.getString("Name")
                    + "(" + resultSet.getString("NumberOfMovies")
                    + " movies)"
                    + "</option>" );
            i++;
        }
        out.print("</div>");
        try {  
            connection.close();
        } catch (SQLException ex) {
           
        }
        try {
            statement.close();
        } catch (SQLException ex) {
           
        }
        try {
            resultSet.close();
        } catch (SQLException ex) {
           
        }   
        
     }
}
