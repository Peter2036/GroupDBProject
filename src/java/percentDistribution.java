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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.jdbc.JDBCPieDataset;

public class percentDistribution extends HttpServlet {
    
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
        if(type.equals("stars")){
            rd = request.getRequestDispatcher("pagePersonLinker?id=" + id + "&type=" + type);
        }
        else if(type.equals("writers")){
           rd = request.getRequestDispatcher("pagePersonLinker?id=" + id + "&type=" + type); 
        }
        else if(type.equals("directors")){
            rd = request.getRequestDispatcher("pagePersonLinker?id=" + id + "&type=" + type);
        }   
        rd.include(request,response);
        try {
            listDistribution(type, out, id);
        } catch (SQLException ex) {
            Logger.getLogger(percentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void listDistribution(String type, PrintWriter out, String id) throws SQLException{
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
        
        
        String queryStar = 
            "select ten.star_ID, Total, "
            +"((zeroOne/Total) * 100) as \"0-1/NULL\", "
            +"((oneTwo/Total) * 100) as \"1-2\", "
            +"((twoThree/Total) * 100) as \"2-3\", "
            +"((threeFour/Total) * 100) as \"3-4\", "
            +"((fourFive/Total) * 100) as \"4-5\", "
            +"((fiveSix/Total) * 100) as \"5-6\", "
            +"((sixSeven/Total) * 100) as \"6-7\", "
            +"((sevenEight/Total) * 100) as \"7-8\", "
            +"((eightNine/Total) * 100) as \"8-9\", "
            +"((nineTen/Total) * 100) as \"9-10\" "
            +"from "
            +"((select count(*) as nineTen, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 9 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as nineTen, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as nineTen, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 9 "
            +"group by star_ID))) ten, "
            +"(select count(*) as Total, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"group by star_ID) tot, "
            +"((select count(*) as eightNine, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 8 "
            +"and nvl(m.rating, 0) <= 9 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as eightNine, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as eightNine, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 8 "
            +"and nvl(m.rating, 0) <= 9 "
            +"group by star_ID))) nine, "
            +"((select count(*) as sevenEight, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 7 "
            +"and nvl(m.rating, 0) <= 8 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as sevenEight, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as sevenEight, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 7 "
            +"and nvl(m.rating, 0) <= 8 "
            +"group by star_ID))) eight, "
            +"((select count(*) as sixSeven, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 6 "
            +"and nvl(m.rating, 0) <= 7 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as sixSeven, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as sixSeven, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 6 "
            +"and nvl(m.rating, 0) <= 7 "
            +"group by star_ID))) seven, "
            +"((select count(*) as fiveSix, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 5 "
            +"and nvl(m.rating, 0) <= 6 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as fiveSix, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as fiveSix, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 5 "
            +"and nvl(m.rating, 0) <= 6 "
            +"group by star_ID))) six, "
            +"((select count(*) as fourFive, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 4 "
            +"and nvl(m.rating, 0) <= 5 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as fourFive, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as fourFive, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 4 "
            +"and nvl(m.rating, 0) <= 5 "
            +"group by star_ID))) five, "
            +"((select count(*) as threeFour, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 3 "
            +"and nvl(m.rating, 0) <= 4 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as threeFour, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as threeFour, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 3 "
            +"and nvl(m.rating, 0) <= 4 "
            +"group by star_ID))) four, "
            +"((select count(*) as twoThree, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 2 "
            +"and nvl(m.rating, 0) <= 3 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as twoThree, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as twoThree, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 2 "
            +"and nvl(m.rating, 0) <= 3 "
            +"group by star_ID))) three, "
            +"((select count(*) as oneTwo, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 1 "
            +"and nvl(m.rating, 0) <= 2 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as oneTwo, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as oneTwo, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) > 1 "
            +"and nvl(m.rating, 0) <= 2 "
            +"group by star_ID))) two, "
            +"((select count(*) as zeroOne, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) >= 0 "
            +"and nvl(m.rating, 0) <= 1 "
            +"group by star_ID) "
            +"union "
            +"(select 0 as zeroOne, star_ID "
            +"from stars_in p "
            +"where not exists (select count(*) as zeroOne, star_ID "
            +"from movies m, stars_in s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.star_ID = p.star_ID "
            +"and nvl(m.rating, 0) >= 0 "
            +"and nvl(m.rating, 0) <= 1 "
            +"group by star_ID))) one "
            +"where ten.star_ID = tot.star_ID "
            +"and nine.star_ID = tot.star_ID "
            +"and eight.star_ID = tot.star_ID "
            +"and seven.star_ID = tot.star_ID "
            +"and six.star_ID = tot.star_ID "
            +"and five.star_ID = tot.star_ID "
            +"and four.star_ID = tot.star_ID "
            +"and three.star_ID = tot.star_ID "
            +"and two.star_ID = tot.star_ID "
            +"and one.star_ID = tot.star_ID "
            +"and one.star_ID = " + id;
        

        String queryWriter = 
            "select ten.writer_ID, Total, "
            +"((zeroOne/Total) * 100) as \"0-1/NULL\", "
            +"((oneTwo/Total) * 100) as \"1-2\", "
            +"((twoThree/Total) * 100) as \"2-3\", "
            +"((threeFour/Total) * 100) as \"3-4\", "
            +"((fourFive/Total) * 100) as \"4-5\", "
            +"((fiveSix/Total) * 100) as \"5-6\", "
            +"((sixSeven/Total) * 100) as \"6-7\", "
            +"((sevenEight/Total) * 100) as \"7-8\", "
            +"((eightNine/Total) * 100) as \"8-9\", "
            +"((nineTen/Total) * 100) as \"9-10\" "
            +"from "
            +"((select count(*) as nineTen, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 9 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as nineTen, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as nineTen, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 9 "
            +"group by writer_ID))) ten, "
            +"(select count(*) as Total, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"group by writer_ID) tot, "
            +"((select count(*) as eightNine, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 8 "
            +"and nvl(m.rating, 0) <= 9 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as eightNine, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as eightNine, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 8 "
            +"and nvl(m.rating, 0) <= 9 "
            +"group by writer_ID))) nine, "
            +"((select count(*) as sevenEight, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 7 "
            +"and nvl(m.rating, 0) <= 8 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as sevenEight, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as sevenEight, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 7 "
            +"and nvl(m.rating, 0) <= 8 "
            +"group by writer_ID))) eight, "
            +"((select count(*) as sixSeven, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 6 "
            +"and nvl(m.rating, 0) <= 7 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as sixSeven, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as sixSeven, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 6 "
            +"and nvl(m.rating, 0) <= 7 "
            +"group by writer_ID))) seven, "
            +"((select count(*) as fiveSix, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 5 "
            +"and nvl(m.rating, 0) <= 6 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as fiveSix, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as fiveSix, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 5 "
            +"and nvl(m.rating, 0) <= 6 "
            +"group by writer_ID))) six, "
            +"((select count(*) as fourFive, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 4 "
            +"and nvl(m.rating, 0) <= 5 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as fourFive, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as fourFive, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 4 "
            +"and nvl(m.rating, 0) <= 5 "
            +"group by writer_ID))) five, "
            +"((select count(*) as threeFour, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 3 "
            +"and nvl(m.rating, 0) <= 4 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as threeFour, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as threeFour, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 3 "
            +"and nvl(m.rating, 0) <= 4 "
            +"group by writer_ID))) four, "
            +"((select count(*) as twoThree, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 2 "
            +"and nvl(m.rating, 0) <= 3 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as twoThree, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as twoThree, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 2 "
            +"and nvl(m.rating, 0) <= 3 "
            +"group by writer_ID))) three, "
            +"((select count(*) as oneTwo, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 1 "
            +"and nvl(m.rating, 0) <= 2 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as oneTwo, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as oneTwo, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) > 1 "
            +"and nvl(m.rating, 0) <= 2 "
            +"group by writer_ID))) two, "
            +"((select count(*) as zeroOne, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) >= 0 "
            +"and nvl(m.rating, 0) <= 1 "
            +"group by writer_ID) "
            +"union "
            +"(select 0 as zeroOne, writer_ID "
            +"from writes_for p "
            +"where not exists (select count(*) as zeroOne, writer_ID "
            +"from movies m, writes_for s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.writer_ID = p.writer_ID "
            +"and nvl(m.rating, 0) >= 0 "
            +"and nvl(m.rating, 0) <= 1 "
            +"group by writer_ID))) one "
            +"where ten.writer_ID = tot.writer_ID "
            +"and nine.writer_ID = tot.writer_ID "
            +"and eight.writer_ID = tot.writer_ID "
            +"and seven.writer_ID = tot.writer_ID "
            +"and six.writer_ID = tot.writer_ID "
            +"and five.writer_ID = tot.writer_ID "
            +"and four.writer_ID = tot.writer_ID "
            +"and three.writer_ID = tot.writer_ID "
            +"and two.writer_ID = tot.writer_ID "
            +"and one.writer_ID = tot.writer_ID "
            +"and one.writer_ID = " + id;
            
        String queryDirector = 
             "select ten.director_ID, Total, "
            +"((zeroOne/Total) * 100) as \"0-1/NULL\", "
            +"((oneTwo/Total) * 100) as \"1-2\", "
            +"((twoThree/Total) * 100) as \"2-3\", "
            +"((threeFour/Total) * 100) as \"3-4\", "
            +"((fourFive/Total) * 100) as \"4-5\", "
            +"((fiveSix/Total) * 100) as \"5-6\", "
            +"((sixSeven/Total) * 100) as \"6-7\", "
            +"((sevenEight/Total) * 100) as \"7-8\", "
            +"((eightNine/Total) * 100) as \"8-9\", "
            +"((nineTen/Total) * 100) as \"9-10\" "
            +"from "
            +"((select count(*) as nineTen, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 9 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as nineTen, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as nineTen, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 9 "
            +"group by director_ID))) ten, "
            +"(select count(*) as Total, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"group by director_ID) tot, "
            +"((select count(*) as eightNine, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 8 "
            +"and nvl(m.rating, 0) <= 9 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as eightNine, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as eightNine, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 8 "
            +"and nvl(m.rating, 0) <= 9 "
            +"group by director_ID))) nine, "
            +"((select count(*) as sevenEight, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 7 "
            +"and nvl(m.rating, 0) <= 8 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as sevenEight, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as sevenEight, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 7 "
            +"and nvl(m.rating, 0) <= 8 "
            +"group by director_ID))) eight, "
            +"((select count(*) as sixSeven, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 6 "
            +"and nvl(m.rating, 0) <= 7 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as sixSeven, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as sixSeven, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 6 "
            +"and nvl(m.rating, 0) <= 7 "
            +"group by director_ID))) seven, "
            +"((select count(*) as fiveSix, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 5 "
            +"and nvl(m.rating, 0) <= 6 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as fiveSix, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as fiveSix, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 5 "
            +"and nvl(m.rating, 0) <= 6 "
            +"group by director_ID))) six, "
            +"((select count(*) as fourFive, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 4 "
            +"and nvl(m.rating, 0) <= 5 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as fourFive, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as fourFive, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 4 "
            +"and nvl(m.rating, 0) <= 5 "
            +"group by director_ID))) five, "
            +"((select count(*) as threeFour, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 3 "
            +"and nvl(m.rating, 0) <= 4 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as threeFour, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as threeFour, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 3 "
            +"and nvl(m.rating, 0) <= 4 "
            +"group by director_ID))) four, "
            +"((select count(*) as twoThree, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 2 "
            +"and nvl(m.rating, 0) <= 3 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as twoThree, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as twoThree, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 2 "
            +"and nvl(m.rating, 0) <= 3 "
            +"group by director_ID))) three, "
            +"((select count(*) as oneTwo, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) > 1 "
            +"and nvl(m.rating, 0) <= 2 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as oneTwo, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as oneTwo, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) > 1 "
            +"and nvl(m.rating, 0) <= 2 "
            +"group by director_ID))) two, "
            +"((select count(*) as zeroOne, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and nvl(m.rating, 0) >= 0 "
            +"and nvl(m.rating, 0) <= 1 "
            +"group by director_ID) "
            +"union "
            +"(select 0 as zeroOne, director_ID "
            +"from directs p "
            +"where not exists (select count(*) as zeroOne, director_ID "
            +"from movies m, directs s "
            +"where m.movie_ID = s.movie_ID "
            +"and s.director_ID = p.director_ID "
            +"and nvl(m.rating, 0) >= 0 "
            +"and nvl(m.rating, 0) <= 1 "
            +"group by director_ID))) one "
            +"where ten.director_ID = tot.director_ID "
            +"and nine.director_ID = tot.director_ID "
            +"and eight.director_ID = tot.director_ID "
            +"and seven.director_ID = tot.director_ID "
            +"and six.director_ID = tot.director_ID "
            +"and five.director_ID = tot.director_ID "
            +"and four.director_ID = tot.director_ID "
            +"and three.director_ID = tot.director_ID "
            +"and two.director_ID = tot.director_ID "
            +"and one.director_ID = tot.director_ID "
            +"and one.director_ID = " + id;

        
        String labels[] = new String[]{
            "0-1/NULL: ",
            "1-2: ",
            "2-3: ",
            "3-4: ",
            "4-5: ",
            "5-6: ",
            "6-7: ",
            "7-8: ",
            "8-9: "
        };
        
        ResultSet resultSet = null;
        PreparedStatement statement = null; 
        if(type.equals("stars")){
            statement = connection.prepareStatement(queryStar);
        }
        else if(type.equals("writers")){
            statement = connection.prepareStatement(queryWriter);
        }
        else if(type.equals("directors")){
            statement = connection.prepareStatement(queryDirector);
        }
        resultSet = statement.executeQuery();
        out.print("<div align = \"center\" style = \"background-color:blue\">");
        out.print("<select size = \"9\">");
        resultSet.next();
       
        out.print("<option value = \"" + 0 + "\"> + "
                + labels[0]  + resultSet.getString("0-1/NULL") + "%"
                + "</option>" );
        out.print("<option value = \"" + 1 + "\"> + "
                + labels[1]  + resultSet.getString("1-2") + "%"
                + "</option>" );
        out.print("<option value = \"" + 2 + "\"> + "
                + labels[2]  + resultSet.getString("2-3") + "%"
                + "</option>" );
        out.print("<option value = \"" + 3 + "\"> + "
                + labels[3]  + resultSet.getString("3-4") + "%"
                + "</option>" );
        out.print("<option value = \"" + 4 + "\"> + "
                + labels[4]  + resultSet.getString("4-5") + "%" 
                + "</option>" );
        out.print("<option value = \"" + 5 + "\"> + "
                + labels[5]  + resultSet.getString("5-6") + "%"
                + "</option>" );
        out.print("<option value = \"" + 6 + "\"> + "
                + labels[6]  + resultSet.getString("6-7") + "%"
                + "</option>" );
        out.print("<option value = \"" + 7 + "\"> + "
                + labels[7]  + resultSet.getString("7-8") + "%"
                + "</option>" );
        out.print("<option value = \"" + 8 + "\"> + "
                + labels[8]  + resultSet.getString("8-9") + "%"
                + "</option>" );
  
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
