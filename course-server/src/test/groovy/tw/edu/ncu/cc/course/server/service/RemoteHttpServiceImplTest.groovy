package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared
import tw.edu.ncu.cc.course.data.v1.Unit


class RemoteHttpServiceImplTest extends SpringSpecification {

    private RemoteHttpService remoteHttpService

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
        remoteHttpService = new RemoteHttpServiceImpl()
        remoteHttpService.setRemotePrefix( "http://localhost:8898/" )
    }

    def "it can get response from remote server"() {
        when:
            def response = remoteHttpService.getObject( Unit.class, "testPath" )
        then:
            response.name == "jason"
    }

}
