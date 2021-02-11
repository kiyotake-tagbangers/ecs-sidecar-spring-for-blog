package com.kiyotagbangers.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author KIYOTA, Takeshi
 */
@Component
@Profile("default")
public class MyDataImpl implements MyData {

    @Value("${message.value}")
    public String message;
}
