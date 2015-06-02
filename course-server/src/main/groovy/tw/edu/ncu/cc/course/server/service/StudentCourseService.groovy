package tw.edu.ncu.cc.course.server.service;

import tw.edu.ncu.cc.course.data.v1.Course;

public interface StudentCourseService {

    Course[] readSelectedCourses( String studentId, String language )
    Course[] readTrackedCourses( String studentId, String language )
    Course[] readRejectedCourses( String studentId, String language )

}
