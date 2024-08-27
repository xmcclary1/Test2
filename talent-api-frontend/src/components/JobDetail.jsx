import React, { useState } from 'react';
import ApplicantList from './ApplicantList';
import axios from 'axios';

const JobDetail = ({ job, onEditComplete, onDeleteComplete, role }) => {
  const { title, company, location, jobType, postedDate, description, applicants } = job;
  const [showApplicants, setShowApplicants] = useState(false);

  const handleToggleApplicants = () => {
    setShowApplicants(!showApplicants);
  };

  const handleEdit = async (updatedJob) => {
    try {
      await axios.put(`/job-listings/${updatedJob.id}`, updatedJob);
      onEditComplete(updatedJob); // used to home to update state
    } catch (error) {
      console.error("Error updating job:", error);
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this job listing?')) {
      try {
        const managerId = 'your-manager-id'; //legacy admin verification, ask if they want to keep
        await axios.delete(`/job-listings/${job.id}`, { params: { managerId } });
        onDeleteComplete(job.id); // update state to homepage
      } catch (error) {
        console.error('Error deleting job:', error);
      }
    }
  };

  return (
    <div className="job-detail-modal">
      <h2>{title}</h2>
      <p><strong>Company:</strong> {company}</p> 
      <p><strong>Location:</strong> {location}</p>
      <p><strong>Type:</strong> {jobType}</p>
      <p><strong>Posted:</strong> {new Date(postedDate).toLocaleDateString()}</p>
      <p><strong>Description:</strong> {description}</p>

      {role === 'manager' && (
        <div className="job-detail-actions">
          <button onClick={() => handleEdit(job)}>Edit Job</button>
          <button onClick={handleDelete}>Delete Job</button>
          
          {/* show applicants in separate div like subtasks */}
          <div className="applicant-section">
            <button onClick={handleToggleApplicants}>
              {showApplicants ? 'Hide Applicants' : 'Show Applicants'}
            </button>
            {showApplicants && (
              <div className="applicant-lists">
                <ApplicantList title="Pending Applicants" applicants={applicants} status="pending" />
                <ApplicantList title="Approved Applicants" applicants={applicants} status="approved" />
              </div>
            )}
          </div>
        </div>
      )}

      {role === 'candidate' && (
        <div className="job-detail-actions">
          <button onClick={() => handleApply(job)}>Apply for Job</button>
        </div>
      )}
    </div>
  );
};

export default JobDetail;
