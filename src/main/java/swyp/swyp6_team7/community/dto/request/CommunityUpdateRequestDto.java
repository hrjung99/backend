package swyp.swyp6_team7.community.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import swyp.swyp6_team7.community.domain.Community;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityUpdateRequestDto {

    private String categoryName;
    @Size(max = 20)
    private String title;

    @Size(max = 2000)
    private String content;

}
