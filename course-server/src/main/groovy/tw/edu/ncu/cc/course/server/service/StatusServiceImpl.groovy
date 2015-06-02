package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.data.v1.Status

@Service
class StatusServiceImpl implements StatusService {

    @Autowired
    def ConnectionService connectionService

    @Override
    Status findCurrentStatus( String language ) {
        connectionService
                .connect( "/status" )
                .header( "Accept-Language", language )
                .get( Status )
    }

}
