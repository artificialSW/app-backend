package org.dcode.artificialswbackend.tree;

import org.dcode.artificialswbackend.tree.dto.TreeFlowerResponseDto;
import org.dcode.artificialswbackend.community.entity.Flowers;
import org.dcode.artificialswbackend.community.repository.FlowersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreeService {

    private final FlowersRepository flowersRepository;

    public TreeService(FlowersRepository flowersRepository) {
        this.flowersRepository = flowersRepository;
    }

    public List<TreeFlowerResponseDto> getTreeFlowers(Long treeId) {
        // treeId로 꽃들 조회
        List<Flowers> flowers = flowersRepository.findByTreeIdOrderByCreatedAtDesc(treeId);
        
        // DTO로 변환하면서 한국어 이름 적용
        return flowers.stream()
                .map(flower -> new TreeFlowerResponseDto(
                    flower.getId(),
                    flower.getFlower().getKoreanName(),
                    flower.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}