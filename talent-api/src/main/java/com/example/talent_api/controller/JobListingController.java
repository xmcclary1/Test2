package com.example.talent_api.controller;

import com.example.talent_api.domain.Applicant;
import com.example.talent_api.domain.JobListing;
import com.example.talent_api.exception.ResourceNotFoundException;
import com.example.talent_api.exception.UnauthorizedException;
import com.example.talent_api.service.JobListingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/job-listings")
public class JobListingController {

    @Autowired
    private JobListingService jobListingService;

    private static final Logger logger = LoggerFactory.getLogger(JobListingController.class);

    //@PreAuthorize("hasRole('candidate') or hasRole('manager')")
    @GetMapping
    public List<JobListing> getAllJobListings() {
        return jobListingService.getAllJobListings();
    }

    //    @PreAuthorize("hasRole('candidate') or hasRole('manager')")
    @GetMapping("/{id}")
    public ResponseEntity<JobListing> getJobListingById(@PathVariable String id) {
        Optional<JobListing> jobListing = jobListingService.getJobListingById(id);
        return jobListing.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createJobListing(@RequestBody JobListing jobListing) {
        // Retrieve the manager's username from the security context (authenticated user)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is null or if the user is not authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to create a job listing.");
        }


        // Get the username of the authenticated manager
        String managerUsername = authentication.getName();  // This will give you the username of the authenticated manager

        // Set the postedBy field to the manager's username
        jobListing.setPostedBy(managerUsername);

        // Create the job listing
        JobListing createdJobListing = jobListingService.createJobListing(jobListing);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobListing);
    }

//    public ResponseEntity<JobListing> createJobListing(@RequestBody JobListing jobListing) {
//        // Retrieve the manager's username from the security context (authenticated user)
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String managerUsername = authentication.getName();  // This will give you the username of the authenticated manager
//
//        // Set the postedBy field to the manager's username
//        jobListing.setPostedBy(managerUsername);
//
//        // Create the job listing
//        JobListing createdJobListing = jobListingService.createJobListing(jobListing);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobListing);
//    }

    //    @PreAuthorize("hasRole('manager')")
    @PutMapping("/{id}")
    public ResponseEntity<JobListing> updateJobListing(@PathVariable String id, @RequestBody JobListing jobListing) {
        JobListing updatedJobListing = jobListingService.updateJobListing(id, jobListing);
        return updatedJobListing != null ? ResponseEntity.ok(updatedJobListing) : ResponseEntity.notFound().build();
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJobListing(@PathVariable String id) {
//        jobListingService.deleteJobListing(id);
//        return ResponseEntity.noContent().build();
//    }

    //    @PreAuthorize("hasRole('manager')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobListing(@PathVariable String id, @RequestParam String managerId) {
        Optional<JobListing> jobListing = jobListingService.getJobListingById(id);

        if (jobListing.isEmpty()) {
            // Job listing not found or already deleted
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job listing not found or already deleted.");
        }

        if (!jobListing.get().getPostedBy().equals(managerId)) {
            // Unauthorized attempt to delete the job listing
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this job listing.");
        }

        jobListingService.deleteJobListing(id, managerId);
        return ResponseEntity.ok("Job listing successfully deleted.");
    }

    //    @PreAuthorize("hasRole('candidate') or hasRole('manager')")
    @GetMapping("/active")
    public List<JobListing> getAllActiveJobListings() {
        return jobListingService.getAllActiveJobListings();
    }

//    @PostMapping("/{id}/apply")
//    public ResponseEntity<String> applyForJob(@PathVariable String id, @RequestParam String userId, @RequestBody Applicant applicant) {
//        JobListing updatedJobListing = jobListingService.addApplicantToJobListing(id, userId, applicant);
//        return ResponseEntity.ok("Application successful: You have applied for the job listing titled '" + updatedJobListing.getTitle() + "'.");
//    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<?> applyForJob(@PathVariable String id, @RequestParam String userId, @RequestBody Applicant applicant) {
        try {
            JobListing updatedJobListing = jobListingService.addApplicantToJobListing(id, userId, applicant);

            // Prepare the response map
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Application successful: You have applied for the job listing titled '" + updatedJobListing.getTitle() + "'.");
            response.put("jobListing", updatedJobListing);

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{jobListingId}/applications/{applicantId}/status")
    public ResponseEntity<?> changeApplicationStatus(
            @PathVariable String jobListingId,
            @PathVariable String applicantId,
            @RequestBody Map<String, String> requestBody) {

        // Retrieve the status from the request body
        String status = requestBody.get("status");

        // Log the received parameters
        logger.info("Received status parameter: " + status);

        // Retrieve the manager's username from the security context (authenticated user)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String managerUsername = authentication.getName();

        // Log the username
        logger.info("Authenticated manager: " + managerUsername);

        // Ensure the status is either "accept" or "reject"
        if (!status.equalsIgnoreCase("accept") && !status.equalsIgnoreCase("reject")) {
            return ResponseEntity.badRequest().body("Invalid status. Only 'accept' or 'reject' are allowed.");
        }

        try {
            // Update the application status
            JobListing updatedJobListing = jobListingService.updateApplicationStatus(jobListingId, applicantId, status, managerUsername);
            return ResponseEntity.ok(updatedJobListing);

        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/applications/{applicantId}")
    public ResponseEntity<?> getApplicantById(@PathVariable String applicantId) {
        try {
            Applicant applicant = jobListingService.getApplicantById(applicantId);
            return ResponseEntity.ok(applicant);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}