package org.dcode.artificialswbackend.mypage;

import org.dcode.artificialswbackend.mypage.dto.MyPageResponseDto;
import org.dcode.artificialswbackend.mypage.dto.MyCommentResponseDto;
import org.dcode.artificialswbackend.mypage.dto.MyLikedQuestionResponseDto;
import org.dcode.artificialswbackend.community.dto.MyQuestionResponseDto;
import org.dcode.artificialswbackend.signup.entity.Families;
import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.dcode.artificialswbackend.signup.repository.FamiliesRepository;
import org.dcode.artificialswbackend.signup.repository.SignUpRepository;
import org.dcode.artificialswbackend.community.entity.Comment;
import org.dcode.artificialswbackend.community.entity.PersonalQuestions;
import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.dcode.artificialswbackend.community.entity.Like;
import org.dcode.artificialswbackend.community.repository.CommentRepository;
import org.dcode.artificialswbackend.community.repository.PersonalQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.PublicQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.LikeRepository;
import org.dcode.artificialswbackend.community.repository.QuestionReferenceRepository;
import org.dcode.artificialswbackend.community.entity.QuestionReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    private final SignUpRepository signUpRepository;
    private final FamiliesRepository familiesRepository;
    private final CommentRepository commentRepository;
    private final PersonalQuestionsRepository personalQuestionsRepository;
    private final PublicQuestionsRepository publicQuestionsRepository;
    private final QuestionReferenceRepository questionReferenceRepository;
    private final LikeRepository likeRepository;

    public MyPageService(SignUpRepository signUpRepository, FamiliesRepository familiesRepository, 
                        CommentRepository commentRepository, PersonalQuestionsRepository personalQuestionsRepository,
                        PublicQuestionsRepository publicQuestionsRepository, QuestionReferenceRepository questionReferenceRepository,
                        LikeRepository likeRepository) {
        this.signUpRepository = signUpRepository;
        this.familiesRepository = familiesRepository;
        this.commentRepository = commentRepository;
        this.personalQuestionsRepository = personalQuestionsRepository;
        this.publicQuestionsRepository = publicQuestionsRepository;
        this.questionReferenceRepository = questionReferenceRepository;
        this.likeRepository = likeRepository;
    }

    public MyPageResponseDto getMyPageInfo(Long userId) {
        // 사용자 정보 조회
        Optional<SignUp> userOpt = signUpRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        SignUp user = userOpt.get();

        // 가족 정보 조회
        Optional<Families> familyOpt = familiesRepository.findById(user.getFamilyId());
        if (familyOpt.isEmpty()) {
            throw new RuntimeException("가족 정보를 찾을 수 없습니다.");
        }

        Families family = familyOpt.get();

        // 응답 DTO 생성
        return new MyPageResponseDto(
                user.getName(),
                user.getBirthday(),
                user.getFamilyType(),
                family.getVerificationCode()
        );
    }

    public void updateMyPageInfo(Long userId, String name, java.time.LocalDate birth, String familyType) {
        // 사용자 정보 조회
        Optional<SignUp> userOpt = signUpRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        SignUp user = userOpt.get();

        // 정보 업데이트 (null이 아닌 값만 업데이트)
        if (name != null) {
            user.setName(name);
        }
        if (birth != null) {
            user.setBirthday(birth);
        }
        if (familyType != null) {
            user.setFamilyType(familyType);
        }

        // 변경사항 저장
        signUpRepository.save(user);
    }

    public List<MyCommentResponseDto> getMyComments(Long userId) {
        // 사용자가 작성한 댓글 조회 (family_id는 나중에 추가로 필터링)
        List<Comment> comments = commentRepository.findAll().stream()
                .filter(comment -> comment.getWriter().equals(userId))
                .collect(Collectors.toList());

        // DTO로 변환 (question_reference를 거쳐서 질문 타입과 내용 조회)
        return comments.stream()
                .map(comment -> {
                    String questionType;
                    String questionContent;
                    Long questionRefId = comment.getQuestionRefId();
                    
                    // question_reference에서 실제 질문 ID와 타입 찾기
                    Optional<QuestionReference> questionRefOpt = questionReferenceRepository.findById(questionRefId);
                    if (questionRefOpt.isPresent()) {
                        QuestionReference questionRef = questionRefOpt.get();
                        Long actualQuestionId = questionRef.getQuestionId();
                        
                        if (questionRef.getQuestionType() == QuestionReference.QuestionType.Personal) {
                            // 개인 질문
                            Optional<PersonalQuestions> personalQuestion = personalQuestionsRepository.findById(actualQuestionId);
                            if (personalQuestion.isPresent()) {
                                questionType = "personal_questions";
                                questionContent = personalQuestion.get().getContent();
                            } else {
                                questionType = "personal_questions";
                                questionContent = "삭제된 개인 질문입니다.";
                            }
                        } else {
                            // 공개 질문
                            Optional<PublicQuestions> publicQuestion = publicQuestionsRepository.findById(actualQuestionId);
                            if (publicQuestion.isPresent()) {
                                questionType = "public_questions";
                                questionContent = publicQuestion.get().getContent();
                            } else {
                                questionType = "public_questions";
                                questionContent = "삭제된 공개 질문입니다.";
                            }
                        }
                    } else {
                        // question_reference를 찾을 수 없음
                        questionType = "unknown";
                        questionContent = "질문을 찾을 수 없습니다.";
                    }
                    
                    return new MyCommentResponseDto(
                        comment.getId(), 
                        comment.getContent(), 
                        questionType, 
                        questionRefId, 
                        questionContent
                    );
                })
                .collect(Collectors.toList());
    }

    public List<MyQuestionResponseDto> getMyQuestions(Long userId) {
        List<MyQuestionResponseDto> myQuestions = new java.util.ArrayList<>();
        
        // Personal Questions에서 내가 작성한 질문 조회 (sender 기준) - findAll로 가져온 후 필터링
        List<PersonalQuestions> personalQuestions = personalQuestionsRepository.findAll().stream()
                .filter(pq -> pq.getSender().equals(userId))
                .collect(Collectors.toList());
        for (PersonalQuestions pq : personalQuestions) {
            myQuestions.add(new MyQuestionResponseDto(
                pq.getId(),
                pq.getContent(),
                pq.getSolved(),
                "personal"
            ));
        }
        
        return myQuestions;
    }

    public List<MyLikedQuestionResponseDto> getMyLikedQuestions(Long userId, Long familyId) {
        // 사용자가 좋아요한 질문들 조회
        List<Like> likedQuestions = likeRepository.findLikedQuestionsByUserIdAndFamilyId(userId, familyId);
        
        return likedQuestions.stream()
                .map(like -> {
                    String questionContent;
                    String questionType;
                    Long questionRefId = null;
                    
                    if (like.getTargetType() == Like.TargetType.question) {
                        // Personal Question 처리
                        Optional<PersonalQuestions> personalQuestionOpt = personalQuestionsRepository.findById(like.getTargetId());
                        if (personalQuestionOpt.isPresent()) {
                            PersonalQuestions personalQuestion = personalQuestionOpt.get();
                            questionContent = personalQuestion.getContent();
                            questionType = "Personal";
                            
                            // question_reference에서 question_ref_id 찾기
                            Optional<QuestionReference> questionRefOpt = questionReferenceRepository
                                .findByQuestionIdAndQuestionTypeAndFamilyId(
                                    personalQuestion.getId(), 
                                    QuestionReference.QuestionType.Personal, 
                                    familyId
                                );
                            if (questionRefOpt.isPresent()) {
                                questionRefId = questionRefOpt.get().getId();
                            }
                        } else {
                            questionContent = "삭제된 개인 질문입니다.";
                            questionType = "Personal";
                        }
                    } else if (like.getTargetType() == Like.TargetType.public_question) {
                        // Public Question 처리
                        Optional<PublicQuestions> publicQuestionOpt = publicQuestionsRepository.findById(like.getTargetId());
                        if (publicQuestionOpt.isPresent()) {
                            PublicQuestions publicQuestion = publicQuestionOpt.get();
                            questionContent = publicQuestion.getContent();
                            questionType = "Public";
                            
                            // question_reference에서 question_ref_id 찾기
                            Optional<QuestionReference> questionRefOpt = questionReferenceRepository
                                .findByQuestionIdAndQuestionTypeAndFamilyId(
                                    publicQuestion.getId(), 
                                    QuestionReference.QuestionType.Public, 
                                    familyId
                                );
                            if (questionRefOpt.isPresent()) {
                                questionRefId = questionRefOpt.get().getId();
                            }
                        } else {
                            questionContent = "삭제된 공개 질문입니다.";
                            questionType = "Public";
                        }
                    } else {
                        questionContent = "알 수 없는 질문 타입입니다.";
                        questionType = "Unknown";
                    }
                    
                    return new MyLikedQuestionResponseDto(questionRefId, questionContent, questionType);
                })
                .collect(Collectors.toList());
    }
}