package com.wk.xin.beans.sample;

import org.springframework.stereotype.Component;

/**
 * 一款摇滚专辑碟片
 *
 * @author dell
 */
@Component
public class SgtPeppers implements CompactDisc {

  private String title = "Sgt. Pepper's Lonely Hearts Club Band";  
  private String artist = "The Beatles";
  
  @Override
  public void play() {
    System.out.println("Playing " + title + " by " + artist);
  }

}
