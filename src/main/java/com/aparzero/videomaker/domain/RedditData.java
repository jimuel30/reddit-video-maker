package com.aparzero.videomaker.domain;

import lombok.Data;

import java.util.List;


@Data
public class RedditData {

    public RedditData(final String url,
                      final String name,
                      final String selfText,
                      final String id,
                      final String commentUrl,
                      final String title){
        this.url=url;
        this.name=name;
        this.selfText = selfText;
        this.id = id;
        this.commentUrl = commentUrl;
        this.title = title;

    }


    private String  url;
    private String name;
    private String selfText;
    private  String id;
    private String commentUrl;
    private String title;

}
