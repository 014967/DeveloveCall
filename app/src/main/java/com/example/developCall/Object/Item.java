package com.example.developCall.Object;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String start_time;
    private String end_time;
    private List<Alternative> alternatives = new ArrayList<Alternative>();
    private String type;
    private String speaker_label;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpeaker_label() {
        return speaker_label;
    }

    public void setSpeaker_label(String speaker_label) {
        this.speaker_label = speaker_label;
    }

    @Override
    public String toString()

    {
        {return  start_time + "/"+ end_time + "/" + speaker_label + "/" + alternatives ;}
    }
}
