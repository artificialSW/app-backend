package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionDto;
import org.dcode.artificialswbackend.community.entity.PersonalQuestions;
import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.dcode.artificialswbackend.community.repository.PersonalQuestionsRepository;
import org.dcode.artificialswbackend.community.repository.PublicQuestionsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CommunityService {
    private final PersonalQuestionsRepository personalQuestionsRepository;
    private final PublicQuestionsRepository publicQuestionsRepository;

    public CommunityService(PersonalQuestionsRepository personalQuestionsRepository, PublicQuestionsRepository publicQuestionsRepository) {
        this.personalQuestionsRepository = personalQuestionsRepository;
        this.publicQuestionsRepository = publicQuestionsRepository;
    }

    public Map<String, Object> getQuestionsWithUnsolvedCount(Long receiverId){
        List<PersonalQuestions> allQuestions = personalQuestionsRepository.findAll();
        long unsolvedCount = personalQuestionsRepository.countByReceiverAndSolvedFalse(receiverId);

        List<PersonalQuestionDto> questions = allQuestions.stream()
                .map(e -> new PersonalQuestionDto(
                        e.getId(),
                        e.getContent(),
                        e.getSender(),
                        e.getReceiver(),
                        e.getIsPublic(),
                        e.getSolved(),
                        e.getLikes(),
                        e.getCreated_at()
                ))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("questions", questions);
        result.put("unsolved", unsolvedCount);

        return result;
    }
    public Map<String, Object> getPublicQuestions() {
        List<PublicQuestions> publicQuestions = publicQuestionsRepository.findAll();

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


    public List<PersonalQuestionDto> getMyQuestions(String userId) {
        Long userIdLong = Long.valueOf(userId);
        List<PersonalQuestions> questions = personalQuestionsRepository.findByReceiver(userIdLong);

        return questions.stream().map(PersonalQuestionDto::fromEntity).collect(Collectors.toList());
    }

 }

