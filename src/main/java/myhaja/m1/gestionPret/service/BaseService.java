package myhaja.m1.gestionPret.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import myhaja.m1.gestionPret.dao.DBConnection;
import myhaja.m1.gestionPret.dao.GeneriqueDao;
import myhaja.m1.gestionPret.bean.BaseModele;
import myhaja.m1.gestionPret.Usefull.GetSet;
import myhaja.m1.gestionPret.Usefull.UtilDb;

public class BaseService {
	private GeneriqueDao dao;
	private String table;
	public BaseService() throws Exception{
		this.dao=new GeneriqueDao();
	}
	public void delete(BaseModele b,Connection c) throws Exception{
		try{
			UtilDb.makeDelete(b, getTable(),c);
			System.out.println("Sortie suppression pas encore de commit...");
		}catch(Exception e){
			throw new Exception("Erreur lors de la suppression : "+e.getMessage());
		
		}
	}
	public void delete(BaseModele b) throws Exception{
		Connection c=new DBConnection().getConnection();
		c.setAutoCommit(false);
		try{
			UtilDb.makeDelete(b, getTable(),c);
			System.out.println("Sortie suppression pas encore de commit...");
			c.commit();
		}catch(Exception e){
			c.rollback();
			throw new Exception("Erreur lors de la suppression : "+e.getMessage());
		}finally{
			c.close();
		}
	}
	public int insert3(BaseModele b, Connection c, List<GetSet> attributClientList, List<String> champClientDbList) throws Exception {
		if((b.getId()==null) || (Integer.parseInt(b.getId())==0)){
			b.setId(Integer.toString(UtilDb.getId(getTable(),c)));
		}
		try{
			//System.out.println("entre makeinsert");
			UtilDb.makeInsert(b, getTable(),c,attributClientList,champClientDbList);
			//System.out.println("Sortie insert pas encore de commit...");
			return Integer.parseInt(b.getId());
		}catch(Exception e){
			
			throw new Exception("Erreur lors de l'insertion : "+e.getMessage());
		}
	}
	public int insert2(BaseModele b,Connection c) throws Exception{
		if((b.getId()==null) || (Integer.parseInt(b.getId())==0)){
			b.setId(Integer.toString(UtilDb.getId(getTable(),c)));
		}
		try{
			//System.out.println("entre makeinsert");
			UtilDb.makeInsert(b, getTable(),c);
			//System.out.println("Sortie insert pas encore de commit...");
			return Integer.parseInt(b.getId());
		}catch(Exception e){
			
			throw new Exception("Erreur lors de l'insertion : "+e.getMessage());
		}
	}
	public int insert2(BaseModele b) throws Exception{
		
		
		Connection c=getDao().getConnection().getConnection();
		if((b.getId()==null) || (Integer.parseInt(b.getId())==0)){
			b.setId(Integer.toString(UtilDb.getId(b,c)));
		}
		c.setAutoCommit(false);
		try{
			UtilDb.makeInsert(b, getTable(),c);
			System.out.println("Sortie insert pas encore de commit...");
			c.commit();
			System.out.println("Sortie insert commit...");
			
			return Integer.parseInt(b.getId());
		}catch(Exception e){
			c.rollback();
			throw new Exception("Erreur lors de l'insertion : "+e.getMessage());
		}finally{
			c.close();
		}
	}
	public void update(BaseModele b,Connection c) throws Exception{
		
		try{
			UtilDb.makeUpdate(b, getTable(),c);
			System.out.println("Sortie update pas encore de commit...");


		}catch(Exception e){
			
			throw new Exception("Erreur lors de l'insertion : "+e.getMessage());
		}
	}
	public void update(BaseModele b) throws Exception{
		Connection c=new DBConnection().getConnection();
		c.setAutoCommit(false);
		try{
			UtilDb.makeUpdate(b, getTable(),c);
			System.out.println("Sortie update pas encore de commit...");
			c.commit();
			System.out.println("Sortie update commit...");

		}catch(Exception e){
			c.rollback();
			throw new Exception("Erreur lors de l'insertion : "+e.getMessage());
		}finally{
			if(c!=null)c.close();
		}
	}
	public void insert(BaseModele b,Connection c) throws Exception{
		b.setId(Integer.toString(UtilDb.getId(b,c)));
		
		try{
			UtilDb.makeInsert(b, getTable(),c);
			System.out.println("Sortie insert pas encore de commit...");
			c.commit();
			System.out.println("Sortie insert commit...");

		}catch(Exception e){
			c.rollback();
			throw new Exception("Erreur lors de l'insertion : "+e.getMessage());
		}
	}
	public void insert(BaseModele b) throws Exception{
		
		Connection c=new DBConnection().getConnection();
		b.setId(Integer.toString(UtilDb.getId(b,c)));
		c.setAutoCommit(false);
		try{
			UtilDb.makeInsert(b, getTable(),c);
			System.out.println("Sortie insert pas encore de commit...");
			c.commit();
			System.out.println("Sortie insert commit...");

		}catch(Exception e){
			c.rollback();
			throw new Exception("Erreur lors de l'insertion : "+e.getMessage());
		}finally{
			c.close();
		}
	}
	public List<BaseModele> findByStrict(Connection c,BaseModele b,List<String> lChamp,List<String> lVal) throws Exception{
		return getDao().findByStrict(c,b, getTable(), lChamp, lVal);

	}
	public List<BaseModele> findByCreneau(BaseModele b,List<String> lChamp,List<String> lVal,String finreq) throws Exception{
		return getDao().findByCreneau(b, getTable(), lChamp, lVal,finreq);
	}
	public List<BaseModele> findByCreneau(Connection c,BaseModele b,List<String> lChamp,List<String> lVal,String finreq) throws Exception{
		return getDao().findByCreneau(c,b, getTable(), lChamp, lVal,finreq);
	}
	
	public List<BaseModele> findById(Connection c,BaseModele b,int id) throws Exception{
		return getDao().findById(c,b, getTable(), id);
	}
	public List<BaseModele> findById(BaseModele b,int id) throws Exception{
		return getDao().findById(b, getTable(), id);
	}
	
	
	public List<BaseModele> findBy(BaseModele b,List<String> lChamp,List<String> lVal) throws Exception{
		return getDao().findBy(b, getTable(), lChamp, lVal);
	}
	public List<BaseModele> findBy(Connection c,BaseModele b,List<String> lChamp,List<String> lVal) throws Exception{
		return getDao().findBy(c,b, getTable(), lChamp, lVal);
	}
	public List<BaseModele> findBy(Connection c,BaseModele b,String sql) throws Exception{
		return getDao().findBy(c,b, getTable(), sql);
	}
	public List<BaseModele> findAll(Connection c,BaseModele b) throws Exception{
		return getDao().findAll(c,b, getTable());
	}
	
	public List<BaseModele> findAll(BaseModele b) throws Exception{
		return getDao().findAll(b, getTable());
	}

	public GeneriqueDao getDao() {
		return dao;
	}

	public void setDao(GeneriqueDao dao) {
		this.dao = dao;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}