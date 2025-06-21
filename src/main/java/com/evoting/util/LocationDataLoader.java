package com.evoting.util;

import com.evoting.model.StateData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class LocationDataLoader {
    public static StateData loadLocationData() {
        try {
            File jsonFile = new File("D:/programming/java_projects/project_pbl/src/main/resources/india_states.json");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonFile, StateData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
