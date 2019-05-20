package com.gc.consumerserver.services.hystrix;

import com.gc.bean.FileInfo;
import com.gc.bean.GroupFile;
import com.gc.consumerserver.services.CateyeService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CatServiceHystrix implements CateyeService {

    @Override
    public Integer redisAddFileInfo(Integer key, String value) {
        System.out.println("redisAddFileInfo血崩了");
        return 0;
    }

    @Override
    public Integer insertVisitor(String key, FileInfo value) {
        return null;
    }

    @Override
    public Set<FileInfo> findVisitorUrl(String key, Long startDate, Long overDate) {
        return null;
    }

    @Override
    public Integer insertWarning(String key, GroupFile value) {
        return null;
    }

    @Override
    public Set<GroupFile> findWarningUrl(String toString, Long startDate, Long overDate) {
        return null;
    }

    @Override
    public Integer findExistDate(Integer deviceId) {
        return null;
    }
}
