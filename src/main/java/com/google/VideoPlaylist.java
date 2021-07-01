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

  String getName() {
    return name;
  }

  void addVideo(Video video) {
    videos.add(video);
  }

  List<Video> getVideos() {
    return videos;
  }

  boolean contains(Video video) {
    return videos.contains(video);
  }

  boolean isEmpty() {
    return videos.isEmpty();
  }

  void removeVideo(Video video) {
    videos.remove(video);
  }

  void clear() {
    videos.clear();
  }

}
