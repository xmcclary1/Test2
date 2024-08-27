import React from 'react';

const ApplicantItem = ({ applicant, onApprove, onReject, handleStatusChange }) => {
  const { applicantName, resumeUrl, status } = applicant;

  const handleDownloadResume = () => {
    //code to download resume idk
  };

  const confirmApprove = () => {
    if (window.confirm(`Are you sure you want to approve of  ${applicantName}?`)) {
      onApprove(applicant.id);
      handleStatusChange(applicant);
    }
  };

  const confirmReject = () => {
    if (window.confirm(`Are you sure you want to reject ${applicantName}?`)) {
      onReject(applicant.id);
      handleStatusChange(applicant);
    }
  };

  return (
    <div className="applicant-item">
      <p>{applicantName}</p>
      <p>Status: {status}</p>
      <button onClick={handleDownloadResume}>Download Resume</button>

      <div className="applicant-actions">
        {status === 'pending' && (
          <>
            <button onClick={confirmApprove}>Approve</button>
            <button onClick={confirmReject}>Reject</button>
          </>
        )}
      </div>
    </div>
  );
};

export default ApplicantItem;
