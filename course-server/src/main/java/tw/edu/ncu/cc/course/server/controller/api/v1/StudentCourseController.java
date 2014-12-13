package tw.edu.ncu.cc.course.server.controller.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tw.edu.ncu.cc.course.data.Course;

@RestController
@RequestMapping( value = "api/v1/student" )
public class StudentCourseController {

    @RequestMapping( value = "{studentID}/selected", method = RequestMethod.GET )
    public ResponseEntity searchSelectedCourse( @PathVariable( value = "studentID" ) String studentID ) {
        Course course = new Course();
        course.setSerialNo( 12034 );
        course.setNo( "EL5001" );
        course.setClassNo( "*" );
        course.setName( "文學/文化理論導讀" );
        course.setClosed( false );
        course.setMemo( "限三、四年級" );
        course.setMasterDoctor( false );
        course.setLanguage( "Chinese" );
        course.setPasswordCard( Course.PasswordCard.no );
        course.setFirstRun( true );
        course.setPreSelect( true );
        course.setTeachers( "錢夫人,阿土伯" );
        course.setCredit( 2 );
        course.setClassRooms( "C2-209,C2-209" );
        course.setTime( "0-5,2-34" );
        course.setType( Course.RequireType.required );
        course.setFullHalf( Course.FullHalf.half );
        course.setMaxStudents( 0 );

        return new ResponseEntity<>( new Course[]{ course }, HttpStatus.OK );
    }

    @RequestMapping( value = "{studentID}/tracking", method = RequestMethod.GET )
    public ResponseEntity searchTrackedCourse( @PathVariable( value = "studentID" ) String studentID ) {
        Course course = new Course();
        course.setSerialNo( 12034 );
        course.setNo( "EL5001" );
        course.setClassNo( "*" );
        course.setName( "文學/文化理論導讀" );
        course.setClosed( false );
        course.setMemo( "限三、四年級" );
        course.setMasterDoctor( false );
        course.setLanguage( "Chinese" );
        course.setPasswordCard( Course.PasswordCard.no );
        course.setFirstRun( true );
        course.setPreSelect( true );
        course.setTeachers( "錢夫人,阿土伯" );
        course.setCredit( 2 );
        course.setClassRooms( "C2-209,C2-209" );
        course.setTime( "0-5,2-34" );
        course.setType( Course.RequireType.required );
        course.setFullHalf( Course.FullHalf.half );
        course.setMaxStudents( 0 );

        return new ResponseEntity<>( new Course[]{ course }, HttpStatus.OK );
    }

}
