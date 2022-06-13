package com.revature.reimbursement.dtos.requests;

public class StatusChangeRequest {

    private String status;
    private String rem_id;

    public StatusChangeRequest() { }

    public StatusChangeRequest(String status, String rem_id) {
        this.status = status;
        this.rem_id = rem_id;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRem_id() {
        return rem_id;
    }
    public void setRem_id(String rem_id) {
        this.rem_id = rem_id;
    }

}
