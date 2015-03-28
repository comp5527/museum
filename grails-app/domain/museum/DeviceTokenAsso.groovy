package museum

class DeviceTokenAsso {
	Long userId
	String deviceToken

	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "device_token_asso"
	}
	
	static constraints = {
	}
	
	static mapWith = "mongo"
}
