package ir.map.g221.guisocialnetwork.controllers.guiutils;

import ir.map.g221.guisocialnetwork.OldMain;

public enum SoundFile {
    RING_SOUND_1("sounds/notification1.mp3", 0.2),
    MESSAGE_SOUND_1("sounds/message1.mp3", 0.9),
    MESSAGE_SOUND_2("sounds/message2.mp3", 0.6);

    private final String filePath;
    private final Double intensity;

    private SoundFile(String filePath, Double intensity) {
        this.filePath = String.valueOf(OldMain.class.getResource(filePath));
        this.intensity = intensity;
    }

    public String getFilePath() {
        return filePath;
    }

    public Double getIntensity() {
        return intensity;
    }
}
