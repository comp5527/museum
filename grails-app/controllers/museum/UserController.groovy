package museum

import grails.converters.JSON

class UserController {

	def index() { }
	
	def userRegistration(){
		//check is emailAddress exist
		//to do.....
		
		//create user domain object
		def user = new User(emailAddress: params.emailAddress,
							userName: params.userName,
							//need hash the password <=======================================
							password: params.password)
		
		//save user
		def result = user.save()
		if( !result ) {
			user.errors.each {
				 println it
			}
		 }
		
		//define response data
		def responseData = [
			'data': user ? ['userId': user.id] : [],
			'status': result ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
	}
	
	def login(){

		def sampleUser = new User(emailAddress: params.emailAddress,
				//need hash the password
									password: params.password)
		def user = User.find(sampleUser)
		
		//define response data
		def responseData = [
			'data': user ? ['userId': user.id] : [],
			'status': user ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
	}
	
	def deviceTokenRegistration(){
		print params;
	}
	
	def logout(){
		print params;
	}
}
