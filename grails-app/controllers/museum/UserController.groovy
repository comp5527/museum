package museum
import grails.converters.JSON

class UserController {

	def index() { }

	def userRegistration(){
		//check is emailAddress exist
		def existingUser = User.findByEmailAddress(params.emailAddress)
		
		def user
		def msg = ""
		def result
		if( existingUser ){
			msg = "User already existed."
		} else if (params.emailAddress == null || params.userName == null || params.password == null) {
			msg = "Invalid Parameters."		
		} else {
			//create user domain object
			user = new User(emailAddress: params.emailAddress, userName : params.userName, password : HashUtils.generateMD5(params.password))

			//save user
			result = user.save()
			if( !result ) {
				user.errors.each { println it }
			} else {
				msg = "User had been created."
			}
		}


		//define response data
		def responseData = [
			'data': user ? ['userId': user.id] : [],
			'status': result ? "SUCCESS" : "FAIL",
			'msg' : msg
		]
		render responseData as JSON
	}

	def login(){
		def msg
		def user
		if (params.emailAddress == null || params.password == null) {
			msg = "Invalid Parameters."
		}else{
			def sampleUser = new User(emailAddress: params.emailAddress,
				password: HashUtils.generateMD5(params.password))
			user = User.find(sampleUser)
		}

		//define response data
		def responseData = [
			'data': user ? ['userId': user.id] : [],
			'status': user ? "SUCCESS" : "FAIL",
			'msg' : msg
		]
		render responseData as JSON
	}	

	def deviceTokenRegistration(){
		def user = User.findByUserId(params.userId)
		
		if( user ){
			def deviceTokenAsso = user.deviceTokenAssos.find {it.deviceToken == params.deviceToken} 
			if( !deviceTokenAsso ){
				deviceTokenAsso = new DeviceTokenAsso(deviceToken: params.deviceToken)
				def result = user.addToDeviceTokenAssos(deviceTokenAsso).save()			
				//save user
				if( !result ) {
					user.errors.each { println it }
				}
			}
		}
		
		//define response data
		def responseData = [
			'status': user ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
	}

	def logout(){
		
		def user = User.findByUserId(params.userId)
		
		if( user ){
			
			def deviceTokenAsso = user.deviceTokenAssos.find { it.deviceToken == params.deviceToken }
			
			if(deviceTokenAsso){
				user.removeFromDeviceTokenAssos(deviceTokenAsso)
				
				def result = deviceTokenAsso.delete()
				//save user
				if( !result ) {
					user.errors.each { println it }
				}
			}
			
		}
		
		//define response data
		def responseData = [
			'status': user ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
	}
}
