package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant{

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

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

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostById, Model model, Page page) {
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostById);
        model.addAttribute("post", post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        //点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostById);
        model.addAttribute("likeCount", likeCount);
        //点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostById);
        model.addAttribute("likeStatus", likeStatus);

        //分页
        page.setRows(post.getCommentCount());
        page.setPath("/discuss/detail/" +  post.getId());
        page.setLimit(5);

        //评论
        List<Comment> commentsList = commentService.findCommentsByEntity(CommunityConstant.ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> comments = new ArrayList<>();
        if (commentsList != null) {
            for (Comment comment : commentsList) {
                Map<String, Object> commentMap = new HashMap<>();
                //评论
                commentMap.put("comment", comment);
                //作者
                commentMap.put("user", userService.findUserById(comment.getUserId()));
                // 点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("likeCount", likeCount);
                // 点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("likeStatus", likeStatus);

                List<Comment> replyList = commentService.findCommentsByEntity(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId(),0, Integer.MAX_VALUE);
                List<Map<String, Object>> replys = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyMap = new HashMap<>();
                        replyMap.put("reply", reply);
                        replyMap.put("user", userService.findUserById(reply.getUserId()));
                        User target = reply.getTargetId() == 0?null:userService.findUserById(reply.getTargetId());
                        replyMap.put("target", target);
                        // 点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyMap.put("likeCount", likeCount);
                        // 点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyMap.put("likeStatus", likeStatus);
                        replys.add(replyMap);
                    }
                    commentMap.put("replys", replys);
                }

                int replyCount = commentService.findCommentCount(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("replyCount", replyCount);
                comments.add(commentMap);
            }
        }

        model.addAttribute("comments", comments);

        return "/site/discuss-detail";
    }
}
