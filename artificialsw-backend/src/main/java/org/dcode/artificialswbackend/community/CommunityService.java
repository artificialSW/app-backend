package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionDto;
import org.dcode.artificialswbackend.community.entity.Community;
import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.dcode.artificialswbackend.community.repository.CommunityRepository;
import org.dcode.artificialswbackend.community.repository.PublicQuestionsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final PublicQuestionsRepository publicQuestionsRepository;
    public CommunityService(CommunityRepository communityRepository,  PublicQuestionsRepository publicQuestionsRepository) {
        this.communityRepository = communityRepository;
        this.publicQuestionsRepository = publicQuestionsRepository;
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

 }

