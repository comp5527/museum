package museum

class DeviceTokenAsso {
	
	String deviceToken
	
	static belongsTo = [ user: User]
	
	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "device_token_asso"
	}
	
	static constraints = {
	}
	
	static mapWith = "mongo"
}
