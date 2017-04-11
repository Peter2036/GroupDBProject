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

public class actorsStarredIn10 extends HttpServlet {
    
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
        
        String query = "select distinct s.name " +
                        "from stars s, stars_in b " +
                        "where s.star_ID = b.star_ID " +
                        "and not exists (select * " +
                        "from movies p " +
                        "where p.movie_ID = b.movie_ID " +
                        "and nvl(p.rating,0) <> 10)";
        
        ResultSet resultSet = null;
        PreparedStatement statement = connection.prepareStatement(query); 
        resultSet = statement.executeQuery(query);
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<select size = \"25\">");
        int i = 0;
        while(resultSet.next()){
            out.print("<option value = \"" + i + "\">"
                    + resultSet.getString("name") + "</option>" );
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
