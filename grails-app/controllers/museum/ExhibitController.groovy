package museum
import java.util.Date;
import java.util.List;

import grails.converters.JSON

class ExhibitController {

	def index() { }
	
	//for internal use
	def addExhibitInfo(){
		//check is exhibitId in use
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		
		def msg = ""
		def result
		if( exhibit ){ 
			msg = "Exhibit ID already in use."
		} else {
			//create exhibit domain object
			exhibit.exhibitName = params.exhibitName
			exhibit.exhibitDescription = params.exhibitDescription

			//save exhibit
			result = exhibit.save()
			if( !result ) {
				exhibit.errors.each { println it }
			} else {
				msg = "Exhibit had been created."
			}
		}


		//define response data
		def responseData = [
			'data': exhibit ? ['exhibitId': exhibit.id] : [],
			'status': result ? "SUCCESS" : "FAIL",
			'msg' : msg
		]
		render responseData as JSON
	}
	
	private static final okcontents = ['image/png', 'image/jpeg', 'image/gif']
	//for internal use
	def setExhibitImage(){		
		// Get the avatar file from the multi-part request
		def f = request.getFile('image')
		
		// List of OK mime-types
		if (!okcontents.contains(f.getContentType())) {
			flash.message = "Image must be one of: ${okcontents}"
			render(view:'/uploadImage')
			return
		}
		
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		
		if(!exhibit){
			flash.message = "Exhibit not found."
			render(view:'/uploadImage')
			return
		}else{
			// Save the image and mime type
			if(exhibit.exhibitImage == null)
				exhibit.exhibitImage = new ExhibitImage()
			
			exhibit.exhibitImage.exhibitImage = f.bytes
			exhibit.exhibitImage.contentType = f.getContentType()
			
			log.info("File uploaded: $exhibit.exhibitImage.exhibitImage")
			
			// Validation works, will check if the image is too big
			if (!exhibit.save()) {
				flash.message = "Save Fail"
				render(view:'/uploadImage')
				return
			}
			flash.message = "Image (${exhibit.exhibitImage.exhibitImage.size()} bytes) uploaded."
			render(view:'/uploadImage')
		}
	}
	
	
	
	
	def getExhibitInfo(){
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		//define response data
		def responseData = [
			'data': exhibit ? [
					'exhibitId' : exhibit.exhibitId,
					'exhibitName': exhibit.exhibitName, 
					'exhibitDescription': exhibit.exhibitDescription
				] : [],
			'status': exhibit ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
	}
	
	def getExhibitImage(){
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		
		def avatarUser = User.get(params.id)
		if (!exhibit || !exhibit.exhibitImage || !exhibit.exhibitImage.exhibitImage || !exhibit.exhibitImage.contentType) {
		  response.sendError(404)
		  return
		}
		response.contentType = exhibit.exhibitImage.contentType
		response.contentLength = exhibit.exhibitImage.exhibitImage.size()
		OutputStream out = response.outputStream
		out.write(exhibit.exhibitImage.exhibitImage)
		out.close()
	}
	
	def getExhibitTopicList(){
		
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		//define response data
		def responseData = [
			'data': exhibit & exhibit.exhibitTopics ? exhibit.exhibitTopics : [],
			'status': exhibit ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
	}
	
	def getExhibitComments(){
		
		def exhibitTopic = ExhibitTopic.findByExhibitTopicId(params.exhibitTopicId)
		//define response data
		def responseData = [
			'data': exhibitTopic & exhibitTopic.exhibitComments ? exhibit.exhibitComments : [],
			'status': exhibitTopic ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
		
	}
	
	def createExhibitTopic(){
		
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		def user = User.findByUserId(params.userId)
		def exhibitTopic
		
		def msg = ""
		def result
		if( !exhibit ){
			msg = "Exhibit not found."
		}else if( !user ){
			msg = "User not found."
		} else {
			//create exhibit topic domain object
			def exhibitComment = new ExhibitComment(commentContent: params.commentContent, 
				createdBy: user, 
				clientSideCreationDate: params.creationTime, 
				clientSideModifiedDate: params.creationTime
				)
			
			exhibitTopic = new ExhibitTopic(exhibitTopicName: params.topicName,
				clientSideCreationDate: params.creationTime,
				createdBy: user,
				exhibitComments: [exhibitComment]
				)
			
			//save Exhibit Topic
			result = exhibit.addToExhibitTopics(exhibitTopic).save()
			if( !result ) {
				exhibit.errors.each { println it }
			} else {
				msg = "exhibitTopic had been created."
			}
		}

		//define response data
		def responseData = [
			'data': exhibitTopic ? ['exhibitTopicId': exhibitTopic.id] : [],
			'status': result ? "SUCCESS" : "FAIL",
			'msg' : msg
		]
		render responseData as JSON
		
	}
	
	
	def addTopicComment(){
		
		//check is exhibitId in use
		def exhibitTopic = ExhibitTopic.findByExhibitTopicId(params.exhibitTopicId)
		def user = User.findByUserId(params.userId)
		def replyComment
		if (params.replyCommentId != null)
			replyComment = ExhibitComment.findByExhibitCommentId(params.replyCommentId)
		
		
		def msg = ""
		def result
		if( exhibitTopic ){
			msg = "Exhibit Topic not found."
		}else if( !user ){
			msg = "User not found."
		}else if( params.replyCommentId != null && replyComment ){
			msg = "Reply Comment not found."
		} else {
			//create exhibitComment domain object
			def exhibitComment = new ExhibitComment(commentContent: params.commentContent,
					replyComment: replyComment,
					createdBy: user,
					clientSideCreationDate: params.insertTime,
					clientSideModifiedDate: params.insertTime
				)
			
			//save Comment
			if(replyComment){
				result = replyComment.addToReplyComments(exhibitComment).save()
				if( !result ) {
					replyComment.errors.each { println it }
				} else {
					msg = "Exhibit had been created."
				}
			} else {
				result = exhibitTopic.addToExhibitComments(exhibitComment).save()
				if( !result ) {
					exhibitTopic.errors.each { println it }
				} else {
					msg = "Exhibit had been created."
				}
			}
			
		}


		//define response data
		def responseData = [
			'data': exhibitTopic ? ['exhibitTopicId': exhibitTopic.id] : [],
			'status': result ? "SUCCESS" : "FAIL",
			'msg' : msg
		]
		render responseData as JSON
	}
	
	
	def updateTopicComment(){
		
		def user = User.findByUserId(params.userId)
		def exhibitComment = ExhibitComment.findByExhibitCommentId(params.exhibitCommentId)
		print params.commentContent;
		print params.updateTime;
		print params.timestamp;
		
		def msg = ""
		def result
		if( exhibitComment ){
			msg = "Exhibit Comment not found."
		}else if( !user ){
			msg = "User not found."
		} else {
			//save exhibitComment domain object
			exhibitComment.find {it.timestamp == params.timestamp}
			exhibitComment.commentContent = params.commentContent
			exhibitComment.clientSideModifiedDate = params.updateTime

			result = exhibitComment.save();
			if( !result ) {
				exhibitComment.errors.each { println it }
			} else {
				msg = "Exhibit Comment had been created."
			}

		}
		

		//define response data
		def responseData = [
			'data': exhibitComment.belongsTo ? ['exhibitTopicId': exhibitComment.belongsTo.id] : [],
			'status': result ? "SUCCESS" : "FAIL",
			'msg' : msg
		]
		render responseData as JSON
	}
	
}
