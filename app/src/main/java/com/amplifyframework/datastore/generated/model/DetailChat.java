package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the DetailChat type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "DetailChats")
@Index(name = "chatId", fields = {"chatID"})
public final class DetailChat implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField CHAT_ID = field("chatID");
  public static final QueryField START_TIME = field("start_time");
  public static final QueryField END_TIME = field("end_time");
  public static final QueryField CONTENT = field("content");
  public static final QueryField SPEAKER_LABEL = field("speaker_label");
  public static final QueryField CREATED_AT = field("createdAt");
  public static final QueryField UPDATED_AT = field("updatedAt");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String chatID;
  private final @ModelField(targetType="String") String start_time;
  private final @ModelField(targetType="String") String end_time;
  private final @ModelField(targetType="String") String content;
  private final @ModelField(targetType="String") String speaker_label;
  private final @ModelField(targetType="String") String createdAt;
  private final @ModelField(targetType="String") String updatedAt;
  public String getId() {
      return id;
  }
  
  public String getChatId() {
      return chatID;
  }
  
  public String getStartTime() {
      return start_time;
  }
  
  public String getEndTime() {
      return end_time;
  }
  
  public String getContent() {
      return content;
  }
  
  public String getSpeakerLabel() {
      return speaker_label;
  }
  
  public String getCreatedAt() {
      return createdAt;
  }
  
  public String getUpdatedAt() {
      return updatedAt;
  }
  
  private DetailChat(String id, String chatID, String start_time, String end_time, String content, String speaker_label, String createdAt, String updatedAt) {
    this.id = id;
    this.chatID = chatID;
    this.start_time = start_time;
    this.end_time = end_time;
    this.content = content;
    this.speaker_label = speaker_label;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      DetailChat detailChat = (DetailChat) obj;
      return ObjectsCompat.equals(getId(), detailChat.getId()) &&
              ObjectsCompat.equals(getChatId(), detailChat.getChatId()) &&
              ObjectsCompat.equals(getStartTime(), detailChat.getStartTime()) &&
              ObjectsCompat.equals(getEndTime(), detailChat.getEndTime()) &&
              ObjectsCompat.equals(getContent(), detailChat.getContent()) &&
              ObjectsCompat.equals(getSpeakerLabel(), detailChat.getSpeakerLabel()) &&
              ObjectsCompat.equals(getCreatedAt(), detailChat.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), detailChat.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getChatId())
      .append(getStartTime())
      .append(getEndTime())
      .append(getContent())
      .append(getSpeakerLabel())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("DetailChat {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("chatID=" + String.valueOf(getChatId()) + ", ")
      .append("start_time=" + String.valueOf(getStartTime()) + ", ")
      .append("end_time=" + String.valueOf(getEndTime()) + ", ")
      .append("content=" + String.valueOf(getContent()) + ", ")
      .append("speaker_label=" + String.valueOf(getSpeakerLabel()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static ChatIdStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static DetailChat justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new DetailChat(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      chatID,
      start_time,
      end_time,
      content,
      speaker_label,
      createdAt,
      updatedAt);
  }
  public interface ChatIdStep {
    BuildStep chatId(String chatId);
  }
  

  public interface BuildStep {
    DetailChat build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep startTime(String startTime);
    BuildStep endTime(String endTime);
    BuildStep content(String content);
    BuildStep speakerLabel(String speakerLabel);
    BuildStep createdAt(String createdAt);
    BuildStep updatedAt(String updatedAt);
  }
  

  public static class Builder implements ChatIdStep, BuildStep {
    private String id;
    private String chatID;
    private String start_time;
    private String end_time;
    private String content;
    private String speaker_label;
    private String createdAt;
    private String updatedAt;
    @Override
     public DetailChat build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new DetailChat(
          id,
          chatID,
          start_time,
          end_time,
          content,
          speaker_label,
          createdAt,
          updatedAt);
    }
    
    @Override
     public BuildStep chatId(String chatId) {
        Objects.requireNonNull(chatId);
        this.chatID = chatId;
        return this;
    }
    
    @Override
     public BuildStep startTime(String startTime) {
        this.start_time = startTime;
        return this;
    }
    
    @Override
     public BuildStep endTime(String endTime) {
        this.end_time = endTime;
        return this;
    }
    
    @Override
     public BuildStep content(String content) {
        this.content = content;
        return this;
    }
    
    @Override
     public BuildStep speakerLabel(String speakerLabel) {
        this.speaker_label = speakerLabel;
        return this;
    }
    
    @Override
     public BuildStep createdAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
     public BuildStep updatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String chatId, String startTime, String endTime, String content, String speakerLabel, String createdAt, String updatedAt) {
      super.id(id);
      super.chatId(chatId)
        .startTime(startTime)
        .endTime(endTime)
        .content(content)
        .speakerLabel(speakerLabel)
        .createdAt(createdAt)
        .updatedAt(updatedAt);
    }
    
    @Override
     public CopyOfBuilder chatId(String chatId) {
      return (CopyOfBuilder) super.chatId(chatId);
    }
    
    @Override
     public CopyOfBuilder startTime(String startTime) {
      return (CopyOfBuilder) super.startTime(startTime);
    }
    
    @Override
     public CopyOfBuilder endTime(String endTime) {
      return (CopyOfBuilder) super.endTime(endTime);
    }
    
    @Override
     public CopyOfBuilder content(String content) {
      return (CopyOfBuilder) super.content(content);
    }
    
    @Override
     public CopyOfBuilder speakerLabel(String speakerLabel) {
      return (CopyOfBuilder) super.speakerLabel(speakerLabel);
    }
    
    @Override
     public CopyOfBuilder createdAt(String createdAt) {
      return (CopyOfBuilder) super.createdAt(createdAt);
    }
    
    @Override
     public CopyOfBuilder updatedAt(String updatedAt) {
      return (CopyOfBuilder) super.updatedAt(updatedAt);
    }
  }
  
}
