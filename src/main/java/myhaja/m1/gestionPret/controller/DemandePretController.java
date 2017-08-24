package myhaja.m1.gestionPret.controller;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import myhaja.m1.gestionPret.bean.BaseModele;
import myhaja.m1.gestionPret.bean.DemandePret;
import myhaja.m1.gestionPret.service.BaseService;

@RestController
public class DemandePretController {
	BaseService bs;
	@RequestMapping(value = "/demandePret", method = RequestMethod.POST, headers = "Accept=application/json")
	 public DemandePret connexion(@RequestBody DemandePret ut) {
		try {
			bs = new BaseService();
			bs.setTable("DemandePret");
			System.out.println("AJout demande pret");
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");	
			DemandePret d=new DemandePret(String.valueOf(ut.getIdClient()),df.format(ut.getDateDemande()), String.valueOf(ut.getMontant()),String.valueOf(ut.getInteret()), String.valueOf(ut.getDuree()));	
			String reportDate = df.format(d.getDateDemande());
			d.setDateValidation(reportDate);
			Connection c=bs.getDao().getConnection().getConnexion();
			bs.setTable("DemandePret");
			bs.insert2(d,c);
			return d;
		}catch (Exception e) {
			e.printStackTrace();
			return null;  
		}
	 }
}
