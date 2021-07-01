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
    showDescription(videos);
  }

  public void playVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video != null) {
      if (video.getFlag() == null) {
        if (current != null)
          stopVideo();
        System.out.printf("Playing video: %s%n", video.getTitle());
        current = video;
        paused = false;
      } else
        System.out.printf("Cannot play video: Video is currently flagged (reason: %s)%n", video.getFlag());
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
    List<Video> videos = videoLibrary.getVideos();
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < videos.size(); i++)
      indices.add(i);
    Collections.shuffle(indices);

    int index = 0;
    boolean success = false;
    while (!success && index < indices.size()) {
      Video video = videos.get(index);
      if (video.getFlag() == null) {
        playVideo(video.getVideoId());
        success = true;
      }
      index++;
    }

    if (!success)
      System.out.println("No videos available");
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
    String message = "";
    Video video = videoLibrary.getVideo(videoId);
    if (video != null && video.getFlag() != null) {
      System.out.printf("Cannot add video to %s: Video is currently flagged (reason: %s)%n", playlistName, video.getFlag());
      return;
    }

    if (!playlists.isEmpty()) {
      for (VideoPlaylist playlist : playlists) {
        if (playlist.getName().equalsIgnoreCase(playlistName)) {
          if (!playlist.contains(video)) {
            playlist.addVideo(video);
            if (video != null)
              System.out.printf("Added video to %s: %s%n", playlistName, video.getTitle());
          } else
            System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
        }
      }
    } else
      message = String.format("Cannot add video to %s: Playlist does not exist%n", playlistName);
    if (video == null && message.isEmpty())
      message = String.format("Cannot add video to %s: Video does not exist%n", playlistName);

    System.out.print(message);

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
        if (!playlist.isEmpty())
          showDescription(playlist.getVideos());
        else
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
          return;
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
      if (title.contains(searchTerm.toLowerCase()) && video.getFlag() == null)
        matches.add(video);
    }

    if (!matches.isEmpty()) {
      System.out.printf("Here are the results for %s:%n", searchTerm);
      listVideos(matches);

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
    if (videoTag.startsWith("#")) { // eliminate edge case
      List<Video> matches = new ArrayList<>();
      for (Video video : videoLibrary.getVideos()) {
        for (String tag : video.getTags()) {
          if (tag.toLowerCase().contains(videoTag.toLowerCase()) && video.getFlag() == null)
            matches.add(video);
        }
      }
      if (!matches.isEmpty()) {
        System.out.printf("Here are the results for %s:%n", videoTag);
        listVideos(matches);

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
    Video video = videoLibrary.getVideo(videoId);
    if (video != null)
      if (video.getFlag() == null) {
        video.flag("Not supplied");
        if (video == current)
          stopVideo();
        System.out.printf("Successfully flagged video: %s (reason: %s)%n", video.getTitle(), "Not supplied");
      } else
        System.out.println("Cannot flag video: Video is already flagged");
    else
      System.out.println("Cannot flag video: Video does not exist");
  }

  public void flagVideo(String videoId, String reason) {
    Video video = videoLibrary.getVideo(videoId);
    if (video != null)
      if (video.getFlag() == null) {
        video.flag(reason);
        if (video == current)
          stopVideo();
        System.out.printf("Successfully flagged video: %s (reason: %s)%n", video.getTitle(), reason);
      } else
        System.out.println("Cannot flag video: Video is already flagged");
    else
      System.out.println("Cannot flag video: Video does not exist");
  }

  public void allowVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video != null)
      if (video.getFlag() != null) {
        video.unflag();
        System.out.printf("Successfully removed flag from video: %s%n", video.getTitle());
      } else
        System.out.println("Cannot remove flag from video: Video is not flagged");
    else
      System.out.println("Cannot remove flag from video: Video does not exist");

  }

  // helper methods
  private void sortByTitle(List<Video> videos) {
    Comparator<Video> compareByTitle = Comparator.comparing(Video::getTitle);
    videos.sort(compareByTitle);
  }

  private void showDescription(List<Video> videos) {
    for (Video video : videos) {
      String description = String.format("%s (%s) [%s]", video.getTitle(), video.getVideoId(), String.join(" ", video.getTags()));
      description = (video.getFlag() != null) ? description + String.format(" - FLAGGED (reason: %s)%n", video.getFlag()) : description + "\n";
      System.out.print(description);
    }
  }

  private void listVideos(List<Video> videos) {
    sortByTitle(videos);
    int count = 1;
    for (Video video : videos) {
      System.out.printf("%d) %s (%s) [%s]%n", count, video.getTitle(), video.getVideoId(), String.join(" ", video.getTags()));
      count++;
    }
  }
}
