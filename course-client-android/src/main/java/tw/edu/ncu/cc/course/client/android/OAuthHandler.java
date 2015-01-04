package tw.edu.ncu.cc.course.client.android;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson.JacksonFactory;
import com.wuman.android.auth.AuthorizationFlow;
import com.wuman.android.auth.AuthorizationUIController;
import com.wuman.android.auth.DialogFragmentController;
import com.wuman.android.auth.OAuthManager;
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore;

import java.io.IOException;
import java.util.Arrays;

public class OAuthHandler {

    private OAuthManager manager;

    public OAuthHandler( String id, String secret, final String callback, Context context, FragmentManager fragmentManager ) {

        CredentialStore credentialStore = buildCredentialStore( context );

        AuthorizationFlow flow = buildAuthorizationFlow( credentialStore, id, secret );

        AuthorizationUIController uiController = buildUIController( fragmentManager, callback );

        manager = new OAuthManager( flow, uiController );

    }

    private CredentialStore buildCredentialStore( Context context ) {
        return new SharedPreferencesCredentialStore( context, "credential.file", new JacksonFactory() );
    }

    private AuthorizationFlow buildAuthorizationFlow( CredentialStore credentialStore, String id, String secret ) {
        return new AuthorizationFlow
                .Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new GenericUrl( "https://appstore.cc.ncu.edu.tw/oauth/oauth/token" ),
                new ClientParametersAuthentication( id, secret ),
                id,
                "https://appstore.cc.ncu.edu.tw/oauth/oauth/authorize" )
                .setCredentialStore( credentialStore )
                .setScopes( Arrays.asList( "CLASS_READ" ) )
                .build();
    }

    private AuthorizationUIController buildUIController( FragmentManager fragmentManager, final String callback ) {
        return new DialogFragmentController( fragmentManager ) {
            @Override
            public boolean isJavascriptEnabledForWebView() {
                return true;
            }
            @Override
            public String getRedirectUri() throws IOException {
                return callback;
            }
        };
    }

    public OAuthManager.OAuthFuture< Credential > authorize( String userID,
                                                             OAuthManager.OAuthCallback< Credential > callback,
                                                             Handler handler ) {
        return manager.authorizeExplicitly( userID, callback, handler );
    }

    public OAuthManager.OAuthFuture< Boolean > deleteCredential( String userId,
                                                                 OAuthManager.OAuthCallback< Boolean > callback,
                                                                 Handler handler ) {
        return manager.deleteCredential( userId, callback, handler );
    }

}
