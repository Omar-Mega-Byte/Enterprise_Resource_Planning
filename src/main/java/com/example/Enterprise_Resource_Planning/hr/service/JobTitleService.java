package com.example.Enterprise_Resource_Planning.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.Enterprise_Resource_Planning.hr.repository.JobTitleRepository;
import com.example.Enterprise_Resource_Planning.hr.model.JobTitle;
import com.example.Enterprise_Resource_Planning.hr.exception.JobTitleNotFoundException;
import com.example.Enterprise_Resource_Planning.hr.exception.InvalidJobTitleException;
import java.util.List;

@Service
@Transactional
public class JobTitleService {

    private final JobTitleRepository jobTitleRepository;

    public JobTitleService(JobTitleRepository jobTitleRepository) {
        this.jobTitleRepository = jobTitleRepository;
    }

    /**
     * Retrieves all job titles.
     * 
     * @return List of all job titles
     */
    @Transactional(readOnly = true)
    public List<JobTitle> findAll() {
        return jobTitleRepository.findAll();
    }

    /**
     * Finds a job title by its ID.
     * 
     * @param id the job title ID
     * @return the job title if found
     * @throws InvalidJobTitleException  if id is null
     * @throws JobTitleNotFoundException if job title is not found
     */
    @Transactional(readOnly = true)
    public JobTitle findById(Long id) {
        if (id == null) {
            throw new InvalidJobTitleException("Job title ID cannot be null");
        }
        return jobTitleRepository.findById(id)
                .orElseThrow(() -> new JobTitleNotFoundException("Job title not found with id: " + id));
    }

    /**
     * Creates a new job title.
     * 
     * @param jobTitle the job title to create
     * @return the created job title
     * @throws InvalidJobTitleException if jobTitle is null
     */
    public JobTitle createJobTitle(JobTitle jobTitle) {
        if (jobTitle == null) {
            throw new InvalidJobTitleException("Job title cannot be null");
        }
        return jobTitleRepository.save(jobTitle);
    }

    /**
     * Updates an existing job title.
     * 
     * @param id       the ID of the job title to update
     * @param jobTitle the updated job title data
     * @return the updated job title
     * @throws InvalidJobTitleException  if parameters are invalid
     * @throws JobTitleNotFoundException if job title doesn't exist
     */
    public JobTitle updateJobTitle(Long id, JobTitle jobTitle) {
        if (id == null) {
            throw new InvalidJobTitleException("Job title ID cannot be null");
        }
        if (jobTitle == null) {
            throw new InvalidJobTitleException("Job title cannot be null");
        }

        return jobTitleRepository.findById(id)
                .map(existingJobTitle -> {
                    jobTitle.setId(id);
                    return jobTitleRepository.save(jobTitle);
                })
                .orElseThrow(() -> new JobTitleNotFoundException("Job title with id " + id + " does not exist"));
    }

    /**
     * Deletes a job title by ID.
     * 
     * @param id the ID of the job title to delete
     * @throws InvalidJobTitleException  if id is null
     * @throws JobTitleNotFoundException if job title doesn't exist
     */
    public void deleteJobTitle(Long id) {
        if (id == null) {
            throw new InvalidJobTitleException("Job title ID cannot be null");
        }

        if (!jobTitleRepository.existsById(id)) {
            throw new JobTitleNotFoundException("Job title with id " + id + " does not exist");
        }

        jobTitleRepository.deleteById(id);
    }
}
