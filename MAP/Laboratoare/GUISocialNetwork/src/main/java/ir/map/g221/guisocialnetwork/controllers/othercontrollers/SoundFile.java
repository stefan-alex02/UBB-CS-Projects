package ir.map.g221.guisocialnetwork.controllers.othercontrollers;

import ir.map.g221.guisocialnetwork.OldMain;

public enum SoundFile {
    RING_SOUND_1("sounds/notification1.mp3");

    private final String filePath;

    private SoundFile(String filePath) {
        this.filePath = String.valueOf(OldMain.class.getResource(filePath));
    }

    public String getFilePath() {
        return filePath;
    }
}
