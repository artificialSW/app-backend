package org.dcode.artificialswbackend.community;

import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.dto.CommentRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionCreateRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionDetailResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionResponseDto;
import org.dcode.artificialswbackend.community.dto.FlowerResultDto;
import org.dcode.artificialswbackend.community.dto.CommentResponseDto;
import org.dcode.artificialswbackend.community.dto.FamilyMembersResponseDto;
import org.dcode.artificialswbackend.community.dto.QuestionWithCommentsResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionWithCommentsResponseDto;
import org.dcode.artificialswbackend.community.dto.MyQuestionsResponseDto;
import org.dcode.artificialswbackend.community.dto.LikeResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionsResponseDto;

import org.dcode.artificialswbackend.community.entity.PersonalQuestions;
import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.dcode.artificialswbackend.community.entity.Comment;
import org.dcode.artificialswbackend.community.entity.Like;
import org.dcode.artificialswbackend.community.entity.QuestionList;
import org.dcode.artificialswbackend.community.entity.Users;
import org.dcode.artificialswbackend.community.entity.QuestionReference;

// ...existing code...
import org.dcode.artificialswbackend.community.repository.PersonalQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.PublicQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.CommentRepository;
import org.dcode.artificialswbackend.community.repository.LikeRepository;
import org.dcode.artificialswbackend.signup.repository.FamiliesRepository;
import org.dcode.artificialswbackend.community.repository.QuestionListRepository;
import org.dcode.artificialswbackend.community.repository.QuestionReferenceRepository;
import org.dcode.artificialswbackend.community.repository.UsersRepository;
import org.dcode.artificialswbackend.community.service.PredictionService;
import org.dcode.artificialswbackend.community.service.FlowerService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Optional;


@Service
public class CommunityService {
    private final PersonalQuestionsRepository personalQuestionsRepository;
    private final PublicQuestionsRepository publicQuestionsRepository;
    private final CommentRepository commentRepository;
    private final QuestionListRepository questionListRepository;
    private final FamiliesRepository familiesRepository;
    private final QuestionReferenceRepository questionReferenceRepository;
    private final UsersRepository usersRepository;
    private final LikeRepository likeRepository;
    private final PredictionService predictionService;
    private final FlowerService flowerService;

    public CommunityService(PersonalQuestionsRepository personalQuestionsRepository, 
                          PublicQuestionsRepository publicQuestionsRepository,  
                          CommentRepository commentRepository, 
                          QuestionListRepository questionListRepository,
                          FamiliesRepository familiesRepository,
                          QuestionReferenceRepository questionReferenceRepository,
                          UsersRepository usersRepository,
                          LikeRepository likeRepository,
                          PredictionService predictionService,
                          FlowerService flowerService) {
        this.personalQuestionsRepository = personalQuestionsRepository;
        this.publicQuestionsRepository = publicQuestionsRepository;
        this.commentRepository = commentRepository;
        this.questionListRepository = questionListRepository;
        this.familiesRepository = familiesRepository;
        this.questionReferenceRepository = questionReferenceRepository;
        this.usersRepository = usersRepository;
        this.likeRepository = likeRepository;
        this.predictionService = predictionService;
        this.flowerService = flowerService;
    }

    public Map<String, Object> getQuestionsWithUnsolvedCount(Long receiverId, Long familyId){
        List<PersonalQuestions> allQuestions = personalQuestionsRepository.findByFamilyId(familyId);
        long unsolvedCount = personalQuestionsRepository.countByFamilyIdAndReceiverAndSolvedFalse(familyId, receiverId);

        List<PersonalQuestionDto> questions = allQuestions.stream()
                .map(PersonalQuestionDto::fromEntity)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("questions", questions);
        result.put("unsolved", unsolvedCount);

        return result;
    }
    public PublicQuestionsResponseDto getPublicQuestions(Long userId, Long familyId) {
        // 1. 가장 최신 공개 질문 찾기 (생성 날짜 기준 최신순)
        Optional<PublicQuestions> latestPublicQuestionOpt = publicQuestionsRepository.findTopByFamilyIdOrderByCreatedAtDesc(familyId);
        Long latestQuestionId = latestPublicQuestionOpt.map(PublicQuestions::getId).orElse(null);
        
        // 2. question_reference에서 public question type인 것들만 가져오기
        List<QuestionReference> publicQuestionRefs = questionReferenceRepository.findByFamilyIdAndQuestionType(familyId, QuestionReference.QuestionType.Public);
        
        List<PublicQuestionsResponseDto.PublicQuestionDto> questions = new ArrayList<>();
        
        for (QuestionReference qRef : publicQuestionRefs) {
            // public_questions에서 실제 질문 정보 가져오기
            Optional<PublicQuestions> publicQuestionOpt = publicQuestionsRepository.findById(qRef.getQuestionId());
            
            if (publicQuestionOpt.isPresent()) {
                PublicQuestions pq = publicQuestionOpt.get();
                
                // 3. 가장 최신 질문이면 제외
                if (latestQuestionId != null && pq.getId().equals(latestQuestionId)) {
                    continue;
                }
                
                // 해당 질문의 댓글 수 계산
                int commentCount = commentRepository.findByQuestionRefId(qRef.getId()).size();
                
                // 새로운 likes 테이블에서 좋아요 수 조회
                long likesCount = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.public_question, pq.getId());
                
                // 사용자가 이 질문에 좋아요를 눌렀는지 확인
                boolean isLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.public_question, pq.getId());
                
                PublicQuestionsResponseDto.PublicQuestionDto questionDto = new PublicQuestionsResponseDto.PublicQuestionDto(
                    qRef.getId(),
                    pq.getContent(),
                    (int) likesCount,
                    commentCount,
                    pq.getCreated_at() != null ? pq.getCreated_at().toString() : "",
                    isLiked
                );
                
                questions.add(questionDto);
            }
        }
        
        return new PublicQuestionsResponseDto(questions);
    }


    public MyQuestionsResponseDto getMyQuestions(String userId, Long familyId) {
        Long userIdLong = Long.valueOf(userId);
        
        // solved가 false인 나의 질문들만 조회 (나에게 온 질문)
        List<PersonalQuestions> unsolvedQuestions = personalQuestionsRepository
                .findByFamilyIdAndReceiverAndSolvedFalse(familyId, userIdLong);
        
        // QuestionReference를 통해 question_ref_id 조회 및 응답 DTO 생성
        List<MyQuestionsResponseDto.MyQuestionDto> questionDtos = new ArrayList<>();
        
        for (PersonalQuestions pq : unsolvedQuestions) {
            // personal_questions의 id로 question_reference에서 question_ref_id 찾기
            Optional<QuestionReference> questionRefOpt = questionReferenceRepository
                    .findByQuestionIdAndQuestionType(pq.getId(), QuestionReference.QuestionType.Personal);
            
            if (questionRefOpt.isPresent()) {
                QuestionReference qRef = questionRefOpt.get();
                
                // sender의 role 정보 조회
                String senderRole = getUserRole(pq.getSender());
                
                MyQuestionsResponseDto.MyQuestionDto dto = new MyQuestionsResponseDto.MyQuestionDto(
                        qRef.getId(),  // question_ref_id
                        pq.getContent(),
                        pq.getSender(),
                        senderRole,
                        pq.getVisibility()
                );
                
                questionDtos.add(dto);
            }
        }
        
        return new MyQuestionsResponseDto(questionDtos);
    }

    public CommentResponseDto saveComment(Long userId, CommentRequestDto request, Long familyId) {
        // 디버깅용 로그 추가
        System.out.println("DEBUG - saveComment called:");
        System.out.println("  userId: " + userId);
        System.out.println("  questionRefId: " + request.getQuestionRefId());
        System.out.println("  content: " + request.getContent());
        System.out.println("  familyId: " + familyId);
        
        Comment comment = new Comment();
        comment.setQuestionRefId(request.getQuestionRefId());
        comment.setContent(request.getContent());
        comment.setWriter(userId);
        comment.setReplyTo(request.getReplyTo()); // null 가능
        comment.setFamilyId(familyId); // familyId 추가

        // 저장 전 comment 객체 상태 확인
        System.out.println("DEBUG - Before save:");
        System.out.println("  comment.questionRefId: " + comment.getQuestionRefId());
        System.out.println("  comment.familyId: " + comment.getFamilyId());
        
        // --- 꽃 생성 로직 추가 (댓글 저장 전에 실행) ---
        System.out.println("DEBUG - Starting flower generation process for questionRefId: " + request.getQuestionRefId());
        
        // 1. question_reference에서 해당 ref id의 question type 확인
        Optional<QuestionReference> qRefOpt = questionReferenceRepository.findById(request.getQuestionRefId());
        System.out.println("DEBUG - QuestionReference found: " + qRefOpt.isPresent());
        
        qRefOpt.ifPresent(qRef -> {
            System.out.println("DEBUG - Question type: " + qRef.getQuestionType());
            System.out.println("DEBUG - Question ID: " + qRef.getQuestionId());
            if (qRef.getQuestionType() == QuestionReference.QuestionType.Personal) {
                System.out.println("DEBUG - Processing Personal question...");
                // Personal 질문에 대한 꽃 생성 로직 (questionId 전달)
                FlowerResultDto flowerResult = checkAndProcessPersonalQuestionCompletion(
                    qRef.getQuestionId(), userId, familyId);
                if (flowerResult != null) {
                    System.out.println("🌸 Personal question flower created: " + flowerResult.getFlower());
                }
            } else if (qRef.getQuestionType() == QuestionReference.QuestionType.Public) {
                // Public 질문에 대한 꽃 생성 로직 (questionRefId 전달)
                FlowerResultDto flowerResult = checkAndProcessPublicQuestionCompletion(
                    request.getQuestionRefId(), familyId);
                if (flowerResult != null) {
                    System.out.println("🌸 Public question flower created: " + flowerResult.getFlower());
                }
            }
        });
        // --- 끝 ---

        Comment saved = commentRepository.save(comment);
        return new CommentResponseDto(saved.getId(), saved.getContent());
    }

    @Transactional
    public LikeResponseDto toggleLike(String type, Long targetId, Long userId, Long familyId) {
        // 1. 유효한 타입인지 확인
        Like.TargetType targetType;
        Long realTargetId = targetId;
        if (type.equals("question") || type.equals("public_question")) {
            // targetId는 question_ref_id임. 실제 questionId와 type을 찾아야 함
            Optional<QuestionReference> qRefOpt = questionReferenceRepository.findById(targetId);
            if (qRefOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid question_ref_id: " + targetId);
            }
            QuestionReference qRef = qRefOpt.get();
            if (type.equals("question") && qRef.getQuestionType() != QuestionReference.QuestionType.Personal) {
                throw new IllegalArgumentException("question_ref_id does not point to a personal question");
            }
            if (type.equals("public_question") && qRef.getQuestionType() != QuestionReference.QuestionType.Public) {
                throw new IllegalArgumentException("question_ref_id does not point to a public question");
            }
            realTargetId = qRef.getQuestionId();
        }
        try {
            targetType = Like.TargetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown like type: " + type);
        }
        // 2. 가족 구성원 검증
        validateFamilyMembership(userId, familyId, targetType, realTargetId);
        // 3. 기존 좋아요 확인
        Optional<Like> existingLike = likeRepository.findByUserIdAndTargetTypeAndTargetId(userId, targetType, realTargetId);
        boolean isLiked;
        if (existingLike.isPresent()) {
            // 좋아요 취소
            likeRepository.delete(existingLike.get());
            decreaseLikes(targetType, realTargetId);
            isLiked = false;
        } else {
            // 좋아요 추가
            Like newLike = new Like(userId, targetType, realTargetId, familyId);
            likeRepository.save(newLike);
            increaseLikes(targetType, realTargetId);
            isLiked = true;
        }
        // 4. 현재 총 좋아요 수 조회
        long totalLikes = likeRepository.countByTargetTypeAndTargetId(targetType, realTargetId);
        return new LikeResponseDto(isLiked, totalLikes);
    }
    
    private void validateFamilyMembership(Long userId, Long familyId, Like.TargetType targetType, Long targetId) {
        // 1. 사용자가 해당 가족 구성원인지 확인
        if (!usersRepository.existsByIdAndFamilyId(userId, familyId)) {
            throw new SecurityException("Family member access required");
        }
        
        // 2. 타겟이 해당 가족의 것인지 확인 (추가 보안)
        switch (targetType) {
            case question -> {
                if (!personalQuestionsRepository.existsByIdAndFamilyId(targetId, familyId)) {
                    throw new SecurityException("Question not found in this family");
                }
            }
            case public_question -> {
                if (!publicQuestionsRepository.existsByIdAndFamilyId(targetId, familyId)) {
                    throw new SecurityException("Public question not found in this family");
                }
            }
            case comment -> {
                if (!commentRepository.existsByIdAndFamilyId(targetId, familyId)) {
                    throw new SecurityException("Comment not found in this family");
                }
            }
        }
    }
    
    private void increaseLikes(Like.TargetType targetType, Long targetId) {
        switch (targetType) {
            case question -> personalQuestionsRepository.increaseLikes(targetId);
            case public_question -> publicQuestionsRepository.increaseLikes(targetId);
            case comment -> commentRepository.increaseLikes(targetId);
        }
    }
    
    private void decreaseLikes(Like.TargetType targetType, Long targetId) {
        switch (targetType) {
            case question -> personalQuestionsRepository.decreaseLikes(targetId);
            case public_question -> publicQuestionsRepository.decreaseLikes(targetId);
            case comment -> commentRepository.decreaseLikes(targetId);
        }
    }

    @Transactional
    public Long createQuestion(Long senderId, QuestionCreateRequestDto request, Long familyId) {
        // 1. PersonalQuestions 엔티티 생성 및 저장
        PersonalQuestions question = new PersonalQuestions();
        question.setFamilyId(familyId);
        question.setContent(request.getContent());
        question.setSender(senderId);
        question.setReceiver(request.getReceiverId());
        question.setVisibility(request.getVisibility() == 1); // Integer를 Boolean으로 변환
        question.setSolved(false);
        question.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));
        question.setUpdated_at(new java.sql.Timestamp(System.currentTimeMillis()));

        PersonalQuestions saved = personalQuestionsRepository.save(question);

        // DB 트리거가 QuestionReference에 자동으로 추가하므로, 여기서 직접 추가하지 않음

        // question_ref_id 반환 (트리거로 생성된 값을 조회해야 할 경우, 별도 로직 필요)
        // 임시로 질문의 id 반환 (필요시 수정)
        return saved.getId();
    }

    public QuestionWithCommentsResponseDto getQuestionDetail(Long questionRefId, Long userId) {
        // 1. QuestionReference에서 질문 정보 가져오기
        Optional<QuestionReference> questionRefOpt = questionReferenceRepository.findById(questionRefId);
        if (questionRefOpt.isEmpty()) {
            throw new RuntimeException("Question reference not found");
        }
        
        QuestionReference questionRef = questionRefOpt.get();
        // 2. 질문 타입에 따라 실제 질문 데이터 가져오기
        QuestionWithCommentsResponseDto.QuestionInfo questionInfo;
        boolean questionIsLiked = false;
        if (questionRef.getQuestionType() == QuestionReference.QuestionType.Personal) {
            Optional<PersonalQuestions> personalQuestionOpt = personalQuestionsRepository.findById(questionRef.getQuestionId());
            if (personalQuestionOpt.isEmpty()) {
                throw new RuntimeException("Personal question not found");
            }
            PersonalQuestions pq = personalQuestionOpt.get();
            // 새로운 likes 테이블에서 좋아요 수 조회
            long likesCount = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.question, pq.getId());
            // sender의 role 정보 조회
            String senderRole = "Unknown";
            if (pq.getSender() != null) {
                Optional<Users> senderUser = usersRepository.findById(pq.getSender());
                if (senderUser.isPresent() && senderUser.get().getFamilyType() != null) {
                    senderRole = senderUser.get().getFamilyType().toString();
                }
            }
            // isLiked 계산
            questionIsLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.question, pq.getId());
            questionInfo = new QuestionWithCommentsResponseDto.QuestionInfo(
                questionRefId,
                pq.getContent(),
                pq.getSender(),
                senderRole,
                (int) likesCount,
                questionIsLiked,
                pq.getCreated_at() != null ? pq.getCreated_at().toString() : "2025-08-12"
            );
        } else {
            Optional<PublicQuestions> publicQuestionOpt = publicQuestionsRepository.findById(questionRef.getQuestionId());
            if (publicQuestionOpt.isEmpty()) {
                throw new RuntimeException("Public question not found");
            }
            PublicQuestions pq = publicQuestionOpt.get();
            // 새로운 likes 테이블에서 좋아요 수 조회
            long likesCount = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.public_question, pq.getId());
            // isLiked 계산
            questionIsLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.public_question, pq.getId());
            questionInfo = new QuestionWithCommentsResponseDto.QuestionInfo(
                questionRefId,
                pq.getContent(),
                0L, // public question은 시스템에서 생성 (0L로 변경)
                "System", // public question은 시스템에서 생성
                (int) likesCount,
                questionIsLiked,
                pq.getCreated_at() != null ? pq.getCreated_at().toString() : "2025-08-12"
            );
        }
        // 3. 효율적으로 모든 댓글 조회 (한 번의 쿼리)
        List<Comment> allComments = commentRepository.findByQuestionRefId(questionRefId);
        // 4. 메모리에서 대댓글 그룹핑 (N+1 쿼리 방지)
        Map<Long, List<Comment>> replyMap = allComments.stream()
                .filter(comment -> comment.getReplyTo() != null)
                .collect(Collectors.groupingBy(Comment::getReplyTo));
        // 5. 최상위 댓글들만 필터링
        List<Comment> rootComments = allComments.stream()
                .filter(comment -> comment.getReplyTo() == null)
                .collect(Collectors.toList());
        // 6. 응답 생성
        List<QuestionWithCommentsResponseDto.CommentInfo> commentInfos = rootComments.stream()
                .map(comment -> {
                    // 해당 댓글에 대한 대댓글들의 전체 정보 가져오기
                    List<QuestionWithCommentsResponseDto.CommentInfo> replyInfos = replyMap.getOrDefault(comment.getId(), new ArrayList<>())
                            .stream()
                            .map(reply -> {
                                // 대댓글 좋아요 수 조회
                                long replyLikes = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.comment, reply.getId());
                                // 대댓글 작성자의 role 조회
                                String replyWriterRole = getUserRole(reply.getWriter());
                                // 대댓글 isLiked
                                boolean replyIsLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.comment, reply.getId());
                                return new QuestionWithCommentsResponseDto.CommentInfo(
                                    reply.getId(),
                                    reply.getWriter(),
                                    replyWriterRole,
                                    reply.getContent(),
                                    (int) replyLikes,
                                    replyIsLiked,
                                    new ArrayList<>() // 대댓글의 대댓글은 없다고 가정
                                );
                            })
                            .collect(Collectors.toList());
                    // 새로운 likes 테이블에서 댓글 좋아요 수 조회
                    long commentLikes = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.comment, comment.getId());
                    // 댓글 작성자의 role 조회
                    String commentWriterRole = getUserRole(comment.getWriter());
                    // 댓글 isLiked
                    boolean commentIsLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.comment, comment.getId());
                    return new QuestionWithCommentsResponseDto.CommentInfo(
                        comment.getId(),
                        comment.getWriter(),
                        commentWriterRole,
                        comment.getContent(),
                        (int) commentLikes,
                        commentIsLiked,
                        replyInfos
                    );
                })
                .collect(Collectors.toList());
        return new QuestionWithCommentsResponseDto(questionInfo, commentInfos);
    }

    @Transactional
    public void updatePublicQuestion(Long familyId) {
        // question_list에서 가장 낮은 ID 순으로 하나씩 가져와서 public_questions에 추가
        Optional<QuestionList> questionListOpt = questionListRepository.findTopByOrderByIdAsc();
        
        if (questionListOpt.isPresent()) {
            QuestionList questionList = questionListOpt.get();
            
            // 현재 가장 높은 counts 값을 해당 family에서 찾아서 +1
            Optional<PublicQuestions> latestQuestion = publicQuestionsRepository.findTopByFamilyIdOrderByCountsDesc(familyId);
            int nextCount = latestQuestion.map(pq -> pq.getCounts() + 1).orElse(1);
            
            // public_questions에 추가
            PublicQuestions publicQuestion = new PublicQuestions();
            publicQuestion.setFamilyId(familyId);
            publicQuestion.setContent(questionList.getContent());
            publicQuestion.setCounts(nextCount); // counts를 질문 번호로 사용
            publicQuestion.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));
            publicQuestion.setUpdated_at(new java.sql.Timestamp(System.currentTimeMillis()));
            
            publicQuestionsRepository.save(publicQuestion);
            
            // question_list에서 해당 질문 삭제
            questionListRepository.delete(questionList);
        }
    }

    public PublicQuestionResponseDto getLatestPublicQuestion(Long familyId) {
        Optional<PublicQuestions> latestQuestion = publicQuestionsRepository.findTopByFamilyIdOrderByCountsDesc(familyId);
        
        if (latestQuestion.isPresent()) {
            PublicQuestions question = latestQuestion.get();
            return new PublicQuestionResponseDto(
                question.getId(),
                question.getContent(),
                question.getCounts()
            );
        }
        
        return null; // 질문이 없는 경우
    }

    public PublicQuestionWithCommentsResponseDto getPublicQuestionDetail(Long questionRefId, Long userId) {
        // 1. QuestionReference에서 질문 정보 가져오기
        Optional<QuestionReference> questionRefOpt = questionReferenceRepository.findById(questionRefId);
        if (questionRefOpt.isEmpty()) {
            throw new RuntimeException("Question reference not found");
        }
        
        QuestionReference questionRef = questionRefOpt.get();
        // 2. Public Question 데이터 가져오기
        Optional<PublicQuestions> publicQuestionOpt = publicQuestionsRepository.findById(questionRef.getQuestionId());
        if (publicQuestionOpt.isEmpty()) {
            throw new RuntimeException("Public question not found");
        }
        PublicQuestions pq = publicQuestionOpt.get();
        // 3. 질문 정보 생성 (새로운 likes 테이블에서 좋아요 수 조회)
        long likesCount = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.public_question, pq.getId());
        boolean questionIsLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.public_question, pq.getId());
        PublicQuestionWithCommentsResponseDto.QuestionInfo questionInfo = 
            new PublicQuestionWithCommentsResponseDto.QuestionInfo(
                questionRefId,
                pq.getContent(),
                (int) likesCount,
                questionIsLiked,
                pq.getCreated_at() != null ? pq.getCreated_at().toString() : "2025-08-12",
                pq.getCounts()
            );
        // 4. 효율적으로 모든 댓글 조회 (한 번의 쿼리)
        List<Comment> allComments = commentRepository.findByQuestionRefId(questionRefId);
        // 5. 메모리에서 대댓글 그룹핑 (N+1 쿼리 방지)
        Map<Long, List<Comment>> replyMap = allComments.stream()
                .filter(comment -> comment.getReplyTo() != null)
                .collect(Collectors.groupingBy(Comment::getReplyTo));
        // 6. 최상위 댓글들만 필터링
        List<Comment> rootComments = allComments.stream()
                .filter(comment -> comment.getReplyTo() == null)
                .collect(Collectors.toList());
        // 7. 응답 생성
        List<PublicQuestionWithCommentsResponseDto.CommentInfo> commentInfos = rootComments.stream()
                .map(comment -> {
                    // 해당 댓글에 대한 대댓글들의 전체 정보 가져오기
                    List<PublicQuestionWithCommentsResponseDto.CommentInfo> replyInfos = replyMap.getOrDefault(comment.getId(), new ArrayList<>())
                            .stream()
                            .map(reply -> {
                                // 대댓글 좋아요 수 조회
                                long replyLikes = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.comment, reply.getId());
                                // 대댓글 작성자의 role 조회
                                String replyWriterRole = getUserRole(reply.getWriter());
                                // 대댓글 isLiked
                                boolean replyIsLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.comment, reply.getId());
                                return new PublicQuestionWithCommentsResponseDto.CommentInfo(
                                    reply.getId(),
                                    reply.getWriter(),
                                    replyWriterRole,
                                    reply.getContent(),
                                    (int) replyLikes,
                                    replyIsLiked,
                                    reply.getReplyTo(),
                                    new ArrayList<>() // 대댓글의 대댓글은 없다고 가정
                                );
                            })
                            .collect(Collectors.toList());
                    // 새로운 likes 테이블에서 댓글 좋아요 수 조회
                    long commentLikes = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.comment, comment.getId());
                    // 댓글 작성자의 role 조회
                    String commentWriterRole = getUserRole(comment.getWriter());
                    // 댓글 isLiked
                    boolean commentIsLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.comment, comment.getId());
                    return new PublicQuestionWithCommentsResponseDto.CommentInfo(
                        comment.getId(),
                        comment.getWriter(),
                        commentWriterRole,
                        comment.getContent(),
                        (int) commentLikes,
                        commentIsLiked,
                        comment.getReplyTo(),
                        replyInfos
                    );
                })
                .collect(Collectors.toList());
        
        return new PublicQuestionWithCommentsResponseDto(questionInfo, commentInfos);
    }

    /**
     * Public Question에 모든 가족 구성원이 댓글을 달았는지 확인하고, 
     * 조건을 만족하면 외부 예측 API를 호출합니다.
     */
    private FlowerResultDto checkAndProcessPersonalQuestionCompletion(Long questionId, Long commentUserId, Long familyId) {
        System.out.println("DEBUG - checkAndProcessPersonalQuestionCompletion called:");
        System.out.println("  questionId: " + questionId);
        System.out.println("  commentUserId: " + commentUserId);
        System.out.println("  familyId: " + familyId);
        
        // 1. 해당 question이 personal question인지 확인
        Optional<PersonalQuestions> personalQuestionOpt = personalQuestionsRepository.findById(questionId);
        System.out.println("DEBUG - PersonalQuestion found: " + personalQuestionOpt.isPresent());
        if (personalQuestionOpt.isEmpty()) {
            System.out.println("DEBUG - PersonalQuestion not found, returning null");
            return null; // personal question이 아니면 처리하지 않음
        }
        
        PersonalQuestions personalQuestion = personalQuestionOpt.get();
        System.out.println("DEBUG - PersonalQuestion details:");
        System.out.println("  id: " + personalQuestion.getId());
        System.out.println("  solved: " + personalQuestion.getSolved());
        System.out.println("  receiver: " + personalQuestion.getReceiver());
        System.out.println("  sender: " + personalQuestion.getSender());
        
        // 2. 이미 solved된 질문이면 처리하지 않음
        if (personalQuestion.getSolved() != null && personalQuestion.getSolved()) {
            System.out.println("🌸 Question already solved, skipping flower creation");
            return null;
        }
        
        // 3. 댓글 작성자가 receiver인지 확인
        System.out.println("DEBUG - Checking if comment writer is receiver:");
        System.out.println("  personalQuestion.getReceiver(): " + personalQuestion.getReceiver());
        System.out.println("  commentUserId: " + commentUserId);
        System.out.println("  equals: " + personalQuestion.getReceiver().equals(commentUserId));
        
        if (!personalQuestion.getReceiver().equals(commentUserId)) {
            System.out.println("🌸 Comment writer is not receiver, skipping flower creation");
            return null; // receiver가 아니면 처리하지 않음
        }
        
        System.out.println("DEBUG - Comment writer is receiver, proceeding...");
        
        // 4. questionId로 questionRefId 찾기
        Optional<QuestionReference> questionRefOpt = questionReferenceRepository.findByQuestionIdAndQuestionType(
            questionId, QuestionReference.QuestionType.Personal);
        if (questionRefOpt.isEmpty()) {
            System.out.println("🌸 Question reference not found for questionId: " + questionId);
            return null;
        }
        Long questionRefId = questionRefOpt.get().getId();
        
        // 5. receiver의 첫 댓글인지 확인 (기존 댓글 중 receiver가 작성한 것이 있는지 체크)
        List<Comment> existingComments = commentRepository.findByQuestionRefId(questionRefId);
        System.out.println("DEBUG - Found " + existingComments.size() + " existing comments");
        
        boolean hasReceiverComment = existingComments.stream()
                .anyMatch(comment -> comment.getWriter().equals(commentUserId));
        System.out.println("DEBUG - hasReceiverComment: " + hasReceiverComment);
        System.out.println("DEBUG - commentUserId: " + commentUserId);
        
        for (Comment comment : existingComments) {
            System.out.println("DEBUG - Comment writer: " + comment.getWriter() + ", content: " + comment.getContent());
        }
        
        if (!hasReceiverComment) {
            System.out.println("🌸 This is receiver's first comment, creating flower...");
            
            // 6. receiver의 첫 댓글이므로 solved = true로 업데이트
            personalQuestion.setSolved(true);
            personalQuestionsRepository.save(personalQuestion);
            
            // 7. AI 호출하여 꽃 생성
            String predictionResult = predictionService.sendPredictionRequest(personalQuestion.getContent());
            if (predictionResult != null) {
                System.out.println("🌸 Personal question solved! Prediction result: " + predictionResult);
                
                // FlowerService를 통해 AI 응답 처리 및 꽃 저장
                FlowerResultDto flowerResult = flowerService.processAiResponseAndSaveFlower(
                        predictionResult,
                        questionRefId, 
                        familyId
                );
                
                if (flowerResult != null) {
                    System.out.println("🌸 Flower created: " + flowerResult.getFlower() + 
                                     ", New unlock: " + flowerResult.isNewlyUnlocked());
                } else {
                    System.out.println("🌸 Failed to create flower");
                }
                
                return flowerResult;
            } else {
                System.out.println("🌸 AI prediction failed");
            }
        } else {
            System.out.println("🌸 Receiver already has comments, skipping flower creation");
        }
        
        return null;
    }

    private FlowerResultDto checkAndProcessPublicQuestionCompletion(Long questionRefId, Long familyId) {
        // 1. 해당 question이 public question인지 확인
        Optional<PublicQuestions> publicQuestionOpt = publicQuestionsRepository.findById(questionRefId);
        if (publicQuestionOpt.isEmpty()) {
            return null; // public question이 아니면 처리하지 않음
        }
        
        PublicQuestions publicQuestion = publicQuestionOpt.get();
        
        // 2. 해당 family의 전체 사용자 수 조회
        List<Users> familyMembers = usersRepository.findByFamilyId(familyId);
        int totalFamilyMembers = familyMembers.size();
        
        // 3. 해당 public question에 달린 댓글의 고유 작성자 수 확인
        List<Comment> comments = commentRepository.findByFamilyIdAndQuestionRefId(familyId, questionRefId);
        long uniqueCommenters = comments.stream()
                .map(Comment::getWriter)
                .distinct()
                .count();
        
        // 4. 모든 가족 구성원이 댓글을 달았는지 확인
        if (uniqueCommenters >= totalFamilyMembers) {
            System.out.println("🎉 All family members commented on public question " + questionRefId + "!");
            
            // 5. AI 호출하여 꽃 생성
            String predictionResult = predictionService.sendPredictionRequest(publicQuestion.getContent());
            if (predictionResult != null) {
                System.out.println("� Public question completed! Prediction result: " + predictionResult);
                
                // FlowerService를 통해 AI 응답 처리 및 꽃 저장
                FlowerResultDto flowerResult = flowerService.processAiResponseAndSaveFlower(
                        predictionResult,
                        questionRefId, 
                        familyId
                );
                
                System.out.println("🌸 Public Flower created: " + flowerResult.getFlower() + 
                                 ", New unlock: " + flowerResult.isNewlyUnlocked());
                
                return flowerResult;
            }
        }
        
        return null;
    }

    public Map<String, Object> getPersonalQuestions(Long userId, Long familyId) {
        // question_reference에서 personal question type인 것들만 가져오기
        List<QuestionReference> personalQuestionRefs = questionReferenceRepository.findByFamilyIdAndQuestionType(familyId, QuestionReference.QuestionType.Personal);
        
        List<Map<String, Object>> questions = new ArrayList<>();
        
        for (QuestionReference qRef : personalQuestionRefs) {
            // personal_questions에서 실제 질문 정보 가져오기
            Optional<PersonalQuestions> personalQuestionOpt = personalQuestionsRepository.findById(qRef.getQuestionId());
            if (personalQuestionOpt.isPresent()) {
                PersonalQuestions pq = personalQuestionOpt.get();
                // solved가 true인 것만 응답
                if (Boolean.TRUE.equals(pq.getSolved())) {
                    // 해당 질문의 댓글 수 계산
                    int commentCount = commentRepository.findByQuestionRefId(qRef.getId()).size();
                    // 새로운 likes 테이블에서 좋아요 수 조회
                    long likesCount = likeRepository.countByTargetTypeAndTargetId(Like.TargetType.question, pq.getId());
                    // 사용자가 이 질문에 좋아요를 눌렀는지 확인
                    boolean isLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.question, pq.getId());
                    Map<String, Object> questionData = new HashMap<>();
                    questionData.put("question_ref_id", qRef.getId());
                    questionData.put("content", pq.getContent());
                    questionData.put("sender", pq.getSender());
                    questionData.put("receiver", pq.getReceiver());
                    questionData.put("likes", likesCount);
                    questionData.put("comments", commentCount);
                    questionData.put("visibility", pq.getVisibility() ? 1 : 0);
                    questionData.put("isLiked", isLiked);
                    questions.add(questionData);
                }
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        
        return response;
    }

    public Map<String, Object> getThisWeekQuestionWithComments(Long receiverId, Long familyId) {
        Map<String, Object> response = new HashMap<>();
        
        // 1. 이번주 최신 공개 질문 가져오기 (생성 날짜 기준 최신순)
        Optional<PublicQuestions> latestPublicQuestionOpt = publicQuestionsRepository.findTopByFamilyIdOrderByCreatedAtDesc(familyId);
        
        if (latestPublicQuestionOpt.isEmpty()) {
            // 공개 질문이 없는 경우 기본값 반환
            response.put("questions", "질문이 없습니다.");
            response.put("question_ref_id", null);
            response.put("comments", new ArrayList<>());
            response.put("unsolved", 0);
            return response;
        }
        
        PublicQuestions publicQuestion = latestPublicQuestionOpt.get();
        
        // 2. question_reference에서 해당 공개 질문의 참조 ID 찾기
        Optional<QuestionReference> questionRefOpt = questionReferenceRepository.findByQuestionIdAndQuestionType(
            publicQuestion.getId(), 
            QuestionReference.QuestionType.Public
        );
        
        Long questionRefId = questionRefOpt.map(QuestionReference::getId).orElse(null);
        
        // 3. 해당 질문에 달린 댓글들 가져오기
        List<Comment> comments = new ArrayList<>();
        if (questionRefId != null) {
            comments = commentRepository.findByQuestionRefId(questionRefId);
        }
        
        List<Map<String, Object>> commentList = comments.stream()
            .filter(comment -> comment.getReplyTo() == null)
            .map(comment -> {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("writer", comment.getWriter()); // Long 타입 그대로 반환
                commentData.put("writer_role", getUserRole(comment.getWriter()));
                commentData.put("contents", comment.getContent());
                return commentData;
            })
            .collect(Collectors.toList());
        
    // 4. receiver에게 온 개인 질문 중 답변하지 않은 개수 계산 (familyId 조건 추가)
    List<PersonalQuestions> unsolvedPersonalQuestions = personalQuestionsRepository.findByFamilyIdAndReceiverAndSolvedFalse(familyId, receiverId);
    int unsolvedCount = unsolvedPersonalQuestions.size();
        
        // 5. 응답 구성
        response.put("questions", publicQuestion.getContent());
        response.put("question_ref_id", questionRefId);
        response.put("comments", commentList);
        response.put("unsolved", unsolvedCount);
        
        return response;
    }

    public FamilyMembersResponseDto getFamilyMembersExcludingUser(Long familyId, Long excludeUserId) {
    // 해당 가족의 모든 구성원 조회
    List<Users> familyMembers = usersRepository.findByFamilyId(familyId);
    // 본인 제외
    List<FamilyMembersResponseDto.FamilyMemberDto> memberDtos = familyMembers.stream()
        .filter(user -> !user.getId().equals(excludeUserId))
        .map(user -> new FamilyMembersResponseDto.FamilyMemberDto(
            user.getId(),
            convertFamilyTypeToEnglish(user.getFamilyType())
        ))
        .collect(Collectors.toList());
    return new FamilyMembersResponseDto(memberDtos);
    }

    /**
     * 사용자 ID로 FamilyType(role) 조회하는 헬퍼 메소드
     */
    private String getUserRole(Long userId) {
        if (userId == null) {
            return "Unknown";
        }
        
        Optional<Users> userOpt = usersRepository.findById(userId);
        if (userOpt.isPresent() && userOpt.get().getFamilyType() != null) {
            return userOpt.get().getFamilyType().toString();
        }
        
        return "Unknown";
    }

    private String convertFamilyTypeToEnglish(Users.FamilyType familyType) {
        if (familyType == null) {
            return "unknown";
        }
        return switch (familyType) {
            case 아빠 -> "father";
            case 엄마 -> "mother";
            case 할아버지 -> "grandfather";
            case 할머니 -> "grandmother";
            case 자녀 -> "sibling";
        };
    }
}

