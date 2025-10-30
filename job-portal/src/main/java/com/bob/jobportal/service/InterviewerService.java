package com.bob.jobportal.service;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.InterviewersDTO;
import com.bob.db.entity.InterviewersEntity;
import com.bob.db.mapper.InterviewersMapper;
import com.bob.db.repository.InterviewersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewerService {

    @Autowired
    private InterviewersRepository interviewersRepository;

    @Autowired
    private InterviewersMapper interviewersMapper;


    public List<InterviewersDTO> getAllInterviewers(){
        List<InterviewersDTO> interviewersDTOS=interviewersMapper.toDtoList(interviewersRepository.findAllByIsActiveTrue());
        return interviewersDTOS;

    }

    public InterviewersDTO createInterviewer(InterviewersDTO interviewersDTO) {
        InterviewersDTO interviewersDTO1=interviewersMapper.toDto(
                interviewersRepository.save(interviewersMapper.toEntity(interviewersDTO)));
        return interviewersDTO1;
    }


    public InterviewersDTO updateInterviewer(Long interviewerId, InterviewersDTO interviewersDTO) {
        if(interviewersRepository.existsById(interviewerId)){
            InterviewersEntity existingInterviewer=interviewersRepository.findById(interviewerId).get();
            existingInterviewer.setFirstName(interviewersDTO.getFirstName());
            existingInterviewer.setLastName(interviewersDTO.getLastName());
            existingInterviewer.setFullName(interviewersDTO.getFullName());
            existingInterviewer.setEmail(interviewersDTO.getEmail());
            InterviewersEntity updatedInterviewer=interviewersRepository.save(existingInterviewer);
            InterviewersDTO interviewersDTO1=interviewersMapper.toDto(updatedInterviewer);
            return interviewersDTO1;
        }else{
            throw new RuntimeException("Interviewer not found");
        }
    }

    public void deleteInterviewer(Long interviewerId) {
            InterviewersEntity existingInterviewer=interviewersRepository.findById(interviewerId).orElseThrow(()->new RuntimeException("Interviewer not found"));
            existingInterviewer.setIsActive(false);
            interviewersRepository.save(existingInterviewer);


    }
}
