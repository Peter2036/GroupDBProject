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

public class starListStars extends HttpServlet {
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
       
        PrintWriter out = response.getWriter();
        String id = (String)request.getParameter("id");
        String type = (String)request.getParameter("type");
        RequestDispatcher rd = null; 
        rd = request.getRequestDispatcher("pagePersonLinker?id=" + id + "&type=" + type);
        rd.include(request,response);
        try {
            listStars(type, out, id);
        } catch (SQLException ex) {
            Logger.getLogger(percentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        
   private void listStars(String type, PrintWriter out, String id) throws SQLException{
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
        
        
       String query = "select s1.star_ID as T1, b.name as Stars "
                    +"from stars_in s1, stars_in s2, stars b "
                    +"where s1.star_ID <> s2.star_ID "
                    +"and s1.movie_ID = s2.movie_ID "
                    +"and s2.star_ID = b.star_ID "
                    +"and s1.star_ID = " + id;
       
        ResultSet resultSet = null;
        PreparedStatement statement = null; 
        statement = connection.prepareStatement(query);
        
        resultSet = statement.executeQuery();
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<select size = \"10\">");
        int i = 0;
        while(resultSet.next()){
            out.print("<option value = \"" + i + "\"> + "
                + resultSet.getString("Stars")
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
