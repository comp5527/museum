package museum

import java.util.List;

class ExhibitTopic {
	Long exhibitTopicId
	String exhibitTopicName
	Date clientSideCreationDate
	Date creationDate
	User createdBy
	List<ExhibitComment> exhibitComments
	
	//Long exhibit_id
	static belongsTo = [exhibit: Exhibit]
	
	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "exhibit_topic"
		
		//attr in Mongodb is to column in relational world
		exhibitTopicId attr: "_id"
		
		creation_date defaultValue: "now()"
		
		exhibitComments lazy: true
	}
	
	static constraints = {
		exhibitTopicId nullable: true
	}
	
	static mapWith = "mongo"
}
