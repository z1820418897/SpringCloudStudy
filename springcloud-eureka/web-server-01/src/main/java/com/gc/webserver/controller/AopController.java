package com.gc.webserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/AopTest")
public class AopController {

    @GetMapping(value = "/user/{id}")
    public String getUserById(@PathVariable String id){

        return " 你好世界，我是"+id+"号梦想";
    }

}
