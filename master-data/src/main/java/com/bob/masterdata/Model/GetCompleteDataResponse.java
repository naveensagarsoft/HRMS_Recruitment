package com.bob.masterdata.Model;

import com.bob.db.dto.CityDTO;
import com.bob.db.dto.LocationDTO;
import com.bob.db.dto.MasterPositionsDTO;
import com.bob.db.dto.StateDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetCompleteDataResponse {

    @JsonProperty("position_title")
    private List<String> positionTitle;

    private List<Map<Long,String>> departments;

    private List<Map<Long,String>> countries;

    private List<LocationDTO> locations;

    private List<CityDTO> cities;

    private List<StateDTO> states;

    private List<Map<Long,String>> skills;

    @JsonProperty("job_grade_data")
    private List<Map<Long,String>> jobGradeData;

    @JsonProperty("mandatory_qualification")
    private List<Map<Long,String>> mandatoryQualification;

    @JsonProperty("preferred_qualification")
    private List<Map<Long,String>> preferredQualification;

    List<MasterPositionsDTO> masterPositionsList;
}
