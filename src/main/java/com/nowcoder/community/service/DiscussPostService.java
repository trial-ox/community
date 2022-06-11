package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    //查询某页数据
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }
    //查询行数
    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    //插入帖子
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        //对用户输入的内容与标题中的html标签进行转义
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        //对敏感词进行过滤
        post.setContent(sensitiveFilter.filter(post.getContent()));
        post.setTitle(sensitiveFilter.filter(post.getTitle()));

        //往数据库添加记录
        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updatePostCommentCount(id, commentCount);
    }


}
