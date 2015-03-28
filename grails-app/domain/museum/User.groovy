package museum

import org.bson.types.ObjectId

class User {
	Long userId
	String emailAddress
	String userName
	String password
	List<DeviceTokenAsso> deviceTokenAssos
	
	//static hasMany = [deviceTokenAssos: DeviceTokenAsso]
	
	static mapping = {
		//Collection in Mongodb is to Table in relational world
		collection "users"
		
		//attr in Mongodb is to column in relational world
		userId attr: "_id"
		
		deviceTokenAssos lazy:true
	}
	static constraints = {
		userId nullable: true
	}
	static mapWith = "mongo"
}
