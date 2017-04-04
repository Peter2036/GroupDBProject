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

public class records extends HttpServlet {
    
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
            Logger.getLogger(records.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }
    
    private void query(PrintWriter out) throws SQLException{
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
        
        String query[] = new String[]{
                
            "SELECT count(*) FROM Movies",
            "SELECT count(*) FROM Stars",
            "SELECT count(*) FROM Directors", 
            "SELECT count(*) FROM Writers", 
            "SELECT count(*) FROM Stars_in",
            "SELECT count(*) FROM directs",
            "SELECT count(*) FROM writes_for"
        };
        
        String labels[] = new String[]{
            "Movies",
            "Stars",
            "Directors",
            "Writers",
            "Stars_in",
            "Directs",
            "Writes_for"
        };
        
        ResultSet resultSet = null;
        PreparedStatement statement = null; 
        int sum = 0;
        out.print("<div style = \"background-color: blue\" "
                + "align = \"center\"><body><Table border = \"2\"><TR>");
        out.print("<th id = \"Movies\" style = color:orange;font-size:20px > Movies </th>");
        out.print("<th id = \"Stars\" style = color:orange;font-size:20px > Stars </th>");
        out.print("<th id = \"Directors\" style = color:orange;font-size:20px> Directors </th>");
        out.print("<th id = \"Writers\" style = color:orange;font-size:20px> Writers </th>");
        out.print("<th id = \"Stars_in\" style = color:orange;font-size:20px> Stars_in </th>");
        out.print("<th id = \"Directs\" style = color:orange;font-size:20px> Directs </th>");
        out.print("<th id = \"Writes_for\" style = color:orange;font-size:20px> Writes_for </th>");
        out.print("</tr> </tr>");
        for(int i = 0; i < 7; i++){
                
                statement = connection.prepareStatement(query[i]);
                resultSet = statement.executeQuery();
                    while(resultSet.next()){
                        
                        out.print("<TD width = 100"
                                + " headers = \" " + labels[i] + "\""
                                + "style = color:orange;font-size:20px" + " >");
                        //out.print("<font color = \"orange\" ");
                        out.print(resultSet.getString("count(*)"));
                       // out.print("</font");
                        out.print("</TD>");
                        sum += Integer.parseInt(resultSet.getString("count(*)"));
                }
                 
        }
        out.print("</TR></Table></div>");
        out.print("<div align = \"center\" style = color:blue;font-size:20px>");
        out.print("Total Record Count: ");
        out.print(sum);
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
