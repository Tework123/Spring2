package springApp2.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BlogController {
    @Value("${db}")
    private String db;

    @GetMapping()
    public String index() {


        System.out.println(db);
        return "blog/index";

    }
}
