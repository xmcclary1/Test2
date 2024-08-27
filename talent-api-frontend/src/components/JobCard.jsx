import React from 'react';

const JobCard = ({ job, onViewDetails, onEdit, onDelete, onApply, role }) => {
  const { title, company, location, jobType, postedDate } = job;

  const handleCardClick = () => {
    onViewDetails(job, onEdit, onDelete, onApply);//props for crud operations only managers can see
  };

  return (
    <div className="job-card" onClick={handleCardClick}>
      <h3>{title}</h3>
      <p><strong>Company:</strong> {company}</p> {/*have to add actual css rather than just styling from old code */}
      <p><strong>Location:</strong> {location}</p>
      <p><strong>Type:</strong> {jobType}</p>
      <p><strong>Posted:</strong> {new Date(postedDate).toLocaleDateString()}</p>
    </div>
  );
};

export default JobCard;
