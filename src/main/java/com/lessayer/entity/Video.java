package com.lessayer.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Video {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "title", length = 100, nullable = false)
	private String title;
	
	@Column(name = "uploader", length = 50, nullable = false)
	private String uploader;
	
	@ManyToOne
	@JoinColumn(name = "lecturer_id", nullable = false)
	private Lecturer lecturer;
	
	@Column(name = "date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date date;
	
	@Column(name = "description", length = 500, nullable = false)
	private String description;
	
	@Column(name = "cover_image")
	private String coverImage;
	
	@Column(name = "video_url", nullable = false)
	private String url;
	
	@Column(name = "language", nullable = false)
	private Language language;
	
	@Column(name = "subtitle", nullable = false)
	private boolean hasSubtitle;
	
	@Column(name = "length", nullable = false)
	private Integer videoLength;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "video_tags",
			joinColumns = @JoinColumn(name = "video_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags;
	
	public Video(String title, String uploader, Lecturer lecturer, Date date, String description, String url,
			Language language, boolean hasSubtitle, Integer videoLength) {
		super();
		this.title = title;
		this.uploader = uploader;
		this.lecturer = lecturer;
		this.date = date;
		this.description = description;
		this.url = url;
		this.language = language;
		this.hasSubtitle = hasSubtitle;
		this.videoLength = videoLength;
	}
	
	public Video(String title, String uploader, Lecturer lecturer, Date date, String description,
			String coverImage, String url, Language language, boolean hasSubtitle, Integer videoLength, Set<Tag> tags) {
		super();
		this.title = title;
		this.uploader = uploader;
		this.lecturer = lecturer;
		this.date = date;
		this.description = description;
		this.coverImage = coverImage;
		this.url = url;
		this.language = language;
		this.hasSubtitle = hasSubtitle;
		this.videoLength = videoLength;
		this.tags = tags;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(date, description, lecturer, title, url, language, videoLength);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		return Objects.equals(date, other.date) && Objects.equals(description, other.description)
				&& Objects.equals(lecturer, other.lecturer)
				&& Objects.equals(title, other.title)
				&& Objects.equals(url, other.url)
				&& Objects.equals(language, other.language)
				&& Objects.equals(videoLength, other.videoLength);
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", title=" + title + ", uploader=" + uploader + ", lecturer=" + lecturer + ", date="
				+ date + ", url=" + url + ", language=" + language + ", hasSubtitle=" + hasSubtitle + ", videoLength="
				+ videoLength + ", tags=" + tags + "]";
	}
	
	@Transient
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
	
	@Transient
	public String getCoverImagePath() {
		if (this.coverImage != null && !this.coverImage.equals("")) {
			return "/videos/" + id + "/image/" + coverImage;
		}
		else {
			return "/images/video-play.png";
		}
	}
	
	@Transient
	public String getURLPath() {
		return "videos/" + id + "/" + url;
	}
}
