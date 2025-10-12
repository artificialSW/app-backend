package org.dcode.artificialswbackend.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDto {
    
    @JsonProperty("isLiked")
    private boolean isLiked;        // 현재 사용자의 좋아요 상태
    
    @JsonProperty("totalLikes")
    private long totalLikes;        // 총 좋아요 수
    
    @JsonProperty("success")
    private boolean success;        // 요청 성공 여부
    
    public LikeResponseDto(boolean isLiked, long totalLikes) {
        this.isLiked = isLiked;
        this.totalLikes = totalLikes;
        this.success = true;
    }
}