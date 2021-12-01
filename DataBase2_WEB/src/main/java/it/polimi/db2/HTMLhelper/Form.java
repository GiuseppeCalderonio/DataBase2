package it.polimi.db2.HTMLhelper;

import java.util.ArrayList;
import java.util.List;

public class Form {
	
	/**
	 * this attribute represents the action of the form that will be done
	 * after the user presses submit, it can be a string that corresponds
	 * to a Servlet class name, and that class will be called in "action" mode
	 */
	private String action;
	
	/**
	 * this attribute represents the method called after the submit of the form
	 * it can be GET or POST
	 */
	private String method;
	/**
	 * this attribute represents the error message displayed when something goes wrong
	 */
	private String errorMessage;
	/**
	 * this attribute represents the form instances that will be displayed
	 * every field to fill will be an elements of that list
	 * ex: username and password form: 2 elements for this list
	 */
	private List<FormInstance> formInstances = new ArrayList<>();
	/**
	 * this attribute represent what the user will see over the submit button
	 */
	private String value;
	
	private String additional = "";
	
	
	public Form(String action, String method, String errorMessage, List<FormInstance> formInstances, String value) {
		this.action = action;
		this.method = method;
		if(formInstances != null)
			this.formInstances = formInstances;
		this.errorMessage = errorMessage;
		this.value = value;
	}
	
	public Form(String action, String method, String errorMessage, List<FormInstance> formInstances, String value, String additional) {
		this.action = action;
		this.method = method;
		if(formInstances != null)
			this.formInstances = formInstances;
		this.errorMessage = errorMessage;
		this.value = value;
		this.additional = additional;
	}
	
	public String toString() {
		String toReturn = "<form action=\"" + action + "\" method=\"" + method + "\">";
		toReturn = toReturn + additional;
		for(FormInstance i : formInstances) {	
			toReturn = toReturn + i.toString();
		}
		toReturn = toReturn + "<input type=\"submit\" value=\"" + value + "\">";
		if(!errorMessage.isEmpty())
			toReturn = toReturn +  errorMessage + "<br>";
		toReturn = toReturn + "</form><br>";
		return toReturn;
	}
	
	

}
