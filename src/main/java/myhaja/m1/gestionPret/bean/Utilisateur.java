package myhaja.m1.gestionPret.bean;

public class Utilisateur extends BaseModele{
	private String login;
	private String password;
	private int idrole;
	private int idTypeUtilisateur;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getIdrole() {
		return idrole;
	}
	public void setIdrole(int idrole) {
		this.idrole = idrole;
	}
	public int getIdTypeUtilisateur() {
		return idTypeUtilisateur;
	}
	public void setIdTypeUtilisateur(int idTypeUtilisateur) {
		this.idTypeUtilisateur = idTypeUtilisateur;
	}
	@Override
	public String toString() {
		return "Utilisateur [login=" + login + ", password=" + password + ", idrole=" + idrole + ", idTypeUtilisateur="
				+ idTypeUtilisateur + "]";
	}
}