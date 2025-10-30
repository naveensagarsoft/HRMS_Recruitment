package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpecialCategoriesDTO {

    @JsonProperty("special_category_id")
    private Long specialCategoryId;

    @JsonProperty("special_category_code")
    private String specialCategoryCode;

    @JsonProperty("special_category_name")
    private String specialCategoryName;

    @JsonProperty("special_category_desc")
    private String specialCategoryDesc;

}
