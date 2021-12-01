package it.polimi.db2.HTMLhelper;

public class FormInstance {
	
	private String nameDescriptor;
	private String type;
	private String name;
	private String value = null;
	private boolean isRequired = true;
	private String realValue;
	
	public FormInstance(String nameDescriptor, String type, String name) {
		this.nameDescriptor = nameDescriptor;
		this.type = type;
		this.name = name;
	}
	
	public FormInstance(String nameDescriptor, String type, String name, boolean isRequired) {
		this.nameDescriptor = nameDescriptor;
		this.type = type;
		this.name = name;
		this.isRequired = isRequired;
	}
	
	public FormInstance(String nameDescriptor, String type, String name, String value) {
		this.nameDescriptor = nameDescriptor;
		this.type = type;
		this.name = name;
		this.value = value;
	}
	
	public String toString() {
		String toReturn =   nameDescriptor + ": <input type=\"" + type + "\" name=\"" + name + "\" ";
		if(value!= null) {
			toReturn = toReturn + " value=\"" + value +"\" ";
		}
			
		else if(isRequired){
			toReturn = toReturn + " required ";
		}
			
		return toReturn + "> <br> \n";
	}
	
	

}
