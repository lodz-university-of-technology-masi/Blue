package com.masiblue.backend.service;

import com.masiblue.backend.exception.IncorrectUsabilityDataFormat;
import com.masiblue.backend.model.UsabilityData;
import com.masiblue.backend.repository.UsabilityDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsabilityDataService {

    @Autowired
    private UsabilityDataRepository usabilityDataRepository;

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
}
