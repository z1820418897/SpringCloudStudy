package com.gc.databaseserver.services;

import org.springframework.web.bind.annotation.RequestParam;

public interface CateyeService {

    public String uploadVideo(@RequestParam(value = "upload-video")Integer deviceId);
}
