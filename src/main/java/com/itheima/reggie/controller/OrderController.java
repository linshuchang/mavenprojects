package com.itheima.reggie.controller;

import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    //还未写
    public static void main(String[] args) {
        System.out.println("3");
        System.out.println("2");
        System.out.println("push github");
    }
}


