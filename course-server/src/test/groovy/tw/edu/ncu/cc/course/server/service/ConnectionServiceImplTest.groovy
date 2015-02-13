package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared
import tw.edu.ncu.cc.course.data.v1.Unit


class ConnectionServiceImplTest extends SpringSpecification {

    private ConnectionService connectionService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/testPath" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody(
                        '''
                        {
                            "id" : 1,
                            "name" : "jason"
                        }
                        '''
                )
        )
    }

    def setup() {
        connectionService = new ConnectionServiceImpl()
        connectionService.setRemotePrefix( "http://localhost:8898/" )
    }

    def "it can get response from remote server"() {
        when:
            def response = connectionService.connect( "testPath" ).get( Unit.class )
        then:
            response.name == "jason"
    }

}
