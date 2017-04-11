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
        
        response.setContentType("image/png");
       
        OutputStream out = response.getOutputStream();
        String id = (String)request.getAttribute("id");
        String type = (String)request.getAttribute("Type");
        RequestDispatcher rd = null;
        JFreeChart chart = null;
          if(type.equals("stars")){
            rd = request.getRequestDispatcher("/star_search.jsp");
        }
        else if(type.equals("writers")){
           rd = request.getRequestDispatcher("/writer_search.jsp"); 
        }
        else if(type.equals("directors")){
            rd = request.getRequestDispatcher("/director_search.jsp");
        }   
        try {
            chart = getChart(type);
        } catch (SQLException ex) {
            Logger.getLogger(percentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
        ChartUtilities.writeChartAsPNG(out, chart, 500, 500);  
        rd.include(request,response);
    }
    
    private JFreeChart getChart(String type) throws SQLException{
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
            
        };
        
        String labels[] = new String[]{
            "0-1/NULL",
            "1-2",
            "2-3",
            "3-4",
            "4-5",
            "5-6",
            "6-7",
            "7-8",
            "8-9"
        };
        
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        ResultSet resultSet = null;
        PreparedStatement statement = null; 
        if(type.equals("stars")){
            statement = connection.prepareStatement(query[0]);
        }
        else if(type.equals("writers")){
            statement = connection.prepareStatement(query[1]);
        }
        else if(type.equals("directors")){
            statement = connection.prepareStatement(query[2]);
        }
        resultSet = statement.executeQuery();
        resultSet.next();

        dataset.setValue(labels[0], Integer.parseInt(resultSet.getString("0-1/NULL")));
        dataset.setValue(labels[1], Integer.parseInt(resultSet.getString("1-2")));
        dataset.setValue(labels[2], Integer.parseInt(resultSet.getString("2-3")));
        dataset.setValue(labels[3], Integer.parseInt(resultSet.getString("3-4")));
        dataset.setValue(labels[4], Integer.parseInt(resultSet.getString("4-5")));
        dataset.setValue(labels[5], Integer.parseInt(resultSet.getString("5-6")));
        dataset.setValue(labels[6], Integer.parseInt(resultSet.getString("6-7")));
        dataset.setValue(labels[7], Integer.parseInt(resultSet.getString("7-8")));
        dataset.setValue(labels[8], Integer.parseInt(resultSet.getString("8-9")));
        dataset.setValue(labels[9], Integer.parseInt(resultSet.getString("9-10")));
           
        JFreeChart chart = ChartFactory.createPieChart("Rating Percentage Distribution", dataset, true, true, true);
	chart.setBorderPaint(Color.BLUE);
	chart.setBorderStroke(new BasicStroke(5.0f));
	chart.setBorderVisible(true);
        PiePlot plot = (PiePlot) chart.getPlot();
        PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator( 
        "{0} = {2}", new DecimalFormat("0"), new DecimalFormat("0.00%")); 
        plot.setLabelGenerator(generator); 
        
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
        
	return chart;
        
    }
}
