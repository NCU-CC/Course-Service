package tw.edu.ncu.cc.course.server.web.api.v1

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.Parameter
import resource.ServerResource
import specification.IntegrationSpecification
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static tw.edu.ncu.cc.oauth.resource.test.ApiAuthMockMvcRequestPostProcessors.accessToken

class StudentCourseControllerTest extends IntegrationSpecification {

    def token = accessToken().user( "user-uid" ).scope( "user.info.basic.read" )

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
                            "name" : "����\\/���Ʋz�׾�Ū",
                            "isClosed" : false,
                            "memo": "���T�B�|�~��",
                            "isMasterDoctor": false,
                            "language": "���y",
                            "passwordCard": "���ϥ�",
                            "isFirstRun": true,
                            "isPreSelect": true,
                            "teachers": [  "���ҤH", "���g�B" ],
                            "credit": 2,
                            "classRooms": [ "C2-209", "C2-209" ],
                            "times": {
                                      "0": ["5"],
                                      "2": ["3", "4"]
                                    },
                            "type": "����",
                            "fullHalf": "��",
                            "maxStudents": 0
                          }
                        ]
                        '''

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "en_US" ) )
                        .withPath( "/students/101502549/courses" )
                        .withQueryStringParameter( new Parameter( "filter", "selected" ) )
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
                        .withPath( "/students/101502549/courses" )
                        .withQueryStringParameter( new Parameter( "filter", "tracking" ) )
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
                        .withPath( "/students/101502549/courses" )
                        .withQueryStringParameter( new Parameter( "filter", "rejected" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody( serverResponse )
        )
    }

    def "it can provide the selected course of user from valid access token"() {
        when:
            def response = JSON(
                    server().perform(
                        get( "/v1/student/selected" )
                            //.with( token )
                            .with( accessToken().user( "101502549" ).scope( "course.schedule.read" ) )
                            .header( "Accept-Language", "en_US" )
                    ).andExpect(
                        status().isOk()
                    ).andReturn()
            )
        then:
            response[0].serialNo == 12034
    }

    def "it can provide the tracking course of user from valid access token"() {
        when:
            def response = JSON(
                    server().perform(
                        get( "/v1/student/tracking" )
                            //.with( token )
                            .with( accessToken().user( "101502549" ).scope( "course.schedule.read" ) )
                            .header( "Accept-Language", "en_US" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].serialNo == 12034
    }

    def "it can provide the rejected course of user from valid access token"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/student/rejected" )
                                    //.with( token )
                                    .with( accessToken().user( "101502549" ).scope( "course.schedule.read" ) )
                                    .header( "Accept-Language", "en_US" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].serialNo == 12034
    }

    def "it will return 403 when access token has insufficient scope"() {
        expect:
            server().perform(
                get( "/v1/student/tracking" )
                    //.with( token )
                    .with( accessToken().user( "101502549" ).scope( "INVALID" ) )
                    .header( "Accept-Language", "en_US" )
            ).andExpect(
                status().isForbidden()
            )
    }

    def "it cannot provide any information if access token not provided"() {
        expect:
            server().perform(
                get( "/v1/student/tracking" )
                    .header( "Accept-Language", "en_US" )
            ).andExpect(
                status().isBadRequest()
            )
    }

}
