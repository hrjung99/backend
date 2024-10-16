package swyp.swyp6_team7.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class ImageCommunityRequestDto {

    private List<String> statuses;
    private List<String> urls;
}
