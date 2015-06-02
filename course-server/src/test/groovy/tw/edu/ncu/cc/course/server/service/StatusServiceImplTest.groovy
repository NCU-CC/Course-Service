package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared


class StatusServiceImplTest extends SpringSpecification {

    @Autowired
    private StatusService statusService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/status" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        {
                          "semester" : 1021 ,
                          "stage" : "¥[°h¿ï¶¥¬q"
                        }
                        '''
                )
        )
    }

    def "it can find status from remote server"() {
        when:
            def status = statusService.findCurrentStatus( "zh_TW" )
        then:
            status.semester == 1021
    }

}
