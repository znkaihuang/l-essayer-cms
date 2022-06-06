package com.lessayer.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
	
	@Column(name = "lecturer", length = 50, nullable = false)
	private String lecturer;
	
	@Column(name = "date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date date;
	
	@Column(name = "desc", length = 500, nullable = false)
	private String description;
	
	@Column(name = "lecturer_desc", length = 500, nullable = false)
	private String lecturerDescription;
	
	@Column(name = "cover_image")
	private String coverImage;
	
	@Column(name = "url")
	private String url;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "video_tags",
			joinColumns = @JoinColumn(name = "video_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags;
	
}
