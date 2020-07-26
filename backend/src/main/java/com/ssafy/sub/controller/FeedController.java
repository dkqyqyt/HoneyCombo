package com.ssafy.sub.controller;

import java.util.List;

import com.ssafy.sub.repo.FeedQueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.ssafy.sub.dto.Feed;
import com.ssafy.sub.dto.User;
import com.ssafy.sub.model.response.ResponseMessage;
import com.ssafy.sub.model.response.Result;
import com.ssafy.sub.model.response.StatusCode;
import com.ssafy.sub.service.FeedService;
import com.ssafy.sub.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feeds")
public class FeedController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	private FeedService feedService;
	@Autowired
	private UserService userService;
	 
	// 1. list 조회
	@ApiOperation(value = "로그인한 유저의 홈 피드를 조회한다", response = Result.class)
	@GetMapping(value="/page")
	public ResponseEntity<Result> feedHomePage() {
		System.out.println("log - feedUserPage");
		Result result;
		
		List<Feed> feedList = feedService.feedHomePageList();
		
		// null값 처리 안하는걸로 그래도 혹시 코드 필요할지 몰라서 남겨둠
//		if(feedList==null) {
//			result = new Result(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_FEED, null);
//			return new ResponseEntity<Result>(result, HttpStatus.NO_CONTENT);
//		}
		
		result = new Result(StatusCode.OK, ResponseMessage.READ_ALL_FEEDS, feedList);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@ApiOperation(value = "유저의 개인 피드 목록을 조회한다", response = Result.class)
	@GetMapping(value="/page/{uid}")
	public ResponseEntity<Result> feedUserPage(@PathVariable String uid, Authentication authentication) {
		System.out.println("log - feedUserPage");

		int user_id = userService.findByUid(uid).getId();
		List<Feed> feedList = feedService.feedUserPageList(user_id);
		
		Result result = new Result(StatusCode.OK, ResponseMessage.READ_USER_FEEDS, feedList);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 2. list 검색 ( 기준이 애매해서 일단 비워둠 )
//	@ApiOperation(value = "feedList의 내용 중 일부를 검색한다", response = Feed.class)
//	@GetMapping(value = "/feed/search")
//	public ResponseEntity<List<Feed>> feedListSearch() {
//		System.out.println("log - feedListSearch");
//
//		List<Feed> FeedList = feedService.feedListSearch();
//		
//	return new ResponseEntity<List<Feed>>(FeedList, HttpStatus.OK);
//	}
	
	// 3. list 추가
	@ApiOperation(value = "feedList에 정보를 추가한다", response = Result.class)
	@PostMapping
	public ResponseEntity<Result> feedInsert(@RequestBody Feed feed, Authentication authentication) {
		System.out.println("log - feedInsert");

		User user = (User) authentication.getPrincipal();
		feed.setUid(user.getId());
		
		feedService.feedInsert(feed);
		
		Result result = new Result(StatusCode.CREATED, ResponseMessage.CREATE_FEED, null);
		return new ResponseEntity<Result>(result, HttpStatus.CREATED);
	}
	
	// 4. list 상세
	@ApiOperation(value = "특정 feed를 조회한다", response = Result.class)
	@GetMapping(value = "/{id}")
	public ResponseEntity<Result> feedDetail(@PathVariable int id) {
		System.out.println("log - feedDetail");

		Feed feed = feedService.feedDetail(id);
		
		Result result = new Result(StatusCode.OK, ResponseMessage.READ_FEED, feed);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 5. list 수정
	@ApiOperation(value = "feed의 정보를 수정한다", response = Result.class)
	@PutMapping(value = "/{id}")
	public ResponseEntity<Result> feedUpdate(@PathVariable int id, @RequestBody Feed feed) {
		System.out.println("log - feedUpdate");

		Feed updateFeed = feedService.feedUpdate(id, feed);
		
		Result result = new Result(StatusCode.OK, ResponseMessage.UPDATE_FEED, updateFeed);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}
	
	// 6. list 삭제
	@ApiOperation(value = "feed의 정보를 삭제한다", response = Result.class)
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Result> feedDelete(@PathVariable int id) {
		System.out.println("log - feedDelete");

		feedService.feedDelete(id);
		
		Result result = new Result(StatusCode.OK, ResponseMessage.DELETE_FEED, null);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 7. list by follower 조회
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header") })
	@ApiOperation(value = "팔로우한 유저들의 피드", response = Result.class)
	@PostMapping(value="/page/follower")
	public ResponseEntity<Result> feedFollowerPage() {
		System.out.println("log - feedFollowerPage");
		Result result;
		
		String id;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		id = authentication.getName();
		
		List<Feed> feedList = feedService.findAllByFollower(Integer.parseInt(id));

		result = new Result(StatusCode.OK, ResponseMessage.READ_ALL_FEEDS, feedList);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}
}