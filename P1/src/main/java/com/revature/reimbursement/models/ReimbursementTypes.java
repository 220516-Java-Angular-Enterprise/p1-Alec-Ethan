package com.revature.reimbursement.models;

public class ReimbursementTypes {

    private String id, type;

    public ReimbursementTypes(String id, String type) {
        this.id = id;
        this.type = type;
    }

    //<editor-fold desc="Get/Set">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    //</editor-fold>

}
