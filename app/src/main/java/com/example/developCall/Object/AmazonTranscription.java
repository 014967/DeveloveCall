package com.example.developCall.Object;


public class AmazonTranscription {


    private String jobName;
    private String accountId;
    private String status;
    private Result results;

    public Result getResults() {
        return results;
    }

    public void setResult(Result results) {
        this.results = results;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}


