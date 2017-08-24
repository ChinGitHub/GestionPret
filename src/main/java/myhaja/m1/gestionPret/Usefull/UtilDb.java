package myhaja.m1.gestionPret.Usefull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import myhaja.m1.gestionPret.dao.DBConnection;
import myhaja.m1.gestionPret.bean.BaseModele;

public class UtilDb {
	public static void makeUpdate(BaseModele b,String table,Connection c)throws Exception{
		try{
			List<GetSet> attributList=Utilitaire.getPropriety(b);
			//System.out.println("taille attributLlist : "+attributList.size());
			for(GetSet gs:attributList){
				//System.out.println(gs.getName()+" getters : "+gs.getGet().getName()+" setters : "+gs.getSet().getName());
			}
			List<String> champDbList=UtilDb.getTableFields(c,table);
			//System.out.println("taille champDbList : "+champDbList.size());
			/*for(String gs:champDbList){
				System.out.println(gs);
			}*/
			String resultat ="UPDATE "+table+" SET ";
			for(int i=0;i<champDbList.size();i++){
				for(int j=0;j<attributList.size();j++){
					//si c'est le dernier champ, on ne met pas de virgule
					if(i==champDbList.size()-1){
						if((attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0)&&(champDbList.get(i).compareToIgnoreCase("id")!=0)){
							resultat=resultat+" "+champDbList.get(i)+"=?";
							//System.out.println(attributList.get(j).getName()+" : "+attributList.get(j).getGet().invoke(b).toString());
						}
					}else{
						if((attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0)&&(champDbList.get(i).compareToIgnoreCase("id")!=0)){
							//System.out.println(champDbList.get(i)+" : " +attributList.get(j).getName());
							resultat=resultat+" "+champDbList.get(i)+"=?,";
							//System.out.println(resultat);
						}
					}
				}
			}
			resultat=resultat+" where id="+b.getId();
			resultat=resultat+";";
			System.out.println(resultat);
			PreparedStatement ps=c.prepareStatement(resultat);
			//System.out.println("requete preparée...");
			champDbList=UtilDb.getTableFields2(c,table);
			//System.out.println("ChampDB taille : " + champDbList.size());
			attributList=Utilitaire.getPropriety(b);
			//System.out.println("attributList taille : " + attributList.size());
			int indicePs=1;
			for(int i=0;i<champDbList.size();i++){
				String[] propChamp=champDbList.get(i).split("/"); //contient le nom et le type du champ
				//System.out.println("nom champ : "+propChamp[0]);
				//System.out.println("type champ : "+propChamp[1]);
				for(int j=0;j<attributList.size();j++){
					
					if((attributList.get(j).getName().compareToIgnoreCase(propChamp[0])==0)&&(attributList.get(j).getName().compareToIgnoreCase("id")!=0)){
						//System.out.println(attributList.get(j).getType().getName());
						if(attributList.get(j).getType().getName().compareToIgnoreCase("java.sql.date")==0){
							java.sql.Date temp=(java.sql.Date)attributList.get(j).getGet().invoke(b);
							ps.setDate(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("java.util.date")==0){
							java.util.Date d=(java.util.Date)attributList.get(j).getGet().invoke(b);
							ps.setDate(indicePs, new java.sql.Date(d.getTime()));
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("int")==0){
							Integer temp=(Integer)attributList.get(j).getGet().invoke(b);
							ps.setInt(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("double")==0){
							Double temp=(Double)attributList.get(j).getGet().invoke(b);
							ps.setDouble(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("float")==0){
							Float temp=(Float)attributList.get(j).getGet().invoke(b);
							ps.setFloat(indicePs, temp);
							indicePs++;
						}else{
							if(propChamp[1].compareTo("4")==0){
								String temp=(String)attributList.get(j).getGet().invoke(b);
								ps.setInt(indicePs, Integer.parseInt(temp));
								indicePs++;
							}else if(propChamp[1].compareTo("8")==0){
								Float temp=(Float)attributList.get(j).getGet().invoke(b);
								ps.setFloat(indicePs, temp);
								indicePs++;
							}else if(propChamp[1].compareTo("-7")==0){
								boolean temp=(boolean)attributList.get(j).getGet().invoke(b);
								ps.setBoolean(indicePs, temp);
								indicePs++;
							}else{
								String temp=(String)attributList.get(j).getGet().invoke(b);
								ps.setString(indicePs, temp);
								indicePs++;	
							}
						}
					
					}
				}
			}
			ps.executeUpdate();
			System.out.println("requete executée...");

		}catch(Exception e){
			throw new Exception("Erreur makeupdate : "+e.getMessage());
		}
	}
	public static void makeDelete(BaseModele b,String table,Connection c)throws Exception{
		try{
			
			String resultat ="DELETE FROM "+table+" WHERE id=?;";
			System.out.println(resultat);
			PreparedStatement ps=c.prepareStatement(resultat);
			ps.setInt(1,Integer.parseInt(b.getId()));
			ps.executeUpdate();
			System.out.println("requete executée...");

		}catch(Exception e){
			throw new Exception("Erreur makedelete : "+e.getMessage());
		}
	}
	public static int getId(String table,Connection c) throws Exception{
		String sql="SELECT nextval('seq"+table+"') as res";
		System.out.println(sql);
		Statement stmnt=c.createStatement();
		ResultSet rs=stmnt.executeQuery(sql);
		int resultat=0;
		while(rs.next()){
			resultat=rs.getInt("res");
		}
		return resultat;
	}
	public static int getId(BaseModele b,Connection c) throws Exception{
		String sql="SELECT nextval('seq"+b.getClass().getSimpleName().toLowerCase()+"') as res";
		System.out.println(sql);
		Statement stmnt=c.createStatement();
		ResultSet rs=stmnt.executeQuery(sql);
		int resultat=0;
		while(rs.next()){
			resultat=rs.getInt("res");
		}
		return resultat;
	}
	/*public static int getId(BaseModele b) throws Exception{
		Connection c=new DBConnection().getConnection();
		String sql="SELECT nextval('seq"+b.getClass().getSimpleName().toLowerCase()+"') as res";
		System.out.println(sql);
		Statement stmnt=c.createStatement();
		ResultSet rs=stmnt.executeQuery(sql);
		int resultat=0;
		while(rs.next()){
			resultat=rs.getInt("res");
		}
		return resultat;
	}*/
	//prépare l'insertion
	public static void makeInsert(BaseModele b, String table, Connection c, List<GetSet> attributList,
			List<String> champDbList) throws Exception {
		try{
			
			String resultat ="INSERT INTO "+table+" VALUES (";
			for(int i=0;i<champDbList.size();i++){
				//System.out.println(champDbList.get(i));
				for(int j=0;j<attributList.size();j++){
					//System.out.println(champDbList.get(i)+" : "+attributList.get(j).getName());
					//si c'est le dernier champ, on ne met pas de virgule
					if(i==champDbList.size()-1){
						if(attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0){
							resultat=resultat+"?";
							//System.out.println(attributList.get(j).getName()+" : "+attributList.get(j).getGet().invoke(b).toString());
						}
					}else{
						if(attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0){
							//System.out.println(champDbList.get(i)+" : " +attributList.get(j).getName());
							resultat=resultat+"? ,";
							//System.out.println(resultat);
						}
					}
				}
			}
			resultat=resultat+")";
			System.out.println(resultat);
			PreparedStatement ps=c.prepareStatement(resultat);
			//System.out.println("requete preparée...");
			champDbList=UtilDb.getTableFields2(c,table); //??????????????????????????????????????????
			//System.out.println("ChampDB taille : " + champDbList.size());
			attributList=Utilitaire.getPropriety(b);//??????????????????????????????????????????????????
			//System.out.println("attributList taille : " + attributList.size());
			int indicePs=1;
			for(int i=0;i<champDbList.size();i++){
				String[] propChamp=champDbList.get(i).split("/"); //contient le nom et le type du champ
				//System.out.println("nom champ : "+propChamp[0]);
				//System.out.println("type champ : "+propChamp[1]);
				for(int j=0;j<attributList.size();j++){
					if(attributList.get(j).getName().compareToIgnoreCase(propChamp[0])==0){

						/*System.out.println(propChamp[0]);
						System.out.println(attributList.get(j).getName());
						System.out.println("type champ attr : "+attributList.get(j).getType().getName());
						System.out.println("type champ base : "+propChamp[1]);*/
						if(attributList.get(j).getType().getName().compareToIgnoreCase("java.sql.date")==0){
							java.sql.Date temp=(java.sql.Date)attributList.get(j).getGet().invoke(b);
							ps.setDate(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("java.util.date")==0){
							java.util.Date d=(java.util.Date)attributList.get(j).getGet().invoke(b);
							ps.setDate(indicePs, new java.sql.Date(d.getTime()));
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("java.lang.double")==0){
							java.lang.Double d=(java.lang.Double)attributList.get(j).getGet().invoke(b);
							ps.setDouble(indicePs, d);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("java.lang.float")==0){
							java.lang.Float d=(java.lang.Float)attributList.get(j).getGet().invoke(b);
							ps.setFloat(indicePs, d);
							indicePs++;
						}
						else if(attributList.get(j).getType().getName().compareToIgnoreCase("int")==0){
							Integer temp=(Integer)attributList.get(j).getGet().invoke(b);
							ps.setInt(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("boolean")==0){
							boolean temp=(Boolean)attributList.get(j).getGet().invoke(b);
							ps.setBoolean(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("double")==0){
							java.lang.Double temp=(java.lang.Double)attributList.get(j).getGet().invoke(b);
							ps.setDouble(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("float")==0){
							java.lang.Float temp=(java.lang.Float)attributList.get(j).getGet().invoke(b);
							ps.setFloat(indicePs, temp);
							indicePs++;
						}else{
							if(propChamp[1].compareTo("4")==0){
								String temp=(String)attributList.get(j).getGet().invoke(b);
								ps.setInt(indicePs, Integer.parseInt(temp));
								indicePs++;
							}else if(propChamp[1].compareTo("7")==0){
								Double temp=(Double)attributList.get(j).getGet().invoke(b);
								ps.setDouble(indicePs, temp);
								indicePs++;
							}else if(propChamp[1].compareTo("8")==0){
								Float temp=(Float)attributList.get(j).getGet().invoke(b);
								ps.setFloat(indicePs, temp);
								indicePs++;
							}
							else{
								String temp=(String)attributList.get(j).getGet().invoke(b);
								ps.setString(indicePs, temp);
								indicePs++;	
							}
						}
					
					}
				}
			}
			ps.executeUpdate();
			//System.out.println("requete executée...");

		}catch(Exception e){
			throw new Exception("Erreur makeinsert : "+e.getMessage());
		}
		
	}
	public static void makeInsert(BaseModele b,String table,Connection c)throws Exception{
		try{
			List<GetSet> attributList=Utilitaire.getPropriety(b);
			//System.out.println("taille attributLlist : "+attributList.size());
			/*for(GetSet gs:attributList){
				System.out.println(gs.getName()+" getters : "+gs.getGet().getName()+" setters : "+gs.getSet().getName());
			}*/
			List<String> champDbList=UtilDb.getTableFields(c,table);
			//System.out.println("taille champDbList : "+champDbList.size());
			/*for(String gs:champDbList){
				System.out.println(gs);
			}*/
			String resultat ="INSERT INTO "+table+" VALUES (";
			for(int i=0;i<champDbList.size();i++){
				//System.out.println(champDbList.get(i));
				for(int j=0;j<attributList.size();j++){
					//System.out.println(champDbList.get(i)+" : "+attributList.get(j).getName());
					//si c'est le dernier champ, on ne met pas de virgule
					if(i==champDbList.size()-1){
						if(attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0){
							resultat=resultat+"?";
							//System.out.println(attributList.get(j).getName()+" : "+attributList.get(j).getGet().invoke(b).toString());
						}
					}else{
						if(attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0){
							//System.out.println(champDbList.get(i)+" : " +attributList.get(j).getName());
							resultat=resultat+"? ,";
							//System.out.println(resultat);
						}
					}
				}
			}
			resultat=resultat+")";
			//System.out.println(resultat);
			PreparedStatement ps=c.prepareStatement(resultat);
			//System.out.println("requete preparée...");
			champDbList=UtilDb.getTableFields2(c,table);
			//System.out.println("ChampDB taille : " + champDbList.size());
			attributList=Utilitaire.getPropriety(b);
			//System.out.println("attributList taille : " + attributList.size());
			int indicePs=1;
			for(int i=0;i<champDbList.size();i++){
				String[] propChamp=champDbList.get(i).split("/"); //contient le nom et le type du champ
				//System.out.println("nom champ : "+propChamp[0]);
				//System.out.println("type champ : "+propChamp[1]);
				for(int j=0;j<attributList.size();j++){
					if(attributList.get(j).getName().compareToIgnoreCase(propChamp[0])==0){

						/*System.out.println(propChamp[0]);
						System.out.println(attributList.get(j).getName());
						System.out.println("type champ attr : "+attributList.get(j).getType().getName());
						System.out.println("type champ base : "+propChamp[1]);*/
						if(attributList.get(j).getType().getName().compareToIgnoreCase("java.sql.date")==0){
							java.sql.Date temp=(java.sql.Date)attributList.get(j).getGet().invoke(b);
							ps.setDate(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("java.util.date")==0){
							java.util.Date d=(java.util.Date)attributList.get(j).getGet().invoke(b);
							ps.setDate(indicePs, new java.sql.Date(d.getTime()));
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("java.lang.double")==0){
							java.lang.Double d=(java.lang.Double)attributList.get(j).getGet().invoke(b);
							ps.setDouble(indicePs, d);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("java.lang.float")==0){
							java.lang.Float d=(java.lang.Float)attributList.get(j).getGet().invoke(b);
							ps.setFloat(indicePs, d);
							indicePs++;
						}
						else if(attributList.get(j).getType().getName().compareToIgnoreCase("int")==0){
							Integer temp=(Integer)attributList.get(j).getGet().invoke(b);
							ps.setInt(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("boolean")==0){
							if(propChamp[1].compareTo("-7")==0) {
								Boolean temp=(Boolean)attributList.get(j).getGet().invoke(b);
								ps.setBoolean(indicePs, temp.booleanValue());
							}else {
								boolean temp=(Boolean)attributList.get(j).getGet().invoke(b);
								ps.setBoolean(indicePs, temp);
							}
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("double")==0){
							java.lang.Double temp=(java.lang.Double)attributList.get(j).getGet().invoke(b);
							ps.setDouble(indicePs, temp);
							indicePs++;
						}else if(attributList.get(j).getType().getName().compareToIgnoreCase("float")==0){
							java.lang.Float temp=(java.lang.Float)attributList.get(j).getGet().invoke(b);
							ps.setFloat(indicePs, temp);
							indicePs++;
						}else{
							if(propChamp[1].compareTo("4")==0){
								String temp=(String)attributList.get(j).getGet().invoke(b);
								ps.setInt(indicePs, Integer.parseInt(temp));
								indicePs++;
							}else if(propChamp[1].compareTo("7")==0){
								Double temp=(Double)attributList.get(j).getGet().invoke(b);
								ps.setDouble(indicePs, temp);
								indicePs++;
							}else if(propChamp[1].compareTo("8")==0){
								Float temp=(Float)attributList.get(j).getGet().invoke(b);
								ps.setFloat(indicePs, temp);
								indicePs++;
							}
							else{
								String temp=(String)attributList.get(j).getGet().invoke(b);
								ps.setString(indicePs, temp);
								indicePs++;	
							}
						}
					
					}
				}
			}
			ps.executeUpdate();
			//System.out.println("requete executée...");

		}catch(Exception e){
			throw new Exception("Erreur makeinsert : "+e.getMessage());
		}
	}
	/*public static String makeInsert(BaseModele b,String table,Connection c)throws Exception{
		try{
			List<GetSet> attributList=Utilitaire.getPropriety(b);
			List<String> champDbList=UtilDb.getTableFields(table);
			String resultat ="INSERT INTO "+table+" VALUES (";
			for(int i=0;i<champDbList.size();i++){
				for(int j=0;j<attributList.size();j++){
					//si c'est le dernier champ, on ne met pas de virgule
					if(i==champDbList.size()-1){
						if(attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0){
							if(attributList.get(j).getType().getName().compareToIgnoreCase("java.sql.date")==0){
								java.sql.Date temp=(java.sql.Date)attributList.get(j).getGet().invoke(b);
								resultat=resultat+"'"+temp+"'";
								System.out.println(attributList.get(j).getName()+" : "+temp);
							}else{
								resultat=resultat+"'" + attributList.get(j).getGet().invoke(b).toString()+"'";
								System.out.println(attributList.get(j).getName()+" : "+attributList.get(j).getGet().invoke(b).toString());
							}
						}
					}else{
						if(attributList.get(j).getName().compareToIgnoreCase(champDbList.get(i))==0){
							if(attributList.get(j).getType().getName().compareToIgnoreCase("java.sql.date")==0){
								java.sql.Date temp=(java.sql.Date)attributList.get(j).getGet().invoke(b);
								resultat=resultat+"'"+temp+"' ,";
								System.out.println(attributList.get(j).getName()+" : "+temp);
							}else{
								resultat=resultat+"'" + attributList.get(j).getGet().invoke(b).toString()+"' ,";
								System.out.println(attributList.get(j).getName()+" : "+attributList.get(j).getGet().invoke(b).toString());
							}
						}
					}
				}
			}
			resultat=resultat+")";
			return resultat;

		}catch(Exception e){
			throw new Exception("Erreur makeinsert : "+e.getMessage());
		}
	}*/
	//récupère les champs d'une table depuis la base
	public static List<String> getTableFields(Connection c,String nomTable)throws Exception{
		try{
			System.out.println("entree getField : ");
			Statement stmnt=c.createStatement();
			String sql="Select * from "+nomTable;
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			//récupère les metadata du resultset, c.a.d les informations concernant le resultat de la requete
			ResultSetMetaData resultmeta=rs.getMetaData();
			for(int i=1;i<=resultmeta.getColumnCount();i++){
				//System.out.println(resultmeta.getColumnName(i));
				listeChamp.add(resultmeta.getColumnName(i));
			}
			return listeChamp;
		}catch(Exception e){
			throw new Exception("Erreur Utildb getTableFields: "+e.getMessage());
		}
	}
	public static List<String> getTableFields(String nomTable,Connection con)throws Exception{
		try{
			
			Statement stmnt=con.createStatement();
			String sql="Select * from "+nomTable;
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			//récupère les metadata du resultset, c.a.d les informations concernant le resultat de la requete
			ResultSetMetaData resultmeta=rs.getMetaData();
			for(int i=1;i<=resultmeta.getColumnCount();i++){
				listeChamp.add(resultmeta.getColumnName(i));
			}
			return listeChamp;
		}catch(Exception e){
			throw new Exception("Erreur Utildb getTableFields: "+e.getMessage());
		}
	}
	public static List<String> getTableFields2(Connection c,String nomTable)throws Exception{
		try{
			
			Statement stmnt=c.createStatement();
			String sql="Select * from "+nomTable;
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			//récupère les metadata du resultset, c.a.d les informations concernant le resultat de la requete
			ResultSetMetaData resultmeta=rs.getMetaData();
			for(int i=1;i<=resultmeta.getColumnCount();i++){
				listeChamp.add(resultmeta.getColumnName(i)+"/"+resultmeta.getColumnType(i));
			}
			return listeChamp;
		}catch(Exception e){
			throw new Exception("Erreur Utildb getTableFields: "+e.getMessage());
		}
	}
	public static List<String> getTableFields2(String nomTable)throws Exception{
		try{
			DBConnection dbc=new DBConnection();
			Connection con=dbc.getConnection();
			Statement stmnt=con.createStatement();
			String sql="Select * from "+nomTable;
			ResultSet rs=stmnt.executeQuery(sql);
			List<String> listeChamp=new ArrayList<String>();
			//récupère les metadata du resultset, c.a.d les informations concernant le resultat de la requete
			ResultSetMetaData resultmeta=rs.getMetaData();
			for(int i=1;i<=resultmeta.getColumnCount();i++){
				listeChamp.add(resultmeta.getColumnName(i)+"/"+resultmeta.getColumnType(i));
			}
			return listeChamp;
		}catch(Exception e){
			throw new Exception("Erreur Utildb getTableFields: "+e.getMessage());
		}
	}
	
}
