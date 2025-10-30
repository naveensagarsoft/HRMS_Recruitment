package com.bob.candidateportal.Controllers;

import com.bob.candidateportal.Service.RazorpayService;
import com.bob.db.dto.ApiResponse;
import com.bob.candidateportal.model.RazorpayOrdersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/razorpay")
public class RazorpayController {
    @Autowired
    private RazorpayService razorpayService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RazorpayOrdersResponse>>> getAllUsers() throws Exception {
        List<RazorpayOrdersResponse> orders = razorpayService.getAllRazorpayOrders();
        ApiResponse<List<RazorpayOrdersResponse>> response = new ApiResponse<>(true, "ORDERS FETCHED SUCCESSFULLY!", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
