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

/** This is an auto generated class representing the Friend type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Friends")
@Index(name = "userFriendId", fields = {"userID"})
public final class Friend implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField USER_ID = field("userID");
  public static final QueryField NUMBER = field("number");
  public static final QueryField NAME = field("name");
  public static final QueryField REMIND_DATE = field("remindDate");
  public static final QueryField FRIEND_IMG = field("friendImg");
  public static final QueryField GROUP = field("group");
  public static final QueryField FAVORITE = field("favorite");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String userID;
  private final @ModelField(targetType="String") String number;
  private final @ModelField(targetType="String") String name;
  private final @ModelField(targetType="String") String remindDate;
  private final @ModelField(targetType="String") String friendImg;
  private final @ModelField(targetType="String") String group;
  private final @ModelField(targetType="Boolean") Boolean favorite;
  private final @ModelField(targetType="Chat") @HasMany(associatedWith = "friendID", type = Chat.class) List<Chat> chat = null;
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return userID;
  }
  
  public String getNumber() {
      return number;
  }
  
  public String getName() {
      return name;
  }
  
  public String getRemindDate() {
      return remindDate;
  }
  
  public String getFriendImg() {
      return friendImg;
  }
  
  public String getGroup() {
      return group;
  }
  
  public Boolean getFavorite() {
      return favorite;
  }
  
  public List<Chat> getChat() {
      return chat;
  }
  
  private Friend(String id, String userID, String number, String name, String remindDate, String friendImg, String group, Boolean favorite) {
    this.id = id;
    this.userID = userID;
    this.number = number;
    this.name = name;
    this.remindDate = remindDate;
    this.friendImg = friendImg;
    this.group = group;
    this.favorite = favorite;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Friend friend = (Friend) obj;
      return ObjectsCompat.equals(getId(), friend.getId()) &&
              ObjectsCompat.equals(getUserId(), friend.getUserId()) &&
              ObjectsCompat.equals(getNumber(), friend.getNumber()) &&
              ObjectsCompat.equals(getName(), friend.getName()) &&
              ObjectsCompat.equals(getRemindDate(), friend.getRemindDate()) &&
              ObjectsCompat.equals(getFriendImg(), friend.getFriendImg()) &&
              ObjectsCompat.equals(getGroup(), friend.getGroup()) &&
              ObjectsCompat.equals(getFavorite(), friend.getFavorite());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getNumber())
      .append(getName())
      .append(getRemindDate())
      .append(getFriendImg())
      .append(getGroup())
      .append(getFavorite())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Friend {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userID=" + String.valueOf(getUserId()) + ", ")
      .append("number=" + String.valueOf(getNumber()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("remindDate=" + String.valueOf(getRemindDate()) + ", ")
      .append("friendImg=" + String.valueOf(getFriendImg()) + ", ")
      .append("group=" + String.valueOf(getGroup()) + ", ")
      .append("favorite=" + String.valueOf(getFavorite()))
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
  public static Friend justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Friend(
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
      number,
      name,
      remindDate,
      friendImg,
      group,
      favorite);
  }
  public interface UserIdStep {
    BuildStep userId(String userId);
  }
  

  public interface BuildStep {
    Friend build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep number(String number);
    BuildStep name(String name);
    BuildStep remindDate(String remindDate);
    BuildStep friendImg(String friendImg);
    BuildStep group(String group);
    BuildStep favorite(Boolean favorite);
  }
  

  public static class Builder implements UserIdStep, BuildStep {
    private String id;
    private String userID;
    private String number;
    private String name;
    private String remindDate;
    private String friendImg;
    private String group;
    private Boolean favorite;
    @Override
     public Friend build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Friend(
          id,
          userID,
          number,
          name,
          remindDate,
          friendImg,
          group,
          favorite);
    }
    
    @Override
     public BuildStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userID = userId;
        return this;
    }
    
    @Override
     public BuildStep number(String number) {
        this.number = number;
        return this;
    }
    
    @Override
     public BuildStep name(String name) {
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep remindDate(String remindDate) {
        this.remindDate = remindDate;
        return this;
    }
    
    @Override
     public BuildStep friendImg(String friendImg) {
        this.friendImg = friendImg;
        return this;
    }
    
    @Override
     public BuildStep group(String group) {
        this.group = group;
        return this;
    }
    
    @Override
     public BuildStep favorite(Boolean favorite) {
        this.favorite = favorite;
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
    private CopyOfBuilder(String id, String userId, String number, String name, String remindDate, String friendImg, String group, Boolean favorite) {
      super.id(id);
      super.userId(userId)
        .number(number)
        .name(name)
        .remindDate(remindDate)
        .friendImg(friendImg)
        .group(group)
        .favorite(favorite);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder number(String number) {
      return (CopyOfBuilder) super.number(number);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder remindDate(String remindDate) {
      return (CopyOfBuilder) super.remindDate(remindDate);
    }
    
    @Override
     public CopyOfBuilder friendImg(String friendImg) {
      return (CopyOfBuilder) super.friendImg(friendImg);
    }
    
    @Override
     public CopyOfBuilder group(String group) {
      return (CopyOfBuilder) super.group(group);
    }
    
    @Override
     public CopyOfBuilder favorite(Boolean favorite) {
      return (CopyOfBuilder) super.favorite(favorite);
    }
  }
  
}
