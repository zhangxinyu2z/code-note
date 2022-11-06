package com.eltorofuerte.beans.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * cd播放器
 */
@Component
public class CDPlayer implements MediaPlayer {

    private CompactDisc cd;

    public CDPlayer() {
    }

    @Autowired
    public CDPlayer(CompactDisc cd) {
        this.cd = cd;
    }

    @Override
    public void play() {
        cd.play();
    }
}
