package com.bob.masterdata.Service;

import com.bob.db.dto.ReservationCategoriesDTO;
import com.bob.db.entity.ReservationCategoriesEntity;
import com.bob.db.mapper.ReservationCategoriesMapper;
import com.bob.db.repository.ReservationCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationCategoriesService {

    @Autowired
    private ReservationCategoriesRepository reservationCategoriesRepository;

    @Autowired
    private ReservationCategoriesMapper reservationCategoriesMapper;

    public ReservationCategoriesDTO createReservationCategories(ReservationCategoriesDTO reservationCategoriesDto) {
        try {
            ReservationCategoriesEntity reservationCategoriesEntity= reservationCategoriesMapper.toEntity(reservationCategoriesDto);
            reservationCategoriesRepository.save(reservationCategoriesEntity);
            return reservationCategoriesMapper.toDto(reservationCategoriesEntity);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReservationCategoriesDTO> getAllResCategories() throws Exception {
        try {
            return reservationCategoriesMapper.toDtoList(reservationCategoriesRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch Reservation categories");
        }
    }

    public ReservationCategoriesDTO updateResCategories(Long id, ReservationCategoriesDTO reservationCategoriesDto) {
        try {
            ReservationCategoriesDTO reservationCategoriesEntity1 = reservationCategoriesDto;
            Optional<ReservationCategoriesEntity> existingReservationCategories = reservationCategoriesRepository.findById(id);
            if (existingReservationCategories.isPresent()) {
                ReservationCategoriesEntity reservationCategoriesEntity = existingReservationCategories.get();
                reservationCategoriesEntity.setReservationCategoriesId(id);
                reservationCategoriesEntity.setCategoryCode(reservationCategoriesDto.getCategoryCode());
                reservationCategoriesEntity.setCategoryName(reservationCategoriesDto.getCategoryName());
                reservationCategoriesEntity.setCategoryDesc(reservationCategoriesDto.getCategoryDesc());
                //reservationCategoriesEntity.setUpdatedBy(reservationCategoriesDto.getUpdatedBy());
                reservationCategoriesRepository.save(reservationCategoriesEntity);
                return reservationCategoriesMapper.toDto(reservationCategoriesEntity);

            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public ReservationCategoriesDTO deleteReservationCategory(Long id) {
        try {
            if (reservationCategoriesRepository.existsById(id)) {
                ReservationCategoriesEntity reservationCategoriesEntity=reservationCategoriesRepository.findById(id).get();
                reservationCategoriesEntity.setIsActive(false);
                reservationCategoriesRepository.save(reservationCategoriesEntity);
                ReservationCategoriesDTO reservationCategoriesDto =reservationCategoriesMapper.toDto(reservationCategoriesEntity);
//                reservationCategoriesRepository.deleteById(id);
                return reservationCategoriesDto;
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
