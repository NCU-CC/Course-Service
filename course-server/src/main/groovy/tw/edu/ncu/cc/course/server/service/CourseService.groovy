package tw.edu.ncu.cc.course.server.service

import tw.edu.ncu.cc.course.data.v1.Course

interface CourseService {

    Course findBySerialNo( String language, String serialNo )
}
