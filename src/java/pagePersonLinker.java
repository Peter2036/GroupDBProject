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
                doStarStuff(out, id);
            } catch (Exception ex) {
                Logger.getLogger(pagePersonLinker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(type.equals("directors")){
             RequestDispatcher rd
                = request.getRequestDispatcher("/director_search.jsp");
            rd.include(request, response);
            try {
                doDirectorStuff(out, id);
            } catch (Exception ex) {
                Logger.getLogger(pagePersonLinker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(type.equals("writers")){
            RequestDispatcher rd
                = request.getRequestDispatcher("/writer_search.jsp");
            rd.include(request, response);
            try {
                doWriterStuff(out, id);
            } catch (Exception ex) {
                Logger.getLogger(pagePersonLinker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
          
    }

    private void doWriterStuff(PrintWriter out, String id) throws Exception{
        
        
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
        out.print("</div>");
        
        
        
        connection.close();  
        statement.close();
        resultSet.close();
        
    }

    private void doDirectorStuff(PrintWriter out, String id) throws Exception {
        
        
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
        out.print("</div>");
        
        
        
        connection.close();  
        statement.close();
        resultSet.close();
    }

    private void doStarStuff(PrintWriter out, String id) throws Exception{
        
        
        
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
        out.print("</div>");
        
        
        
        connection.close();  
        statement.close();
        resultSet.close();
    }
    
}
