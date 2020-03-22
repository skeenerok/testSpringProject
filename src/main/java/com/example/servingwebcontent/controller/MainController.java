package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.Message;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repos.MessageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageRepos  messageRepos;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
    @GetMapping("/main")
    public  String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model){
        Iterable<Message> messages;
        if( filter != null && !filter.isEmpty()){
            messages = messageRepos.findByTag(filter);
        }else {
            messages = messageRepos.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }
    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model){
        messageRepos.save(new Message(text, tag, user));
        Iterable<Message> messages = messageRepos.findAll();
        model.put("messages", messages);
        return "main";
    }
    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model){
        Iterable<Message> messages;
        if( filter != null && !filter.isEmpty()){
            messages = messageRepos.findByTag(filter);
        }else {
            messages = messageRepos.findAll();
        }
        model.put("messages", messages);
        return "main";
    }
}
