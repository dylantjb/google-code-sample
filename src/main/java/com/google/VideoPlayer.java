package com.google;

import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final List<VideoPlaylist> playlists;
  private final VideoLibrary videoLibrary;
  private Video current;
  private boolean paused;

  public VideoPlayer() {
    this.paused = false;
    this.playlists = new ArrayList<>();
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    List<Video> videos = videoLibrary.getVideos();
    sortByTitle(videos);

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
    } else
      System.out.println("Cannot play video: Video does not exist");
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
    if (!playlists.stream().map(VideoPlaylist::getName).map(String::toLowerCase).collect(Collectors.toList()).contains(playlistName.toLowerCase())) {
      playlists.add(new VideoPlaylist(playlistName));
      System.out.printf("Successfully created new playlist: %s%n", playlistName);
    } else
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    boolean found = false;
    boolean exists = true;

    for (VideoPlaylist playlist : playlists) {
      if (playlist.getName().equalsIgnoreCase(playlistName)) {
        Video video = videoLibrary.getVideo(videoId);
        if (video == null) {
          System.out.printf("Cannot add video to %s: Video does not exist%n", playlistName);
          exists = false;
          break;
        }
        if (!playlist.contains(video)) {
          playlist.addVideo(video);
          System.out.printf("Added video to %s: %s%n", playlistName, video.getTitle());
          found = true;
        } else {
          System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
          exists = false;
        }
      }
    }
    if (!found && exists || playlists.isEmpty())
      System.out.printf("Cannot add video to %s: Playlist does not exist%n", playlistName);
  }

  public void showAllPlaylists() {
    if (!playlists.isEmpty()) {
      Comparator<VideoPlaylist> compareByTitle = Comparator.comparing(VideoPlaylist::getName);
      playlists.sort(compareByTitle);
      System.out.println("Showing all playlists:");
      for (VideoPlaylist playlist : playlists)
        System.out.println(playlist.getName());
    } else
      System.out.println("No playlists exist yet");
  }

  public void showPlaylist(String playlistName) {
    boolean found = false;
    for (VideoPlaylist playlist : playlists) {
      if (playlist.getName().equalsIgnoreCase(playlistName)) {
        System.out.printf("Showing playlist: %s%n", playlistName);
        if (!playlist.isEmpty()) {
          for (Video video : playlist.getVideos())
            System.out.printf("%s (%s) [%s]%n", video.getTitle(), video.getVideoId(), String.join(" ", video.getTags()));
        } else
          System.out.println("No videos here yet");
        found = true;
      }
    }
    if (!found)
      System.out.printf("Cannot show playlist %s: Playlist does not exist", playlistName);
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    boolean found = false;
    boolean exists = true;

    for (VideoPlaylist playlist : playlists) {
      if (playlist.getName().equalsIgnoreCase(playlistName)) {
        Video video = videoLibrary.getVideo(videoId);
        if (video == null) {
          System.out.printf("Cannot remove video from %s: Video does not exist%n", playlistName);
          exists = false;
          break;
        }
        if (playlist.contains(video)) {
          playlist.removeVideo(video);
          System.out.printf("Removed video from %s: %s%n", playlistName, video.getTitle());
          found = true;
        } else {
          System.out.printf("Cannot remove video from %s: Video is not in playlist%n", playlistName);
          exists = false;
        }
      }
    }
    if (!found && exists || playlists.isEmpty())
      System.out.printf("Cannot remove video from %s: Playlist does not exist%n", playlistName);
  }

  public void clearPlaylist(String playlistName) {
    boolean found = false;
    if (!playlists.isEmpty()) {
      for (VideoPlaylist playlist : playlists) {
        if (playlist.getName().equalsIgnoreCase(playlistName)) {
          playlist.clear();
          System.out.printf("Successfully removed all videos from %s%n", playlistName);
          found = true;
        }
      }
    }
    if (!found)
      System.out.printf("Cannot clear playlist %s: Playlist does not exist%n", playlistName);
  }

  public void deletePlaylist(String playlistName) {
    boolean found = false;
    for (VideoPlaylist playlist : playlists) {
      if (playlist.getName().equalsIgnoreCase(playlistName)) {
        System.out.printf("Deleted playlist: %s%n", playlistName);
        found = true;
      }
    }
    if (!found)
      System.out.printf("Cannot delete playlist %s: Playlist does not exist%n", playlistName);
  }

  public void searchVideos(String searchTerm) {
    List<Video> matches = new ArrayList<>();
    for (Video video : videoLibrary.getVideos()) {
      String title = video.getTitle().toLowerCase();
      if (title.contains(searchTerm.toLowerCase()))
        matches.add(video);
    }

    if (!matches.isEmpty()) {
      System.out.printf("Here are the results for %s:%n", searchTerm);
      sortByTitle(matches);
      int count = 1;
      for (Video match : matches) {
        System.out.printf("%d) %s (%s) [%s]%n", count, match.getTitle(), match.getVideoId(), String.join(" ", match.getTags()));
        count++;
      }

      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      Scanner in = new Scanner(System.in);

      if (in.hasNextInt()) {
        int choice = in.nextInt();
        if (choice <= matches.size() && choice >= 1)
          playVideo(matches.get(choice - 1).getVideoId());
      }

    } else
      System.out.printf("No search results for %s%n", searchTerm);
  }

  public void searchVideosWithTag(String videoTag) {
    if (videoTag.startsWith("#")) {
      List<Video> matches = new ArrayList<>();
      for (Video video : videoLibrary.getVideos()) {
        for (String tag : video.getTags()) {
          if (tag.toLowerCase().contains(videoTag.toLowerCase()))
            matches.add(video);
        }
      }
      if (!matches.isEmpty()) {
        System.out.printf("Here are the results for %s:%n", videoTag);
        sortByTitle(matches);
        int count = 1;
        for (Video match : matches) {
          System.out.printf("%d) %s (%s) [%s]%n", count, match.getTitle(), match.getVideoId(), String.join(" ", match.getTags()));
          count++;
        }

        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
        System.out.println("If your answer is not a valid number, we will assume it's a no.");
        Scanner in = new Scanner(System.in);

        if (in.hasNextInt()) {
          int choice = in.nextInt();
          if (choice <= matches.size() && choice >= 1)
            playVideo(matches.get(choice - 1).getVideoId());
        }

      } else
        System.out.printf("No search results for %s%n", videoTag);


    } else
      System.out.printf("No search results for %s%n", videoTag);
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

  // helper method(s)
  private void sortByTitle(List<Video> videos) {
    Comparator<Video> compareByTitle = Comparator.comparing(Video::getTitle);
    videos.sort(compareByTitle);
  }
}
