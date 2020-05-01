package com.example.servingwebcontent.domain.dto;

import com.example.servingwebcontent.domain.Message;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.util.MessageHelper;

public class MessageDto {
    private Long id;
    private String text;
    private String filename;
    private User author;
    private String tag;
    private Long likes;
    private Boolean meLikes;

    public MessageDto(Message message, Long likes, Boolean meLikes) {
        this.id = message.getId();
        this.text = message.getText();
        this.filename = message.getFilename();
        this.author = message.getAuthor();
        this.tag = message.getTag();
        this.likes = likes;
        this.meLikes = meLikes;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getFilename() {
        return filename;
    }

    public User getAuthor() {
        return author;
    }

    public String getTag() {
        return tag;
    }

    public Long getLikes() {
        return likes;
    }

    public Boolean getMeLikes() {
        return meLikes;
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", author=" + author +
                ", likes=" + likes +
                ", meLikes=" + meLikes +
                '}';
    }
}
