package com.wk.xin.beans.sample;

import java.util.List;

/**
 * 空光盘
 */
public class BlankDisc implements CompactDisc {

  private String title;
  /** 艺术家 */
  private String artist;
  /** 磁道 */
  private List<String> tracks;

  public BlankDisc() {
  }

  public BlankDisc(String title, String artist, List<String> tracks) {
    this.title = title;
    this.artist = artist;
    this.tracks = tracks;
  }

  @Override
  public void play() {
    System.out.println("Playing " + title + " by " + artist);
    for (String track : tracks) {
      System.out.println("-Track: " + track);
    }
  }

}