class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/UserRegistration"(controller: "user", action: "userRegistration")
		
		"/Login"(controller: "user", action: "login")
		
		"/DeviceTokenRegistration"(controller: "user", action: "deviceTokenRegistration")
		
		"/Logout"(controller: "user", action: "logout")
		
		"/AddExhibitInfo"(controller: "exhibit", action: "addExhibitInfo")
		
		"/UpdateExhibitInfo"(controller: "exhibit", action: "updateExhibitInfo")
		
		"/UploadImage"(view:"/uploadImage")
				
		"/SetExhibitImage"(controller: "exhibit", action: "setExhibitImage")
		
		"/GetExhibitInfo"(controller: "exhibit", action: "getExhibitInfo")
		
		"/GetExhibitImage"(controller: "exhibit", action: "getExhibitImage")

		"/GetExhibitTopicList"(controller: "exhibit", action: "getExhibitTopicList")
		
		"/GetExhibitComments"(controller: "exhibit", action: "getExhibitComments")
		
		"/CreateExhibitTopic"(controller: "exhibit", action: "createExhibitTopic")
		
		"/AddTopicComment"(controller: "exhibit", action: "addTopicComment")
		
		"/UpdateTopicComment"(controller: "exhibit", action: "updateTopicComment")
				
        "/"(view:"/index")
        "500"(view:'/error')
	}
}
