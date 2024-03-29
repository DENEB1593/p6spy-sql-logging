package io.study.deneb.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "post")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String content;

  public Post() { }

  public Post(String content) {
    this.content = content;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "Post{" +
      "id=" + id +
      ", content='" + content + '\'' +
      '}';
  }
}
