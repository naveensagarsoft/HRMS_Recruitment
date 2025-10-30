package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.UserDTO;
import com.bob.masterdata.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() throws Exception {
            List<UserDTO> users = userService.getAllUsers();
            ApiResponse<List<UserDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", users);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) throws Exception {

            UserDTO user1 = userService.createUser(userDTO);
            ApiResponse<UserDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", user1);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable int id,@RequestBody UserDTO userDTO){
            UserDTO user1 = userService.updateUser(id, userDTO);
            ApiResponse<UserDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", user1);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@PathVariable int id){
            UserDTO user = userService.deleteUser(id);
            ApiResponse<UserDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
