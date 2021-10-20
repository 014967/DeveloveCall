package com.example.developCall.Object;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private List<Transcript> transcripts = new ArrayList<Transcript>();
    private List<Item> items       = new ArrayList<Item>();
    private Speaker  speaker_labels ;


    public Speaker getSpeaker_labels() {
        return speaker_labels;
    }

    public void setSpeaker_labels(Speaker speaker_labels) {
        this.speaker_labels = speaker_labels;
    }

    public List<Transcript> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(List<Transcript> transcripts) {
        this.transcripts = transcripts;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Result{" +
                "transcripts=" + transcripts +
                '}';
    }
}
