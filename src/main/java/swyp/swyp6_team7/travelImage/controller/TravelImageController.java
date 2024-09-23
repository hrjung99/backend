package swyp.swyp6_team7.travelImage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.travelImage.domain.TravelImage;
import swyp.swyp6_team7.travelImage.service.TravelImageService;
import swyp.swyp6_team7.travelImage.domain.TravelNumberParam;
import swyp.swyp6_team7.travelImage.dto.response.CommonResponse;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel")
public class TravelImageController {

    @PutMapping(value = "/image/{travelId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> imageUpload(@RequestPart TravelNumberParam travelNumberParam, @RequestPart MultipartFile imageFile) throws IOException  {
        TravelImage savedImage = TravelImageService.saveImage(imageFile, travelNumberParam);
        return CommonResponse.sucess(savedImage, "이미지 등록 성공 : 성공 시 이미지, 실패 시 null 반환");

    }
}

