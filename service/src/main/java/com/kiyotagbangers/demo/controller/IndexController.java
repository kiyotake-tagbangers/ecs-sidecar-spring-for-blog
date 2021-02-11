package com.kiyotagbangers.demo.controller;

import com.kiyotagbangers.demo.MyData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KIYOTA, Takeshi
 */
@RestController
@RequestMapping({"/", "/index"})
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MyData myData;

    public IndexController(MyData myData) {
        this.myData = myData;
    }

    @GetMapping
    public MyData index() {
        logger.info("index");
        return this.myData;
    }
}
