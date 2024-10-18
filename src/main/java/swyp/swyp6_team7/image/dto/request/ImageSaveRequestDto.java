package swyp.swyp6_team7.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageSaveRequestDto {

    private List<String> deletedTempUrls;
    private List<String> tempUrls;

}
