package com.aparzero.videomaker.model;


import com.aparzero.videomaker.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name="reddit_video")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RedditVideo {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long videoId;

    private Status status;

    private Date dateRequested;

    private String url;



}
