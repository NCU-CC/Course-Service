package tw.edu.ncu.cc.course.server.service

import tw.edu.ncu.cc.course.data.v1.Course


interface SearchService {

    Course[] search( String language, String departmentId, Integer week, String period, String keyword, Integer limit )
    Course[] findByDepartmentId( String language, String departmentId )
    Course[] findByTargetId( String language, String targetId )
    Course[] findBySummerStage( String language, String stage )
}
