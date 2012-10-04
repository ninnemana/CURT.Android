
package curt.android.ginger.CategoryObject;


public class ContentType{
   	private boolean allowHTML;
   	private Number cTypeID;
   	private String type;

 	public boolean getAllowHTML(){
		return this.allowHTML;
	}
	public void setAllowHTML(boolean allowHTML){
		this.allowHTML = allowHTML;
	}
 	public Number getCTypeID(){
		return this.cTypeID;
	}
	public void setCTypeID(Number cTypeID){
		this.cTypeID = cTypeID;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
