package com.gc.consumerserver.controller;

import com.gc.bean.DateUtil;
import com.gc.bean.FileInfo;
import com.gc.bean.GroupFile;
import com.gc.code.ErrorCode;
import com.gc.consumerserver.services.CateyeService;
import com.gc.util.DateFormat;
import com.gc.util.Message;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * @Data 2019-4-28 11:21
 * @Name 郑华晨
 */

@RestController
public class CateyeController {
    @Autowired
    private CateyeService cateyeService;

    @Value("${fileHttpAddr}")
    String fileHttpAddr;

    @Autowired
    private FastFileStorageClient storageClient;

//    @Autowired
//    private ThumbImageConfig thumbImageConfig;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String uploadVideo(@RequestParam Integer deviceId) {

        //File file = new File("C:\\Users\\admin\\Desktop\\a.txt");
        /*String cFileName = file.getOriginalFilename();
        String last = cFileName.substring(cFileName.lastIndexOf(".") + 1);
        System.out.println("last" + last);*/

        try {
            // StorePath txt = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), last, null);
            // System.out.println("txt-----" + txt.getFullPath());

            Integer insertFileInfoToRedisIsTrueOrFalse = cateyeService.redisAddFileInfo(deviceId, "你好");
            if (insertFileInfoToRedisIsTrueOrFalse == 1) {
                return "上传成功-http://" + fileHttpAddr + "/" + "你好";
            } else {
                return "上传失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
    }

    /**
     * 访客图片上传
     *
     * @param file     图片文件
     * @param deviceId 设备id
     */
    @PostMapping(value = "upload-visitor")
    public Message uploadVisitor(@RequestParam Integer deviceId, @RequestParam MultipartFile file) {
        Message message = new Message();
        message.setData("");
        String cFileName = file.getOriginalFilename();
        String last = cFileName.substring(cFileName.lastIndexOf(".") + 1);
        StorePath uploadResult = null;

        try {
            uploadResult = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), last, null);
        } catch (Exception e) {
            e.printStackTrace();
            message.setType(ErrorCode.CODE_UPLOAD_VISITOR.getValue());
            return message;
        }
        FileInfo fileInfo = new FileInfo();

        fileInfo.setDate(new Date().getTime());
        fileInfo.setFileUrl(fileHttpAddr + uploadResult.getFullPath());

        Integer saveVisitoryIsTrueOrFalse = cateyeService.insertVisitor(deviceId.toString(), fileInfo);

        if (saveVisitoryIsTrueOrFalse == 1) {
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
            message.setMsg("上传成功");
        } else {
            message.setType(ErrorCode.CODE_INSERT_REDIS_VISITOR.getValue());
            message.setMsg("上传失败");
        }
        return message;
    }

    /**
     * 访客图片下载地址查询
     *
     * @param deviceId 设备id
     * @param date     时间
     */
    @PostMapping(value = "find-visitor-url")
    public Message findVisitorUrl(@RequestParam Integer deviceId, @RequestParam String date) {
        Message message = new Message();
        Long startDate = 0L, overDate = 0L;

        try {
//            startDate = DateFormat.str_to_long(date);
//            overDate = DateFormat.str_to_Long_day(date, 1);

            DateUtil dateUtil = DateFormat.JudgeByD(date);
            startDate=dateUtil.getStartDate();
            overDate=dateUtil.getOverDate();
        } catch (Exception e) {
            message.setData("");
            message.setMsg("find-visitor-url日期格式化错误");
            message.setType(ErrorCode.CODE_DATE_FORMAT.getValue());
            return message;
        }

        Set<FileInfo> visitorUrl = cateyeService.findVisitorUrl(deviceId.toString(), startDate, overDate);
        if (visitorUrl != null) {
            message.setData(visitorUrl);
            message.setMsg("查询成功");
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
            return message;
        } else {
            message.setData("");
            message.setMsg("查询失败");
            message.setType(ErrorCode.CODE_FIND_REDIS_VISITOR_URL.getValue());
            return message;
        }

    }



   /* @RequestMapping(value = "find-visitor-exist-date")
    public Message downloadVisitor(@RequestParam Integer deviceId){
        cateyeService.findExistDate(deviceId);


    }*/


    /**
     * 警报消息
     *
     * @param deviceId 设备id
     * @param files    文件数组
     * @param type     文件类型 1 图片 2 视频
     */

    @PostMapping(value = "upload-warning")
    public Message uploadWarning(@RequestParam Long deviceId, @RequestParam MultipartFile[] files, @RequestParam Integer type) {
        Message message = new Message();

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {

            String cFileName = file.getOriginalFilename();
            String last = cFileName.substring(cFileName.lastIndexOf(".") + 1);
            StorePath uploadResult = null;
            try {
                uploadResult = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), last, null);
                urls.add(fileHttpAddr + uploadResult.getFullPath());
            } catch (Exception e) {
                e.printStackTrace();
                message.setType(ErrorCode.CODE_UPLOAD_WARNING.getValue());
                return message;
            }

        }

        GroupFile groupFile = new GroupFile();

        groupFile.setDate(new Date().getTime());
        groupFile.setFiles(urls);
        groupFile.setType(type);

        Integer upWarningIsTrueOrFalse = cateyeService.insertWarning(deviceId.toString(),groupFile);
        if (upWarningIsTrueOrFalse == 1) {
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
            message.setMsg("上传成功");
            message.setData("");
            return message;

        } else {
            message.setType(ErrorCode.CODE_INSERT_REDIS_WARNING.getValue());
            message.setData("");
            message.setMsg("上传失败");

            return message;
        }

    }


    /**
     * 查询警报消息
     *
     * @param deviceId 设备id
     * @param date     开始时间
     */
    @PostMapping(value = "find-warning-url")
    public Message findWarningUrl(@RequestParam Long deviceId, @RequestParam String date) {
        Message message = new Message();
        Long startDate = 0L, overDate = 0L;

        try {

            DateUtil dateUtil = DateFormat.JudgeByD(date);
            startDate=dateUtil.getStartDate();
            overDate=dateUtil.getOverDate();

        } catch (Exception e) {
            message.setData("");
            message.setMsg("find-warning-url日期格式化错误");
            message.setType(ErrorCode.CODE_DATE_FORMAT.getValue());
            return message;
        }

        Set<GroupFile> warningUrl = cateyeService.findWarningUrl(deviceId.toString(), startDate, overDate);

        if (warningUrl != null) {
            message.setData(warningUrl);
            message.setMsg("查询成功");
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
            return message;
        } else {
            message.setData("");
            message.setMsg("查询失败");
            message.setType(ErrorCode.CODE_FIND_REDIS_WARNING_URL.getValue());
            return message;
        }
    }

    /**
     * 查询警报消息条数
     */
    @PostMapping(value = "find-warning-count")
    public Message findWarningCount(@RequestParam Long deviceId, @RequestParam String date) {
        Message message = new Message();
        Long startDate = 0L, overDate = 0L;

        try {

            DateUtil dateUtil = DateFormat.JudgeByD(date);
            startDate=dateUtil.getStartDate();
            overDate=dateUtil.getOverDate();

        } catch (Exception e) {
            message.setData("");
            message.setMsg("find-warning-count日期格式化错误");
            message.setType(ErrorCode.CODE_DATE_FORMAT.getValue());
            return message;
        }
        Set<GroupFile> warning = cateyeService.findWarningUrl(deviceId.toString(), startDate, overDate);

        if (warning != null) {
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
            message.setData(warning.size());
            message.setMsg("查询成功");
        } else {
            message.setType(ErrorCode.CODE_FIND_REDIS_WARNING_COUNT.getValue());
            message.setData(0);
            message.setMsg("查询redis失败");
        }
        return message;
    }

    /**
     * 查询警报消息条数
     */
    @PostMapping(value = "find-visitor-count")
    public Message findVisitorCount(@RequestParam Long deviceId, @RequestParam String date) {
        Message message = new Message();
        Long startDate = 0L, overDate = 0L;

        try {
            DateUtil dateUtil = DateFormat.JudgeByD(date);
            startDate=dateUtil.getStartDate();
            overDate=dateUtil.getOverDate();
        } catch (Exception e) {
            message.setData("");
            message.setMsg("find-visitor-count日期格式化错误");
            message.setType(ErrorCode.CODE_DATE_FORMAT.getValue());
            return message;
        }

        Set<FileInfo> visitor = cateyeService.findVisitorUrl(deviceId.toString(), startDate, overDate);

        if (visitor != null) {
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
            message.setData(visitor.size());
            message.setMsg("查询成功");
        } else {
            message.setType(ErrorCode.CODE_FIND_REDIS_VISITOR_COUNT.getValue());
            message.setData(0);
            message.setMsg("查询redis失败");

        }
        return message;
    }

    /**
     * 按照月份查询存在警报的day
     *
     * @param type 查询的类型 1 警报消息 2 访客消息
     */
    @PostMapping(value = "find-exist-day")
    public Message findExistDay(@RequestParam Long deviceId, @RequestParam Integer type, @RequestParam String date) {
        Message message = new Message();
        Set<Long> existDay = null;
        Long startDate = 0L, overDate = 0L;

        try {

            DateUtil dateUtil = DateFormat.JudgeByM(date);
            startDate=dateUtil.getStartDate();
            overDate=dateUtil.getOverDate();

        } catch (Exception e) {
            message.setData("");
            message.setMsg("find-exist-day日期格式化错误");
            message.setType(ErrorCode.CODE_DATE_FORMAT.getValue());
            return message;
        }


        if (type == 1) {
            existDay = cateyeService.findExistWarningDay(deviceId.toString(), startDate, overDate);
        } else if (type == 2) {
            existDay = cateyeService.findExistVisitorDay(deviceId.toString(), startDate, overDate);
        }

        if (existDay != null) {
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
            message.setData(existDay);
            message.setMsg("查询成功");
        } else {
            message.setType(ErrorCode.CODE_FIND_REDIS_EXITS_DAY.getValue());
            message.setData("");
            message.setMsg("查询redis错误");
        }

        return message;
    }

    @PostMapping(value = "delete-data")
    public Message deleteData(@RequestParam Long deviceId, @RequestParam Integer type, @RequestParam String urls, @RequestParam String dates) {
        Message message = new Message();

        String[] url = urls.split(",");
        for (int i = 0; i < url.length; i++) {
            try {
                storageClient.deleteFile(url[i]);
            } catch (Exception e) {
                continue;
            }
        }

        String[] split = dates.split(",");
        Long[] aLong = null;
        try {

            for (int i = 0; i < split.length; i++) {
                aLong[i] = DateFormat.str_to_long(split[i]);
            }

        } catch (Exception e) {
            message.setData("");
            message.setMsg("delete-data日期格式化错误");
            message.setType(ErrorCode.CODE_DATE_FORMAT.getValue());
            return message;
        }

        Integer size = null;
        if (type == 1) {
            size=cateyeService.deleteWarningData(deviceId.toString(),aLong);
        } else if (type == 2) {
            size=cateyeService.deleteVisitorData(deviceId.toString(),aLong);
        }else{
            message.setMsg("类型错误");
        }

        if (size != null) {
            message.setMsg("删除成功");
            message.setData("");
            message.setType(ErrorCode.CODE_SUCEESS.getValue());
        } else {
            message.setMsg("删除失败，请重试");
            message.setData("");
            message.setType(ErrorCode.CODE_FIND_REDIS_DELETE.getValue());
        }


        return message;
    }


}
