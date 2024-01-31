package com.ar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ar.util.Dependency;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class DBSection {
    
    private static DBSection handler = null;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mavendb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "arrahmankm";
    private static Connection conn=null;
    private static Statement stmt = null;

    public static DBSection getInstance(){
        if(handler == null){
           handler =  new DBSection();
        }
        return handler;
    }
    private DBSection() {
        driverRegistration();
        createConnection();
    }
    
    private void driverRegistration(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("driver registered");
        } catch (ClassNotFoundException ex) {
            System.err.println("JDBC Driver not Found");
        }
    }
    
    private void createConnection(){
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Connection success");
        } catch (SQLException ex) {
             System.err.println("Connection not successfull");
        }
    }
    
    public ResultSet allDependencies() throws SQLException  {
        try {
            String query = "select * from dependency";
            stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Error occured fetching data");
            return null;
        }finally{
//            stmt.close();
        }
    }
    
    private boolean primaryKeyNoViolation(String dependency){
        try {
            String sql = "select count(*) from dependency where dependency='"+dependency+"'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                int count = rs.getInt(1);
                System.out.println("Count   :"+count);
                return (count <= 0); //return true there is no dependendency in that name
            }    
        } catch (SQLException ex) {
            System.err.println("Primary key violation");
            return false;
        }
        return false;
    }
    
    public Boolean insertDependency(Dependency dependency , Node node) throws SQLException{
        boolean bool = primaryKeyNoViolation(dependency.getDependency());
//        System.out.println("bool pk val:"+bool);
        if(!bool){
                alertErrorSection(node, "Primary key violation", "Dependency name already exist",
                        "Change name of dependency\n Primary Key Violation :(");
                
                return false;
        }
        
        String sql = "insert into dependency(dependency,groupid,artifactid,version,description,scope,optional)"
                + " values(?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        
        try {
            pst.setString(1, dependency.getDependency());
            pst.setString(2, dependency.getGroupId());
            pst.setString(3, dependency.getArtifactId());
            pst.setString(4, dependency.getVersion());
            pst.setString(5, dependency.getDescription());
            pst.setString(6, dependency.getScope());
            pst.setString(7, dependency.getOptional());
            pst.execute();
            return true;
            
        } catch (SQLException ex) {
            System.err.println("error in insertion");
            return false;
        }
        finally{
            pst.close();
        }   
    }
    public Boolean updateDependency(Dependency dependency,String dependencyId) throws SQLException{
        String sql = "update dependency set dependency=?,groupid=?,artifactid=?,version=?,description=?,scope=?,optional=?"
                + "  where dependency=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        int result ;
        try {
            pst.setString(1, dependency.getDependency());
            pst.setString(2, dependency.getGroupId());
            pst.setString(3, dependency.getArtifactId());
            pst.setString(4, dependency.getVersion());
            pst.setString(5, dependency.getDescription());
            pst.setString(6, dependency.getScope());
            pst.setString(7, dependency.getOptional());
            pst.setString(8, dependencyId);
            result = pst.executeUpdate();
            return (result > 0);
        } catch (SQLException ex) {
            System.err.println("");
            return false;
        }
        finally{
            pst.close();
        }   
    }
    
    public Boolean deleteDependency(String dependency) throws SQLException{    
        String sql = "delete from dependency where dependency=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        int result ;
        try {
            pst.setString(1,dependency);
            result = pst.executeUpdate();
            return (result > 0);
        } catch (SQLException ex) {
            System.err.println("");
            return false;
        }
        finally{
            pst.close();
        }   
    }
    
    private void alertErrorSection(Node node,String title,String header,String content){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.initOwner((Stage)node.getScene().getWindow()); //making alert on top
                alert.showAndWait();
    }
}

