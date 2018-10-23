package com.neu.cs5610.fall18.course.manager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.neu.cs5610.fall18.course.manager.entities.Lesson;
import com.neu.cs5610.fall18.course.manager.entities.Topic;
import com.neu.cs5610.fall18.course.manager.entities.Widget;
import com.neu.cs5610.fall18.course.manager.repositories.WidgetRepository;

@Service
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class WidgetService {
	@Autowired
	private TopicService topicService;
	@Autowired
	private WidgetRepository widgetRepo;
	@PostMapping("/api/topic/{topicId}/widget")
	public Widget createTopic(@PathVariable("topicId") Long topicId, 
								@RequestBody Widget widget) {
			
		Topic t = topicService.findTopicById(topicId);
		if(t==null) return null;
		widget.setTopic(t);
		Widget updatedWidget = widgetRepo.save(widget);
		t.addToWidgets(updatedWidget);
		return updatedWidget;
	}
	
	@GetMapping("/api/topic/{topicId}/widget")
	public List<Widget> findAllWidget(@PathVariable("topicId") Long topicId){
		Topic t = topicService.findTopicById(topicId);
		if(t != null)
			return new ArrayList<Widget>(t.getWidgets());
		else 
			return null;
	}
	
	@GetMapping("/api/widget/{widgetId}")
	public Widget findTopicById(@PathVariable("widgetId") Long widgetId) {
		Optional<Widget> opt = widgetRepo.findById(widgetId);
		if(opt.isPresent())
			return opt.get();
		else 
			return null;
	}
	
	@PutMapping("/api/widget/{widgetId}")
	public Widget updateTopic(@PathVariable("widgetId") Long widgetId, @RequestBody Widget widget) {
		Optional<Widget> opt = widgetRepo.findById(widgetId);
		if(opt.isPresent())
		{
			Widget old = opt.get();
			widget.setId(old.getId());
			widget.setTopic(old.getTopic());
			
			Widget updatedWidget = widgetRepo.save(widget);
			updatedWidget.getTopic().getWidgets().remove(old);
			updatedWidget.getTopic().getWidgets().add(updatedWidget);
			
			return updatedWidget;
		}
		else 
			return null;
	}
	
	@DeleteMapping("/api/widget/{widgetId}")
	public void deleteCourse(@PathVariable("widgetId") Long widgetId) {
		if(widgetRepo.existsById(widgetId))
			widgetRepo.deleteById(widgetId);
	}
}