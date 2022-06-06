package com.nowcoder.community.controller;

import com.alibaba.fastjson2.JSON;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/detail/{discussPostById}")
    public String getDiscussPost(@PathVariable("discussPostById") int discussPostById, Model model) {
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostById);
        model.addAttribute("post", post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        //回复

        return "/site/discuss-detail";
    }
}
