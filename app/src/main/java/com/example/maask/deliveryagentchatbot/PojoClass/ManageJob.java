package com.example.maask.deliveryagentchatbot.PojoClass;

import java.io.Serializable;

public class ManageJob implements Serializable {

    public String jobId;
    public String Status;

    public ManageJob() {}

    public ManageJob(String jobId, String status) {
        this.jobId = jobId;
        Status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
