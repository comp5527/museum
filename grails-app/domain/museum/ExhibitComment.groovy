package museum

class ExhibitComment {
	Long exhibitCommentId
//	reply_comment_id
	List<ExhibitComment> replyComments
	String commentContent
	Date clientSideCreationDate
	Date creationDate
	User createdBy
	Date clientSideModifiedDate
	Date modifiedDate
	Long timestamp
	
	//Long exhibit_topic_id
	static belongsTo = [exhibitTopic: ExhibitTopic]
	
	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "exhibit_comment"
		
		//attr in Mongodb is to column in relational world
		exhibitCommentId attr: "_id"
		
		creationDate defaultValue: "now()"
		
	}
	
	static constraints = {
		exhibitCommentId nullable: true
	}
	
	static mapWith = "mongo"
}
