package tw.edu.ncu.cc.course.server.helper;

import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RestTemplateHelper {

    private static boolean romePresent =
            ClassUtils.isPresent( "com.rometools.rome.feed.WireFeed", RestTemplate.class.getClassLoader() );

    private static final boolean jaxb2Present =
            ClassUtils.isPresent( "javax.xml.bind.Binder", RestTemplate.class.getClassLoader() );

    private static final boolean jackson2Present =
            ClassUtils.isPresent( "com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader() ) &&
                    ClassUtils.isPresent( "com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader() );

    private static final boolean jackson2XmlPresent =
            ClassUtils.isPresent( "com.fasterxml.jackson.dataformat.xml.XmlMapper", RestTemplate.class.getClassLoader() );

    private static final boolean gsonPresent =
            ClassUtils.isPresent( "com.google.gson.Gson", RestTemplate.class.getClassLoader() );

    public static List< HttpMessageConverter< ? > > createConverters() {

        List< HttpMessageConverter< ? > > messageConverters = new ArrayList<>();

        messageConverters.add( new ByteArrayHttpMessageConverter() );

        messageConverters.add( prepareMessageConverter() );
        messageConverters.add( new ResourceHttpMessageConverter() );
        messageConverters.add( new SourceHttpMessageConverter<>() );
        messageConverters.add( new AllEncompassingFormHttpMessageConverter() );

        if ( romePresent ) {
            messageConverters.add( new AtomFeedHttpMessageConverter() );
            messageConverters.add( new RssChannelHttpMessageConverter() );
        }
        if ( jackson2XmlPresent ) {
            messageConverters.add( new MappingJackson2XmlHttpMessageConverter() );
        } else if ( jaxb2Present ) {
            messageConverters.add( new Jaxb2RootElementHttpMessageConverter() );
        }
        if ( jackson2Present ) {
            messageConverters.add( new MappingJackson2HttpMessageConverter() );
        } else if ( gsonPresent ) {
            messageConverters.add( new GsonHttpMessageConverter() );
        }
        return messageConverters;
    }

    private static StringHttpMessageConverter prepareMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter( Charset.forName( "UTF-8" ) );
        List<MediaType> supportMedias = new LinkedList<>();
        supportMedias.add( new MediaType( "text", "plain", Charset.forName( "UTF-8" ) ) );
        supportMedias.add( new MediaType( "test", "html", Charset.forName( "UTF-8" ) ) );
        supportMedias.add( new MediaType( "application", "json", Charset.forName( "UTF-8" ) ) );
        converter.setSupportedMediaTypes( supportMedias );
        return converter;
    }


}
