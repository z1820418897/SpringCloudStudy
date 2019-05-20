package com.gc.consumerserver.services;

import com.gc.bean.FileInfo;
import com.gc.bean.GroupFile;
import com.gc.consumerserver.services.hystrix.CatServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(value = "DATABASE-SERVER-01",fallback = CatServiceHystrix.class )
public interface CateyeService {

    @RequestMapping(value="redis-add-file-info",method = RequestMethod.POST)
    public Integer redisAddFileInfo(@RequestParam(value = "key")Integer key,@RequestParam(value="value") String value);

    @RequestMapping(value="insert-visitor",method = RequestMethod.POST)
    public Integer insertVisitor(@RequestParam(value = "key")String key,@RequestParam(value="value") FileInfo value);

    @RequestMapping(value="find-visitor",method = RequestMethod.POST)
    public  Set<FileInfo> findVisitorUrl(@RequestParam(value = "key") String key, @RequestParam(value = "startDate") Long startDate, @RequestParam(value = "overDate") Long overDate);

    @RequestMapping(value="insert-warning",method = RequestMethod.POST)
    public Integer insertWarning(@RequestParam(value = "key") String key, @RequestParam(value = "value") GroupFile value);

    @RequestMapping(value="find-warning",method = RequestMethod.POST)
    public Set<GroupFile> findWarningUrl(@RequestParam(value = "key")String key,@RequestParam(value = "startDate") Long startDate,@RequestParam(value = "overDate") Long overDate);



    @RequestMapping(value="find-exist-warning-day",method = RequestMethod.POST)
    public Set<Long> findExistWarningDay(@RequestParam(value = "key")String key, @RequestParam(value = "startDate") Long startDate, @RequestParam(value = "overDate") Long overDate);

    @RequestMapping(value="find-exist-visitor-day",method = RequestMethod.POST)
    public Set<Long> findExistVisitorDay(@RequestParam(value = "key")String key,@RequestParam(value = "startDate") Long startDate, @RequestParam(value = "overDate") Long overDate);

    @RequestMapping(value="delete-warning-data",method = RequestMethod.POST)
    public Integer deleteWarningData(@RequestParam(value = "key") String key,@RequestParam(value = "date")Long[] date);

    @RequestMapping(value="delete-visitor-data",method = RequestMethod.POST)
    public Integer deleteVisitorData(@RequestParam(value = "key") String key,@RequestParam(value = "date")Long[] date);
}
