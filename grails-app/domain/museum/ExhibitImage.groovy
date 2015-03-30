package museum

class ExhibitImage {
//	Long exhibitId
	byte[] exhibitImage
	String contentType
	
	static belongsTo = [exhibit: Exhibit]
	
	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "exhibit_images"
		
	}
	
	static constraints = {
	}
	
	static mapWith = "mongo"
}
