package com.lessayer.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "article")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "title", length = 100, nullable = false)
	private String title;
	
	@Column(name = "author", length = 50, nullable = false)
	private String author;
	
	@Column(name = "date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date date;
	
	@Column(name = "summary", length = 500, nullable = false)
	private String summary;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "articles_tags",
			joinColumns = @JoinColumn(name = "article_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags;
	
	@Column(name = "content", length = 10000, nullable = false)
	private String content;
	
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArticleImage> imageFiles;
	
	public void addImageFiles(ArticleImage imageFile) {
		imageFiles.add(imageFile);
	}
	
	public void removeAllImageFiles() {
		imageFiles.removeAll(imageFiles);
	}
	
	public Article(String title, String author, Date date, String summary, String content,
			Set<Tag> tags) {
		this.title = title;
		this.author = author;
		this.date = date;
		this.summary = summary;
		this.content = content;
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", author=" + author + ", date=" + date + ", tags="
				+ tags + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, date, id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		return Objects.equals(author, other.author) && Objects.equals(date, other.date) && Objects.equals(id, other.id)
				&& Objects.equals(title, other.title);
	}
	
	public void addKeyword(Tag tag) {
		this.tags.add(tag);
	}
	
	public void removeKeyword(Tag tag) {
		this.tags.remove(tag);
	}
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Transient
	public String getDateString() {
		return dateFormat.format(date).toString();
	}
	
	@Transient
	public Set<Tag> getSortedTags() {
		TreeSet<Tag> sortedTags = new TreeSet<>(tags);
		return sortedTags;
	}
	
	@Transient
	public String getTagsString() {
		StringBuilder stringBuilder = new StringBuilder();
		tags.forEach(tag -> stringBuilder.append(tag.getTag() + ", "));
		stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
		
		return stringBuilder.toString();
	}
	
}
