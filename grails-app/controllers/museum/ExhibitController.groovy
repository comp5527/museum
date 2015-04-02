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
		}else if (params.exhibitName == null || params.exhibitDescription == null) {
			msg = "Invalid Parameters."
		} else {
			//create exhibit domain object
		 	exhibit = new Exhibit(exhibitId: params.exhibitId,
								 exhibitName: params.exhibitName, 
								 exhibitDescription: params.exhibitDescription)

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
		
		def msg = ""
		if( exhibit == null){ 
			msg = "Exhibit ID not found."
		}
		//define response data
		def responseData = [
			'data': exhibit ? [
					'exhibitId' : exhibit.exhibitId,
					'exhibitName': exhibit.exhibitName, 
					'exhibitDescription': exhibit.exhibitDescription
				] : [],
			'status': exhibit ? "SUCCESS" : "FAIL",
			"msg": msg
		]
		render responseData as JSON
	}
	
	def getExhibitImage(){
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		
		if (!exhibit || !exhibit.exhibitImage || !exhibit.exhibitImage.exhibitImage || !exhibit.exhibitImage.contentType) {		  
		  def responseData = [
			  'status': "FAIL",
			  'msg' : "Image not found."
		  ]
		  render responseData as JSON
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
		
		def toRenderData = []
		if(exhibit != null && exhibit.exhibitTopics != null){
			toRenderData = exhibit.exhibitTopics.collect { exhibitTopic->
		        [
					"exhibitTopicId": exhibitTopic.exhibitTopicId, 
					"exhibitTopicName": exhibitTopic.exhibitTopicName,
					"creationDate": exhibitTopic.clientSideCreationDate,
					"createdBy": exhibitTopic.createdBy.userId
				]
		    }
		}
		//define response data
		def responseData = [
			'data': toRenderData,
			'status': exhibit ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
	}
	
	def getExhibitComments(){
		
		def exhibitTopic = ExhibitTopic.findByExhibitTopicId(params.exhibitTopicId)
		
		def root
		def parentval
		def returnList = []
		def childNodes = []
		if(exhibitTopic != null ){
			root = ExhibitComment.withCriteria{
							eq('exhibitTopic.', exhibitTopic )
						}
			
			{ inroot ->
				inroot.each {
					returnList << [
						"commentId": it.exhibitCommentId,
						"replyCommentId": (parentval)?parentval.exhibitCommentId:"",
						"content": it.commentContent,
						"modifiedDate": it.clientSideModifiedDate,
						"modifiedBy": it.createdBy.userId, // modifiedBy = createdBy
						"timestamp": it.timestamp
					]
					parentval = it
					childNodes = ExhibitComment.withCriteria {
						eq('exhibitTopic', parentval )
					}
				}
				if(childNodes)
					call(childNodes)
			}(root)
		    
		}
		//define response data
		def responseData = [
			'data': returnList,
			'status': exhibitTopic ? "SUCCESS" : "FAIL"
		]
		render responseData as JSON
		
	}
	
	def createExhibitTopic(){
		
		def exhibit = Exhibit.findByExhibitId(params.exhibitId)
		def user = User.findByUserId(params.userId)
		def creationTime = params.date('creationTime', 'yyyyMMddHHmmss')
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
				clientSideCreationDate: creationTime, 
				clientSideModifiedDate: creationTime
				)
			
			
			exhibitTopic = new ExhibitTopic(exhibitTopicName: params.topicName,
				clientSideCreationDate: creationTime,
				createdBy: user,
				exhibitComments: [exhibitComment]
				)
			
			
			//save Exhibit Topic
			exhibit.exhibitImage
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
