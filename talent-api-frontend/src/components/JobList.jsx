import React from 'react';
import JobCard from './JobCard';

const JobList = ({ jobListings, onViewDetails, isHorizontal = false }) => {
  return (
    <div className={`job-list ${isHorizontal ? 'horizontal' : 'vertical'}`}>
      {jobListings.length > 0 ? (
        jobListings.map(job => (
          <JobCard
            key={job.id}
            job={job}
            onViewDetails={onViewDetails}
          />
        ))
      ) : (
        <p>No job listings.</p>
      )}
    </div>
  );
};

export default JobList;
