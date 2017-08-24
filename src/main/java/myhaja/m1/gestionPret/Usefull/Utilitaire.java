package myhaja.m1.gestionPret.Usefull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.joda.time.Days;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import myhaja.m1.gestionPret.bean.BaseModele;
import myhaja.m1.gestionPret.bean.Utilisateur;

public class Utilitaire {
	public static java.util.Date stringToDate(String dateDemande) throws Exception{
		String formatString = "dd/MM/yyyy";
		try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            return format.parse(dateDemande);
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	public static String getEncodedPassword(String key) {
		  byte[] uniqueKey = key.getBytes();
		  byte[] hash = null;
		  try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		  } catch (NoSuchAlgorithmException e) {
			throw new Error("no MD5 support in this VM");
		  }
		  StringBuffer hashString = new StringBuffer();
		  for ( int i = 0; i < hash.length; ++i ) {
			String hex = Integer.toHexString(hash[i]);
			if ( hex.length() == 1 ) {
			  hashString.append('0');
			  hashString.append(hex.charAt(hex.length()-1));
			} else {
			  hashString.append(hex.substring(hex.length()-2));
			}
		  }
		  return hashString.toString();
		}
	public static String formatMillier(Number nb){
		if(nb.intValue()>=1000){
			String temp=nb.toString();
			char[]tab=temp.toCharArray();
			int it=0;
			for(int i=tab.length;i>=0;i--){
				it++;
				if(it%3==0){
					//System.out.println(temp.substring(0,i)+"  i="+i+"  it="+it);
					temp=temp.substring(0,i)+" "+temp.substring(i,temp.length());
				}
			}
			return temp;
		}else return nb.toString();
	}
	public static boolean isValidJH(String str){
		if(Utilitaire.isNumeric(str)){
			double d=Double.parseDouble(str);
			if((d>=0)&&(d<=8))return true;
			else return false;
		}else return false;
	}
	public static boolean isNumeric(String str){
		try{
			double d=Double.parseDouble(str);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	public static List<String> lireJSON(String json) throws ParseException{
		 JSONParser parser = new JSONParser();
		  Object lDateobj = parser.parse(json);
		  JSONArray ldateArray = (JSONArray)lDateobj;
		  List<String> res=new ArrayList<String>();
		  for(int i=0;i<ldateArray.size();i++){
			  res.add((String)ldateArray.get(i));
		  }
		  return res;
	}
	public static String getMois(int mois){
		String[] listemois={"Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre"};
		for(int i=0;i<=listemois.length;i++){
			if(mois==i){
				//System.out.println("Mois : "+listemois[i-1]);
				return listemois[i-1]+"/"+String.valueOf(mois);
			}
		}
		return "";
	}
	public static java.util.Date getDate(String d,String sep){
		String[] ld=d.split(sep);
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(ld[2]));
		cal.set(Calendar.MONTH, Integer.parseInt(ld[1])-1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ld[0]));
		return cal.getTime();
	}
	public static String toJSON(List<String> liste){
		String res="[";
		for(int i=0;i<liste.size();i++){
			if(i!=liste.size()-1){	
				res+="\"";
				res+=liste.get(i);
				res+="\",";
			}else{
				res+="\"";
				res+=liste.get(i);
				res+="\"";
			}
		}
		res+="];";
		return res;
	}
	public void getJourOuvrable(Date début,Date fin){}
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        fields = getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
	public static List<java.util.Date> getJourOuvrable(int mois,int annee) {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.MONTH, mois);
	    cal.set(Calendar.YEAR, annee);
	    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    //SimpleDateFormat df = new SimpleDateFormat("EEE dd-MM-yyyy");
	    SimpleDateFormat format = new SimpleDateFormat("EEEE");
	    List<java.util.Date> listeJourOuvrable=new ArrayList<java.util.Date>();
	    for (int i = 0; i < maxDay; i++) {
	        cal.set(Calendar.DAY_OF_MONTH, i + 1);
	        String nomJour=format.format(cal.getTime());
	        if(/*(nomJour.compareToIgnoreCase("samedi")!=0) && */(nomJour.compareToIgnoreCase("dimanche")!=0)&& /*(nomJour.compareToIgnoreCase("saturday")!=0)&&*/ (nomJour.compareToIgnoreCase("sunday")!=0)){
	        	listeJourOuvrable.add(cal.getTime());
	        }
	        
	    }
	    //System.out.println("mois : "+mois+" an : "+annee);
	    //System.out.println("Jour ouvrable size : "+listeJourOuvrable.size());
	    return listeJourOuvrable;
	}
	public static int getJHduMois(int mois,int annee){
		//System.out.println("mois + "+mois+" an "+annee);
		//System.out.println("taille liste date: "+Utilitaire.getJourOuvrable(mois-1,annee).size());
		return Utilitaire.getJourOuvrable(mois-1,annee).size();
	}
	public static List<java.util.Date> getJourOuvrable(int mois) {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.MONTH, mois);
	    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    //SimpleDateFormat df = new SimpleDateFormat("EEE dd-MM-yyyy");
	    SimpleDateFormat format = new SimpleDateFormat("EEEE");
	    List<java.util.Date> listeJourOuvrable=new ArrayList<java.util.Date>();
	    for (int i = 0; i < maxDay; i++) {
	        cal.set(Calendar.DAY_OF_MONTH, i + 1);
	        String nomJour=format.format(cal.getTime());
	        if(/*(nomJour.compareToIgnoreCase("samedi")!=0) &&*/ (nomJour.compareToIgnoreCase("dimanche")!=0)&& /*(nomJour.compareToIgnoreCase("saturday")!=0)&&*/ (nomJour.compareToIgnoreCase("sunday")!=0)){
	        	listeJourOuvrable.add(cal.getTime());
	        }
	        
	    }
	    return listeJourOuvrable;
	}
	public static List<java.util.Date> getJourOuvrable() {
		Calendar cal = Calendar.getInstance();
		java.util.Date aujourdui=new java.util.Date();
		int moisAujourdhui=aujourdui.getMonth();
		//System.out.println("Mois aujourd hui :"+moisAujourdhui);
	    cal.set(Calendar.MONTH, moisAujourdhui);
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
		//System.out.println("liste attribut final length :"+listeAttributFinal.size());
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
			//System.out.println("attribut liste taille : "+attributList.length);
			/*for(Field f:attributList){
				System.out.println(f.getName());
			}*/
			Field[] attributParentList =b.getClass().getSuperclass().getDeclaredFields();
			List<Object> liste=Utilitaire.arrayToList(attributList);
			for(Field parent:attributParentList){
				liste.add(parent);
			}
			//System.out.println("Liste des fields : ");
			List<GetSet> gettersSetters=new ArrayList<GetSet>();
			for(Object attribut: liste){
				Field att=(Field)attribut;
				//System.out.println(att.getName());
				gettersSetters.add(Utilitaire.getPropriety(b, att.getName()));
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
			//System.out.println("Taille attribut : "+attributList.length);
			//System.out.println("Taille attributParent : "+attributParentList.length);
			for(Field attribut: attributList){
				//verifie si le champ et le champ en parametre ont le meme nom
				if(attribut.getName().compareToIgnoreCase(nomAttribut)==0){
					//System.out.println(attribut.getName()+" == "+nomAttribut);
					Method[] methodList=b.getClass().getDeclaredMethods();
					for(Method method : methodList){
						//enlever d'abord les get ou les set devant le nom de la methode
						String nomMethod=method.getName().toLowerCase();
						nomMethod=nomMethod.replace("get", "");
						nomMethod=nomMethod.replace("set", "");
						//System.out.println("Nom methode : "+nomMethod+"  :  "+nomAttribut);
						//si le nom de la methode contient le nom du champ
						if(nomMethod.compareTo(nomAttribut.toLowerCase())==0){
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
		//System.out.println("tafiditr lire");
		try{
			//parcours des attributs de la classe et des champs de la table
			for(String champ : listeChamp){
				for(String champTable : listeChampTable){
					if(champ.compareToIgnoreCase(champTable)==0){
						//vérifier le type du champ renvoyé par le resulset
						//récupérer la valeur de la base et le mettre dans l'attribut de la classe adéquat
						//System.out.println(rs.getObject(champ).getClass().getSimpleName());
						//System.out.println(champ +" : "+ rs.getObject(champ).toString());
						//System.out.println(rs.getObject(champ).getClass().getSimpleName());
						if(rs.getObject(champ)!=null){
							//System.out.println("champ : "+champ);
							//System.out.println("valeur : "+rs.getObject(champ));
							GetSet gettersSetters=Utilitaire.getPropriety(obj, champ);
							//System.out.println(gettersSetters.getName());
							if(gettersSetters !=null){
								/*System.out.println("entree gettersetters non null pour le champ : " +gettersSetters.getName());
								System.out.println("getter: " +gettersSetters.getGet().getName());
								System.out.println("setter: " +gettersSetters.getSet().getName());
								System.out.println("Type: " +gettersSetters.getGet().getReturnType().getSimpleName());
								System.out.println("Type ao am base: " +rs.getObject(champ).getClass().getSimpleName());*/
								if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("integer")==0){
									int valeur=rs.getInt(champ);
									//System.out.println("Valeur ! "+valeur);
									if (valeur !=0){
										if(gettersSetters.getGet().getReturnType().getSimpleName().compareToIgnoreCase("string")==0){
											gettersSetters.getSet().invoke(obj, new Object[]{Integer.toString(valeur)});
										}else{
											Class<?>[] pType  = gettersSetters.getSet().getParameterTypes();
											//System.out.println(pType[0].getName());
											if(pType[0].getName().compareTo("java.lang.String")==0){
												gettersSetters.getSet().invoke(obj, new Object[]{Integer.toString(valeur)});
											}else gettersSetters.getSet().invoke(obj, new Object[]{valeur});
											
										}
										
									}
								}else if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("string")==0){
									String valeur=rs.getString(champ);
									if(valeur !=null)gettersSetters.getSet().invoke(obj, new Object[]{valeur});
								}else if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("date")==0){
									Date valeur=rs.getDate(champ);
									if(valeur !=null){
										Class<?>[] pType  = gettersSetters.getSet().getParameterTypes();
										//System.out.println(pType[0].getName());
										if(pType[0].getName().compareTo("java.lang.String")==0){
											DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
											String reportDate = df.format(valeur);
											gettersSetters.getSet().invoke(obj, new Object[]{reportDate});
										}else gettersSetters.getSet().invoke(obj, new Object[]{valeur});
										
										
									}
								}else if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("float")==0){
									Float valeur=rs.getFloat(champ);
									if(valeur !=null){
										Class<?>[] pType  = gettersSetters.getSet().getParameterTypes();
										//System.out.println(pType[0].getName());
										if(pType[0].getName().compareTo("java.lang.String")==0){
											gettersSetters.getSet().invoke(obj, new Object[]{Float.toString(valeur)});
										}else gettersSetters.getSet().invoke(obj, new Object[]{valeur});
									}
								}
								else if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("boolean")==0){
									boolean valeur=rs.getBoolean(champ);
									gettersSetters.getSet().invoke(obj, new Object[]{valeur});
								}else if(rs.getObject(champ).getClass().getSimpleName().compareToIgnoreCase("double")==0){
									
									Double valeur=new Double(rs.getDouble(champ));
									if (valeur !=0){
										if(gettersSetters.getGet().getReturnType().getSimpleName().compareToIgnoreCase("integer")==0){
											gettersSetters.getSet().invoke(obj, new Object[]{valeur.intValue()});
										}else if(gettersSetters.getGet().getReturnType().getSimpleName().compareToIgnoreCase("float")==0){
											gettersSetters.getSet().invoke(obj, new Object[]{valeur.floatValue()});
										}
										else{
											Class<?>[] pType  = gettersSetters.getSet().getParameterTypes();
											//System.out.println(pType[0].getName());
											if(pType[0].getName().compareTo("java.lang.String")==0){
												gettersSetters.getSet().invoke(obj, new Object[]{Double.toString(valeur)});
											}else gettersSetters.getSet().invoke(obj, new Object[]{valeur});
										}
										
									}
								}else{
									String valeur=rs.getString(champ);
									gettersSetters.getSet().invoke(obj, new Object[]{valeur});
								}
								//System.out.println("sortie gettersetters non null pour le champ : " +gettersSetters.getName());
							}
							
						}else{
							//System.out.println("Champ : "+champ);
							GetSet gettersSetters=Utilitaire.getPropriety(obj, champ);
							//System.out.println(gettersSetters.getName());
							
							if(gettersSetters.getGet().getReturnType().getSimpleName().compareToIgnoreCase("string")==0){
								String valeur=new String("-");
								//System.out.println("La valeur du string est : "+valeur);
								gettersSetters.getSet().invoke(obj, new Object[]{valeur});
							}else{
								//System.out.println("ato");
								float valeur=0;
								gettersSetters.getSet().invoke(obj, new Object[]{valeur});
							}
							
						}
						
					}
				}
			}
		}catch(Exception e){
			throw new Exception("Erreur Utilitaire lire "+e.getMessage());
		}
	}
	public static int getDifferenceEntreDeuxDate(java.util.Date dateRemboursement, java.util.Date d) {
		int res=(int)( (dateRemboursement.getTime() - d.getTime()) / (1000 * 60 * 60 * 24));
		if(res>0)return res;
		else return res*(-1);
	}
	public static String getDateRandom(int idDemande) {
		Calendar c=Calendar.getInstance();
		c.add(Calendar.MONTH, idDemande+1);
		return Utilitaire2.getDateTexte(c.getTime());
	}
	
}
