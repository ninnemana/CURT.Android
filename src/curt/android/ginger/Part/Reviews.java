
package curt.android.ginger.Part;

public class Reviews{
   	private String createdDate;
   	private String email;
   	private String name;
   	private Number partID;
   	private Number rating;
   	private Number reviewID;
   	private String review_text;
   	private String subject;

 	public String getCreatedDate(){
		return this.createdDate;
	}
	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}
 	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email = email;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public Number getPartID(){
		return this.partID;
	}
	public void setPartID(Number partID){
		this.partID = partID;
	}
 	public Number getRating(){
		return this.rating;
	}
	public void setRating(Number rating){
		this.rating = rating;
	}
 	public Number getReviewID(){
		return this.reviewID;
	}
	public void setReviewID(Number reviewID){
		this.reviewID = reviewID;
	}
 	public String getReview_text(){
		return this.review_text;
	}
	public void setReview_text(String review_text){
		this.review_text = review_text;
	}
 	public String getSubject(){
		return this.subject;
	}
	public void setSubject(String subject){
		this.subject = subject;
	}
}
