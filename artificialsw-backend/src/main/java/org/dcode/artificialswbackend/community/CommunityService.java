package org.dcode.artificialswbackend.community;

import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionDto;
import org.dcode.artificialswbackend.community.dto.CommentRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionCreateRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionDetailResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionResponseDto;
import org.dcode.artificialswbackend.community.dto.FlowerResultDto;
import org.dcode.artificialswbackend.community.dto.CommentResponseDto;

import org.dcode.artificialswbackend.community.entity.PersonalQuestions;
import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.dcode.artificialswbackend.community.entity.Comment;
import org.dcode.artificialswbackend.community.entity.QuestionList;
import org.dcode.artificialswbackend.community.entity.Users;
import org.dcode.artificialswbackend.community.entity.QuestionReference;

import org.dcode.artificialswbackend.community.repository.CommentRepository;
import org.dcode.artificialswbackend.community.repository.PersonalQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.PublicQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.QuestionListRepository;
import org.dcode.artificialswbackend.community.repository.FamiliesRepository;
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
    private final PredictionService predictionService;
    private final FlowerService flowerService;

    public CommunityService(PersonalQuestionsRepository personalQuestionsRepository, 
                          PublicQuestionsRepository publicQuestionsRepository,  
                          CommentRepository commentRepository, 
                          QuestionListRepository questionListRepository,
                          FamiliesRepository familiesRepository,
                          QuestionReferenceRepository questionReferenceRepository,
                          UsersRepository usersRepository,
                          PredictionService predictionService,
                          FlowerService flowerService) {
        this.personalQuestionsRepository = personalQuestionsRepository;
        this.publicQuestionsRepository = publicQuestionsRepository;
        this.commentRepository = commentRepository;
        this.questionListRepository = questionListRepository;
        this.familiesRepository = familiesRepository;
        this.questionReferenceRepository = questionReferenceRepository;
        this.usersRepository = usersRepository;
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
    public Map<String, Object> getPublicQuestions(Long familyId) {
        List<PublicQuestions> publicQuestions = publicQuestionsRepository.findByFamilyId(familyId);

        List<PublicQuestionDto> questions = publicQuestions.stream()
                .map(e -> new PublicQuestionDto(
                        e.getId(),
                        e.getContent(),
                        e.getLikes(),
                        e.getCounts()
                ))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("questions", questions);

        return result;
    }


    public List<PersonalQuestionDto> getMyQuestions(String userId, Long familyId) {
        Long userIdLong = Long.valueOf(userId);
        List<PersonalQuestions> questions = personalQuestionsRepository.findByFamilyIdAndReceiver(familyId, userIdLong);

        return questions.stream().map(PersonalQuestionDto::fromEntity).collect(Collectors.toList());
    }

    public CommentResponseDto saveComment(Long userId, CommentRequestDto request, Long familyId) {
        Comment comment = new Comment();
        comment.setQuestionRefId(request.getQuestionRefId());
        comment.setContent(request.getContent());
        comment.setWriter(userId);
        comment.setReplyTo(request.getReplyTo()); // null 가능
        comment.setLikes(0);

        Comment saved = commentRepository.save(comment);
        
        // 댓글 저장 후 개인 질문 해결 체크 (receiver의 첫 댓글인 경우)
        FlowerResultDto flowerResult = checkAndProcessPersonalQuestionCompletion(request.getQuestionRefId(), userId, familyId);
        
        // 댓글 저장 후 public question 완료 체크
        checkAndProcessPublicQuestionCompletion(request.getQuestionRefId(), familyId);
        
        // 응답 DTO 생성
        if (flowerResult != null) {
            return new CommentResponseDto(saved.getId(), flowerResult.getFlower(), flowerResult.isNewlyUnlocked());
        } else {
            return new CommentResponseDto(saved.getId());
        }
    }

    @Transactional
    public void addLike(String type, Long id) {
        switch (type) {
            case "question" -> personalQuestionsRepository.increaseLikes(id);
            case "public_question" -> publicQuestionsRepository.increaseLikes(id);
            case "comment" -> commentRepository.increaseLikes(id);

            default -> throw new IllegalArgumentException("Unknown like type: " + type);
        }
    }

    public Long createQuestion(Long senderId, QuestionCreateRequestDto request, Long familyId) {
        PersonalQuestions question = new PersonalQuestions();
        question.setFamilyId(familyId);
        question.setContent(request.getContent());
        question.setSender(senderId);
        question.setReceiver(request.getReceiver());
        question.setVisibility(request.getIsPublic());
        question.setSolved(false);
        question.setLikes(0);
        question.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));
        question.setUpdated_at(new java.sql.Timestamp(System.currentTimeMillis()));

        PersonalQuestions saved = personalQuestionsRepository.save(question);
        return saved.getId();
    }

    public QuestionDetailResponseDto getQuestionDetail(Long questionId) {
        Optional<PersonalQuestions> questionOpt = personalQuestionsRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            throw new RuntimeException("Question not found");
        }

        PersonalQuestions question = questionOpt.get();
        
        String flowerName = null;
        Boolean isNewFlowerUnlocked = false;
        
        // solved가 true인 경우 이미 생성된 flower 정보 조회
        if (question.getSolved() != null && question.getSolved()) {
            // TODO: 이미 생성된 flower 정보를 DB에서 조회하는 로직 추가
            // 현재는 댓글 작성 시점에서만 꽃이 생성되므로 여기서는 조회만
            System.out.println("Question " + questionId + " is already solved");
        }
        
        // 질문 정보 생성
        QuestionDetailResponseDto.QuestionInfo questionInfo = new QuestionDetailResponseDto.QuestionInfo(
                question.getId(),
                question.getContent(),
                question.getSender().toString(),
                question.getLikes(),
                question.getCreated_at() != null ? question.getCreated_at().toString() : "2025-09-28",
                flowerName,
                isNewFlowerUnlocked
        );

        // 댓글 목록 조회 (question_reference 테이블을 통해)
        // 여기서는 간단하게 questionId를 questionRefId로 사용
        List<Comment> comments = commentRepository.findByQuestionRefId(questionId);
        
        List<QuestionDetailResponseDto.CommentInfo> commentInfos = comments.stream()
                .map(comment -> {
                    // 대댓글 ID 목록 생성 (간단하게 replyTo가 있는 경우)
                    List<String> replyIds = new ArrayList<>();
                    if (comment.getReplyTo() != null) {
                        replyIds.add(comment.getReplyTo().toString());
                    }
                    
                    return new QuestionDetailResponseDto.CommentInfo(
                            comment.getId(),
                            comment.getWriter().toString(),
                            comment.getContent(),
                            comment.getLikes(),
                            replyIds
                    );
                })
                .collect(Collectors.toList());

        return new QuestionDetailResponseDto(questionInfo, commentInfos);
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
            publicQuestion.setLikes(0);
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

    public QuestionDetailResponseDto getPublicQuestionDetail(Long questionId) {
        Optional<PublicQuestions> questionOpt = publicQuestionsRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            throw new RuntimeException("Public question not found");
        }

        PublicQuestions question = questionOpt.get();
        
        // 질문 정보 생성
        QuestionDetailResponseDto.QuestionInfo questionInfo = new QuestionDetailResponseDto.QuestionInfo(
                question.getId(),
                question.getContent(),
                "system", // public question은 시스템에서 생성하므로 sender를 "system"으로 설정
                question.getLikes(),
                question.getCreated_at() != null ? question.getCreated_at().toString() : "2025-09-28"
        );

        // 댓글 목록 조회 (public question의 댓글도 동일한 방식으로 조회)
        List<Comment> comments = commentRepository.findByQuestionRefId(questionId);
        
        List<QuestionDetailResponseDto.CommentInfo> commentInfos = comments.stream()
                .map(comment -> {
                    // 대댓글 ID 목록 생성 (간단하게 replyTo가 있는 경우)
                    List<String> replyIds = new ArrayList<>();
                    if (comment.getReplyTo() != null) {
                        replyIds.add(comment.getReplyTo().toString());
                    }
                    
                    return new QuestionDetailResponseDto.CommentInfo(
                            comment.getId(),
                            comment.getWriter().toString(),
                            comment.getContent(),
                            comment.getLikes(),
                            replyIds
                    );
                })
                .collect(Collectors.toList());

        return new QuestionDetailResponseDto(questionInfo, commentInfos);
    }

    /**
     * Public Question에 모든 가족 구성원이 댓글을 달았는지 확인하고, 
     * 조건을 만족하면 외부 예측 API를 호출합니다.
     */
    private FlowerResultDto checkAndProcessPersonalQuestionCompletion(Long questionRefId, Long commentUserId, Long familyId) {
        // 1. 해당 question이 personal question인지 확인
        Optional<PersonalQuestions> personalQuestionOpt = personalQuestionsRepository.findById(questionRefId);
        if (personalQuestionOpt.isEmpty()) {
            return null; // personal question이 아니면 처리하지 않음
        }
        
        PersonalQuestions personalQuestion = personalQuestionOpt.get();
        
        // 2. 이미 solved된 질문이면 처리하지 않음
        if (personalQuestion.getSolved() != null && personalQuestion.getSolved()) {
            return null;
        }
        
        // 3. 댓글 작성자가 receiver인지 확인
        if (!personalQuestion.getReceiver().equals(commentUserId)) {
            return null; // receiver가 아니면 처리하지 않음
        }
        
        // 4. receiver의 첫 댓글인지 확인 (기존 댓글 중 receiver가 작성한 것이 있는지 체크)
        List<Comment> existingComments = commentRepository.findByQuestionRefId(questionRefId);
        boolean hasReceiverComment = existingComments.stream()
                .anyMatch(comment -> comment.getWriter().equals(commentUserId));
        
        if (!hasReceiverComment) {
            // 5. receiver의 첫 댓글이므로 solved = true로 업데이트
            personalQuestion.setSolved(true);
            personalQuestionsRepository.save(personalQuestion);
            
            // 6. AI 호출하여 꽃 생성
            String predictionResult = predictionService.sendPredictionRequest(personalQuestion.getContent());
            if (predictionResult != null) {
                System.out.println("🌸 Personal question solved! Prediction result: " + predictionResult);
                
                // FlowerService를 통해 AI 응답 처리 및 꽃 저장
                FlowerResultDto flowerResult = flowerService.processAiResponseAndSaveFlower(
                        predictionResult,
                        questionRefId, 
                        familyId
                );
                
                System.out.println("🌸 Flower created: " + flowerResult.getFlower() + 
                                 ", New unlock: " + flowerResult.isNewlyUnlocked());
                
                return flowerResult;
            }
        }
        
        return null;
    }

    private void checkAndProcessPublicQuestionCompletion(Long questionRefId, Long familyId) {
        // 1. 해당 question이 public question인지 확인
        Optional<PublicQuestions> publicQuestionOpt = publicQuestionsRepository.findById(questionRefId);
        if (publicQuestionOpt.isEmpty()) {
            return; // public question이 아니면 처리하지 않음
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
            // AI 호출 기능 제거됨
            System.out.println("🎉 All family members commented on public question " + questionRefId + "!");
        }
    }

    public Map<String, Object> getPersonalQuestions(Long receiverId, Long familyId) {
        // question_reference에서 personal question type인 것들만 가져오기
        List<QuestionReference> personalQuestionRefs = questionReferenceRepository.findByFamilyIdAndQuestionType(familyId, QuestionReference.QuestionType.personal);
        
        List<Map<String, Object>> questions = new ArrayList<>();
        
        for (QuestionReference qRef : personalQuestionRefs) {
            // personal_questions에서 실제 질문 정보 가져오기
            Optional<PersonalQuestions> personalQuestionOpt = personalQuestionsRepository.findById(qRef.getQuestionId());
            
            if (personalQuestionOpt.isPresent()) {
                PersonalQuestions pq = personalQuestionOpt.get();
                
                // 해당 질문의 댓글 수 계산
                int commentCount = commentRepository.findByQuestionRefId(qRef.getId()).size();
                
                Map<String, Object> questionData = new HashMap<>();
                questionData.put("question_ref_id", qRef.getId().toString());
                questionData.put("content", pq.getContent());
                questionData.put("sender", pq.getSender().toString());
                questionData.put("receiver", pq.getReceiver().toString());
                questionData.put("likes", pq.getLikes());
                questionData.put("comments", commentCount);
                questionData.put("visibility", pq.getVisibility() ? 1 : 0);
                
                questions.add(questionData);
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        
        return response;
    }

 }

