package com.example.maask.deliveryagentchatbot.HelperClass;

import java.io.Serializable;

public class ManageJob implements Serializable {

    public String jobId;
    public int Status;

    public ManageJob() {}

    public ManageJob(String jobId, int status) {
        this.jobId = jobId;
        Status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
