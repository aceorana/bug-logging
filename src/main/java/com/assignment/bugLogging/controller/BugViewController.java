package com.assignment.bugLogging.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller   // ✅ normal MVC controller
public class BugViewController {

    @Value("${api.base-url:http://localhost:8080}")
    private String apiBaseUrl;

    @GetMapping("/bugs")   // ✅ maps exactly GET /bugs
    public String showBugListPage(Model model) {
        model.addAttribute("apiBaseUrl", apiBaseUrl);
        // logical view name -> /WEB-INF/views/bug-list.jsp
        return "bug-list";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "BugViewController is working";
    }
}



