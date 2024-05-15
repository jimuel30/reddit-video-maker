package com.aparzero.videomaker.domain;

import lombok.Data;

@Data
public class VideoResource {

    public  VideoResource(final String imageUrl, final String audioUrl){
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }
    private String imageUrl;
    private String audioUrl;
}
