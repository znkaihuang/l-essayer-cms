package com.lessayer.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tag")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Tag implements Comparable<Tag> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Setter
	private String tag;
	
	public Tag(String tag) {
		this.tag = tag;
	}

	@Override
	public int compareTo(Tag anotherTag) {
		return tag.compareTo(anotherTag.getTag());
	}

	@Override
	public int hashCode() {
		return Objects.hash(tag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		return Objects.equals(tag, other.tag);
	}

	@Override
	public String toString() {
		return "Tag [tag=" + tag + "]";
	}
	
}
