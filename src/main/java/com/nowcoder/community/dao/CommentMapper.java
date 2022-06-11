package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    //根据实体查询评论
    List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit);
    //查询实体总行数
    int selectCommentCows(int entityType, int entityId);

    int insertComment(Comment comment);

}
