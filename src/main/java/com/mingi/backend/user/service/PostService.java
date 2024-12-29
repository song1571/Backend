package com.mingi.backend.user.service;

import com.mingi.backend.CustomPage;
import com.mingi.backend.user.domain.*;
import com.mingi.backend.user.dto.PostDTO;
import com.mingi.backend.user.dto.PostDTOTest;
import com.mingi.backend.user.repository.*;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import com.querydsl.core.types.Projections;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.as;

@Service
public class PostService {

    private final JPAQueryFactory queryFactory;
    private final CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    public PostService(JPAQueryFactory queryFactory, CommentRepository commentRepository) {
        this.queryFactory = queryFactory;
        this.commentRepository = commentRepository;
    }

    // 게시판에 보여줄 게시물을 반환 (삭제안된 게시물 / 삭제안되고 자기가 작성한 게시물)
    public CustomPage<PostDTO> pagingPost(Pageable pageable, Long userKey) {
        QUser user = QUser.user;
        QPost post = QPost.post;
        List<PostDTO> content;
        if(userKey == null) {
            userKey = -1L;
        }
        content = queryFactory
                .select(Projections.constructor(
                        PostDTO.class, as(post.postId, "numbers"), post.title, post.content, post.likes, post.views,
                        post.isBlockComment, post.isPrivate, post.isDelete, post.postDate,
                        as(user.id, "writer"), as(user.profileImage, "writerImage")))
                .from(post)
                .join(user).on(user.userKey.eq(post.writer.userKey))
                .where(post.isDelete.notLike("Y").and(post.isPrivate.notLike("Y"))
                        .or(post.isDelete.notLike("Y").and(post.isPrivate.like("Y").and(post.writer.userKey.eq(userKey)))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = AllPostCount();
        Page<PostDTO> page = new PageImpl<>(content, pageable, total);
        return new CustomPage<>(page);  // CustomPage로 감싸서 반환
    }

    // 비공개나 삭제되지 않은 게시물의 전체 수를 반환
    public Long AllPostCount() {
        QPost post = QPost.post;
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.isDelete.notLike("Y"))
                .fetchOne();
    }

    // DB에 게시물을 저장
    public void savePost(PostDTOTest postSaveDTO, String userId) {
        Post post = new Post();
        post.setTitle(postSaveDTO.getTitle());
        post.setContent(postSaveDTO.getContent());
        post.setIsDelete("N");
        LocalDateTime time = LocalDateTime.now();
        post.setPostDate(time);
        if(postSaveDTO.getPrivatePost() == null || postSaveDTO.getPrivatePost().equals("false")) {
            post.setIsPrivate("N");
        }
        else {
            post.setIsPrivate("Y");
        }
        if(postSaveDTO.getBlockComment() == null || postSaveDTO.getBlockComment().equals("false")) {
            post.setIsBlockComment("N");
        }
        else {
            post.setIsBlockComment("Y");
        }
        QUser user = QUser.user;
        Long userKey = queryFactory
                .select(user.userKey)
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
        User writer = userRepository.findById(userKey).orElseThrow(() -> new RuntimeException("User not found"));
        post.setWriter(writer);
        postRepository.save(post);

        List<String> tagDatas = postSaveDTO.getTags();
        for(String tags : tagDatas) {
            QTag tag = QTag.tag;
            Long tagId = queryFactory
                    .select(tag.tagId)
                    .from(tag)
                    .where(tag.tagData.eq(String.valueOf(tags)))
                    .fetchOne();
            if(tagId == null) {
                Tag tagData = new Tag();
                tagData.setTagData(String.valueOf(tags));
                tagRepository.save(tagData);
                tagId = queryFactory
                        .select(tag.tagId)
                        .from(tag)
                        .where(tag.tagData.eq(String.valueOf(tags)))
                        .fetchOne();
            }
            PostTag postTag = new PostTag();
            Tag getTag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("TagId not found"));
            postTag.setTag(getTag);

            QPost post1 = QPost.post;
            Long postId = queryFactory
                    .select(post1.postId)
                    .from(post1)
                    .where(post1.title.eq(post.getTitle()).and(post1.postDate.eq(time)))
                    .fetchOne();
            Post getPost = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("PostId not found"));
            postTag.setPost(getPost);
//            System.out.println(postTag);
//            postTagRepository.save(postTag);
        }
    }

    // DB에 수정된 게시물을 저장
    public void updatePost(PostDTO postSaveDTO, Long postId, String userId) {
        Post post = new Post();
        post.setPostId(postId);
        post.setTitle(postSaveDTO.getTitle());
        post.setContent(postSaveDTO.getContent());
        post.setIsDelete("N");

        PostDTO PostData = getPostDetail(postId); // 아니 받아오는 데이터에 조회수랑 추천수가 없잖아
        post.setLikes(PostData.getLikes());
        post.setViews(PostData.getViews());

        post.setPostDate(LocalDateTime.now());
        if(postSaveDTO.getPrivatePost() == null || postSaveDTO.getPrivatePost().equals("false")) {
            post.setIsPrivate("N");
        } else {
            post.setIsPrivate("Y");
        }
        if(postSaveDTO.getBlockComment() == null || postSaveDTO.getBlockComment().equals("false")) {
            post.setIsBlockComment("N");
        } else {
            post.setIsBlockComment("Y");
        }

        QUser user = QUser.user;
        Long userKey = queryFactory
                .select(user.userKey)
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
        User writer = userRepository.findById(userKey).orElseThrow(() -> new RuntimeException("User not found"));
        post.setWriter(writer);
        postRepository.save(post);
    }

    // 게시글 조회 수 증가
    public void updatePostView(Long postId) {
       QPost post = QPost.post;
       Post postData = queryFactory
               .selectFrom(post)
               .where(post.postId.eq(postId))
               .fetchOne();
       postData.setViews(postData.getViews() + 1);
       postRepository.save(postData);
    }

    // 해당 게시물 상세 정보를 반환
    public PostDTOTest getPostDetail(Long postId) {
        // 태그 삽입 기능
        QPostTag postTag = QPostTag.postTag;
        List<Long> tagPostData;
        List<String> tagData;
        tagPostData = queryFactory
                .select(postTag.tag.tagId)
                .from(postTag)
                .where(postTag.post.postId.eq(postId))
                .fetch();
        if(!tagPostData.isEmpty()) { // 태그가 존재하면 찾아서 넣기
            QTag tag = QTag.tag;
            tagData = queryFactory
                    .select(tag.tagData)
                    .from(tag)
                    .where(tag.tagId.in(tagPostData))
                    .fetch();
        } else {
            tagData = List.of(); // 없으면 공란으로 넣기
        }

        QPost post = QPost.post;
        QUser user = QUser.user;

        return queryFactory
                .select(Projections.constructor(
                        PostDTOTest.class, as(post.postId, "postId"), post.title, post.content, post.likes, post.views,
                        post.isBlockComment, post.isPrivate, post.isDelete, post.postDate,
                        as(user.id, "writer"), as(user.profileImage, "writerImage"), ConstantImpl.create(tagData)))
                .from(post)
                .join(user).on(user.userKey.eq(post.writer.userKey))
                .where(post.postId.eq(postId))
                .fetchOne();
    }

    // 게시물에 보여줄 댓글을 반환
    public List<Comment> getComment(Long postId) {
        QComment comment = QComment.comment;
        return queryFactory
                .selectFrom(comment)
                .where(comment.post.postId.eq(postId).and(comment.isDelete.like("N")))
                .fetch();
    }

    // 게시물이 삭제되어 비공개가 되도록 DB에 저장
    public void deletePost(Long postId) {
        QPost post = QPost.post;
        Post postData = queryFactory
                .selectFrom(post)
                .where(post.postId.eq(postId))
                .fetchOne();
        postData.setIsDelete("Y");
        postRepository.save(postData);
    }

    // 게시물에 댓글 추가
    public void addComment(Comment comment, Long userKey, Long postId) {
        comment.setPostDate(LocalDateTime.now());
        comment.setIsDelete("N");
        comment.setWriter(comment.getWriter());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        comment.setPost(post);

        User user = userRepository.findById(userKey)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        comment.setWriter(user);

        commentRepository.save(comment);
    }
}
