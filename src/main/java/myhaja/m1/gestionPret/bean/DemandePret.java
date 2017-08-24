package myhaja.m1.gestionPret.bean;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DemandePret extends BaseModele{
	private int idClient;
	private Date dateDemande;
	private Date dateValidation;
	private double montant;
	private float interet;
	private int duree;
	private boolean valide;
	
	
	public DemandePret(String dateDemande,String montant,String interet,String duree) throws Exception{
		try{
			setDateDemande(dateDemande);
			setMontant(montant);
			setInteret(interet);
			setDuree(duree);
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	public DemandePret(String client,String dateDemande,String montant,String interet,String duree) throws Exception{
		try{
			setIdClient(client);
			setDateDemande(dateDemande);
			setMontant(montant);
			setInteret(interet);
			setDuree(duree);
			setValide(false);
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	public DemandePret() {
		// TODO Auto-generated constructor stub
	}
	public Date getDateDemande() {
		return dateDemande;
	}
	public void setDateDemande(String dateDemande) throws Exception {
		try {
            /*Daty d=new Daty();
            this.dateDemande=d.validerDate(dateDemande);;*/
			String formatString = "dd/MM/yyyy";
			SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            Date d=format.parse(dateDemande);
			this.dateDemande=d;
        } catch (Exception e) {
        	
        	throw new Exception(e.getMessage());
        }
		
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(String montant) throws Exception {
		try{
			this.montant=Double.parseDouble(montant);
		}catch(Exception e){
			throw new Exception("Montant invalide. "+e.getMessage());
		}
	}
	public float getInteret() {
		return interet;
	}
	public void setInteret(String interet) throws Exception {
		try{
			this.interet=Float.parseFloat(interet);
		}catch(Exception e){
			throw new Exception("Interet invalide. "+e.getMessage());
		}
	}
	
	/**
	 * @return the duree
	 */
	public int getDuree() {
		return duree;
	}
	/**
	 * @param duree the duree to set
	 * @throws Exception 
	 */
	public void setDuree(String duree) throws Exception {
		try{
			this.duree = Integer.parseInt(duree);
		}catch(Exception e){
			throw new Exception("Durée invalide. "+e.getMessage());
		}
	}
	/**
	 * @return the idClient
	 */
	public int getIdClient() {
		return idClient;
	}
	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(String idClient) {
		this.idClient = Integer.parseInt(idClient);
	}
	/**
	 * @return the valide
	 */
	public boolean getValide() {
		return valide;
	}
	/**
	 * @param valide the valide to set
	 */
	public void setValide(boolean valide) {
		this.valide = valide;
	}
	/**
	 * @return the dateValidation
	 */
	public Date getDateValidation() {
		return dateValidation;
	}
	/**
	 * @param dateValidation the dateValidation to set
	 * @throws Exception 
	 */
	public void setDateValidation(String dateDemande) throws Exception {
		try {
            String formatString = "dd/MM/yyyy";
			SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            this.dateValidation=format.parse(dateDemande);
        } catch (Exception e) {
        	
        	throw new Exception(e.getMessage());
        }
	}
	
}
