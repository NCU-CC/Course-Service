package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.data.v1.Course

@Service
class CourseServiceImpl implements CourseService {

    @Autowired
    def ConnectionService connectionService

    @Override
    Course findBySerialNo( String language, String serialNo ) {
        connectionService
                .connect( "/course/{serialNo}" )
                .variables( serialNo )
                .header( "Accept-Language", language )
                .get( Course )
    }

}
