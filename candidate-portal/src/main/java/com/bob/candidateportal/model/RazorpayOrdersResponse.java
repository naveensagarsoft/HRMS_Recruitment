package com.bob.candidateportal.model;

import com.bob.db.dto.RazorpayOrderDTO;
import lombok.Data;

@Data
public class RazorpayOrdersResponse {
    RazorpayOrderDTO razorpayOrderDetails;
    String candidateName;
    String candidateEmail;
    String candidateCurrentDesignation;
    String positionTitle;
    String positionDescription;
}
