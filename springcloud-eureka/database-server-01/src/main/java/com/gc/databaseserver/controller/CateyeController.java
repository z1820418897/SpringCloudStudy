package com.gc.databaseserver.controller;

import com.gc.bean.FileInfo;
import com.gc.bean.GroupFile;
import com.gc.databaseserver.services.CateyeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
public class CateyeController {

    @Autowired
    private CateyeService cateyeService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "upload-video", method = RequestMethod.GET)
    public String uploadVideo(@RequestParam Integer deviceId) {
        return cateyeService.uploadVideo(deviceId);
    }

    @PostMapping(value = "redis-add-file-info")
    public Integer redisAddFileInfo(@RequestParam String key, @RequestParam String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }


    //-------------------------------------------------------------------------------------------------------
    @PostMapping(value = "insert-visitor")
    public Integer insertVisitor(@RequestParam String key, @RequestParam FileInfo value) {
        try {
            redisTemplate.opsForZSet().add(key + "-visitor", value, new Date().getTime());

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @PostMapping(value = "find-visitor")
    public Set findVisitorUrl(@RequestParam String key, @RequestParam Long startDate, @RequestParam Long overDate) {
        Set<FileInfo> set = null;
        try {
            set = redisTemplate.opsForZSet().rangeByScore(key + "-visitor", startDate, overDate);
        } catch (Exception e) {
            e.printStackTrace();
            return set;
        }
        return set;
    }

    @PostMapping(value = "insert-warning")
    public Integer insertWarning(@RequestParam String key, @RequestParam GroupFile value) {
        try {
            redisTemplate.opsForZSet().add(key + "-warning", value, value.getDate());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @PostMapping(value = "find-warning")
    public Set findWarningUrl(@RequestParam String key, @RequestParam Long startDate, @RequestParam Long overDate) {
        Set<GroupFile> set = null;
        try {
            set = redisTemplate.opsForZSet().rangeByScore(key + "-visitor", startDate, overDate);
        } catch (Exception e) {
            e.printStackTrace();
            return set;
        }
        return set;
    }

    @PostMapping(value = "find-exist-warning-day")
    public Set<Long> findExistWarningDay(@RequestParam(value = "key") String key, @RequestParam(value = "startDate") Long startDate, @RequestParam(value = "overDate") Long overDate) {

        Set<Long> set = new HashSet<>();
        Set<ZSetOperations.TypedTuple> data = redisTemplate.opsForZSet().rangeWithScores(key + "-warning", startDate, overDate);

        for (ZSetOperations.TypedTuple score : data) {
            Double score1 = score.getScore();
            set.add(Math.round(score1));
        }

        return set;
    }


    @PostMapping(value = "find-exist-visitor-day")
    public Set<Long> findExistVisitorDay(@RequestParam(value = "key") String key, @RequestParam(value = "startDate") Long startDate, @RequestParam(value = "overDate") Long overDate) {

        Set<Long> set = new HashSet<>();
        Set<ZSetOperations.TypedTuple> data = redisTemplate.opsForZSet().rangeWithScores(key + "-visitor", startDate, overDate);

        for (ZSetOperations.TypedTuple score : data) {
            Double score1 = score.getScore();
            set.add(Math.round(score1));
        }

        return set;
    }

    @PostMapping(value = "deleteWarningData")
    public Integer deleteWarningData(@RequestParam(value = "key") String key, @RequestParam(value = "date") Long[] date) {

        int sum = 0;
        for (Long score : date) {
            Long aLong = redisTemplate.opsForZSet().removeRangeByScore(key+"-warning", score, score);
            sum++;
        }
        return sum;
    }

    @PostMapping(value = "deleteVisitorData")
    public Integer deleteVisitorData(@RequestParam(value = "key") String key, @RequestParam(value = "date") Long[] date) {

        int sum = 0;
        for (Long score : date) {
            Long aLong = redisTemplate.opsForZSet().removeRangeByScore(key+"-visitor", score, score);
            sum++;
        }
        return sum;
    }

}
