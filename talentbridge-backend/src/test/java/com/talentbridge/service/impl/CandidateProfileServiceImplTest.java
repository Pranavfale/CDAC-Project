package com.talentbridge.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import com.talentbridge.dto.request.CandidateProfileRequest;
import com.talentbridge.dto.response.CandidateProfileResponse;
import com.talentbridge.dto.response.ResumeDownload;
import com.talentbridge.entity.CandidateProfile;
import com.talentbridge.entity.User;
import com.talentbridge.enums.Role;
import com.talentbridge.exception.DuplicateCandidateProfileException;
import com.talentbridge.exception.ResumeNotFoundException;
import com.talentbridge.mapper.CandidateProfileMapper;
import com.talentbridge.repository.CandidateProfileRepository;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.storage.ResumeStorageService;
import com.talentbridge.storage.StoredResume;
import com.talentbridge.storage.StoredResumeFile;

/**
 * Unit tests for authenticated Candidate Profile and resume operations.
 *
 * These tests use Mockito and do not start Spring Boot, connect to MySQL,
 * or access the real filesystem.
 */
@ExtendWith(MockitoExtension.class)
class CandidateProfileServiceImplTest {

    private static final String CANDIDATE_EMAIL =
            "candidate@example.com";

    private static final Long CANDIDATE_ID = 1L;

    private static final Long PROFILE_ID = 10L;

    private static final String ORIGINAL_RESUME_NAME =
            "Candidate_Resume.pdf";

    private static final String OLD_STORED_RESUME_NAME =
            "11111111-1111-1111-1111-111111111111.pdf";

    private static final String NEW_STORED_RESUME_NAME =
            "22222222-2222-2222-2222-222222222222.pdf";

    @Mock
    private CandidateProfileRepository candidateProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResumeStorageService resumeStorageService;

    @Mock
    private MultipartFile multipartFile;

    private CandidateProfileServiceImpl candidateProfileService;

    private CandidateProfileMapper candidateProfileMapper;

    private User candidate;

    @BeforeEach
    void setUp() {

        candidateProfileMapper =
                new CandidateProfileMapper();

        candidateProfileService =
                new CandidateProfileServiceImpl(
                        candidateProfileRepository,
                        userRepository,
                        candidateProfileMapper,
                        resumeStorageService);

        candidate =
                createCandidate();
    }

    @Test
    @DisplayName(
        "getProfile returns the authenticated candidate profile"
    )
    void getProfileReturnsAuthenticatedCandidateProfile() {

        CandidateProfile profile =
                createCompleteProfileWithResume();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(profile));

        CandidateProfileResponse response =
                candidateProfileService.getProfile(
                        CANDIDATE_EMAIL);

        assertAll(
                () -> assertNotNull(response),

                () -> assertEquals(
                        PROFILE_ID,
                        response.getProfileId()),

                () -> assertEquals(
                        CANDIDATE_ID,
                        response.getUserId()),

                () -> assertEquals(
                        "Candidate User",
                        response.getFullName()),

                () -> assertEquals(
                        CANDIDATE_EMAIL,
                        response.getEmail()),

                () -> assertEquals(
                        "Java Developer",
                        response.getHeadline()),

                () -> assertTrue(
                        response.isResumeUploaded()),

                () -> assertEquals(
                        ORIGINAL_RESUME_NAME,
                        response.getResumeFileName()),

                () -> assertEquals(
                        100,
                        response.getProfileCompletion())
        );

        verify(candidateProfileRepository)
                .findByUser_Id(CANDIDATE_ID);
    }

    @Test
    @DisplayName(
        "createProfile creates one profile and calculates 90 percent completion"
    )
    void createProfileCreatesProfileSuccessfully() {

        CandidateProfileRequest request =
                createCompleteProfileRequest();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .existsByUser_Id(CANDIDATE_ID))
                .thenReturn(false);

        when(candidateProfileRepository
                .save(any(CandidateProfile.class)))
                .thenAnswer(invocation -> {

                    CandidateProfile savedProfile =
                            invocation.getArgument(0);

                    savedProfile.setProfileId(PROFILE_ID);

                    return savedProfile;
                });

        CandidateProfileResponse response =
                candidateProfileService.createProfile(
                        CANDIDATE_EMAIL,
                        request);

        assertAll(
                () -> assertNotNull(response),

                () -> assertEquals(
                        PROFILE_ID,
                        response.getProfileId()),

                () -> assertEquals(
                        CANDIDATE_ID,
                        response.getUserId()),

                () -> assertEquals(
                        "Java Developer",
                        response.getHeadline()),

                () -> assertEquals(
                        "Pune",
                        response.getCity()),

                () -> assertEquals(
                        "Java, Spring Boot, MySQL",
                        response.getSkills()),

                () -> assertEquals(
                        90,
                        response.getProfileCompletion()),

                () -> assertEquals(
                        false,
                        response.isResumeUploaded())
        );

        ArgumentCaptor<CandidateProfile> profileCaptor =
                ArgumentCaptor.forClass(
                        CandidateProfile.class);

        verify(candidateProfileRepository)
                .save(profileCaptor.capture());

        CandidateProfile savedProfile =
                profileCaptor.getValue();

        assertAll(
                () -> assertSame(
                        candidate,
                        savedProfile.getUser()),

                () -> assertEquals(
                        90,
                        savedProfile.getProfileCompletion()),

                () -> assertEquals(
                        "Java Developer",
                        savedProfile.getHeadline()),

                () -> assertEquals(
                        "Java, Spring Boot, MySQL",
                        savedProfile.getSkills()),

                () -> assertEquals(
                        null,
                        savedProfile.getResumeFileName()),

                () -> assertEquals(
                        null,
                        savedProfile.getResumeFilePath())
        );
    }

    @Test
    @DisplayName(
        "createProfile rejects a duplicate candidate profile"
    )
    void createProfileRejectsDuplicateProfile() {

        CandidateProfileRequest request =
                createCompleteProfileRequest();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .existsByUser_Id(CANDIDATE_ID))
                .thenReturn(true);

        DuplicateCandidateProfileException exception =
                assertThrows(
                        DuplicateCandidateProfileException.class,
                        () ->
                                candidateProfileService.createProfile(
                                        CANDIDATE_EMAIL,
                                        request));

        assertEquals(
                "Candidate profile already exists",
                exception.getMessage());

        verify(candidateProfileRepository, never())
                .save(any(CandidateProfile.class));

        verifyNoInteractions(resumeStorageService);
    }

    @Test
    @DisplayName(
        "updateProfile updates editable fields and preserves resume metadata"
    )
    void updateProfileUpdatesFieldsAndPreservesResume() {

        CandidateProfile existingProfile =
                createIncompleteProfileWithResume();

        CandidateProfileRequest request =
                createCompleteProfileRequest();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(existingProfile));

        when(candidateProfileRepository
                .save(existingProfile))
                .thenReturn(existingProfile);

        CandidateProfileResponse response =
                candidateProfileService.updateProfile(
                        CANDIDATE_EMAIL,
                        request);

        assertAll(
                () -> assertEquals(
                        "Java Developer",
                        existingProfile.getHeadline()),

                () -> assertEquals(
                        "Pune",
                        existingProfile.getCity()),

                () -> assertEquals(
                        "Java, Spring Boot, MySQL",
                        existingProfile.getSkills()),

                () -> assertEquals(
                        ORIGINAL_RESUME_NAME,
                        existingProfile.getResumeFileName()),

                () -> assertEquals(
                        OLD_STORED_RESUME_NAME,
                        existingProfile.getResumeFilePath()),

                () -> assertEquals(
                        100,
                        existingProfile.getProfileCompletion()),

                () -> assertTrue(
                        response.isResumeUploaded()),

                () -> assertEquals(
                        100,
                        response.getProfileCompletion())
        );

        verify(candidateProfileRepository)
                .save(existingProfile);

        verifyNoInteractions(resumeStorageService);
    }

    @Test
    @DisplayName(
        "uploadResume stores the new resume and removes the previous file"
    )
    void uploadResumeReplacesPreviousResumeSuccessfully() {

        CandidateProfile profile =
                createCompleteProfileWithResume();

        StoredResume storedResume =
                new StoredResume(
                        "Updated_Candidate_Resume.pdf",
                        NEW_STORED_RESUME_NAME);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(profile));

        when(resumeStorageService.store(multipartFile))
                .thenReturn(storedResume);

        when(candidateProfileRepository
                .saveAndFlush(profile))
                .thenReturn(profile);

        CandidateProfileResponse response =
                candidateProfileService.uploadResume(
                        CANDIDATE_EMAIL,
                        multipartFile);

        assertAll(
                () -> assertEquals(
                        "Updated_Candidate_Resume.pdf",
                        profile.getResumeFileName()),

                () -> assertEquals(
                        NEW_STORED_RESUME_NAME,
                        profile.getResumeFilePath()),

                () -> assertEquals(
                        100,
                        profile.getProfileCompletion()),

                () -> assertTrue(
                        response.isResumeUploaded()),

                () -> assertEquals(
                        "Updated_Candidate_Resume.pdf",
                        response.getResumeFileName()),

                () -> assertEquals(
                        100,
                        response.getProfileCompletion())
        );

        verify(resumeStorageService)
                .store(multipartFile);

        verify(candidateProfileRepository)
                .saveAndFlush(profile);

        /*
         * Unit tests invoke the service directly without an active Spring
         * transaction. The service therefore uses its direct-cleanup
         * fallback and deletes the previous file immediately.
         */
        verify(resumeStorageService)
                .deleteIfExists(
                        OLD_STORED_RESUME_NAME);

        verify(resumeStorageService, never())
                .deleteIfExists(
                        NEW_STORED_RESUME_NAME);
    }

    @Test
    @DisplayName(
        "uploadResume removes the newly stored file when database saving fails"
    )
    void uploadResumeCleansNewFileAfterDatabaseFailure() {

        CandidateProfile profile =
                createCompleteProfileWithResume();

        StoredResume storedResume =
                new StoredResume(
                        "Updated_Candidate_Resume.pdf",
                        NEW_STORED_RESUME_NAME);

        RuntimeException databaseFailure =
                new RuntimeException(
                        "Database save failed");

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(profile));

        when(resumeStorageService.store(multipartFile))
                .thenReturn(storedResume);

        when(candidateProfileRepository
                .saveAndFlush(profile))
                .thenThrow(databaseFailure);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                candidateProfileService.uploadResume(
                                        CANDIDATE_EMAIL,
                                        multipartFile));

        assertSame(
                databaseFailure,
                exception);

        verify(resumeStorageService)
                .deleteIfExists(
                        NEW_STORED_RESUME_NAME);

        verify(resumeStorageService, never())
                .deleteIfExists(
                        OLD_STORED_RESUME_NAME);
    }

    @Test
    @DisplayName(
        "downloadResume returns the candidate resume and safe metadata"
    )
    void downloadResumeReturnsStoredResumeSuccessfully() {

        CandidateProfile profile =
                createCompleteProfileWithResume();

        byte[] resumeContent = {
            1,
            2,
            3,
            4
        };

        Resource resource =
                new ByteArrayResource(resumeContent);

        StoredResumeFile storedResumeFile =
                new StoredResumeFile(
                        resource,
                        "application/pdf",
                        resumeContent.length);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(profile));

        when(resumeStorageService
                .load(OLD_STORED_RESUME_NAME))
                .thenReturn(storedResumeFile);

        ResumeDownload download =
                candidateProfileService.downloadResume(
                        CANDIDATE_EMAIL);

        assertAll(
                () -> assertSame(
                        resource,
                        download.resource()),

                () -> assertEquals(
                        ORIGINAL_RESUME_NAME,
                        download.fileName()),

                () -> assertEquals(
                        "application/pdf",
                        download.contentType()),

                () -> assertEquals(
                        resumeContent.length,
                        download.contentLength())
        );

        verify(resumeStorageService)
                .load(OLD_STORED_RESUME_NAME);
    }

    @Test
    @DisplayName(
        "downloadResume rejects a profile without uploaded resume metadata"
    )
    void downloadResumeRejectsMissingResume() {

        CandidateProfile profile =
                createIncompleteProfileWithoutResume();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(profile));

        ResumeNotFoundException exception =
                assertThrows(
                        ResumeNotFoundException.class,
                        () ->
                                candidateProfileService.downloadResume(
                                        CANDIDATE_EMAIL));

        assertEquals(
                "Resume has not been uploaded",
                exception.getMessage());

        verify(resumeStorageService, never())
                .load(any(String.class));
    }

    @Test
    @DisplayName(
        "candidate profile operations reject an inactive candidate"
    )
    void profileOperationsRejectInactiveCandidate() {

        candidate.setActive(false);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () ->
                                candidateProfileService.getProfile(
                                        CANDIDATE_EMAIL));

        assertEquals(
                "User account is inactive",
                exception.getMessage());

        verifyNoInteractions(candidateProfileRepository);
        verifyNoInteractions(resumeStorageService);
    }

    @Test
    @DisplayName(
        "candidate profile operations reject a non-candidate user"
    )
    void profileOperationsRejectNonCandidateUser() {

        candidate.setRole(Role.HR);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () ->
                                candidateProfileService.getProfile(
                                        CANDIDATE_EMAIL));

        assertEquals(
                "Only candidates can manage a candidate profile",
                exception.getMessage());

        verifyNoInteractions(candidateProfileRepository);
        verifyNoInteractions(resumeStorageService);
    }

    /**
     * Creates an active Candidate account.
     */
    private User createCandidate() {

        User user =
                new User();

        user.setId(CANDIDATE_ID);
        user.setFullName("Candidate User");
        user.setEmail(CANDIDATE_EMAIL);
        user.setPassword("encoded-password");
        user.setRole(Role.CANDIDATE);
        user.setActive(true);

        return user;
    }

    /**
     * Creates a request containing every field counted by the current
     * profile-completion calculation.
     *
     * Eighteen profile fields contribute 90 percent. The remaining
     * 10 percent comes from an uploaded resume.
     */
    private CandidateProfileRequest
            createCompleteProfileRequest() {

        CandidateProfileRequest request =
                new CandidateProfileRequest();

        request.setHeadline(
                "Java Developer");

        request.setDateOfBirth(
                LocalDate.of(1998, 5, 10));

        request.setGender(
                "Male");

        request.setAddress(
                "Kothrud");

        request.setCity(
                "Pune");

        request.setState(
                "Maharashtra");

        request.setPincode(
                "411038");

        request.setHighestQualification(
                "Bachelor of Engineering");

        request.setSpecialization(
                "Computer Engineering");

        request.setCollege(
                "Engineering College");

        request.setUniversity(
                "Pune University");

        request.setPassingYear(
                2020);

        request.setPercentageOrCgpa(
                new BigDecimal("78.50"));

        request.setExperienceYears(
                new BigDecimal("3.00"));

        request.setCurrentCompany(
                "TalentBridge Technologies");

        request.setCurrentDesignation(
                "Software Developer");

        request.setSkills(
                "Java, Spring Boot, MySQL");

        request.setLinkedinUrl(
                "https://linkedin.com/in/candidate");

        request.setGithubUrl(
                "https://github.com/candidate");

        request.setProfileSummary(
                "Backend developer with Spring Boot experience.");

        return request;
    }

    /**
     * Creates a complete profile with resume metadata.
     */
    private CandidateProfile
            createCompleteProfileWithResume() {

        CandidateProfile profile =
                candidateProfileMapper.toEntity(
                        createCompleteProfileRequest(),
                        candidate);

        profile.setProfileId(PROFILE_ID);

        profile.setResumeFileName(
                ORIGINAL_RESUME_NAME);

        profile.setResumeFilePath(
                OLD_STORED_RESUME_NAME);

        profile.setProfileCompletion(100);

        return profile;
    }

    /**
     * Creates a partially completed profile that already has a resume.
     */
    private CandidateProfile
            createIncompleteProfileWithResume() {

        CandidateProfile profile =
                new CandidateProfile();

        profile.setProfileId(PROFILE_ID);
        profile.setUser(candidate);

        profile.setHeadline(
                "Old headline");

        profile.setCity(
                "Old city");

        profile.setResumeFileName(
                ORIGINAL_RESUME_NAME);

        profile.setResumeFilePath(
                OLD_STORED_RESUME_NAME);

        profile.setProfileCompletion(20);

        return profile;
    }

    /**
     * Creates a profile without resume metadata.
     */
    private CandidateProfile
            createIncompleteProfileWithoutResume() {

        CandidateProfile profile =
                new CandidateProfile();

        profile.setProfileId(PROFILE_ID);
        profile.setUser(candidate);
        profile.setHeadline("Java Developer");
        profile.setProfileCompletion(5);

        return profile;
    }
}