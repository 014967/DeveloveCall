package com.example.developCall.Object;

public class Transcript {
    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    private String transcript;

    @Override
    public String toString()
    {
        return "Transcript{" + transcript + "}";
    }

}
