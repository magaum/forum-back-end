package br.inpe.forum.model;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

//@JsonInclude(Include.NON_NULL)
@Document(collection = "topics")
public class Topic {

	@Id
	private String objectId;
	@NonNull
	private String title;
	@NonNull
	@Size(max = 100000)
	private String description;
	@DBRef
	@NonNull
	private User user;
	private List<String> subjects;
	private Set<String> likes;
	@DBRef(db = "forum")
	private List<Comment> comments;

	public Topic() {
	}

	public Topic(String id, String title, String description, User user, List<String> subjects,
			List<Comment> comments) {
		super();
		this.objectId = id;
		this.user = user;
		this.title = title;
		this.description = description;
		this.subjects = subjects;
		this.comments = comments;
	}

	public Topic(String title, String description, User user, List<String> subjects, List<Comment> comments) {
		super();
		this.user = user;
		this.title = title;
		this.description = description;
		this.subjects = subjects;
		this.comments = comments;
	}

	public Topic(String title, String description, User user, List<String> subjects) {
		super();
		this.user = user;
		this.title = title;
		this.description = description;
		this.subjects = subjects;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public Set<String> getLikes() {
		return likes;
	}

	public void setLikes(Set<String> likes) {
		this.likes = likes;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
}
