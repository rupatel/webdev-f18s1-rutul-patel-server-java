package com.neu.cs5610.fall18.course.manager.repositories;

import org.springframework.data.repository.CrudRepository;

import com.neu.cs5610.fall18.course.manager.entities.Lesson;

public interface LessonRepository extends CrudRepository<Lesson, Long>{

}
