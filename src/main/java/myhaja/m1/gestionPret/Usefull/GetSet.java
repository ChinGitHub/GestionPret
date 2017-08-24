package myhaja.m1.gestionPret.Usefull;

import java.lang.reflect.Method;

public class GetSet {
	private Method get;
	private Method set;
	public Method getGet() {
		return get;
	}
	public void setGet(Method get) {
		this.get = get;
	}
	public Method getSet() {
		return set;
	}
	public void setSet(Method set) {
		this.set = set;
	}
	public String getName(){
		return this.getGet().getName().substring(3);
	}
	public Class<?> getType(){
		return this.getGet().getReturnType();
	}
}