    package com.mingi.backend.user.controller;

    import com.mingi.backend.ResponseMessage;
    import com.mingi.backend.user.domain.Comment;
    import com.mingi.backend.user.dto.PostDTO;
    import com.mingi.backend.user.dto.PostDTOTest;
    import com.mingi.backend.user.service.PostService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Collections;

    @RestController
    public class PostController {

        @Autowired
        private PostService postService;

        // 게시글 목록 불러오기
        @GetMapping("/posts")
        public ResponseEntity<?> loadPostList(@RequestParam(defaultValue = "1") int page, HttpSession session) {
            Long userKey = (Long) session.getAttribute("userKey");
            Pageable pageable = PageRequest.of(page, 10); // PageRequest로 Pageable 객체 생성
            return ResponseEntity.ok(postService.pagingPost(pageable, userKey));
        }

        // 페이징에 필요한 게시글 수 불러오기
        @GetMapping("/posts/count")
        public ResponseEntity<?> loadPostCount(@RequestParam(defaultValue = "normal") String type) {
            return ResponseEntity.ok(Collections.singletonMap("data", postService.AllPostCount()));
        }

        // 서버로 데이터 제출 (게시물 작성)
        @PostMapping("/posts")
        public ResponseEntity<?> uploadPost(@RequestPart("postRequest") PostDTOTest postDTO, HttpSession session) {
            String userId = (String) session.getAttribute("userId");
            postService.savePost(postDTO, userId);
            return ResponseEntity.ok("");
        }

        // 서버로 데이터 제출 (게시물 수정)
        @PutMapping("/posts/{postId}")
        public ResponseEntity<?> editPost(@PathVariable Long postId, @RequestPart("postRequest") PostDTO postDTO, HttpSession session) {
            String userId = (String) session.getAttribute("userId");
            postService.updatePost(postDTO, postId, userId);
            return ResponseEntity.ok("");
        }

        // 게시물의 상세 정보를 적재
        @GetMapping("/posts/{postId}")
        public ResponseEntity<?> LoadPostDetail(@PathVariable Long postId, HttpServletRequest request) {
            HttpSession session = request.getSession();
            session.setAttribute("postId", postId);  // 세션에 유저 ID를 저장
            return ResponseEntity.ok(Collections.singletonMap("data", postService.getPostDetail(postId)));
        }

        // 페이지를 불러올때 조회수가 증가
        @PatchMapping("/posts/views/{postId}")
        public ResponseEntity<?> updatePostView(@PathVariable Long postId) {
            postService.updatePostView(postId);
            return ResponseEntity.ok("");
        }

        // 게시물에 댓글 표시
        @GetMapping("/comments/{postId}")
        public ResponseEntity<?> postComment(@PathVariable Long postId) {
            return ResponseEntity.ok(Collections.singletonMap("data", postService.getComment(postId)));
        }

        // 게시물 삭제 (isDelete만 Y로 변경)
        @DeleteMapping("/posts/{postId}")
        public ResponseEntity<?> deletePost(@PathVariable Long postId) {
            postService.deletePost(postId);
            return ResponseEntity.ok(new ResponseMessage("게시글이 삭제되었습니다."));
        }

        // 댓글 작성
        @PostMapping("/comments")
        public ResponseEntity<?> addComment(@RequestBody Comment comment, HttpSession session) {
            Long userKey = (Long) session.getAttribute("userKey");
            Long postId = (Long) session.getAttribute("postId");

            if(userKey == null) {
                return ResponseEntity.ok("로그인이 필요합니다.");
            } else {
                postService.addComment(comment, userKey, postId);
                return ResponseEntity.ok("");
            }
        }

//        // 댓글 삭제 (미완, 삭제 버튼이 안보임?)
//        @DeleteMapping("/comments/{commentId}")
//        public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
//            System.out.println(commentId);
//            return ResponseEntity.ok("");
//        }
    }
