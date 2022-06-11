package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests {

	@Autowired
	DiscussPostMapper discussPostMapper;

	@Test
	void contextLoads() {

	}

	@Test
	public  void testBeanConfig() {
		System.out.println(discussPostMapper.selectDiscussPostRows(0));
		System.out.println("---------------------------");
		System.out.println(discussPostMapper.selectDiscussPostRows(101));
		System.out.println("---------------------------");
		List<DiscussPost> posts = discussPostMapper.selectDiscussPosts(0, 0, 20);
		for (DiscussPost post : posts) {
			System.out.println(post);
		}
		System.out.println("---------------------------");
		List<DiscussPost> userPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
		for (DiscussPost userPost : userPosts) {
			System.out.println(userPost);
		}



	}

}
