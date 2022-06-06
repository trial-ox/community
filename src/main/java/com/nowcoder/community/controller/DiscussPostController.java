package com.nowcoder.community.controller;

import com.alibaba.fastjson2.JSON;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        if (hostHolder.getUser() == null) {
            return CommunityUtil.getJSONString(403,"你还没登录哦!");
        }
        //补全帖子
        DiscussPost post = new DiscussPost();
        post.setTitle(title);
        post.setUserId(hostHolder.getUser().getId());
        post.setContent(content);
        post.setCreateTime(new Date());

        //报错后面统一处理

        //添加帖子
        discussPostService.addDiscussPost(post);
        return CommunityUtil.getJSONString(0,"发布成功！");
    }
}
