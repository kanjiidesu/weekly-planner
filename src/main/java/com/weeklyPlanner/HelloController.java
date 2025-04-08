package com.weeklyPlanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    // http://localhost:8080/hello
    // website for going in
    public String helloWorld() {
        return "TEEEEEST";
    }
}
