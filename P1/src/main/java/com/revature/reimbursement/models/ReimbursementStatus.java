package com.revature.reimbursement.models;

public class ReimbursementStatus {

    private String id, status;

    public ReimbursementStatus(String id, String status) {
        this.id = id;
        this.status = status;
    }

    //<editor-fold desc="Get/Set">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    //</editor-fold>


}
