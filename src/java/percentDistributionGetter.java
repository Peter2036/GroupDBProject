
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author peter
 */
public class percentDistributionGetter extends HttpServlet {
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        String id = (String)request.getAttribute("id");
        String type = (String)request.getAttribute("type");
        PrintWriter out = response.getWriter();  

        if(type.equals("stars")){
            RequestDispatcher rd
                = request.getRequestDispatcher("/star_search.jsp");
            rd.include(request, response);
        }
        else if(type.equals("writers")){
            RequestDispatcher rd
                = request.getRequestDispatcher("/writer_search.jsp");
            rd.include(request, response);   
        }
        else if(type.equals("directors")){
            RequestDispatcher rd
                = request.getRequestDispatcher("/director_search.jsp");
            rd.include(request, response);
        }   
        
        out.print("<p style = \"text-align:center\"><img \"middle\" border=\"1\" src=\"http://localhost:8080/GroupDBProject/percentDistribution\"" +
            "width= \"500\" height=\"500\"></p>");
    }
}
