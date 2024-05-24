package com.aparzero.videomaker.repo;


import com.aparzero.videomaker.model.RedditVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedditVideoRepo extends JpaRepository<RedditVideo, Long> {
}
