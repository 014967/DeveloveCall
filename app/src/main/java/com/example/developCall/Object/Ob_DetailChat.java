package com.example.developCall.Object;

import com.amplifyframework.core.model.temporal.Temporal;

public class Ob_DetailChat {
    String content;
    String spk;
    String id;
    Temporal.DateTime created_at;

    public Temporal.DateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Temporal.DateTime created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpk() {
        return spk;
    }

    public void setSpk(String spk) {
        this.spk = spk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
