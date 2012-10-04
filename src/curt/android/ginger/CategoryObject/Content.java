
package curt.android.ginger.CategoryObject;

public class Content{
   	private ContentType contentType;
   	private Number cTypeID;
   	private Number contentID;
   	private String text;

 	public ContentType getContentType(){
		return this.contentType;
	}
	public void setContentType(ContentType contentType){
		this.contentType = contentType;
	}
 	public Number getCTypeID(){
		return this.cTypeID;
	}
	public void setCTypeID(Number cTypeID){
		this.cTypeID = cTypeID;
	}
 	public Number getContentID(){
		return this.contentID;
	}
	public void setContentID(Number contentID){
		this.contentID = contentID;
	}
 	public String getText(){
		return this.text;
	}
	public void setText(String text){
		this.text = text;
	}
}
