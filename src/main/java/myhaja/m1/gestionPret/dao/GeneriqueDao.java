package myhaja.m1.gestionPret.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import myhaja.m1.gestionPret.bean.*;
import myhaja.m1.gestionPret.service.*;
import myhaja.m1.gestionPret.Usefull.*;
public class GeneriqueDao {
	public GeneriqueDao() throws Exception{
		connection=new DBConnection();
	}
	private  DBConnection connection;

	public DBConnection getConnection() {
		return connection;
	}

	public void setConnection(DBConnection c) {
		this.connection = c;
	}
	public int delete(BaseModele b,String table,Connection c) throws Exception{
		try{
			UtilDb.makeDelete(b, table,c);
			return 1;
		}catch(Exception e){
			throw new Exception("Erreur GeneriqueDao delete : "+e.getMessage());
		}
	}
	public int update(BaseModele b,String table,Connection c)throws Exception{
		System.out.println(b.getClass());
		try{
			UtilDb.makeUpdate(b, table,c);
			return 1;
		}catch(Exception e){
			throw new Exception("Erreur GeneriqueDao update : "+e.getMessage());
		}
	}
	public int insert(BaseModele b,String table,Connection c)throws Exception{
		System.out.println(b.getClass());
		try{
			UtilDb.makeInsert(b, table,c);
			return 1;
		}catch(Exception e){
			throw new Exception("Erreur GeneriqueDao insert : "+e.getMessage());
		}
	}
public List<BaseModele> findById(Connection c,BaseModele b, String table,int id)throws Exception{
		
		String alias="alias";
		try{
			Statement stmnt=c.createStatement();
			//création de la requete sql
			String sql="select * from ( select e.*  from " + table + " e where id="+id;
			sql+=") as "+alias;
			System.out.println(sql);
			//éxécution de la requete
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			listeChamp=Utilitaire.getListeAttribut(b);
			List<String> listeChampTable=new ArrayList<String>();
			listeChampTable=UtilDb.getTableFields(table,c);
			List<BaseModele> liste=new ArrayList<BaseModele>();
			while(rs.next()){
				//instancier l'objet reflété pour qu'il accueille les données
				BaseModele nouvelleObjet=b.getClass().newInstance();
				//System.out.println("nouvelle objet : "+nouvelleObjet);
				//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
				Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
				liste.add(nouvelleObjet);
			}
			return liste;
		}catch(Exception e){
			throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
		}
	}
	public List<BaseModele> findById(BaseModele b, String table,int id)throws Exception{
		Connection c=this.connection.getConnection();
		String alias="alias";
		try{
			Statement stmnt=c.createStatement();
			//création de la requete sql
			String sql="select * from ( select e.*  from " + table + " e where id="+id;
			sql+=") as "+alias;
			System.out.println(sql);
			//éxécution de la requete
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			listeChamp=Utilitaire.getListeAttribut(b);
			List<String> listeChampTable=new ArrayList<String>();
			listeChampTable=UtilDb.getTableFields(table,c);
			List<BaseModele> liste=new ArrayList<BaseModele>();
			while(rs.next()){
				//instancier l'objet reflété pour qu'il accueille les données
				BaseModele nouvelleObjet=b.getClass().newInstance();
				//System.out.println("nouvelle objet : "+nouvelleObjet);
				//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
				Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
				liste.add(nouvelleObjet);
			}
			return liste;
		}catch(Exception e){
			throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
		}finally{
			c.close();
		}
	}
	//récupère les lignes répondant à un critère
	public List<BaseModele> findByCreneau(Connection c,BaseModele b, String table,List<String> champList,List<String> valeurList,String finRequete)throws Exception{
		
		String alias="alias";
		try{
			Statement stmnt=c.createStatement();
			//création de la requete sql
			String sql="select * from ( select e.*  from " + table + " e where ";
			for(int i=0;i<champList.size();i++){
				if(i==0){
					if(champList.get(i).startsWith("id")){
						sql = sql +champList.get(i) + " = " + valeurList.get(i);
					}else if(champList.get(i).startsWith("date")){
						sql = sql + champList.get(i) + " ='" + valeurList.get(i) + "' ";
					}else sql = sql + "LOWER(CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
				}
				else{
					if(champList.get(i).startsWith("id")){
						sql = sql +" and "+champList.get(i) + " = " + valeurList.get(i);
					}else if(champList.get(i).startsWith("date")){
						sql = sql +" and "+ champList.get(i) + " ='" + valeurList.get(i) + "' ";
					}else sql = sql + " and LOWER (CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
					
				}
			}
			sql+=finRequete;
			sql+=") as "+alias;
			System.out.println(sql);
			//éxécution de la requete
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			listeChamp=Utilitaire.getListeAttribut(b);
			List<String> listeChampTable=new ArrayList<String>();
			listeChampTable=UtilDb.getTableFields(c,table);
			List<BaseModele> liste=new ArrayList<BaseModele>();
			while(rs.next()){
				//instancier l'objet reflété pour qu'il accueille les données
				BaseModele nouvelleObjet=b.getClass().newInstance();
				//System.out.println("nouvelle objet : "+nouvelleObjet);
				//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
				Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
				liste.add(nouvelleObjet);
			}
			return liste;
		}catch(Exception e){
			throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
		}
	}
		public List<BaseModele> findByCreneau(BaseModele b, String table,List<String> champList,List<String> valeurList,String finRequete)throws Exception{
			Connection c=this.connection.getConnection();
			String alias="alias";
			try{
				Statement stmnt=c.createStatement();
				//création de la requete sql
				String sql="select * from ( select e.*  from " + table + " e where ";
				for(int i=0;i<champList.size();i++){
					if(i==0){
						if(champList.get(i).startsWith("id")){
							sql = sql +champList.get(i) + " = " + valeurList.get(i);
						}else if(champList.get(i).startsWith("date")){
							sql = sql + champList.get(i) + " ='" + valeurList.get(i) + "' ";
						}else sql = sql + "LOWER(CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
					}
					else{
						if(champList.get(i).startsWith("id")){
							sql = sql +" and "+champList.get(i) + " = " + valeurList.get(i);
						}else if(champList.get(i).startsWith("date")){
							sql = sql +" and "+ champList.get(i) + " ='" + valeurList.get(i) + "' ";
						}else sql = sql + " and LOWER (CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
						
					}
				}
				sql+=finRequete;
				sql+=") as "+alias;
				System.out.println(sql);
				//éxécution de la requete
				ResultSet rs=stmnt.executeQuery(sql);
				List<String> listeChamp=new ArrayList<String>();
				listeChamp=Utilitaire.getListeAttribut(b);
				List<String> listeChampTable=new ArrayList<String>();
				listeChampTable=UtilDb.getTableFields(table,c);
				List<BaseModele> liste=new ArrayList<BaseModele>();
				while(rs.next()){
					//instancier l'objet reflété pour qu'il accueille les données
					BaseModele nouvelleObjet=b.getClass().newInstance();
					//System.out.println("nouvelle objet : "+nouvelleObjet);
					//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
					Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
					liste.add(nouvelleObjet);
				}
				return liste;
			}catch(Exception e){
				throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
			}finally{
				c.close();
			}
		}
	//récupère les lignes répondant à un critère
		public List<BaseModele> findByStrict(Connection c,BaseModele b, String table,List<String> champList,List<String> valeurList)throws Exception{
			String alias="alias";
			try{
				Statement stmnt=c.createStatement();
				//création de la requete sql
				String sql="select * from ( select e.*  from " + table + " e where ";
				for(int i=0;i<champList.size();i++){
					if(i==0){
						sql = sql + champList.get(i) + " ='" + valeurList.get(i) + "' ";		
					}
					else{
						sql = sql +" and "+ champList.get(i) + " ='" + valeurList.get(i) + "' ";
					}
				}
				sql+=") as "+alias;
				System.out.println(sql);
				//éxécution de la requete
				ResultSet rs=stmnt.executeQuery(sql);
				List<String> listeChamp=new ArrayList<String>();
				listeChamp=Utilitaire.getListeAttribut(b);
				List<String> listeChampTable=new ArrayList<String>();
				listeChampTable=UtilDb.getTableFields(c,table);
				List<BaseModele> liste=new ArrayList<BaseModele>();
				while(rs.next()){
					//instancier l'objet reflété pour qu'il accueille les données
					BaseModele nouvelleObjet=b.getClass().newInstance();
					//System.out.println("nouvelle objet : "+nouvelleObjet);
					//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
					Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
					liste.add(nouvelleObjet);
				}
				return liste;
			}catch(Exception e){
				throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
			}
		}
		public List<BaseModele> findBy(Connection c,BaseModele b, String table,String sql)throws Exception{
			try{
				Statement stmnt=c.createStatement();
				//création de la requete sql
				
				System.out.println(sql);
				//éxécution de la requete
				ResultSet rs=stmnt.executeQuery(sql);
				List<String> listeChamp=new ArrayList<String>();
				listeChamp=Utilitaire.getListeAttribut(b);
				List<String> listeChampTable=new ArrayList<String>();
				listeChampTable=UtilDb.getTableFields(c,table);
				List<BaseModele> liste=new ArrayList<BaseModele>();
				while(rs.next()){
					//instancier l'objet reflété pour qu'il accueille les données
					BaseModele nouvelleObjet=b.getClass().newInstance();
					//System.out.println("nouvelle objet : "+nouvelleObjet);
					//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
					Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
					liste.add(nouvelleObjet);
				}
				return liste;
			}catch(Exception e){
				throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
			}
		}
	public List<BaseModele> findBy(Connection c,BaseModele b, String table,List<String> champList,List<String> valeurList)throws Exception{
		String alias="alias";
		try{
			Statement stmnt=c.createStatement();
			//création de la requete sql
			String sql="select * from ( select e.*  from " + table + " e where ";
			for(int i=0;i<champList.size();i++){
				if(i==0){
					if(champList.get(i).startsWith("id")){
						sql = sql +champList.get(i) + " = " + valeurList.get(i);
					}else if(champList.get(i).startsWith("date")){
						sql = sql + champList.get(i) + " ='" + valeurList.get(i) + "' ";
					}else sql = sql + "LOWER(CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
				}
				else{
					if(champList.get(i).startsWith("id")){
						sql = sql +" and "+champList.get(i) + " = " + valeurList.get(i);
					}else if(champList.get(i).startsWith("date")){
						sql = sql +" and "+ champList.get(i) + " ='" + valeurList.get(i) + "' ";
					}else sql = sql + " and LOWER (CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
					
				}
			}
			sql+=") as "+alias;
			System.out.println(sql);
			//éxécution de la requete
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			listeChamp=Utilitaire.getListeAttribut(b);
			List<String> listeChampTable=new ArrayList<String>();
			listeChampTable=UtilDb.getTableFields(c,table);
			List<BaseModele> liste=new ArrayList<BaseModele>();
			while(rs.next()){
				//instancier l'objet reflété pour qu'il accueille les données
				BaseModele nouvelleObjet=b.getClass().newInstance();
				//System.out.println("nouvelle objet : "+nouvelleObjet);
				//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
				Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
				liste.add(nouvelleObjet);
			}
			return liste;
		}catch(Exception e){
			throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
		}
	}
	public List<BaseModele> findBy(BaseModele b, String table,List<String> champList,List<String> valeurList)throws Exception{
		Connection c=this.connection.getConnection();
		String alias="alias";
		try{
			Statement stmnt=c.createStatement();
			//création de la requete sql
			String sql="select * from ( select e.*  from " + table + " e where ";
			for(int i=0;i<champList.size();i++){
				if(i==0){
					if(champList.get(i).startsWith("id")){
						sql = sql +champList.get(i) + " = " + valeurList.get(i);
					}else if(champList.get(i).startsWith("date")){
						sql = sql + champList.get(i) + " ='" + valeurList.get(i) + "' ";
					}else sql = sql + "LOWER(CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
				}
				else{
					if(champList.get(i).startsWith("id")){
						sql = sql +" and "+champList.get(i) + " = " + valeurList.get(i);
					}else if(champList.get(i).startsWith("date")){
						sql = sql +" and "+ champList.get(i) + " ='" + valeurList.get(i) + "' ";
					}else sql = sql + " and LOWER (CAST ("+champList.get(i) + " AS TEXT)) LIKE LOWER('%" + valeurList.get(i) + "%') ";
					
				}
			}
			sql+=") as "+alias;
			System.out.println(sql);
			//éxécution de la requete
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			listeChamp=Utilitaire.getListeAttribut(b);
			List<String> listeChampTable=new ArrayList<String>();
			listeChampTable=UtilDb.getTableFields(table,c);
			List<BaseModele> liste=new ArrayList<BaseModele>();
			while(rs.next()){
				//instancier l'objet reflété pour qu'il accueille les données
				BaseModele nouvelleObjet=b.getClass().newInstance();
				//System.out.println("nouvelle objet : "+nouvelleObjet);
				//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
				Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
				liste.add(nouvelleObjet);
			}
			return liste;
		}catch(Exception e){
			throw new Exception("Erreur generiqueDao findBy : "+e.getMessage());
		}finally{
			c.close();
		}
	}
	//récupère toutes les lignes d'une table
		public List<BaseModele> findAll(Connection c,BaseModele b, String table)throws Exception{
			try{
				Statement stmnt=c.createStatement();
				String sql="Select * from "+table;
				ResultSet rs=stmnt.executeQuery(sql);
				System.out.println(sql);
				List<String> listeChamp=new ArrayList<String>();
				listeChamp=Utilitaire.getListeAttribut(b);
				//System.out.println("tafavoka getListeAttribut : size :" +listeChamp.size());
				List<String> listeChampTable=new ArrayList<String>();
				listeChampTable=UtilDb.getTableFields(table,c);
				//System.out.println("tafavoka getTableFields : size :" +listeChampTable.size());

				List<BaseModele> liste=new ArrayList<BaseModele>();
				while(rs.next()){
					//instancier l'objet reflété pour qu'il accueille les données
					BaseModele nouvelleObjet=b.getClass().newInstance();
					//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
					Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
					liste.add(nouvelleObjet);
				}
				return liste;
			}catch(Exception e){
				throw new Exception("Erreur generiqueDao findAll : "+e.getMessage());
			}
		}
	//récupère toutes les lignes d'une table
	public List<BaseModele> findAll(BaseModele b, String table)throws Exception{
		Connection c=this.connection.getConnection();
		try{
			Statement stmnt=c.createStatement();
			String sql="Select * from "+table;
			ResultSet rs=stmnt.executeQuery(sql);
			System.out.println(sql);
			List<String> listeChamp=new ArrayList<String>();
			listeChamp=Utilitaire.getListeAttribut(b);
			//System.out.println("tafavoka getListeAttribut : size :" +listeChamp.size());
			List<String> listeChampTable=new ArrayList<String>();
			listeChampTable=UtilDb.getTableFields(table,c);
			//System.out.println("tafavoka getTableFields : size :" +listeChampTable.size());

			List<BaseModele> liste=new ArrayList<BaseModele>();
			while(rs.next()){
				//instancier l'objet reflété pour qu'il accueille les données
				BaseModele nouvelleObjet=b.getClass().newInstance();
				//lit la ligne du tableau et assigne les valeurs recceuillies aux champs de la classe
				Utilitaire.lire(nouvelleObjet, listeChamp, listeChampTable, rs);
				liste.add(nouvelleObjet);
			}
			return liste;
		}catch(Exception e){
			throw new Exception("Erreur generiqueDao findAll : "+e.getMessage());
		}finally{
			c.close();
		}
	}
}