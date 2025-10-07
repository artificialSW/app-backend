package org.dcode.artificialswbackend.mypage;

import org.dcode.artificialswbackend.mypage.dto.MyPageResponseDto;
import org.dcode.artificialswbackend.mypage.dto.MyCommentResponseDto;
import org.dcode.artificialswbackend.signup.entity.Families;
import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.dcode.artificialswbackend.signup.repository.FamiliesRepository;
import org.dcode.artificialswbackend.signup.repository.SignUpRepository;
import org.dcode.artificialswbackend.community.entity.Comment;
import org.dcode.artificialswbackend.community.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    private final SignUpRepository signUpRepository;
    private final FamiliesRepository familiesRepository;
    private final CommentRepository commentRepository;

    public MyPageService(SignUpRepository signUpRepository, FamiliesRepository familiesRepository, CommentRepository commentRepository) {
        this.signUpRepository = signUpRepository;
        this.familiesRepository = familiesRepository;
        this.commentRepository = commentRepository;
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
        // 사용자가 작성한 댓글 조회
        List<Comment> comments = commentRepository.findByWriter(userId);

        // DTO로 변환
        return comments.stream()
                .map(comment -> new MyCommentResponseDto(comment.getId(), comment.getContent()))
                .collect(Collectors.toList());
    }
}