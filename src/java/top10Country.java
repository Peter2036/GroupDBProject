
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

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

public class top10Country extends HttpServlet {
     @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("image/png");
       
        RequestDispatcher rd
                = request.getRequestDispatcher("/stats.jsp");
        OutputStream out = response.getOutputStream();
        
        JFreeChart chart = null;
        try {
            chart = getChart();
        } catch (SQLException ex) {
            Logger.getLogger(recordsChart.class.getName()).log(Level.SEVERE, null, ex);
        }

        ChartUtilities.writeChartAsPNG(out, chart, 500, 500);
        
        rd.include(request,response);
    }
    
     private JFreeChart getChart() throws SQLException{
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
        
        String query = "select * from( " 
                + "select country, count(country) as total from movies group by country order by total desc) " 
                + "where ROWNUM  <= 10 AND country != 'NULL'";
       
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        ResultSet resultSet = null;
        PreparedStatement statement = null; 
        
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        while(resultSet.next()){
             dataset.setValue(resultSet.getString("Country"), Integer.parseInt(resultSet.getString("total")));
        }
       
      
           
        JFreeChart chart = ChartFactory.createPieChart("Countries", dataset, true, true, true);
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
