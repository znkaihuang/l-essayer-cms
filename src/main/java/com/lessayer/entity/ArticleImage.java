package com.lessayer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "fileName", length = 50, nullable = false)
	private String fileName;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
	
	@Column(name = "width")
	private Integer width;
	
	@Column(name = "height")
	private Integer height;
	
	@Column(name = "size")
	private Double size;

	public ArticleImage(String fileName, Article article) {
		super();
		this.fileName = fileName;
		this.article = article;
	}

	public ArticleImage(String fileName, Integer width, Integer height, Article article) {
		super();
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		this.article = article;
	}

	public ArticleImage(String fileName, Integer width, Integer height, Double size, Article article) {
		super();
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		this.size = size;
		this.article = article;
	}
	
}
