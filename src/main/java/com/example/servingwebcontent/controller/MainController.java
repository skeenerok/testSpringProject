package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.Message;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repos.MessageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepos  messageRepos;

    @Value("${upload.path}")
    private String uploadPath;
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
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam(value = "file") MultipartFile file) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()){
            Map<String, String> errorsMaps = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMaps);
            model.addAttribute("message", message);
        }else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                System.out.println(file.getOriginalFilename());
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String fileName = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + fileName));
                message.setFilename(fileName);
            }
            model.addAttribute("message", null);
            messageRepos.save(message);
        }
        Iterable<Message> messages = messageRepos.findAll();
        model.addAttribute("messages", messages);
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
