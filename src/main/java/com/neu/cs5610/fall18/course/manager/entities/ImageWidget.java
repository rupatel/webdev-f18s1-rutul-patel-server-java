package com.neu.cs5610.fall18.course.manager.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("IMAGE")
public class ImageWidget extends Widget{
	private String src;
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
}
