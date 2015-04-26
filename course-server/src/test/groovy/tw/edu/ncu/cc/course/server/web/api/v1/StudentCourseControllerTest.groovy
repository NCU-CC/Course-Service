package tw.edu.ncu.cc.course.server.web.api.v1

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import resource.ServerResource
import specification.IntegrationSpecification
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static tw.edu.ncu.cc.oauth.resource.test.ApiAuthMockMvcRequestPostProcessors.accessToken
import static tw.edu.ncu.cc.oauth.resource.test.ApiAuthMockMvcRequestPostProcessors.apiToken

class StudentCourseControllerTest extends IntegrationSpecification {

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    @Shared
    private String serverResponse =
                        '''
                        [
                             {
                                "serialNo" : 12034,
                                "no" : "EL5001",
                                "classNo" : "*",
                                "name" : "Literature",
                                "isClosed" : false,
                                "memo": "freshman",
                                "isMasterDoctor": false,
                                "language": "Chinese",
                                "passwordCard": "no",
                                "isFirstRun": true,
                                "isPreSelect": true,
                                "teachers": ["Huffman"],
                                "credit": 2,
                                "classRooms": ["C2-209","C2-209"],
                                "times": { "1" : [5,6] },
                                "type": "required",
                                "fullHalf": "half",
                                "maxStudents": 0
                            }
                        ]
                        '''

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "en_US" ) )
                        .withPath( "/student/101502549/selected" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody( serverResponse )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "en_US" ) )
                        .withPath( "/student/101502549/tracking" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody( serverResponse )
        )
    }

    def "it can get the selected course of user from valid access token"() {
        when:
            def response = JSON( server()
                        .perform(
                            get( "/v1/student/selected" )
                                .with( apiToken() )
                                .with( accessToken().user( "101502549" ).scope( "CLASS_READ" ) )
                                .header( "Accept-Language", "en_US" )
                        )
                        .andExpect( status().isOk() )
                        .andReturn()
            )
        then:
            response[0].serialNo == 12034
    }

    def "it can get the tracking course of user from valid access token"() {
        when:
            def response = JSON( server()
                    .perform(
                        get( "/v1/student/tracking" )
                            .with( apiToken() )
                            .with( accessToken().user( "101502549" ).scope( "CLASS_READ" ) )
                            .header( "Accept-Language", "en_US" )
                    )
                    .andExpect( status().isOk() )
                    .andReturn()
            )
        then:
            response[0].serialNo == 12034
    }

    def "it will return 403 when access token has insufficient scope"() {
        expect:
            server()
                    .perform(
                        get( "/v1/student/tracking" )
                            .with( apiToken() )
                            .with( accessToken().user( "101502549" ).scope( "INVALID" ) )
                            .header( "Accept-Language", "en_US" )
                    )
                    .andExpect( status().isForbidden() )
    }

    def "it will return 400 when access token is not exist"() {
        expect:
            server()
                    .perform(
                        get( "/v1/student/tracking" )
                            .with( apiToken() )
                            .header( "Accept-Language", "en_US" )
                    )
                    .andExpect( status().isBadRequest() )
    }

}
