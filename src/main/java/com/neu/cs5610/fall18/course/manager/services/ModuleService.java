package com.neu.cs5610.fall18.course.manager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.neu.cs5610.fall18.course.manager.entities.Course;
import com.neu.cs5610.fall18.course.manager.entities.Lesson;
import com.neu.cs5610.fall18.course.manager.entities.Module;
import com.neu.cs5610.fall18.course.manager.repositories.CourseRepository;
import com.neu.cs5610.fall18.course.manager.repositories.ModuleRepository;

@Service
@RestController
public class ModuleService {
	@Autowired
	private CourseService courseService;
	@Autowired
	private ModuleRepository moduleRepo;
	@Autowired
	private CourseRepository courseRepo;
	@PostMapping("/api/course/{courseId}/module")
	public Module createModule(@PathVariable("courseId") Long courseId, 
								@RequestBody Module module) {
			
		Course c = courseService.findCourseById(courseId);
		if(c==null) return null;
		if(module.getLessons() != null)
		{
			for(Lesson l : module.getLessons())
				module.addToLessons(l);
		}
		
		module.setCourse(c);
		c.addToModules(module);
		return moduleRepo.save(module);
	}
	
	@GetMapping("/api/course/{cid}/module")
	public List<Module> findAllModule(@PathVariable("cid") Long cid){
		Course c = courseService.findCourseById(cid);
		if(c!=null)
			return new ArrayList<Module>(c.getModules());
		return null;
	}
	
	@GetMapping("/api/module/{moduleId}")
	public Module findModuleById(@PathVariable("moduleId") Long moduleId) {
		Optional<Module> opt = moduleRepo.findById(moduleId);
		if(opt.isPresent())
			return opt.get();
		else 
			return null;
	}
	
	@PutMapping("/api/module/{moduleId}")
	public Module updateModule(@PathVariable("moduleId") Long moduleId, @RequestBody Module module) {
		Optional<Module> opt = moduleRepo.findById(moduleId);
		if(opt.isPresent())
		{
			Module old = opt.get();
			module.setId(old.getId());
			module.setLessons(old.getLessons());
		
			Set<Module> modules = old.getCourse().getModules().stream().map(m -> {
				return (m.getId().equals(moduleId) ? module : m);
			}).collect(Collectors.toSet());
			
			old.getCourse().getModules().clear();
			old.getCourse().getModules().addAll(modules);
			module.setCourse(old.getCourse());
			
			courseRepo.save(module.getCourse());
			
			return findModuleById(module.getId());
		}
		else 
			return null;
	}
	
	@DeleteMapping("/api/module/{moduleId}")
	public void deleteModule(@PathVariable("moduleId") Long moduleId) {
		if(moduleRepo.existsById(moduleId))
			moduleRepo.deleteById(moduleId);
	}
}