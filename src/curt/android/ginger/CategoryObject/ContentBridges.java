
package curt.android.ginger.CategoryObject;


public class ContentBridges{
   	private Content content;
   	private String part;
   	private Number cBridgeID;
   	private Number catID;
   	private Number contentID;
   	private String partID;

 	public Content getContent(){
		return this.content;
	}
	public void setContent(Content content){
		this.content = content;
	}
 	public String getPart(){
		return this.part;
	}
	public void setPart(String part){
		this.part = part;
	}
 	public Number getCBridgeID(){
		return this.cBridgeID;
	}
	public void setCBridgeID(Number cBridgeID){
		this.cBridgeID = cBridgeID;
	}
 	public Number getCatID(){
		return this.catID;
	}
	public void setCatID(Number catID){
		this.catID = catID;
	}
 	public Number getContentID(){
		return this.contentID;
	}
	public void setContentID(Number contentID){
		this.contentID = contentID;
	}
 	public String getPartID(){
		return this.partID;
	}
	public void setPartID(String partID){
		this.partID = partID;
	}
}
