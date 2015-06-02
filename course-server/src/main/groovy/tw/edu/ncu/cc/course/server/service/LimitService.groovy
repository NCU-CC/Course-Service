package tw.edu.ncu.cc.course.server.service

import tw.edu.ncu.cc.course.data.v1.Limit


interface LimitService {

    Limit findByCourseSerialNo( String language, String serialNo )

}