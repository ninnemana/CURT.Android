
package curt.android.ginger.CategoryObject;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;

import curt.android.ginger.JSONHandler;
import curt.android.ginger.Part.*;

public class Category{
   	private List<ContentBridges> contentBridges;
   	private List lifestyle_Trailers;
   	private Number catID;
   	private String catTitle;
   	private Number codeID;
   	private String dateAdded;
   	private String image;
   	private Number isLifestyle;
   	private String longDesc;
   	private Number parentID;
   	private String shortDesc;
   	private Number sort;
   	private boolean vehicleSpecific;

 	public List getContentBridges(){
		return this.contentBridges;
	}
	public void setContentBridges(List contentBridges){
		this.contentBridges = contentBridges;
	}
 	public List getLifestyle_Trailers(){
		return this.lifestyle_Trailers;
	}
	public void setLifestyle_Trailers(List lifestyle_Trailers){
		this.lifestyle_Trailers = lifestyle_Trailers;
	}
 	public Number getCatID(){
		return this.catID;
	}
	public void setCatID(Number catID){
		this.catID = catID;
	}
 	public String getCatTitle(){
		return this.catTitle;
	}
	public void setCatTitle(String catTitle){
		this.catTitle = catTitle;
	}
 	public Number getCodeID(){
		return this.codeID;
	}
	public void setCodeID(Number codeID){
		this.codeID = codeID;
	}
 	public String getDateAdded(){
		return this.dateAdded;
	}
	public void setDateAdded(String dateAdded){
		this.dateAdded = dateAdded;
	}
 	public String getImage(){
		return this.image;
	}
	public void setImage(String image){
		this.image = image;
	}
 	public Number getIsLifestyle(){
		return this.isLifestyle;
	}
	public void setIsLifestyle(Number isLifestyle){
		this.isLifestyle = isLifestyle;
	}
 	public String getLongDesc(){
		return this.longDesc;
	}
	public void setLongDesc(String longDesc){
		this.longDesc = longDesc;
	}
 	public Number getParentID(){
		return this.parentID;
	}
	public void setParentID(Number parentID){
		this.parentID = parentID;
	}
 	public String getShortDesc(){
		return this.shortDesc;
	}
	public void setShortDesc(String shortDesc){
		this.shortDesc = shortDesc;
	}
 	public Number getSort(){
		return this.sort;
	}
	public void setSort(Number sort){
		this.sort = sort;
	}
 	public boolean getVehicleSpecific(){
		return this.vehicleSpecific;
	}
	public void setVehicleSpecific(boolean vehicleSpecific){
		this.vehicleSpecific = vehicleSpecific;
	}
	
	public List<Category> GetParents(){
		String url = "GetParentCategories?dataType=JSON";
		String jString = JSONHandler.getJSONListFromURL(url);
		Gson gson = new Gson();
		List<Category> cats = null;
		try{
			cats = gson.fromJson(jString, new TypeToken<List<Category>>(){}.getType());
		}catch(Exception e){
			Log.e("log_tag", "Error parsing json to object"+e.toString());
		}
		return cats;
	}
	
	public List<Category> GetSubcategories(){
		String url = "GetCategories?dataType=JSON&parentID=" + this.catID;
		String jString = JSONHandler.getJSONListFromURL(url);
		Gson gson = new Gson();
		List<Category> subs = null;
		try{
			subs = gson.fromJson(jString, new TypeToken<List<Category>>(){}.getType());
		}catch(Exception e){
			Log.e("log_tag", "Error parsing json to object " + e.toString());
		}
		return subs;
	}
	
	public List<Part> GetCategoryParts(){
		String url = "GetCategoryParts?dataType=JSON&catID=" + this.catID.intValue();
		String jString = JSONHandler.getJSONListFromURL(url);
		Gson gson = new Gson();
		List<Part> parts = null;
		try{
			parts = gson.fromJson(jString, new TypeToken<List<Part>>(){}.getType());
		}catch(Exception e){
			Log.e("log_tag", "Error parsing json to object "+ e.toString());
		}
		return parts;
	}
}
