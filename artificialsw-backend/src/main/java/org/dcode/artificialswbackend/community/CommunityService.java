package org.dcode.artificialswbackend.community;

import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionDto;
import org.dcode.artificialswbackend.community.dto.CommentRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionCreateRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionDetailResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionResponseDto;

import org.dcode.artificialswbackend.community.entity.PersonalQuestions;
import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.dcode.artificialswbackend.community.entity.Comment;
import org.dcode.artificialswbackend.community.entity.QuestionList;
import org.dcode.artificialswbackend.community.entity.Users;

import org.dcode.artificialswbackend.community.repository.CommentRepository;
import org.dcode.artificialswbackend.community.repository.PersonalQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.PublicQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.QuestionListRepository;
import org.dcode.artificialswbackend.community.repository.FamiliesRepository;
import org.dcode.artificialswbackend.community.repository.QuestionReferenceRepository;
import org.dcode.artificialswbackend.community.repository.UsersRepository;
import org.dcode.artificialswbackend.community.service.PredictionService;

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

    public CommunityService(PersonalQuestionsRepository personalQuestionsRepository, 
                          PublicQuestionsRepository publicQuestionsRepository,  
                          CommentRepository commentRepository, 
                          QuestionListRepository questionListRepository,
                          FamiliesRepository familiesRepository,
                          QuestionReferenceRepository questionReferenceRepository,
                          UsersRepository usersRepository,
                          PredictionService predictionService) {
        this.personalQuestionsRepository = personalQuestionsRepository;
        this.publicQuestionsRepository = publicQuestionsRepository;
        this.commentRepository = commentRepository;
        this.questionListRepository = questionListRepository;
        this.familiesRepository = familiesRepository;
        this.questionReferenceRepository = questionReferenceRepository;
        this.usersRepository = usersRepository;
        this.predictionService = predictionService;
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

    public Long saveComment(Long userId, CommentRequestDto request, Long familyId) {
        Comment comment = new Comment();
        comment.setQuestionRefId(request.getQuestionRefId());
        comment.setContent(request.getContent());
        comment.setWriter(userId);
        comment.setReplyTo(request.getReplyTo()); // null 가능
        comment.setLikes(0);

        Comment saved = commentRepository.save(comment);
        
        // 댓글 저장 후 public question 완료 체크
        checkAndProcessPublicQuestionCompletion(request.getQuestionRefId(), familyId);
        
        return saved.getId();
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
        
        // solved가 true인 경우 외부 예측 API 호출
        if (question.getSolved() != null && question.getSolved()) {
            String predictionResult = predictionService.sendPredictionRequest(question.getContent());
            if (predictionResult != null) {
                System.out.println("Prediction result for question " + questionId + ": " + predictionResult);
            }
        }
        
        // 질문 정보 생성
        QuestionDetailResponseDto.QuestionInfo questionInfo = new QuestionDetailResponseDto.QuestionInfo(
                question.getId(),
                question.getContent(),
                question.getSender().toString(),
                question.getLikes(),
                question.getCreated_at() != null ? question.getCreated_at().toString() : "2025-09-28"
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
            // 5. 외부 예측 API 호출
            String predictionResult = predictionService.sendPredictionRequest(publicQuestion.getContent());
            if (predictionResult != null) {
                System.out.println("🎉 All family members commented on public question " + questionRefId + 
                                 "! Prediction result: " + predictionResult);
            }
        }
    }

 }

