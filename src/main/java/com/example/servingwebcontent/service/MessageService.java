package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.Message;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.dto.MessageDto;
import com.example.servingwebcontent.repos.MessageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class MessageService {
    @Autowired
    private MessageRepos messageRepos;
    @Autowired
    private EntityManager em;

    public Page<MessageDto> messageList(Pageable pageable, String filter, User user){
        Page<MessageDto> page;
        if (filter != null && !filter.isEmpty()) {
            page = messageRepos.findByTag(filter, pageable, user);
        } else {
            page = messageRepos.findAll(pageable, user);
        }
        return page;
    }

    public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepos.findByUser(pageable, author, currentUser);
    }
}
