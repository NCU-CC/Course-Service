package tw.edu.ncu.cc.course.server.service

import tw.edu.ncu.cc.course.data.v1.Status

interface StatusService {
    public Status findCurrentStatus( String language )
}