import React, { useState } from 'react';
import axios from 'axios';

const JobForm = ({ job = {}, onSubmit }) => {
  const [title, setTitle] = useState(job.title || '');
  const [company, setCompany] = useState(job.company || '');
  const [location, setLocation] = useState(job.location || '');
  const [jobType, setJobType] = useState(job.jobType || 'Full-Time');
  const [description, setDescription] = useState(job.description || '');

  const handleSubmit = async (event) => {
    event.preventDefault();
    const jobData = { title, company, location, jobType, description };

    try {
      if (job.id) {
        await axios.put(`/job-listings/${job.id}`, jobData); // update job listing
      } else {
        await axios.post('/job-listings', jobData); //new job listing
      }
      onSubmit(); // state update refresh the list or close the form
    } catch (error) {
      console.error('Error on job submitting form:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Title</label>
        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
      </div>
      <div>
        <label>Company</label>
        <input type="text" value={company} onChange={(e) => setCompany(e.target.value)} required />
      </div>
      <div>
        <label>Location</label>
        <input type="text" value={location} onChange={(e) => setLocation(e.target.value)} required />
      </div>
      <div>
        <label>Job Type</label>
        <select value={jobType} onChange={(e) => setJobType(e.target.value)}>
          <option value="Full-Time">Full-Time</option>
          <option value="Part-Time">Part-Time</option>
        </select>
      </div>
      <div>
        <label>Description</label>
        <textarea value={description} onChange={(e) => setDescription(e.target.value)} required />
      </div>
      <button type="submit">{job.id ? 'Update Job' : 'Create Job'}</button>
    </form>
  );
};

export default JobForm;
