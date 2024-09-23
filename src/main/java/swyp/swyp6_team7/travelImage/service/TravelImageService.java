package swyp.swyp6_team7.travelImage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.repository.TravelCustomRepository;
import swyp.swyp6_team7.travel.repository.TravelCustomRepositoryImpl;
import swyp.swyp6_team7.travel.repository.TravelRepository;
import swyp.swyp6_team7.travelImage.domain.TravelImage;
import swyp.swyp6_team7.travelImage.domain.TravelNumberParam;
import swyp.swyp6_team7.travelImage.repository.FileUploadRepository;
import swyp.swyp6_team7.travelImage.upload.S3Uploader;
import swyp.swyp6_team7.travelImage.repository.TravelImageRepository;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TravelImageService {
    private final TravelRepository travelRepository;
    private final TravelCustomRepositoryImpl travelCustomRepository;
    private final TravelCustomRepository travelCustomRepositoryImpl;

    private final FileUploadRepository fileUploadRepository;
    private final S3Uploader s3Uploader;

    public TravelImage saveImage(MultipartFile multipartFile, TravelNumberParam travelNumberParam) throws IOException {
        Travel travel = travelRepository.findByNumber(travelNumberParam.getTravelNumber())
                .orElseThrow(() -> new IllegalArgumentException("travelNumber not found"));
        if (!multipartFile.isEmpty()) {
            Map<String, String> storeFileURL = s3Uploader.upload(multipartFile, "travelImage");
        }

        return null;
    }
}
