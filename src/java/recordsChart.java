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

public class recordsChart extends HttpServlet {
    
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
        
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        ResultSet resultSet = null;
        PreparedStatement statement = null; 
        for(int i = 0; i < 7; i++){
            statement = connection.prepareStatement(query[i]);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                dataset.setValue(labels[i], Integer.parseInt(resultSet.getString("count(*)")));
            }
       
        }
           
        JFreeChart chart = ChartFactory.createPieChart("Records", dataset, true, true, true);
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
