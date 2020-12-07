package com.tts.TechTalentTwitter.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.TechTalentTwitter.model.Tag;
import com.tts.TechTalentTwitter.model.Tweet;
import com.tts.TechTalentTwitter.model.TweetDisplay;
import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.repository.TagRepository;
import com.tts.TechTalentTwitter.service.TweetService;
import com.tts.TechTalentTwitter.service.UserService;

@Controller
public class TweetController {
	@Autowired
	private UserService userService;

	@Autowired
	private TweetService tweetService;

	@Autowired
	private TagRepository tagRepository;

//  @GetMapping(value= {"/tweets", "/"}) //, "/tags"
//  public String getFeed(@Valid User user, BindingResult bindingResult, Model model){
//      List<TweetDisplay> tweets = tweetService.findAll();
//      model.addAttribute("tweetList", tweets);
//      
//  		if (tweets.isEmpty()) {
//  			model.addAttribute("noTweet", true);
//  		}else {
//  			model.addAttribute("noTweet", false);
//				
//			}        
//      
//      return "ttsTwitter/feed";
//} //end getFeed *************************************************

	@GetMapping(value = { "/tweets", "/" })
	public String getFeed(@RequestParam(value = "filter", required = false) String filter, Model model) {
		User loggedInUser = userService.getLoggedInUser();
		List<TweetDisplay> tweets = new ArrayList<>();
		if (filter == null) {
			filter = "all";
		}
		if (filter.equalsIgnoreCase("following")) {
			List<User> following = loggedInUser.getFollowing();
			tweets = tweetService.findAllByUsers(following);
			model.addAttribute("filter", "following");
		} else {
			tweets = tweetService.findAll();
			model.addAttribute("filter", "all");
		}
		model.addAttribute("tweetList", tweets);

		if (tweets.isEmpty()) {
			model.addAttribute("noTweet", true);
		} else {
			model.addAttribute("noTweet", false);

		}
		return "ttsTwitter/feed";
	} //end getFeed *************************************************

	
	
	@GetMapping(value = "/tweets/new")
	public String getTweetForm(Model model) {
		model.addAttribute("tweet", new Tweet());
		return "ttsTwitter/newTweet";
	}

	@PostMapping(value = "/tweets")
	public String submitTweetForm(@Valid Tweet tweet, BindingResult bindingResult, Model model) {
		User user = userService.getLoggedInUser();
		if (!bindingResult.hasErrors()) {
			tweet.setUser(user);
			tweetService.save(tweet);
			model.addAttribute("successMessage", "Tweet successfully created!");
			model.addAttribute("tweet", new Tweet());
		}
		return "ttsTwitter/newTweet";
	}

	@GetMapping(value = "/tweets/{tag}")
	public String getTweetsByTag(@PathVariable(value = "tag") String tag, Model model) {
		List<TweetDisplay> tweets = tweetService.findAllWithTag(tag);
		model.addAttribute("tweetList", tweets);
		model.addAttribute("tag", tag);
		return "ttsTwitter/taggedTweets";
	}

	// ---------------======================

//    @GetMapping(value = "/tags")
//    public String getTags(Model model) {
//    	List<Tag> tag = (List<Tag>)tagRepository.findAll();
//    	model.addAttribute("tagList", tag);
//    	return "tags";
//    }

	@GetMapping(value = { "/tags" })
	public String getTags(Model model) { // @Valid User user, BindingResult bindingResult, , List<User> users

		List<Tag> tag = (List<Tag>) tagRepository.findAll();
		model.addAttribute("tagList", tag);
//        
//    		if (tag.isEmpty() ) {  //|| alltagedtweets.isEmpty()
//    			model.addAttribute("noTweet", true);
//    		}else {
//    			model.addAttribute("noTweet", false);
//				
//			}

//          model.addAttribute("alltagedtweets", alltagedtweets);
//          List<Tweet> tweets = tweetService.findAll();

//    	List<Tweet> alltagedtweets = tweetService.findAllByUsers(users);        
//    	List<Tweet> tagedtweets = tweetService.findAllWithTag(tag);        

		return "ttsTwitter/tags";
	}

}