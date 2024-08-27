
package com.example.talent_api.domain;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_applications")
public class JobApplication {




private String applicantName;
private String applicantEmail;
private String resume;  
private LocalDateTime applicationDate;
private String status;
private String applicantId;




public JobApplication() {}
public JobApplication(String applicantName, String applicantEmail, String resume, LocalDateTime applicationDate, String status) {
    this.applicantName = applicantName;
    this.applicantEmail = applicantEmail;
    this.resume = resume;
    this.applicationDate = applicationDate;
    this.status = status;
  

}


public String getApplicantName() {
    return applicantName;
}
public void setApplicantName(String applicantName) {
    this.applicantName = applicantName;
}

public String getApplicantEmail() {
    return applicantEmail;
}
public void setApplicantEmail(String applicantEmail) {
    this.applicantEmail = applicantEmail;
}

public String getResume() {
    return resume;
}
public void setResume(String resume) {
    this.resume = resume;
}


public LocalDateTime getApplicationDate() {
return applicationDate;
}

public void setApplicationDate(LocalDateTime applicationDate) {
    this.applicationDate = applicationDate;
}

public String getStatus() {
    return status;
}
public void setStatus(String status) {
    this.status = status;
}

public String getApplicantId() {
    return applicantId;
}

public void setApplicantId(User user) {
    this.applicantId = user.getId();
}





}