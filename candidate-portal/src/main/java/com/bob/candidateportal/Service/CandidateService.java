package com.bob.candidateportal.Service;

import com.bob.candidateportal.Feign.FeignPositionDTO;
import com.bob.candidateportal.model.*;
import com.bob.candidateportal.util.AppConstants;
import com.bob.candidateportal.util.SecurityUtils;
import com.bob.db.dto.InterviewsDTO;
import com.bob.db.entity.CityEntity;
import com.bob.db.entity.CountryEntity;
import com.bob.db.entity.LocationEntity;
import com.bob.db.entity.StateEntity;
import com.bob.db.mapper.CandidatesMapper;
import com.bob.db.repository.CityRepository;
import com.bob.db.repository.CountryRepository;
import com.bob.db.repository.LocationRepository;
import com.bob.db.repository.StateRepository;
import com.bob.db.repository.*;
import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.*;
import com.bob.db.entity.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CandidateService {
    @Autowired
    private CandidatesRepository candidatesRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private CandidateNationalityRepository candidateNationalityRepository;

    @Autowired
    private CandidateApplicationsRepository candidateApplicationsRepository;
    @Autowired
    private MailService mailService;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CandidateDocumentsRepository candidateDocumentsRepository;
    @Autowired
    private JobPostingLocationRepository jobPostingLocationRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PositionsRepository positionsRepository;
    @Autowired
    private InterviewersRepository interviewerRepository;
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private FeignPositionDTO feignPositionDTO;

    @Autowired
    private InterviewsRepository interviewsRepository;

    @Autowired
    private CandidatesMapper candidatesMapper;

    @Autowired
    private WorkflowApprovalEntityRepository workflowApprovalRepository;

    @Autowired
    private CandidateHelperService candidateHelperService;

    @Autowired
    private InterviewPanelsRepository interviewPanelsRepository;

    @Autowired
    private UserRepository userRepository;


    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    public List<GetCandidateDetailsByPositionIdResponse> getCandidateDetailsByPositionId(UUID position_id) {
        List<CandidateApplicationsEntity> candidateApplicationsList = candidateApplicationsRepository.findByPositionId(position_id);
        if (candidateApplicationsList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> locationIds = jobPostingLocationRepository.findByPositionIdNative(position_id);

        Map<Long, LocationEntity> locationMap = locationRepository.findAllByLocationIdInAndIsActiveTrue(locationIds).stream().collect(Collectors.toMap(LocationEntity::getLocationId, loc -> loc));

        Map<Long, CityEntity> cityMap = cityRepository.findAllByCityIdInAndIsActiveTrue(
                        locationMap.values().stream().map(LocationEntity::getCityId).collect(Collectors.toSet())
                ).stream().
                collect(Collectors.toMap(CityEntity::getCityId, city -> city));

        Map<Long, StateEntity> stateMap = stateRepository.findAllByStateIdInAndIsActiveTrue(
                        cityMap.values().stream()
                                .map(CityEntity::getStateId)
                                .collect(Collectors.toSet())
                ).stream().
                collect(Collectors.toMap(StateEntity::getStateId, state -> state));

        Map<Long, CountryEntity> countryMap = countryRepository.findAllByCountryIdInAndIsActiveTrue(
                        stateMap.values().stream().map(StateEntity::getCountryId).collect(Collectors.toSet())
                ).stream().
                collect(Collectors.toMap(CountryEntity::getCountryId, country -> country));

        return candidateApplicationsList.stream().map(application -> {
            CandidatesEntity candidate = application.getCandidate();
            if (candidate == null) return null;

            LocationEntity location = locationMap.get(locationIds.get(0));
            CityEntity city = location != null ? cityMap.get(location.getCityId()) : null;
            StateEntity state = city != null ? stateMap.get(city.getStateId()) : null;
            CountryEntity country = state != null ? countryMap.get(state.getCountryId()) : null;

            Map<Long, String> locationDetails = location != null ? Map.of(location.getLocationId(), location.getLocationName()) : null;
            Map<Long, String> cityDetails = city != null ? Map.of(city.getCityId(), city.getCityName()) : null;
            Map<Long, String> stateDetails = state != null ? Map.of(state.getStateId(), state.getStateName()) : null;
            Map<Long, String> countryDetails = country != null ? Map.of(country.getCountryId(), country.getCountryName()) : null;

            List<CandidateDocumentsEntity> documents = candidateDocumentsRepository.findByCandidateIdAndApplicationId(
                    candidate.getCandidateId(),
                    application.getApplicationId()
            );
            String  offerLetterUrl = documents.stream()
                    .filter(doc -> AppConstants.CAND_DOC_TYPE_OFFERLETTER.equals(doc.getDocumentType()))
                    .map(CandidateDocumentsEntity::getFileUrl)
                    .findFirst()
                    .orElse(null);

            return GetCandidateDetailsByPositionIdResponse.builder()
                    .applicationId(application.getApplicationId())
                    .candidateId(candidate.getCandidateId())
                    .fullName(candidate.getFullName())
                    .username(candidate.getUsername())
                    .email(candidate.getEmail())
                    .phone(candidate.getPhone())
                    .reservationCategoryId(candidate.getReservationCategoryId())
                    .highestQualificationId(candidate.getHighestQualificationId())
                    .idProof(candidate.getIdProof())
                    .nationalityId(candidate.getNationalityId())
                    .totalExperience(candidate.getTotalExperience())
                    .address(candidate.getAddress())
                    .gender(candidate.getGender())
                    .specialCategoryId(candidate.getSpecialCategoryId())
                    .fileUrl(candidate.getFileUrl())
                    .applicationStatus(application.getApplicationStatus())
                    .locationDetails(locationDetails)
                    .cityDetails(cityDetails)
                    .stateDetails(stateDetails)
                    .countryDetails(countryDetails)
                    .offerLetterUrl(offerLetterUrl)
                    .documentUrl(candidate.getDocumentUrl())
                    .rank(candidate.getRank())
                    .skills(candidate.getSkills())
                    .comments(candidate.getComments())
                    .currentEmployer(candidate.getCurrentEmployer())
                    .currentDesignation(candidate.getCurrentDesignation())
                    .dateOfBirth(candidate.getDateOfBirth())
                    .educationQualification(candidate.getEducationQualification())
                    .build();
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<CandidatesDTO> getDetailsByPositionId(UUID position_id) {
        List<CandidateApplicationsEntity> candidateApplicationsList = candidateApplicationsRepository.findByPositionId(position_id);
        if (candidateApplicationsList.isEmpty()) {
            return null; // or throw an exception if no applications found
        }
        List<CandidatesEntity> candidatesList = candidateApplicationsList.stream()
                .map(application -> application.getCandidate())
                .filter(Objects::nonNull)
                .toList();

        return candidatesMapper.toDtoList(candidatesList);
    }

    public CandidatesDTO getCandidatesById(UUID candidate_id) {
        return candidateRepository.findById(candidate_id)
                .map(candidatesMapper::toDto)
                .orElse(null);
    }

    // Changed from private to package-private for testing



    public String offer(OfferDTO offerdto) throws MessagingException, UnsupportedEncodingException {
        try {
            // 1. Fetch candidate
            CandidatesEntity candidates = candidateRepository.findById(offerdto.getCandidateId()).orElse(null);
            if (candidates == null) {
                return "Candidate not found!";
            }

            // 2. Fetch position title
            String positionTitle = positionsRepository.findById(offerdto.getPositionId())
                    .map(PositionsEntity::getPositionTitle)
                    .orElse("Unknown Position");

            // 3. Fetch candidate application
            List<CandidateApplicationsEntity> applications = candidateApplicationsRepository
                    .findByCandidateIdAndPositionId(offerdto.getCandidateId(), offerdto.getPositionId());

            if (applications.isEmpty()) {
                return "Candidate application not found!";
            }

            CandidateApplicationsEntity application = applications.get(0);

            // 4. Ensure candidate is "Scheduled"
//            if (!application.getApplicationStatus().equalsIgnoreCase("Scheduled")) {
//                return "Offer can only be sent to candidates with 'Scheduled' status.";
//            }

            // 5. Prepare email data
            Map<String, Object> offerDetails = new HashMap<>();
            offerDetails.put("COMPANY_NAME", AppConstants.OFFER_DETAILS_COMPANY_NAME);
            offerDetails.put("CANDIDATE_NAME", candidates.getFullName());
            offerDetails.put("POSITION_TITLE", positionTitle);
            offerDetails.put("PATH", offerdto.getOfferLetterPath());
            offerDetails.put(AppConstants.EMAIL_DATA_STATUS,AppConstants.CAND_APP_STATUS_OFFERED);
            String email = candidates.getEmail();
            String path = offerdto.getOfferLetterPath();


            boolean candidateOfferLetterSent = candidateHelperService.sendEmailWithAttachmentRetry(email, "Offer Letter!", "OfferLetter", offerDetails, path);


            if (!candidateOfferLetterSent) {
                return "Failed to send offer letter email.";
            }

            // 7. Save candidate document
            CandidateDocumentsEntity candidateDocuments = new CandidateDocumentsEntity();
            candidateDocuments.setDocumentId(UUID.randomUUID());
            candidateDocuments.setCandidateId(offerdto.getCandidateId());
            candidateDocuments.setApplicationId(application.getApplicationId());
            candidateDocuments.setDocumentType(AppConstants.CAND_DOC_TYPE_OFFERLETTER);
            candidateDocuments.setFileName(offerdto.getCandidateId().toString() + ".pdf");
            candidateDocuments.setFileUrl(path);
            candidateDocuments.setUploadedDate(LocalDateTime.now());
            candidateDocumentsRepository.save(candidateDocuments);

            // 8. Update application status
            application.setApplicationStatus(AppConstants.CAND_APP_STATUS_OFFERED);
            application.setUpdatedDate(LocalDateTime.now());
            application.setJoiningDate(offerdto.getJoiningDate());
            application.setCtc(offerdto.getSalary());
            application.setDesignation(offerdto.getDesignation());
            saveCandidateApplicationWithWorkflow(application);

            return "Offer sent!";
        } catch (Exception e) {
            return "Failed to send email due to: " + e.getMessage();
        }
    }

    public List<CandidateDetailsResponse> getCandidateByApplicationStatus(String status) {

        //Get applications by status
        List<CandidateApplicationsEntity> applications =
                candidateApplicationsRepository.findByApplicationStatus(status);

        if (applications.isEmpty()) return Collections.emptyList();

        // get positionIds
        List<UUID> positionIds = applications.stream()
                .map(CandidateApplicationsEntity::getPositionId)
                .distinct()
                .toList();

        //Get candidateIds
        List<UUID> candidateIds = applications.stream()
                .map(app -> app.getCandidate().getCandidateId())
                .distinct()
                .toList();

        // Get joblocations for positions
        List<JobPostingLocationEntity> jobLocations =
                jobPostingLocationRepository.findByPositionIdIn(positionIds);

        // Create map of positionId to locationId
        Map<UUID, Long> positionToLocationMap = jobLocations.stream()
                .collect(Collectors.toMap(JobPostingLocationEntity::getPositionId,
                        JobPostingLocationEntity::getLocationId));

        // Get all required Locations
        List<Long> locationIds = new ArrayList<>(positionToLocationMap.values());

        Map<Long, LocationEntity> locationMap = locationRepository.findAllByLocationIdInAndIsActiveTrue(locationIds)
                .stream()
                .collect(Collectors.toMap(LocationEntity::getLocationId, l -> l));

        // Get All required Cities
        Set<Long> cityIds = locationMap.values().stream()
                .map(LocationEntity::getCityId)
                .collect(Collectors.toSet());

        Map<Long, CityEntity> cityMap = cityRepository.findAllByCityIdInAndIsActiveTrue(cityIds)
                .stream()
                .collect(Collectors.toMap(CityEntity::getCityId, c -> c));

        // Get all required States
        Set<Long> stateIds = cityMap.values().stream()
                .map(CityEntity::getStateId)
                .collect(Collectors.toSet());

        Map<Long, StateEntity> stateMap = stateRepository.findAllByStateIdInAndIsActiveTrue(stateIds)
                .stream()
                .collect(Collectors.toMap(StateEntity::getStateId, s -> s));

        // Get all countries
        Set<Long> countryIds = stateMap.values().stream()
                .map(StateEntity::getCountryId)
                .collect(Collectors.toSet());


        Map<Long, CountryEntity> countryMap = countryRepository.findAllByCountryIdInAndIsActiveTrue(countryIds)
                .stream()
                .collect(Collectors.toMap(CountryEntity::getCountryId, c -> c));

        // Get all candidates

        Map<UUID, CandidatesEntity> candidateMap = candidateRepository.findByCandidateIdInAndIsActiveTrue(candidateIds).stream()
                .collect(Collectors.toMap(CandidatesEntity::getCandidateId, c -> c));

        // Use streams to map data
        return applications.stream()
                .map(app -> {
                    CandidatesEntity candidate = candidateMap.get(app.getCandidate().getCandidateId());
                    if (candidate == null) return null;

                    CandidateDetailsResponse response = new CandidateDetailsResponse();
                    response.setCandidateId(candidate.getCandidateId());
                    response.setFullName(candidate.getFullName());
                    response.setUsername(candidate.getUsername());
                    response.setEmail(candidate.getEmail());
                    response.setPhone(candidate.getPhone());
                    response.setGender(candidate.getGender());
                    response.setReservationCategoryId(candidate.getReservationCategoryId());
                    response.setHighestQualificationId(candidate.getHighestQualificationId());
                    response.setTotalExperience(candidate.getTotalExperience());
                    response.setAddress(candidate.getAddress());
                    response.setSpecialCategoryId(candidate.getSpecialCategoryId());
                    response.setFileUrl(candidate.getFileUrl());
                    response.setApplicationStatus(app.getApplicationStatus());
                    response.setComments(candidate.getComments());
                    response.setRank(candidate.getRank());
                    response.setSkills(candidate.getSkills());
                    response.setCurrentEmployer(candidate.getCurrentEmployer());
                    response.setCurrentDesignation(candidate.getCurrentDesignation());
                    response.setDateOfBirth(candidate.getDateOfBirth());
                    response.setEducationQualification(candidate.getEducationQualification());
                    response.setDocumentUrl(candidate.getDocumentUrl());
                    response.setIdProof(candidate.getIdProof());
                    response.setNationalityId(candidate.getNationalityId());

                    // Location details
                    Long locationId = positionToLocationMap.get(app.getPositionId());
                    if (locationId != null) {
                        LocationEntity location = locationMap.get(locationId);
                        if (location != null) {
                            response.setLocationDetails(Map.of(location.getLocationId(), location.getLocationName()));

                            CityEntity city = cityMap.get(location.getCityId());
                            if (city != null) {
                                response.setCityDetails(Map.of(city.getCityId(), city.getCityName()));

                                StateEntity state = stateMap.get(city.getStateId());
                                if (state != null) {
                                    response.setStateDetails(Map.of(state.getStateId(), state.getStateName()));

                                    CountryEntity country = countryMap.get(state.getCountryId());
                                    if (country != null) {
                                        response.setCountryDetails(Map.of(country.getCountryId(), country.getCountryName()));
                                    }
                                }
                            }
                        }
                    }

                    return response;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public Integer countCandidatesByStatus(String status) {
        return getCandidateByApplicationStatus(status).size();
    }

    //Method to get all candidate details
    public List<CandidateDetailsResponse> getAllCandidateDetails() {
        // Step 1: Fetch all applications
        List<CandidateApplicationsEntity> applications = candidateApplicationsRepository.findAll();

        if (applications.isEmpty()) return Collections.emptyList();

        // Step 2: Collect all positionIds and candidateIds
        List<UUID> positionIds = applications.stream()
                .map(CandidateApplicationsEntity::getPositionId)
                .distinct()
                .toList();

        List<UUID> candidateIds = applications.stream()
                .map(app -> app.getCandidate().getCandidateId())
                .distinct()
                .toList();

        // Step 3: Fetch JobPostingLocations in one call
        List<JobPostingLocationEntity> jobLocations = jobPostingLocationRepository.findByPositionIdIn(positionIds);

        Map<UUID, Long> positionToLocationMap = jobLocations.stream()
                .collect(Collectors.toMap(JobPostingLocationEntity::getPositionId,
                        JobPostingLocationEntity::getLocationId));

        // Step 4: Fetch all required Locations
        List<Long> locationIds = new ArrayList<>(positionToLocationMap.values());

        Map<Long, LocationEntity> locationMap = locationRepository.findAllByLocationIdInAndIsActiveTrue(locationIds).stream()
                .collect(Collectors.toMap(LocationEntity::getLocationId, l -> l));

        // Step 5: Fetch all required Cities
        Set<Long> cityIds = locationMap.values().stream()
                .map(LocationEntity::getCityId)
                .collect(Collectors.toSet());


        Map<Long, CityEntity> cityMap = cityRepository.findAllByCityIdInAndIsActiveTrue(cityIds).stream()
                .collect(Collectors.toMap(CityEntity::getCityId, c -> c));

        // Step 6: Fetch all required States
        Set<Long> stateIds = cityMap.values().stream()
                .map(CityEntity::getStateId)
                .collect(Collectors.toSet());

        Map<Long, StateEntity> stateMap = stateRepository.findAllByStateIdInAndIsActiveTrue(stateIds).stream()
                .collect(Collectors.toMap(StateEntity::getStateId, s -> s));
        // Step 7: Fetch all required Countries
        Set<Long> countryIds = stateMap.values().stream()
                .map(StateEntity::getCountryId)
                .collect(Collectors.toSet());

        Map<Long, CountryEntity> countryMap = countryRepository.findAllByCountryIdInAndIsActiveTrue(countryIds).stream()
                .collect(Collectors.toMap(CountryEntity::getCountryId, c -> c));

        // Step 8: Fetch all candidates in one go
        Map<UUID, CandidatesEntity> candidateMap = candidateRepository.findByCandidateIdInAndIsActiveTrue(candidateIds).stream()
                .collect(Collectors.toMap(CandidatesEntity::getCandidateId, c -> c));

        // Step 9: Build response with streams
        return applications.stream()
                .map(app -> {
                    CandidatesEntity candidate = candidateMap.get(app.getCandidate().getCandidateId());
                    if (candidate == null) return null;

                    CandidateDetailsResponse response = new CandidateDetailsResponse();
                    response.setCandidateId(candidate.getCandidateId());
                    response.setFullName(candidate.getFullName());
                    response.setUsername(candidate.getUsername());
                    response.setEmail(candidate.getEmail());
                    response.setPhone(candidate.getPhone());
                    response.setGender(candidate.getGender());
                    response.setReservationCategoryId(candidate.getReservationCategoryId());
                    response.setHighestQualificationId(candidate.getHighestQualificationId());
                    response.setTotalExperience(candidate.getTotalExperience());
                    response.setAddress(candidate.getAddress());
                    response.setSpecialCategoryId(candidate.getSpecialCategoryId());
                    response.setFileUrl(candidate.getFileUrl());
                    response.setApplicationStatus(app.getApplicationStatus());
                    response.setComments(candidate.getComments());
                    response.setRank(candidate.getRank());
                    response.setSkills(candidate.getSkills());
                    response.setCurrentEmployer(candidate.getCurrentEmployer());
                    response.setCurrentDesignation(candidate.getCurrentDesignation());
                    response.setDateOfBirth(candidate.getDateOfBirth());
                    response.setEducationQualification(candidate.getEducationQualification());
                    response.setDocumentUrl(candidate.getDocumentUrl());
                    response.setIdProof(candidate.getIdProof());
                    response.setNationalityId(candidate.getNationalityId());


                    // Location details
                    Long locationId = positionToLocationMap.get(app.getPositionId());
                    if (locationId != null) {
                        LocationEntity location = locationMap.get(locationId);
                        if (location != null) {
                            response.setLocationDetails(Map.of(location.getLocationId(), location.getLocationName()));

                            CityEntity city = cityMap.get(location.getCityId());
                            if (city != null) {
                                response.setCityDetails(Map.of(city.getCityId(), city.getCityName()));

                                StateEntity state = stateMap.get(city.getStateId());
                                if (state != null) {
                                    response.setStateDetails(Map.of(state.getStateId(), state.getStateName()));

                                    CountryEntity country = countryMap.get(state.getCountryId());
                                    if (country != null) {
                                        response.setCountryDetails(Map.of(country.getCountryId(), country.getCountryName()));
                                    }
                                }
                            }
                        }
                    }

                    return response;
                })
                .filter(Objects::nonNull)
                .toList();
    }




    public InterviewsEntity getInterviewDetailsByInterviewId(UUID interview_id) {

        return interviewsRepository.findById(interview_id).orElse(null);
    }




    public InterviewerResponse getInterviewsByApplicationId(UUID applicationId)  {
        InterviewsEntity interviewsList = interviewsRepository.findByApplicationId(applicationId).orElse(null);

        if (interviewsList  == null ) {
            return null;
        }

        InterviewsEntity interview = interviewsList;
        InterviewersEntity interviewersEntity = null;
        InterviewPanelsEntity interviewPanels = null;
        if(interview.getIsPanelInterview()){
            interviewPanels = interviewPanelsRepository.findById(interview.getInterviewerId()).orElse(null);
        }else {
            interviewersEntity = interviewerRepository.findById(interview.getInterviewerId()).orElse(null);
        }
        InterviewerResponse interviewerResponse=new InterviewerResponse();

        interviewerResponse.setInterviewId(interview.getInterviewId());
        interviewerResponse.setType(interview.getType());
        interviewerResponse.setStatus(interview.getStatus());
        interviewerResponse.setScheduledAt(interview.getScheduledAt());
        interviewerResponse.setPhone(interview.getPhone());
        interviewerResponse.setIsPanelInterview(interview.getIsPanelInterview());
        interviewerResponse.setLocation(interview.getLocation());
        interviewerResponse.setInterviewerId(interview.getInterviewerId());
        interviewerResponse.setInterviewer( !interview.getIsPanelInterview() ? interviewersEntity.getFullName() : interviewPanels.getPanelName());
        interviewerResponse.setInterviewerEmail( !interview.getIsPanelInterview() ? interviewersEntity.getEmail() : null);

        return interviewerResponse;


    }





    public CandidateApplicationsEntity createCandidateShortlistObject(UUID candidateId, UUID positionId){
        CandidateApplicationsEntity candidateApplications = new CandidateApplicationsEntity();
        candidateApplications.setCandidate(candidatesRepository.findById(candidateId).orElse(null));
        candidateApplications.setCandidateId(candidateId);
        candidateApplications.setPositionId(positionId);
        candidateApplications.setApplicationStatus(AppConstants.CAND_APP_STATUS_SHORTLISTED);
        candidateApplications.setApplicationDate( LocalDateTime.now());
        return candidateApplications;
    }

    public String shortlistCandidate(ShortlistCandidateDTO shortlistCandidateDTO) {

        try {
            CandidateApplicationsEntity candidateApplications = createCandidateShortlistObject(shortlistCandidateDTO.getCandidateId(),shortlistCandidateDTO.getPositionId());
            saveCandidateApplicationWithWorkflow(candidateApplications);
            return "Applied for Job!";
        } catch (Exception e) {
            return "Couldn't Apply!";
        }
    }


    public List<JobPositionsModel> getAllDetailsByCandidateId(UUID candidate_id) {
        List<CandidateApplicationsEntity> candidateApplicationsList = candidateApplicationsRepository.findByCandidateId(candidate_id);
        List<UUID> positionIds=candidateApplicationsList.stream().map(CandidateApplicationsEntity::getPositionId).toList();

        String token = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getToken()
                .getTokenValue();

        ResponseEntity<ApiResponse<List<JobPositionsModel>>> responseDTOS=feignPositionDTO.getActiveJobs("Bearer "+token);

        List<JobPositionsModel> jobPositionsDTOList=responseDTOS.getBody().getData();

        return jobPositionsDTOList.stream().filter(jobPositionsDTO -> positionIds.contains(jobPositionsDTO.getPositionId())).toList();

    }

    public CandidatesDTO updateCandidate(CandidatesDTO candidate) {
        try {
            CandidatesEntity existingCandidate = candidateRepository.findById(candidate.getCandidateId())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            // Update candidate details
            existingCandidate.setFullName(candidate.getFullName());
            existingCandidate.setDateOfBirth(candidate.getDateOfBirth());
            existingCandidate.setPhone(candidate.getPhone());
            existingCandidate.setIdProof(candidate.getIdProof());
            existingCandidate.setGender(candidate.getGender());
            existingCandidate.setNationalityId(candidate.getNationalityId());
            existingCandidate.setReservationCategoryId(candidate.getReservationCategoryId()); // Assuming 1 is the default value for reservation category
            existingCandidate.setSpecialCategoryId(candidate.getSpecialCategoryId()); // Assuming 1 is the default value for special category
            existingCandidate.setHighestQualificationId(null);
            existingCandidate.setAddress(candidate.getAddress());
            existingCandidate.setTotalExperience(candidate.getTotalExperience());
            existingCandidate.setComments(candidate.getComments());
            existingCandidate.setSkills(candidate.getSkills());
            existingCandidate.setCurrentDesignation(candidate.getCurrentDesignation());
            existingCandidate.setCurrentEmployer(candidate.getCurrentEmployer());
            existingCandidate.setFileUrl(candidate.getFileUrl());
            existingCandidate.setEducationQualification(candidate.getEducationQualification());
            existingCandidate.setDocumentUrl(candidate.getDocumentUrl());
            existingCandidate.setDobValidated(candidate.isDobValidated());
            // Save updated candidate
            CandidatesEntity updatedCandidate = candidateRepository.save(existingCandidate);

            // Convert to DTO and return
            return candidatesMapper.toDto(updatedCandidate);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't create candidate due to: " + e.getMessage());
        }
    }

    @Transactional
    public String scheduleInterview(ScheduleInterviewRequest req) throws MessagingException, UnsupportedEncodingException {
        // Fetch Interview record or create new
        InterviewsEntity interview = candidateHelperService.getOrCreateInterview(req);

        // Fetch required entities
        CandidateApplicationsEntity application = candidateApplicationsRepository.findById(req.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Can't find the application"));



        if(req.getStatus().equals("Rescheduled")){
        // Update interview
        interview.setScheduledAt(LocalDateTime.of(req.getDate(), req.getTime()));
        interview.setPhone(req.getPhone());
        interview.setInterviewerId(req.getInterviewerId());
        interview.setLocation(req.getLocation());
        interview.setIsPanelInterview(req.getIsPanelInterview());
        interview.setType(req.getType());
        interview.setStatus(req.getStatus());
        }else if(req.getStatus().equals("Cancelled")){
            interview.setStatus(req.getStatus());
        }


        // Update DB
        application.setApplicationStatus(req.getStatus());
        InterviewsEntity interviewsEntity =interviewsRepository.saveWithAudit(null, interview);
        CandidateApplicationsEntity candidateApplications = candidateApplicationsRepository.saveWithWorkflow(application);
        saveCandidateApplicationWithWorkflow(candidateApplications);
        candidateHelperService.sendEmailScheduleForCandidatesAndInterviews(interviewsEntity,candidateApplications);
        return "Interview " + req.getStatus() + " successfully!";

    }

    @Transactional
    public String submitFeedback(FeedbackRequest feedbackRequest) {
        try {
            // 1. Get candidate application safely
            CandidateApplicationsEntity candidateApplications = candidateApplicationsRepository.findById(feedbackRequest.getApplicationId()).orElse(null);

            if (candidateApplications == null) {
                throw new RuntimeException("Candidate application not found!");
            }
            // 2. Get interview safely
            InterviewsEntity interviews = interviewsRepository.findByApplicationId(candidateApplications.getApplicationId()).orElseThrow(()-> new RuntimeException("Interview not found"));


            if (interviews == null) {
                throw new RuntimeException("Interview not found for candidate!");
            }

            // 3. Create workflow approval
            WorkflowApprovalEntity workflowApproval = WorkflowApprovalEntity.builder()
                    .entityId(candidateApplications.getApplicationId())
                    .approverRole(interviews.getIsPanelInterview()? AppConstants.WORKFLOW_INTERVIEWER_APPROVER_ROLE_PANEL : AppConstants.WORKFLOW_INTERVIEWER_APPROVER_ROLE)
                    .approverId(feedbackRequest.getInterviewerId())
                    .action(AppConstants.WORKFLOW_INTERVIEWER_APPROVER_FEEDBACK)
                    .actionDate(LocalDateTime.now())
                    .comments(feedbackRequest.getComments())
                    .status(feedbackRequest.getStatus())
                    .entityType(AppConstants.WORKFLOW_INTERVIEWS_ENTITY_TYPE)
                    .stepNumber(1)
                    .build();

            InterviewsEntity oldInterviewsEntity = interviews.toBuilder().build();
            interviews.setStatus(feedbackRequest.getStatus());

            // 4. Update candidate applications
            candidateApplications.setApplicationStatus(feedbackRequest.getStatus());
            candidateApplications.setUpdatedDate(LocalDateTime.now());

            // 5. Save all entities (transaction will commit only if all succeed)
            interviewsRepository.saveWithAudit(oldInterviewsEntity,interviews);
            candidateApplicationsRepository.saveWithWorkflow(candidateApplications);
            workflowApprovalRepository.save(workflowApproval); //TODO: Mahesh check if this can be moved inside saveWithWorkflow()

            return "Feedback submitted successfully!";
        } catch (Exception e) {
            // Log full stack trace for debugging

            throw new RuntimeException("Couldn't submit feedback: " + e.getMessage(), e);
        }
    }

    public List<FeedbackResponse> getFeedbackByApplicationId(UUID application_id){
        try{
            List<FeedbackResponse> feedbackResponse = new ArrayList<>();
            CandidateApplicationsEntity candidateApplications=candidateApplicationsRepository.findById(application_id).orElse(null);
            if(candidateApplications==null){
                return null;
            }

            List<WorkflowApprovalEntity> workflowApprovalEntity =
                    workflowApprovalRepository.findByEntityIdAndEntityTypeAndAction(
                            candidateApplications.getApplicationId(),
                            AppConstants.WORKFLOW_INTERVIEWS_ENTITY_TYPE,
                            AppConstants.WORKFLOW_INTERVIEWER_APPROVER_FEEDBACK
                    );



            List<Long> interviewerIds = workflowApprovalEntity.stream().filter(entity->entity.getApproverRole().equals(AppConstants.WORKFLOW_INTERVIEWER_APPROVER_ROLE)).map(WorkflowApprovalEntity::getApproverId).toList();

            List<Long> panelIds = workflowApprovalEntity.stream().filter(entity->entity.getApproverRole().equals(AppConstants.WORKFLOW_INTERVIEWER_APPROVER_ROLE_PANEL)).map(WorkflowApprovalEntity::getApproverId).toList();

            List<InterviewersEntity> interviewersEntities = interviewerRepository.findAllById(interviewerIds);
            List<InterviewPanelsEntity> interviewPanelsEntities = interviewPanelsRepository.findAllById(panelIds);

            for(WorkflowApprovalEntity workflowApproval:workflowApprovalEntity){
                String userName = AppConstants.USER_UNKNOWN;
                if (workflowApproval.getApproverRole().equals(AppConstants.WORKFLOW_INTERVIEWER_APPROVER_ROLE)) {
                    userName = interviewersEntities.stream()
                            .filter(entity -> entity.getInterviewerId().equals(workflowApproval.getApproverId()))
                            .map(entity -> entity.getFullName())   // map to string directly
                            .findFirst()
                            .orElse(AppConstants.USER_UNKNOWN);
                } else {
                    userName = interviewPanelsEntities.stream()
                            .filter(entity -> entity.getPanelId().equals(workflowApproval.getApproverId()))
                            .map(entity -> entity.getPanelName())  // or getFullName() depending on field
                            .findFirst()
                            .orElse(AppConstants.USER_UNKNOWN);
                }
                FeedbackResponse feedback= FeedbackResponse.builder()
                        .approvalId(workflowApproval.getApprovalId())
                        .comments(workflowApproval.getComments())
                        .status(workflowApproval.getStatus())
                        .actionDate(workflowApproval.getActionDate())
                        .interviewerName(userName)
                        .build();

                feedbackResponse.add(feedback);
            }

            return feedbackResponse;
        }catch (Exception e){
            return null;
        }
    }



    //panel free slots

    public List<Map<String, Object>> getPanelSlots(Long panelId, String date) {
        LocalDate currentdate = LocalDate.parse(date);
        LocalDateTime startOfDay = currentdate.atTime(9, 0);
        LocalDateTime endOfDay = currentdate.atTime(18, 0);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        // Fetch booked slots
        List<InterviewsEntity> booked = interviewsRepository
                .findAllByinterviewerIdAndScheduledAtBetweenAndIsPanelInterviewTrue(
                        panelId, startOfDay, endOfDay);

        Set<LocalDateTime> bookedSlots = booked.stream()
                .map(InterviewsEntity::getScheduledAt)
                .collect(Collectors.toSet());

        List<Map<String, Object>> slots = new ArrayList<>();
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");

        LocalDateTime current = LocalDateTime.of(currentdate, startTime);

        while (current.isBefore(LocalDateTime.of(currentdate, endTime))) {
            LocalDateTime slotEnd = current.plusMinutes(30);

            Map<String, Object> slot = new HashMap<>();
            slot.put("start", current.atZone(zoneId).toInstant().toString());
            slot.put("end", slotEnd.atZone(zoneId).toInstant().toString());

            // ✅ Booked if slot start time is booked or previous 30-min slot is booked
            boolean isBooked = bookedSlots.contains(current) ||
                    bookedSlots.contains(current.minusMinutes(30)) ||
                    bookedSlots.contains(current.plusMinutes(30));

            slot.put("booked", isBooked);

            slots.add(slot);
            current = slotEnd;
        }

        return slots.stream()
                .filter(slot -> Boolean.FALSE.equals(slot.get("booked")))
                .toList();
    }


    public void saveCandidateApplicationWithWorkflow(CandidateApplicationsEntity application){
        candidateApplicationsRepository.save(application);
        saveWorkflowStatusChange(application);
    }

    public List<CandidatesDTO> getCandidatesNotAppliedBulkUpload(UUID positionId) {
        List<CandidatesEntity> notAppliedBulk = candidatesRepository.findBulkUploadNotAppliedForPosition(positionId,securityUtils.getCurrentUserToken());
        return notAppliedBulk.stream().map(candidatesMapper::toDto).toList();
    }


    public Map<UUID, String> bulkShortlistCandidates(BulkShortlistRequest request) {
        Map<UUID, String> result = new HashMap<>();
        List<CandidateApplicationsEntity> applications = new ArrayList<>();
        for (UUID candidateId : request.getCandidateIds()) {
            try {
                CandidateApplicationsEntity app = createCandidateShortlistObject(candidateId, request.getPositionId());
                applications.add(app);
            } catch (Exception e) {
                result.put(candidateId, "Error creating application: " + e.getMessage());
            }
        }
        // Save all applications with workflow in one batch
        List<CandidateApplicationsEntity> saved = candidateApplicationsRepository.saveAll(applications);

        // Save workflow for capturing status changes
        saveWorkflowStatusChangeList(saved);

        for (CandidateApplicationsEntity app : saved) {
            result.put(app.getCandidateId(), "Applied for Job!");
        }
        // For any candidateId not in result, mark as failed
        for (UUID candidateId : request.getCandidateIds()) {
            result.putIfAbsent(candidateId, "Failed to apply");
        }
        return result;
    }

    public WorkflowApprovalEntity createWorkflowEntityCandidateApplication(CandidateApplicationsEntity savedEntity){
        return WorkflowApprovalEntity.builder()
                .entityId(savedEntity.getApplicationId())
                .entityType(CandidateApplicationsEntity.ENTITY_TYPE)
                .stepNumber(1) // Assuming step 1 for now
                .approverRole(securityUtils.getCurrentUserRole())
                .approverId(securityUtils.getCurrentUserId())
                .action(savedEntity.getApplicationStatus())
                .actionDate(LocalDateTime.now())
                .comments("Status updated to " + savedEntity.getApplicationStatus())
                .status(savedEntity.getApplicationStatus())
                .build();
    }

    public void saveWorkflowStatusChange(CandidateApplicationsEntity savedEntity){
        try {
            workflowApprovalRepository.save(createWorkflowEntityCandidateApplication(savedEntity));
        } catch (Exception ex) {
            logger.error("Failed to save workflow approval for applicationId {}: {}", savedEntity.getApplicationId(), ex.getMessage(), ex);
        }
    }

    public void saveWorkflowStatusChangeList(List<CandidateApplicationsEntity> savedEntities) {
        try {
            List<WorkflowApprovalEntity> workflowApprovals = savedEntities.stream()
                    .map(this::createWorkflowEntityCandidateApplication)
                    .collect(Collectors.toList());
            workflowApprovalRepository.saveAll(workflowApprovals);
        } catch (Exception ex) {
            logger.error("Failed to save bulk workflow approvals for {} applications: {}", savedEntities.size(), ex.getMessage(), ex);
        }
    }

}
