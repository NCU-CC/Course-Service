package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared


class LimitServiceImplTest extends SpringSpecification {

    @Autowired
    LimitService limitService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/course/91001/limit" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        {
                          "serialNo" : "91001",
                          "no" : "LA4001",
                          "name" : "文學與劇場",
                          "memo" : "some memo here"
                        }
                        '''
                )
        )
    }

    def "it can find limit of course by serialNo"() {
        when:
            def limit = limitService.findByCourseSerialNo( "zh_TW", "91001" )
        then:
            limit.no == "LA4001"
    }

}
