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


public class recordsChartGetter extends HttpServlet{
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
 
        out.print("<p style = \"text-align:center\"><img \"middle\" border=\"1\" src=\"http://localhost:8080/GroupDBProject/recordsChart\"" +
    "width= \"500\" height=\"500\"></p>");
   
    }
}
