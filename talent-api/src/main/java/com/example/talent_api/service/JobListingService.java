package com.example.talent_api.service;

import com.example.talent_api.domain.Applicant;
import com.example.talent_api.domain.JobListing;
import com.example.talent_api.domain.User;
import com.example.talent_api.exception.ResourceNotFoundException;
import com.example.talent_api.exception.UnauthorizedException;
import com.example.talent_api.repository.JobListingRepository;
import com.example.talent_api.repository.UserRepository;
import com.example.talent_api.service.Impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class JobListingService {

    private static final Logger logger = LoggerFactory.getLogger(JobListingService.class);


    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }

    public Optional<JobListing> getJobListingById(String id) {
        return jobListingRepository.findById(id);
    }

    public List<JobListing> getJobListingsByManager(String managerId) {
        return jobListingRepository.findByPostedBy(managerId);
    }

//    public JobListing createJobListing(JobListing jobListing) {
//        Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsername(jobListing.getPostedBy()));
//
//        if (userOpt.isPresent()) {
//            jobListing.setPostedByRole(userOpt.get().getRole());
//        } else {
//            throw new ResourceNotFoundException("User not found");
//        }
//
//        return jobListingRepository.save(jobListing);
//    }

    public JobListing createJobListing(JobListing jobListing) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsername(jobListing.getPostedBy()));

        if (userOpt.isPresent()) {
            User manager = userOpt.get();
            jobListing.setPostedByRole(manager.getRole());

            JobListing createdJobListing = jobListingRepository.save(jobListing);

            // Add the job listing ID to the manager's jobListingIds array
            if (manager.getJobListingIds() == null) {
                manager.setJobListingIds(new ArrayList<>());  // Initialize if null
            }
            manager.getJobListingIds().add(createdJobListing.getId());
            userRepository.save(manager);  // Save the updated manager document

            return createdJobListing;
        } else {
            throw new ResourceNotFoundException("Manager not found");
        }
    }



//    public void deleteJobListing(String id) {
//        jobListingRepository.deleteById(id);
//    }

    public void deleteJobListing(String id, String managerId) {
        Optional<JobListing> jobListing = jobListingRepository.findById(id);
        if (jobListing.isPresent() && jobListing.get().getPostedBy().equals(managerId)) {
            jobListingRepository.deleteById(id);
        } else {
            throw new UnauthorizedException("You do not have permission to delete this job listing.");
        }
    }

    public void softDeleteJobListing(String id, String managerId) {
        Optional<JobListing> jobListing = jobListingRepository.findById(id);
        if (jobListing.isPresent() && jobListing.get().getPostedBy().equals(managerId)) {
            JobListing listing = jobListing.get();
            listing.setDeleted(true);
            jobListingRepository.save(listing);
        } else {
            throw new UnauthorizedException("You do not have permission to delete this job listing.");
        }
    }

    public List<JobListing> getAllActiveJobListings() {
        return jobListingRepository.findAll().stream()
                .filter(jobListing -> !jobListing.isDeleted())
                .collect(Collectors.toList());
    }

    public JobListing updateJobListing(String id, JobListing jobListing) {
        if (jobListingRepository.existsById(id)) {
            jobListing.setId(id);
            return jobListingRepository.save(jobListing);
        } else {
            return null;
        }
    }

//    public JobListing addApplicantToJobListing(String jobListingId, Applicant applicant) {
//        Optional<JobListing> jobListingOpt = jobListingRepository.findById(jobListingId);
//
//        if (jobListingOpt.isPresent()) {
//            JobListing jobListing = jobListingOpt.get();
//            jobListing.getApplicants().add(applicant);
//            return jobListingRepository.save(jobListing);
//        } else {
//            throw new ResourceNotFoundException("Job Listing not found with ID: " + jobListingId);
//        }
//    }

//    public JobListing addApplicantToJobListing(String jobListingId, String userId, Applicant applicant) {
//        Optional<JobListing> jobListingOpt = jobListingRepository.findById(jobListingId);
//        Optional<User> userOpt = userRepository.findById(userId);
//
//        if (jobListingOpt.isPresent() && userOpt.isPresent()) {
//            JobListing jobListing = jobListingOpt.get();
//            User user = userOpt.get();
//
//            // Set Applicant's details using User's information
//            applicant.setId(user.getId());
//            applicant.setFirstname(user.getFirstname());
//            applicant.setLastname(user.getLastname());
//            applicant.setEmail(user.getEmail());
//            applicant.setPhone(user.getPhone());
//            applicant.setRole(user.getRole());
//
//            jobListing.getApplicants().add(applicant);
//            return jobListingRepository.save(jobListing);
//        } else {
//            throw new ResourceNotFoundException("Job Listing or User not found");
//        }
//    }

//    public JobListing addApplicantToJobListing(String jobListingId, String userId, Applicant applicant) {
//        Optional<JobListing> jobListingOpt = jobListingRepository.findById(jobListingId);
//        Optional<User> userOpt = userRepository.findById(userId);
//
//        if (jobListingOpt.isPresent() && userOpt.isPresent()) {
//            JobListing jobListing = jobListingOpt.get();
//            User user = userOpt.get();
//
//            logger.info("User found: " + user.getUsername() + " with ID: " + user.getId());
//            logger.info("Job Listing found: " + jobListing.getTitle() + " with ID: " + jobListing.getId());
//
//            // Set Applicant's details using User's information
//            applicant.setId(user.getId());
//            applicant.setFirstname(user.getFirstname());
//            applicant.setLastname(user.getLastname());
//            applicant.setEmail(user.getEmail());
//            applicant.setPhone(user.getPhone());
//            applicant.setRole(user.getRole());
//
//            jobListing.getApplicants().add(applicant);
//            return jobListingRepository.save(jobListing);
//        } else {
//            logger.warn("Job Listing or User not found. JobListing ID: " + jobListingId + ", User ID: " + userId);
//            throw new ResourceNotFoundException("Job Listing or User not found");
//        }
//    }

    public JobListing addApplicantToJobListing(String jobListingId, String userId, Applicant applicant) {
        Optional<JobListing> jobListingOpt = jobListingRepository.findById(jobListingId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (jobListingOpt.isPresent() && userOpt.isPresent()) {
            JobListing jobListing = jobListingOpt.get();
            User user = userOpt.get();

            logger.info("User found: " + user.getUsername() + " with ID: " + user.getId());
            logger.info("Job Listing found: " + jobListing.getTitle() + " with ID: " + jobListing.getId());

            // Check if the applicant has already applied for this job
            boolean alreadyApplied = jobListing.getApplicants().stream()
                    .anyMatch(existingApplicant -> existingApplicant.getId().equals(user.getId()));

            if (alreadyApplied) {
                throw new IllegalStateException("You have already applied for this job listing.");
            }

            // Set Applicant's details using User's information
            applicant.setId(user.getId());
            applicant.setFirstname(user.getFirstname());
            applicant.setLastname(user.getLastname());
            applicant.setEmail(user.getEmail());
            applicant.setPhone(user.getPhone());
            applicant.setRole(user.getRole());

            jobListing.getApplicants().add(applicant);
            return jobListingRepository.save(jobListing);
        } else {
            logger.warn("Job Listing or User not found. JobListing ID: " + jobListingId + ", User ID: " + userId);
            throw new ResourceNotFoundException("Job Listing or User not found");
        }
    }

    public JobListing updateApplicationStatus(String jobListingId, String applicantId, String status, String managerUsername) {
        Optional<JobListing> jobListingOpt = jobListingRepository.findById(jobListingId);

        if (jobListingOpt.isPresent()) {
            JobListing jobListing = jobListingOpt.get();

            // Check if the manager is authorized to change the status
            if (!jobListing.getPostedBy().equals(managerUsername)) {
                throw new UnauthorizedException("You do not have permission to change the status of this application.");
            }

            // Find the applicant within the job listing
            Optional<Applicant> applicantOpt = jobListing.getApplicants().stream()
                    .filter(applicant -> applicant.getId().equals(applicantId))
                    .findFirst();

            if (applicantOpt.isPresent()) {
                Applicant applicant = applicantOpt.get();
                applicant.setStatus(status);  // Set the status to "accept" or "reject"
                jobListingRepository.save(jobListing);
                return jobListing;
            } else {
                throw new ResourceNotFoundException("Applicant not found for the given job listing.");
            }
        } else {
            throw new ResourceNotFoundException("Job Listing not found.");
        }
    }

    public Applicant getApplicantById(String applicantId) {
        List<JobListing> jobListings = jobListingRepository.findAll();

        for (JobListing jobListing : jobListings) {
            Optional<Applicant> applicantOpt = jobListing.getApplicants().stream()
                    .filter(applicant -> applicant.getId().equals(applicantId))
                    .findFirst();

            if (applicantOpt.isPresent()) {
                return applicantOpt.get();
            }
        }

        throw new ResourceNotFoundException("Applicant not found with ID: " + applicantId);
    }




}
