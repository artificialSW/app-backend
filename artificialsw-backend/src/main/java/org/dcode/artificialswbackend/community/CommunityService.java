package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.entity.Community;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public Map<String, Object> getQuestionsWithUnsolvedCount(Long receiverId){
        List<Community> allQuestions = communityRepository.findAll();
        long unsolvedCount = communityRepository.countByReceiverAndSolvedFalse(receiverId);

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

    public Map<String, Object> getPublicQuestions(){
        List<Community> publicQuestions = communityRepository.findByIsPublicTrue();

        List<PersonalQuestionDto> questions = publicQuestions.stream()
                .map(e-> new PersonalQuestionDto(
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

        return result;
    }
 }
