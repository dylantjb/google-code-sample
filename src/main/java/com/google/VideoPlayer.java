package com.google;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video current;
  private boolean paused;

  public VideoPlayer() {
    this.paused = false;
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    List<Video> videos = videoLibrary.getVideos();
    Comparator<Video> compareByTitle = Comparator.comparing(Video::getTitle);
    videos.sort(compareByTitle);

    System.out.println("Here's a list of all available videos:");
    for (Video video : videos)
      System.out.printf("%s (%s) [%s]%n", video.getTitle(), video.getVideoId(), String.join(" ", video.getTags()));
  }

  public void playVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video != null) {
      if (current != null)
        stopVideo();
      System.out.printf("Playing video: %s%n", video.getTitle());
      current = video;
      paused = false;
    } else {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if (this.current != null)
      System.out.printf("Stopping video: %s%n", current.getTitle());
    else
      System.out.println("Cannot stop video: No video is currently playing");
    current = null;
  }

  public void playRandomVideo() {
    int index = new Random().nextInt(videoLibrary.getVideos().size());
    playVideo(videoLibrary.getVideos().get(index).getVideoId());
  }

  public void pauseVideo() {
    if (current == null)
      System.out.println("Cannot pause video: No video is currently playing");
    else if (paused)
      System.out.printf("Video already paused: %s%n", current.getTitle());
    else {
      System.out.printf("Pausing video: %s%n", current.getTitle());
      paused = true;
    }
  }

  public void continueVideo() {
    if (current == null)
      System.out.println("Cannot continue video: No video is currently playing");
    else if (!paused)
      System.out.println("Cannot continue video: Video is not paused");
    else {
      System.out.printf("Continuing video: %s%n", current.getTitle());
      paused = true;
    }
  }

  public void showPlaying() {
    if (current != null) {
      String extension = (paused) ? " - PAUSED" : "";
      System.out.printf("Currently playing: %s (%s) [%s]%s%n", current.getTitle(), current.getVideoId(), String.join(" ", current.getTags()), extension);
    } else
      System.out.println("No video is currently playing");
  }

  public void createPlaylist(String playlistName) {
    System.out.println("createPlaylist needs implementation");
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    System.out.println("addVideoToPlaylist needs implementation");
  }

  public void showAllPlaylists() {
    System.out.println("showAllPlaylists needs implementation");
  }

  public void showPlaylist(String playlistName) {
    System.out.println("showPlaylist needs implementation");
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    System.out.println("removeFromPlaylist needs implementation");
  }

  public void clearPlaylist(String playlistName) {
    System.out.println("clearPlaylist needs implementation");
  }

  public void deletePlaylist(String playlistName) {
    System.out.println("deletePlaylist needs implementation");
  }

  public void searchVideos(String searchTerm) {
    System.out.println("searchVideos needs implementation");
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}

// TODO: Find out if we can replace current with the actual video instead of the videoId