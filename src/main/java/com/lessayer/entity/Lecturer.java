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
	
	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;
	
	@Column(name = "lecturer_desc", length = 500, nullable = false)
	private String lecturerDescription;
	
	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Video> videos;
	
	public Lecturer(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Lecturer(String firstName, String lastName, String lecturerDescription) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.lecturerDescription = lecturerDescription;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
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
		return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
	}

	@Override
	public String toString() {
		return "Lecturer [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
}
