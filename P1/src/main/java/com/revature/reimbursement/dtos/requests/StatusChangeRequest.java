package com.revature.reimbursement.dtos.requests;

import java.sql.Timestamp;

public class StatusChangeRequest {

    private String status;
    private String status_id;
    private String rem_id;
    private String author_id;
    private String resolver_id;
    private Timestamp resolved;

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

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getResolver_id() {
        return resolver_id;
    }

    public void setResolver_id(String resolver_id) {
        this.resolver_id = resolver_id;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }
}
