package com.tts.TechTalentTwitter.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.TechTalentTwitter.model.Tag;
import com.tts.TechTalentTwitter.model.Tweet;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long>  {
    
	Tag findByPhrase(String phrase);
	
//	List<Tweet> findAllByOrderByCreatedAtDesc();
	

}