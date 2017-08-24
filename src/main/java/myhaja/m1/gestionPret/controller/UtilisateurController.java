package myhaja.m1.gestionPret.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import myhaja.m1.gestionPret.bean.*;
import myhaja.m1.gestionPret.service.BaseService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurController {
	BaseService bs;
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST, headers = "Accept=application/json")
	 public Utilisateur inscription(@RequestBody Utilisateur ut) {
		try {
			bs = new BaseService();
			bs.setTable("Utilisateur");
			
			List<String> lChamp=Arrays.asList("login","password");
			List<String> lVal=Arrays.asList(ut.getLogin(),ut.getPassword());
			List<BaseModele> listOfCountries = bs.findBy(new Utilisateur(), lChamp, lVal);
			List<Utilisateur> listeFin=new ArrayList<Utilisateur>();
			if(!listOfCountries.isEmpty()) {
				for(BaseModele m:listOfCountries) {
					listeFin.add((Utilisateur)m);
				}
				return listeFin.get(0);
			}else {
				ut.setIdrole(1);
				ut.setIdTypeUtilisateur(1);
				//System.out.println(ut.toString());
				bs.insert(ut);
				return ut;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;  
		}
	 }
	@RequestMapping(value = "/user", method = RequestMethod.POST, headers = "Accept=application/json")
	 public Utilisateur connexion(@RequestBody Utilisateur ut) {
		try {
			bs = new BaseService();
			bs.setTable("Utilisateur");
			
			List<String> lChamp=Arrays.asList("login","password");
			List<String> lVal=Arrays.asList(ut.getLogin(),ut.getPassword());
			List<BaseModele> listOfCountries = bs.findBy(new Utilisateur(), lChamp, lVal);
			List<Utilisateur> listeFin=new ArrayList<Utilisateur>();
			if(!listOfCountries.isEmpty()) {
				for(BaseModele m:listOfCountries) {
					listeFin.add((Utilisateur)m);
				}
				return listeFin.get(0);
			}else return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;  
		}
	 }
	@RequestMapping(value = "/getAllusers", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Utilisateur> getCountries() {
		try {
			bs = new BaseService();
			bs.setTable("Utilisateur");
			List<BaseModele> listOfCountries = bs.findAll(new Utilisateur());
			List<Utilisateur> listeFin=new ArrayList<Utilisateur>();
			for(BaseModele m:listOfCountries) {
				listeFin.add((Utilisateur)m);
			}
			return listeFin;
		}catch (Exception e) {
			e.printStackTrace();
			return null;  
		}
		
	}
}
