package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.data.v1.Limit

@Service
class LimitServiceImpl implements LimitService {

    @Autowired
    def ConnectionService connectionService

    @Override
    Limit findByCourseSerialNo( String language, String serialNo ) {
        connectionService
                .connect( "/course/{serialNo}/limit" )
                .variables( serialNo )
                .header( "Accept-Language", language )
                .get( Limit )
    }

}
