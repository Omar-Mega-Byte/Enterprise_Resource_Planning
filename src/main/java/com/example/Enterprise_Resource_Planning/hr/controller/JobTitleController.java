package com.example.Enterprise_Resource_Planning.hr.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Enterprise_Resource_Planning.hr.model.JobTitle;
import com.example.Enterprise_Resource_Planning.hr.service.JobTitleService;

/**
 * REST Controller for Job Title management.
 * Handles HTTP requests for CRUD operations on job titles.
 * 
 * All exceptions are handled by the GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/job-titles")
public class JobTitleController {
    
    private final JobTitleService jobTitleService;

    /**
     * Constructor for JobTitleController.
     * 
     * @param jobTitleService the job title service
     */
    public JobTitleController(JobTitleService jobTitleService) {
        this.jobTitleService = jobTitleService;
    }

    /**
     * Retrieves all job titles.
     * 
     * @return ResponseEntity containing list of all job titles
     */
    @GetMapping
    public ResponseEntity<List<JobTitle>> getAllJobTitles() {
        List<JobTitle> jobTitles = jobTitleService.findAll();
        return ResponseEntity.ok(jobTitles);
    }

    /**
     * Retrieves a specific job title by ID.
     * 
     * @param id the job title ID
     * @return ResponseEntity containing the job title
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobTitle> getJobTitle(@PathVariable Long id) {
        JobTitle jobTitle = jobTitleService.findById(id);
        return ResponseEntity.ok(jobTitle);
    }

    /**
     * Creates a new job title.
     * 
     * @param jobTitle the job title to create
     * @return ResponseEntity containing the created job title with 201 CREATED status
     */
    @PostMapping
    public ResponseEntity<JobTitle> createJobTitle(@RequestBody JobTitle jobTitle) {
        JobTitle createdJobTitle = jobTitleService.createJobTitle(jobTitle);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobTitle);
    }

    /**
     * Updates an existing job title.
     * 
     * @param id the ID of the job title to update
     * @param jobTitle the updated job title data
     * @return ResponseEntity containing the updated job title
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobTitle> updateJobTitle(@PathVariable Long id, @RequestBody JobTitle jobTitle) {
        JobTitle updatedJobTitle = jobTitleService.updateJobTitle(id, jobTitle);
        return ResponseEntity.ok(updatedJobTitle);
    }

    /**
     * Deletes a job title by ID.
     * 
     * @param id the ID of the job title to delete
     * @return ResponseEntity with 204 NO CONTENT status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobTitle(@PathVariable Long id) {
        jobTitleService.deleteJobTitle(id);
        return ResponseEntity.noContent().build();
    }
}