package tw.edu.ncu.cc.course.server.controller.api.v1

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import resource.ServerResource
import specification.IntegrationSpecification
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
                        .withPath( "/token/string/TOKEN1" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody(
                            '''
                            {
                                "id" : 10,
                                "user" : "101502549",
                                "scope" : [ "CLASS_READ" ],
                                "last_updated" : "2014-12-15"
                            }
                            '''
                        )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/token/string/TOKEN2" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody(
                            '''
                            {
                                "id" : 10,
                                "user" : "101502549",
                                "scope" : [ "OTHER_PERMISSION" ],
                                "last_updated" : "2014-12-15"
                            }
                            '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/token/string/TOKEN3" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 404 )
                        .withBody(
                            '''
                            {
                                "error" : 1,
                                "error_description" : "resourse not exist"
                            }
                            '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
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
                        .perform( get( "/api/v1/student/selected" ).header( "Authorization", "Bearer TOKEN1" ) )
                        .andExpect( status().isOk() )
                        .andReturn()
            )
        then:
            response[0].serialNo == 12034
    }

    def "it can get the tracking course of user from valid access token"() {
        when:
            def response = JSON( server()
                    .perform( get( "/api/v1/student/tracking" ).header( "Authorization", "Bearer TOKEN1" ) )
                    .andExpect( status().isOk() )
                    .andReturn()
            )
        then:
            response[0].serialNo == 12034
    }

    def "it will return 403 when access token has insufficient scope"() {
        expect:
            server()
                    .perform( get( "/api/v1/student/tracking" ).header( "Authorization", "Bearer TOKEN2" ) )
                    .andExpect( status().is( 403 ) )
    }

    def "it will return 403 when access token is not exist"() {
        expect:
            server()
                    .perform( get( "/api/v1/student/tracking" ).header( "Authorization", "Bearer TOKEN3" ) )
                    .andExpect( status().is( 403 ) )
    }

}
