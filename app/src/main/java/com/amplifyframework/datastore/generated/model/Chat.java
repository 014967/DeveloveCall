package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;

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

/** This is an auto generated class representing the Chat type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Chats")
@Index(name = "userId", fields = {"userID"})
@Index(name = "friendId", fields = {"friendID"})
public final class Chat implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField USER_ID = field("userID");
  public static final QueryField FRIEND_ID = field("friendID");
  public static final QueryField DATE = field("date");
  public static final QueryField S3_URL = field("s3_url");
  public static final QueryField KEY_WORD = field("keyWord");
  public static final QueryField SUMMARY = field("summary");
  public static final QueryField MEMO = field("memo");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String userID;
  private final @ModelField(targetType="ID", isRequired = true) String friendID;
  private final @ModelField(targetType="String") String date;
  private final @ModelField(targetType="String") String s3_url;
  private final @ModelField(targetType="String") String keyWord;
  private final @ModelField(targetType="String") String summary;
  private final @ModelField(targetType="String") String memo;
  private final @ModelField(targetType="DetailChat") @HasMany(associatedWith = "chatID", type = DetailChat.class) List<DetailChat> detailChat = null;
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return userID;
  }
  
  public String getFriendId() {
      return friendID;
  }
  
  public String getDate() {
      return date;
  }
  
  public String getS3Url() {
      return s3_url;
  }
  
  public String getKeyWord() {
      return keyWord;
  }
  
  public String getSummary() {
      return summary;
  }
  
  public String getMemo() {
      return memo;
  }
  
  public List<DetailChat> getDetailChat() {
      return detailChat;
  }
  
  private Chat(String id, String userID, String friendID, String date, String s3_url, String keyWord, String summary, String memo) {
    this.id = id;
    this.userID = userID;
    this.friendID = friendID;
    this.date = date;
    this.s3_url = s3_url;
    this.keyWord = keyWord;
    this.summary = summary;
    this.memo = memo;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Chat chat = (Chat) obj;
      return ObjectsCompat.equals(getId(), chat.getId()) &&
              ObjectsCompat.equals(getUserId(), chat.getUserId()) &&
              ObjectsCompat.equals(getFriendId(), chat.getFriendId()) &&
              ObjectsCompat.equals(getDate(), chat.getDate()) &&
              ObjectsCompat.equals(getS3Url(), chat.getS3Url()) &&
              ObjectsCompat.equals(getKeyWord(), chat.getKeyWord()) &&
              ObjectsCompat.equals(getSummary(), chat.getSummary()) &&
              ObjectsCompat.equals(getMemo(), chat.getMemo());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getFriendId())
      .append(getDate())
      .append(getS3Url())
      .append(getKeyWord())
      .append(getSummary())
      .append(getMemo())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Chat {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userID=" + String.valueOf(getUserId()) + ", ")
      .append("friendID=" + String.valueOf(getFriendId()) + ", ")
      .append("date=" + String.valueOf(getDate()) + ", ")
      .append("s3_url=" + String.valueOf(getS3Url()) + ", ")
      .append("keyWord=" + String.valueOf(getKeyWord()) + ", ")
      .append("summary=" + String.valueOf(getSummary()) + ", ")
      .append("memo=" + String.valueOf(getMemo()))
      .append("}")
      .toString();
  }
  
  public static UserIdStep builder() {
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
  public static Chat justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Chat(
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
      userID,
      friendID,
      date,
      s3_url,
      keyWord,
      summary,
      memo);
  }
  public interface UserIdStep {
    FriendIdStep userId(String userId);
  }
  

  public interface FriendIdStep {
    BuildStep friendId(String friendId);
  }
  

  public interface BuildStep {
    Chat build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep date(String date);
    BuildStep s3Url(String s3Url);
    BuildStep keyWord(String keyWord);
    BuildStep summary(String summary);
    BuildStep memo(String memo);
  }
  

  public static class Builder implements UserIdStep, FriendIdStep, BuildStep {
    private String id;
    private String userID;
    private String friendID;
    private String date;
    private String s3_url;
    private String keyWord;
    private String summary;
    private String memo;
    @Override
     public Chat build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Chat(
          id,
          userID,
          friendID,
          date,
          s3_url,
          keyWord,
          summary,
          memo);
    }
    
    @Override
     public FriendIdStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userID = userId;
        return this;
    }
    
    @Override
     public BuildStep friendId(String friendId) {
        Objects.requireNonNull(friendId);
        this.friendID = friendId;
        return this;
    }
    
    @Override
     public BuildStep date(String date) {
        this.date = date;
        return this;
    }
    
    @Override
     public BuildStep s3Url(String s3Url) {
        this.s3_url = s3Url;
        return this;
    }
    
    @Override
     public BuildStep keyWord(String keyWord) {
        this.keyWord = keyWord;
        return this;
    }
    
    @Override
     public BuildStep summary(String summary) {
        this.summary = summary;
        return this;
    }
    
    @Override
     public BuildStep memo(String memo) {
        this.memo = memo;
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
    private CopyOfBuilder(String id, String userId, String friendId, String date, String s3Url, String keyWord, String summary, String memo) {
      super.id(id);
      super.userId(userId)
        .friendId(friendId)
        .date(date)
        .s3Url(s3Url)
        .keyWord(keyWord)
        .summary(summary)
        .memo(memo);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder friendId(String friendId) {
      return (CopyOfBuilder) super.friendId(friendId);
    }
    
    @Override
     public CopyOfBuilder date(String date) {
      return (CopyOfBuilder) super.date(date);
    }
    
    @Override
     public CopyOfBuilder s3Url(String s3Url) {
      return (CopyOfBuilder) super.s3Url(s3Url);
    }
    
    @Override
     public CopyOfBuilder keyWord(String keyWord) {
      return (CopyOfBuilder) super.keyWord(keyWord);
    }
    
    @Override
     public CopyOfBuilder summary(String summary) {
      return (CopyOfBuilder) super.summary(summary);
    }
    
    @Override
     public CopyOfBuilder memo(String memo) {
      return (CopyOfBuilder) super.memo(memo);
    }
  }
  
}
