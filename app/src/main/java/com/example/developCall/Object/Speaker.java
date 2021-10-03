package com.example.developCall.Object;

import java.util.ArrayList;
import java.util.List;

public class Speaker { //speaker Label
    private List<Segment> segments = new ArrayList<Segment>();

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    private int speakers;

    public void setSpeakers(int speakers) {
        this.speakers = speakers;
    }

    public int getSpeakers() {
        return speakers;
    }


    @Override
    public String toString() {
        return "" + segments;
    }
}
