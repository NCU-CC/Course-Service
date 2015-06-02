package tw.edu.ncu.cc.course.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tw.edu.ncu.cc.course.data.v1.Status
import tw.edu.ncu.cc.course.server.service.StatusService

@RestController
@RequestMapping( value = "v1/status", method = RequestMethod.GET )
class StatusController {

    @Autowired
    def StatusService statusService

    @RequestMapping
    public Status findCurrentStatus( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language ) {

        statusService.findCurrentStatus( language )
    }

}
