package com.lessayer.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Lecturer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "lecturer_desc", length = 500)
	private String lecturerDescription;
	
	@OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Video> videos;
	
	public Lecturer(String name) {
		super();
		this.name = name;
	}
	
	public Lecturer(String name, String lecturerDescription) {
		super();
		this.name = name;
		this.lecturerDescription = lecturerDescription;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lecturer other = (Lecturer) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Lecturer [name=" + name + "]";
	}
	
}
