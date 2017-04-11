/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author peter
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.*;  
import java.sql.*;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.sql.DataSource;

public class debut extends HttpServlet {
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
       
        PrintWriter out = response.getWriter();
        String id = (String)request.getParameter("id");
        RequestDispatcher rd = null; 
        rd = request.getRequestDispatcher("pageMovieLinker?id=" + id);
        rd.include(request,response);
        try {
            listDirectors(out, id);
        } catch (SQLException ex) {
            Logger.getLogger(percentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void listDirectors(PrintWriter out, String id) throws SQLException{
        InitialContext initialContext = null;
        try {
            initialContext = new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(records.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) initialContext.lookup("jdbc/group");
        } catch (NamingException ex) {
            Logger.getLogger(records.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(records.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            String query = "select distinct m1.movie_ID as T1, m2.title as T2, m1.country, m1.release_year "
                + "from movies m1, movies m2 "
                + "where m1.movie_ID <> m2.movie_ID "
                + "and m1.country = m2.country " 
                + "and m1.release_year = m2.release_year "
                + "and m1.movie_ID = " + id;
       
        ResultSet resultSet = null;
        PreparedStatement statement = null; 
        statement = connection.prepareStatement(query);
        
        resultSet = statement.executeQuery();
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<select size = \"20\">");
        int i = 0;
        while(resultSet.next()){
            out.print("<option value = \"" + i + "\"> + "
                + resultSet.getString("T2")
                + "</option>" );
            i++;
        }
         out.print("</div>");

         try {  
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(records.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(records.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(records.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
