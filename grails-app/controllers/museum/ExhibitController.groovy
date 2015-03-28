package museum

class ExhibitController {

	def index() { }
	
	def getExhibitInfo(){
		print params.exhibitId;
	}
	
	def getExhibitImage(){
		print params.exhibitId;
	}
	
	def getExhibitComments(){
		print params.exhibitTopicId;
	}

	
	def createExhibitTopic(){
		print params.userId;
		print params.exhibitId;
		print params.topicName;
		print params.commentContent;
		print params.creationTime;
	}
	
	
	def addTopicComment(){
		print params.userId;
		print params.exhibitTopicId;
		print params.replyCommentId;
		print params.commentContent;
		print params.insertTime;
	}
	
	
	def updateTopicComment(){
		print params.userId;
		print params.exhibitCommentId;
		print params.commentContent;
		print params.updateTime;
		print params.timestamp;
	}
	
}
