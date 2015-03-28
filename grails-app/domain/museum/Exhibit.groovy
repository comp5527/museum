package museum

import java.util.List;

class Exhibit {
	Long exhibitId
	String exhibitName
	String exhibitDescription
	ExhibitImage exhibitImage
	List<ExhibitTopic> exhibitTopics
	
	//static hasOne = [exhibitImage: ExhibitImage]
	
	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "exhibits"
		
		//attr in Mongodb is to column in relational world
		exhibitId attr: "_id"
		
		exhibitImage lazy: true
		exhibitTopics lazy: true
	}
	
	static constraints = {
		exhibitId nullable: true
	}
	
	static mapWith = "mongo"
}
