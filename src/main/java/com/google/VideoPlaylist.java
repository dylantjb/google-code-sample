package com.google;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to represent a Playlist
 */
class VideoPlaylist {
  String name;
  List<Video> videos;

  public VideoPlaylist(String name) {
    this.name = name;
    videos = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void addVideo(Video video) {
    videos.add(video);
  }

  public List<Video> getVideos() {
    return videos;
  }

  public boolean contains(Video video) {
    return videos.contains(video);
  }

  public boolean isEmpty() {
    return videos.isEmpty();
  }

  public void removeVideo(Video video) {
    videos.remove(video);
  }

  public void clear() {
    videos.clear();
  }

}
