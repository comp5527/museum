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
