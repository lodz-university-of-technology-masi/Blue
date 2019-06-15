package com.masiblue.backend.service;

import com.masiblue.backend.exception.IncorrectUsabilityDataFormat;
import com.masiblue.backend.model.UsabilityData;
import com.masiblue.backend.repository.UsabilityDataRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UsabilityDataService {

    private static final String BASE64_HEADER = ";base64,";
    private static final String SCREENSHOT_IMG_EXTENSION = ".jpg";
    private UsabilityDataRepository usabilityDataRepository;

    public UsabilityDataService(UsabilityDataRepository usabilityDataRepository) {
        this.usabilityDataRepository = usabilityDataRepository;
    }

    public void addUsabilityData(String userName, UsabilityData usabilityData) throws IncorrectUsabilityDataFormat {
        if (usabilityData.getIsFailed() != 0 && usabilityData.getIsFailed() != 1) {
            throw new IncorrectUsabilityDataFormat("The value of 'isFailed' field must be either 0 or 1");
        }
        usabilityData.setUsername(userName);

        List<UsabilityData> allByUsername = usabilityDataRepository.findAllByUsername(userName);
        int userMeasurementNumber = allByUsername.size() + 1;
        usabilityData.setUserMeasurementNumber(userMeasurementNumber);

        usabilityDataRepository.save(usabilityData);
    }

    public String saveScreenshot(String userName, String decodedImg) throws IOException {
        if (decodedImg.contains(BASE64_HEADER)) {
            int index = decodedImg.lastIndexOf(BASE64_HEADER) + BASE64_HEADER.length();
            decodedImg = decodedImg.substring(index);
        }

        long timestamp = System.currentTimeMillis() / 1000;
        String fileName = userName + "_" + timestamp;
        String filePath = "./../" + fileName + SCREENSHOT_IMG_EXTENSION;
        File outputFile = new File(filePath);
        byte[] imageByte;
        BASE64Decoder decoder = new BASE64Decoder();
        imageByte = decoder.decodeBuffer(decodedImg);
        FileUtils.writeByteArrayToFile(outputFile, imageByte);

        return fileName;
    }
}
