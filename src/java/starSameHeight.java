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

public class starSameHeight extends HttpServlet {
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
            listHeight(type, out, id);
        } catch (SQLException ex) {
            Logger.getLogger(percentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void listHeight(String type, PrintWriter out, String id) throws SQLException{
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
        
        
       String query ="(select S2.name as Stars\n" +
                    "from stars S, stars S2\n" +
                    "where S.star_ID <> S2.star_ID\n" +
                    "and S.height = S2.height\n" +
                    "and S.height != 'NULL'\n" +
                    "and S.name != S2.name\n" +
                    "and S.star_ID = " + id + ")\n" +
                    "UNION\n" +
                    "(select W.name as Writers\n" +
                    "from stars S, writers W\n" +
                    "where S.height = W.height\n" +
                    "and S.height != 'NULL'\n" +
                    "and S.star_ID = " + id + ")\n" +
                    "UNION\n" +
                    "(select D.name as Directors\n" +
                    "from stars S, Directors D\n" +
                    "where S.height = D.height\n" +
                    "and S.height != 'NULL'\n" +
                    "and S.star_ID = " + id + ")";
       
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
