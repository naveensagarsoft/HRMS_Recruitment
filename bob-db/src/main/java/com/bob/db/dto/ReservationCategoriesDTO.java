package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReservationCategoriesDTO {

    @JsonProperty("reservation_categories_id")
    private Long reservationCategoriesId;

    @JsonProperty("category_code")
    private String categoryCode;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_desc")
    private String categoryDesc;


}
