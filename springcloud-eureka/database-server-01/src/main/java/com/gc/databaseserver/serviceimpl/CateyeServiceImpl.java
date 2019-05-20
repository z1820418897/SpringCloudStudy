package com.gc.databaseserver.serviceimpl;

import com.gc.databaseserver.services.CateyeService;
import org.springframework.stereotype.Service;

@Service
public class CateyeServiceImpl implements CateyeService {

    @Override
    public String uploadVideo(Integer deviceId) {

        return "上传成功";
    }
}
