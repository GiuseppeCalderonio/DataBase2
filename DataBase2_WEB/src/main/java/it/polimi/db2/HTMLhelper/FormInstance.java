package it.polimi.db2.HTMLhelper;

public class FormInstance {
	
	private String nameDescriptor;
	private String type;
	private String name;
	
	
	public FormInstance(String nameDescriptor, String type, String name) {
		this.nameDescriptor = nameDescriptor;
		this.type = type;
		this.name = name;
	}
	
	public String toString() {
		return nameDescriptor + ": <input type=\"" + type + "\" name=\"" + name + "\" required><br>";
	}
	
	

}
