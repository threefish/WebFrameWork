package com.web.action;

import com.sgaop.web.frame.server.mvc.annotation.WebAction;
import com.sgaop.web.frame.server.mvc.annotation.WebController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 30695 on 2016/5/8 0008.
 */
@WebController(path = "/testController")
public class TestController {

    @WebAction(path = "/3")
    public void testAction(HttpServletRequest request) {
        System.out.println("xxxx1");
    }

    @WebAction
    public void test() {
        System.out.println("test");
    }
}
