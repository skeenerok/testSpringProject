package com.example.servingwebcontent.repos;

import com.example.servingwebcontent.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepos extends CrudRepository<Message, Long> {

    Page<Message> findByTag(String tag, Pageable pageable);
    Page<Message> findAll(Pageable pageable);

}