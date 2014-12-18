package tw.edu.ncu.cc.course.client.android;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tw.edu.ncu.cc.course.client.tool.config.CourseConfig;
import tw.edu.ncu.cc.course.client.tool.response.ResponseListener;
import tw.edu.ncu.cc.course.data.Course;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NCUCourseClient {

    private OAuthHandler oAuthHandler;
    private RequestQueue queue;
    private String baseURL;
    private String token;

    public NCUCourseClient( CourseConfig config, OAuthHandler oAuthHandler, Context context ) {
        this.baseURL = config.getServerAddress();
        this.queue = Volley.newRequestQueue( context );
        this.oAuthHandler = oAuthHandler;
        initAccessToken();
    }

    private void initAccessToken() {
        try {
            this.token = oAuthHandler.authorize( "user", null, null ).getResult().getAccessToken();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public void getSelectedCourse( ResponseListener< Course[] > responseListener ) {
        get(
                "api/v1/student/selected", responseListener, new TypeToken< Course[] >(){}
        );
    }

    public void getTrackingCourse( ResponseListener< Course[] > responseListener ) {
        get(
                "api/v1/student/tracking", responseListener, new TypeToken< Course[] >(){}
        );
    }

    private < T > void get( String path, final ResponseListener< T > responseListener, final TypeToken typeToken ) {
        queue.add( new StringRequest( Request.Method.GET, baseURL + path,
                new Response.Listener< String >() {
                    @Override
                    public void onResponse( String response ) {
                        T data = new Gson().fromJson( response, typeToken.getType() );
                        responseListener.onResponse( data );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse( VolleyError error ) {
                        responseListener.onError( error );
                        if( error.networkResponse.statusCode == 403 ) {
                            initAccessToken();
                        }
                    }
                }
        ) {
            public Map< String, String > getHeaders() throws AuthFailureError {
                Map< String, String > headers = new HashMap<>();
                headers.put( "Authorization", "Bearer " + token );
                return headers;
            }
        } );
    }


}
