package myhaja.m1.gestionPret.dao;

import java.sql.*;

public class DBConnection {
	//fonction qui return la connexion à la base de donnée
	private Connection connexion;
	public DBConnection() throws Exception{
		connexion=getConnection();
	}
	public Connection getConnection()throws Exception{
		/*if(connexion==null){
			Connection c = null;
			
		      try {
		         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		         c = DriverManager
		            .getConnection("jdbc:sqlserver://localhost:56755;databaseName=GestionPret;integratedSecurity=true");
		      
		         return c;
		      } catch (Exception e) {
		         throw new Exception("Erreur de connexion : "+e.getMessage());
		      }
		}else {
			if(connexion.isClosed()){
				Connection c = null;
			      try {
			    	  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				         c = DriverManager
				            .getConnection("jdbc:sqlserver://localhost:56755;databaseName=GestionPret;integratedSecurity=true");
			         return c;
			      } catch (Exception e) {
			         throw new Exception("Erreur de connexion : "+e.getMessage());
			      }
			}
			else return connexion;
		}*/
		if(connexion==null){
			Connection c = null;
			
		      try {
		         Class.forName("org.postgresql.Driver");
		         c = DriverManager
		            .getConnection("jdbc:postgresql://localhost:5432/GestionPret",
		            "postgres", "123456");
		         return c;
		      } catch (Exception e) {
		         throw new Exception("Erreur de connexion : "+e.getMessage());
		      }
		}else {
			if(connexion.isClosed()){
				Connection c = null;
			      try {
			         Class.forName("org.postgresql.Driver");
			         c = DriverManager
			            .getConnection("jdbc:postgresql://localhost:5432/GestionPret",
			            "postgres", "123456");
			         return c;
			      } catch (Exception e) {
			         throw new Exception("Erreur de connexion : "+e.getMessage());
			      }
			}
			else return connexion;
		}
		 
	}
	public Connection getConnexion() throws Exception {
		if(connexion==null){
			return getConnection();
		}else{
			if(connexion.isClosed()){
				return getConnection();
			}
			else return connexion;
		}
	}
	public void setConnexion(Connection connexion) {
		this.connexion = connexion;
	}
}