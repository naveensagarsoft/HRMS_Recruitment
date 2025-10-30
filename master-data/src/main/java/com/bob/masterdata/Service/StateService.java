package com.bob.masterdata.Service;


import com.bob.db.dto.StateDTO;
import com.bob.db.entity.StateEntity;
import com.bob.db.mapper.StateMapper;
import com.bob.db.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateService {
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private StateMapper stateMapper;

    public StateDTO createState(StateDTO stateDto) {
        try {
            return stateMapper.toDto(stateRepository.save(stateMapper.toEntity(stateDto)));
        } catch (Exception e) {
            return null;
        }
    }

    public List<StateDTO> getAllStates() throws Exception {
        try {
            return stateMapper.toDtoList(stateRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch state");
        }
    }

    public StateDTO updateState(Long id,StateDTO state) {
        try {
            Optional<StateEntity> existingState = stateRepository.findById(id);
            if (existingState.isPresent()) {
                StateEntity stateEntity=existingState.get();
                stateEntity.setStateId(id);
                stateEntity.setStateName(state.getStateName());
                stateEntity.setCountryId(state.getCountryId());
                stateRepository.save(stateEntity);
                return stateMapper.toDto(stateEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public StateDTO deleteState(Long id) {
        try {
            if (stateRepository.existsById(id)) {
                StateEntity state=stateRepository.findById(id).get();
                state.setIsActive(false);
                stateRepository.save(state);
//                stateRepository.deleteById(id);
                return stateMapper.toDto(state);
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
