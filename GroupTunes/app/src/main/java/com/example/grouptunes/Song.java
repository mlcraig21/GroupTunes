package com.example.grouptunes;

import java.time.Duration;

class Song {
    private final String name;
    private final Duration duration;
    public Song(String name, Duration duration) {
        this.name = name;
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }
}
