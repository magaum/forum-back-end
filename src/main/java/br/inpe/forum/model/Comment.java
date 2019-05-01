package br.inpe.forum.model;

import java.util.Set;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@Document("comments")
public class Comment {

	@Id
	private String objectId;
	@NonNull
	@Size(max = 10000)
	private String description;
	private Set<String> likes;
	@DBRef(db = "forum")
	@NonNull
	private User user;

	public Comment() {
	}

	public Comment(String id, String description, Set<String> likes, User user) {
		super();
		this.objectId = id;
		this.likes = likes;
		this.description = description;
		this.user = user;
	}

	public Comment(String description, Set<String> likes, User user) {
		super();
		this.description = description;
		this.likes = likes;
		this.user = user;
	}

	public Comment(String id, String description, User user) {
		super();
		this.objectId = id;
		this.description = description;
		this.user = user;
	}

	public Comment(String description, User user) {
		super();
		this.description = description;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getLikes() {
		return likes;
	}

	public void setLikes(Set<String> likes) {
		this.likes = likes;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
