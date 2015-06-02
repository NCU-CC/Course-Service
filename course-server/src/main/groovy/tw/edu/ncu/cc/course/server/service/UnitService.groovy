package tw.edu.ncu.cc.course.server.service

import tw.edu.ncu.cc.course.data.v1.Unit

interface UnitService {
    Unit[] findAllColleges( String language )
    Unit[] findAllDepartmentsByCollegeId( String language, String collegeId )
    Unit[] findAllTargetsByDepartmentId( String language, String departmentId )
}
