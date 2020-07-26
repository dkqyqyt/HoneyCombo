package com.ssafy.sub.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ssafy.sub.repo.FeedQueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ssafy.sub.dto.Feed;
import com.ssafy.sub.exception.RestException;
import com.ssafy.sub.model.response.ResponseMessage;
import com.ssafy.sub.model.response.StatusCode;
import com.ssafy.sub.repo.FeedRepository;
@Service
public class FeedServiceImpl implements FeedService {

	@Autowired
	FeedRepository feedRepository;
	@Autowired
	FeedQueryDsl feedQueryDsl;

	@Override
	public List<Feed> feedHomePageList() {
		return feedRepository.findAll();
	}
	
	@Override
	public List<Feed> feedUserPageList(int uid) {
		return feedRepository.findByUid(uid);
	}

	@Override
	public List<Feed> findAllByFollower(int id) {
		List<Feed> test = feedQueryDsl.findAllByFollower(id);
		
		for (Feed feed : test) {
			System.out.println(feed.toString());
		}
		
		if(test.size() == 0)
			System.out.println("null z?");
		return test;
	}

	@Override
	public Feed feedDetail(int id) {
		return feedRepository.findById(id)
					.orElseThrow(() -> new RestException(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_FEED, HttpStatus.NOT_FOUND));
	}

	@Override
	public Feed feedInsert(Feed feed) {
		return feedRepository.save(feed);
	}

	@Override
	@Transactional
	public Feed feedUpdate(int id, Feed feed) {
		Date now = new Date();
		Optional<Feed> updateFeed = feedRepository.findById(id);
		if(!updateFeed.isPresent()) throw new RestException(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_FEED, HttpStatus.NOT_FOUND);
		updateFeed.get().setTitle(feed.getTitle());
		updateFeed.get().setContent(feed.getContent());
		updateFeed.get().setEditdate(now);
		
		feed.setRegdate(updateFeed.get().getRegdate());
		feed.setEditdate(now);
		return updateFeed.get();
	}

	@Override
	public Long feedDelete(int id) {
		return feedRepository.deleteById(id);
	}

	
	
}