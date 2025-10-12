package org.dcode.artificialswbackend.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMembersResponseDto {
    
    @JsonProperty("familyMembers")
    private List<FamilyMemberDto> familyMembers;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyMemberDto {
        private String id;
        private String role;
    }
}