package museum

import java.util.List;

class Exhibit {
	Long exhibitId
	String exhibitName
	String exhibitDescription
	//ExhibitImage exhibitImage
//	List<ExhibitTopic> exhibitTopics
	
	static hasMany = [exhibitTopics: ExhibitTopic]
	static hasOne = [exhibitImage: ExhibitImage]
	
	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "exhibits"
		
		//attr in Mongodb is to column in relational world
		exhibitId attr: "_id"
		
		exhibitTopics lazy: true
		exhibitImage lazy: true
	}
	
	static constraints = {
		exhibitImage nullable: true
	}
	
	static mapWith = "mongo"
}
