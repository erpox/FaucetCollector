/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.gttools.faucets.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import main.java.prubea.StreamTest;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DBConnection {

    private static Connection connection;

    public static void createConnection() {
        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver");

            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/Contiply", "app", "app");

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void FillTable(JTable table, String Query) {
        try {
            createConnection();
            try (Statement stat = connection.createStatement()) {
                ResultSet rs = stat.executeQuery("SELECT * FROM APP.UNTITLED");

                //To remove previously added rows
                while (table.getRowCount() > 0) {
                    ((DefaultTableModel) table.getModel()).removeRow(0);
                }
                int columns = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[columns];
                    for (int i = 1; i <= columns; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
                }

                rs.close();
            }
            connection.close();
        } catch (SQLException e) {
        }
    }

    public static void updateQuery(String query) {
        try {
            createConnection();
            Statement stat = connection.createStatement();
            stat.executeUpdate(query);
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void recursosTableQuery(Elements list, int primeKey) {
        String sql = "INSERT INTO APP.RECURSOS VALUES(?,?,?,?,?,?,?)";

        LocalDate sdA = LocalDate.parse(list.get(1).text(), DateTimeFormatter.ofPattern("d/M/yyyy"));
        LocalDate sdA2 = LocalDate.parse(list.get(3).text(), DateTimeFormatter.ofPattern("d/M/yyyy"));

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, primeKey);
            //Nro_ingreso
            pstmt.setString(2, list.get(0).text());
            //Fec_Ing
            pstmt.setDate(3, java.sql.Date.valueOf(sdA));
            //Ubicacion
            pstmt.setString(4, list.get(2).text());
            //Fec_Ub
            pstmt.setDate(5, java.sql.Date.valueOf(sdA2));
            //Corte
            pstmt.setString(6, list.get(4).text());
            //Caratulado
            pstmt.setString(7, list.get(5).text());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void historyTableQuery(Elements list, int primeKey) {
        String sql = "INSERT INTO APP.HISTORIA VALUES(?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, primeKey);
            pstmt.setString(2, list.get(0).text());
            pstmt.setString(3, list.get(1).text());
            String ke = list.get(2).toString();
            String link = StringUtils.substringBetween(ke, "(", ")").replace("amp;", "").replace("'", "");            
          //  pstmt.setBlob(4, StreamTest.getPDF(link));            
            pstmt.setString(5, list.get(2).text());
            pstmt.setString(6, list.get(4).text());
            pstmt.setString(7, list.get(5).text());
            pstmt.setString(8, list.get(6).text());
            pstmt.setString(9, list.get(7).text());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void litigantesTableQuery(Elements list, int primeKey) {
        String sql = "INSERT INTO APP.LITIGANTE VALUES(?,?,?,?,?)";   
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, list.get(0).text());
            pstmt.setString(2, list.get(1).text());
            pstmt.setString(3, list.get(2).text());
            pstmt.setString(4, list.get(3).text());
            pstmt.setInt(5, primeKey);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
