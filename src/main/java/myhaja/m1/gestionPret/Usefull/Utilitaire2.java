package myhaja.m1.gestionPret.Usefull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import myhaja.m1.gestionPret.bean.*;

public class Utilitaire2 {
	public static String getNumberTexte(String forme,Double d){
		try {
			DecimalFormat myFormatter = new DecimalFormat(forme);
			String output = myFormatter.format(d);
			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d.toString();
	}
	public static String getNumberTexte(String forme,float d){
		try {
			DecimalFormat myFormatter = new DecimalFormat(forme);
			String output = myFormatter.format(d);
			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.valueOf(d);
	}
	public static String getDateTexte(java.util.Date d){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");			
		String reportDate = df.format(d);
		return reportDate;
	}
	public void getJourOuvrable(Date début,Date fin){}
	public static List<java.util.Date> getJourOuvrable() {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.MONTH, 11);
	    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    //SimpleDateFormat df = new SimpleDateFormat("EEE dd-MM-yyyy");
	    SimpleDateFormat format = new SimpleDateFormat("EEEE");
	    List<java.util.Date> listeJourOuvrable=new ArrayList<java.util.Date>();
	    for (int i = 0; i < maxDay; i++) {
	        cal.set(Calendar.DAY_OF_MONTH, i + 1);
	        String nomJour=format.format(cal.getTime());
	        if(/*(nomJour.compareToIgnoreCase("samedi")!=0) && */(nomJour.compareToIgnoreCase("dimanche")!=0)&& /*(nomJour.compareToIgnoreCase("saturday")!=0)&& */(nomJour.compareToIgnoreCase("sunday")!=0)){
	        	listeJourOuvrable.add(cal.getTime());
	        }
	        
	    }
	    return listeJourOuvrable;
	}
	//récupérer le nom de tous les attributs d'une classe
	public static List<String> getListeAttribut(BaseModele classe){
		List<String> listeAttributFinal=new ArrayList<String>();
		Field[] listeParentAttribut=classe.getClass().getSuperclass().getDeclaredFields();
		Field[] listeAttribut=classe.getClass().getDeclaredFields();
		for(Field attribut : listeParentAttribut){
			listeAttributFinal.add(attribut.getName());
		}
		for(Field attribut : listeAttribut){
			listeAttributFinal.add(attribut.getName());
		}
		System.out.println("liste attribut final length :"+listeAttributFinal.size());
		return listeAttributFinal;
	}
	//convertit un tableau d'objet en liste
	public static List<Object> arrayToList(Object[] lobj){
		List<Object> res=new ArrayList<Object>();
		for(Object obj:lobj){
			res.add(obj);
		}
		return res;
	}
	//récupérer tous les getter et setter d'une classe
	//1er élément getter
	//2e élément setter
	public static List<GetSet> getPropriety(BaseModele b)throws Exception{
		try{
			Field[] attributList =b.getClass().getDeclaredFields();
			Field[] attributParentList =b.getClass().getSuperclass().getDeclaredFields();
			List<Object> liste=Utilitaire2.arrayToList(attributList);
			for(Field parent:attributParentList){
				liste.add(parent);
			}
			List<GetSet> gettersSetters=new ArrayList<GetSet>();
			for(Object attribut: liste){
				Field att=(Field)attribut;
				gettersSetters.add(Utilitaire2.getPropriety(b, att.getName()));
			}
			return gettersSetters;
		}catch(Exception e){
			throw new Exception("Erreur getGettersSetters "+e.getMessage());
		}
		
	}
	//récupérer le getter et setter d'un attribut
	//1er élément getter
	//2e élément setter
	public static GetSet getPropriety(BaseModele b, String nomAttribut)throws Exception{
		try{
			GetSet gettersSetters=new GetSet();
			Field[] attributList =b.getClass().getDeclaredFields();
			Field[] attributParentList=b.getClass().getSuperclass().getDeclaredFields();
			System.out.println("Taille attribut : "+attributList.length);
			System.out.println("Taille attributParent : "+attributParentList.length);
			for(Field attribut: attributList){
				//verifie si le champ et le champ en parametre ont le meme nom
				if(attribut.getName().compareToIgnoreCase(nomAttribut)==0){
					Method[] methodList=b.getClass().getDeclaredMethods();
					for(Method method : methodList){
						//si le nom de la method contient le nom du champ

						if(method.getName().toLowerCase().indexOf(nomAttribut.toLowerCase())>0){
							//System.out.println(nomAttribut+" "+method.getName());
							if(method.getName().startsWith("get")){
								gettersSetters.setGet(method);
							}else if(method.getName().startsWith("set")){
								gettersSetters.setSet(method);
							}
						}
					}
				}
			}
			for(Field attribut: attributParentList){
				//verifie si le champ et le champ en parametre ont le meme nom
				if(attribut.getName().compareToIgnoreCase(nomAttribut)==0){
					Method[] methodList=b.getClass().getSuperclass().getDeclaredMethods();
					for(Method method : methodList){
						//si le nom de la method contient le nom du champ

						if(method.getName().toLowerCase().indexOf(nomAttribut.toLowerCase())>0){
							//System.out.println(nomAttribut+" "+method.getName());
							if(method.getName().startsWith("get")){
								gettersSetters.setGet(method);
							}else if(method.getName().startsWith("set")){
								gettersSetters.setSet(method);
							}

						}
					}
				}
			}

			//System.out.println(nomAttribut+" "+gettersSetters[0]);
			return gettersSetters;
		}catch(Exception e){
			throw new Exception("Erreur getGettersSetters "+e.getMessage());
		}
		
	}
	
	//lit la ligne obtenues par le resultSet et assigne chaque champ de la table au champ de la classe 
	public static void lire(BaseModele obj,List<String> listeChamp,List<String> listeChampTable,ResultSet rs)throws Exception{
		try{
			//parcours des attributs de la classe et des champs de la table
			for(String champ : listeChamp){
				for(String champTable : listeChampTable){
					if(champ.compareToIgnoreCase(champTable)==0){
						//vérifier le type du champ renvoyé par le resulset
						//récupérer la valeur de la base et le mettre dans l'attribut de la classe adéquat
					//	System.out.println(rs.getObject(champ).getClass().getSimpleName());
						//ystem.out.println(champ +" : "+ rs.getObject(champ).toString());
						//System.out.println(rs.getObject(champ).getClass().getSimpleName());
						if(rs.getObject(champ)!=null){
							System.out.println("champ : "+champ);
							System.out.println("valeur : "+rs.getObject(champ));
							GetSet gettersSetters=Utilitaire2.getPropriety(obj, champ);
							System.out.println(gettersSetters.getName());
							if(gettersSetters !=null){
								System.out.println("entree gettersetters non null pour le champ : " +gettersSetters.getName());
								System.out.println("getter: " +gettersSetters.getGet().getName());
								System.out.println("setter: " +gettersSetters.getSet().getName());
								if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("integer")==0){
									int valeur=rs.getInt(champ);
									if (valeur !=0)gettersSetters.getSet().invoke(obj, new Object[]{valeur});
								}else if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("string")==0){
									String valeur=rs.getString(champ);
									if(valeur !=null)gettersSetters.getSet().invoke(obj, new Object[]{valeur});
								}else if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("date")==0){
									Date valeur=rs.getDate(champ);
									if(valeur !=null)gettersSetters.getSet().invoke(obj, new Object[]{valeur});
								}else{
									String valeur=rs.getString(champ);
									gettersSetters.getSet().invoke(obj, new Object[]{valeur});
								}
							}
							
						}
						
					}
				}
			}
		}catch(Exception e){
			throw new Exception("Erreur Utilitaire lire "+e.getMessage());
		}
	}
	
	public static void initData() {
		// TODO Auto-generated method stub
		
	}
}
