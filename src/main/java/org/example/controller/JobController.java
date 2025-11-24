package org.example.controller;

import org.example.entity.JobModel;
import org.example.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.repository.JobRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
@CrossOrigin("*")
public class JobController {

    @Autowired
    private  JobRepository jobRepository;
    @PostMapping("/admin/create")
    public JobModel createJob(@RequestBody JobModel jobModel){
        jobModel.setPostedDate(LocalDate.now().toString());
        jobModel.setLastDate(LocalDate.now().toString());
        jobModel.setJobPostUrl(jobModel.getJobPostUrl());
        return jobRepository.save(jobModel);
    }

    @GetMapping
    public List<JobModel> getAllJobs(){
        return jobRepository.findAll();
    }

    @GetMapping("/{id}")
    public JobModel getJobById(@PathVariable String id){
        return jobRepository.findById(id).get();
    }

    @DeleteMapping("/admin/delete/{id}")
    public String deleteJob(@PathVariable String id) {
        if (!jobRepository.existsById(id)) {
            return "Job Not Found!";
        }
        jobRepository.deleteById(id);
        return "Job Deleted Successfully!";
    }

    @PutMapping("/{id}/apply")
    public ResponseEntity<?> incrementApplyClicks(@PathVariable String id) {
        Optional<JobModel> jobOptional = jobRepository.findById(id);

        if (jobOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }

        JobModel job = jobOptional.get();  // ✅ Correct model

        job.setApplyClicks(job.getApplyClicks() + 1); // increment click
        jobRepository.save(job);

        return ResponseEntity.ok("Click counted successfully");
    }

}
