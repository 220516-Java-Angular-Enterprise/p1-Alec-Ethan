package com.revature.reimbursement.models;

import java.sql.Blob;
import java.sql.Timestamp;

public class Reimbursements {

    private String id;
    private Double amount;
    private Timestamp submitted, resolved;
    private String description;
    private Blob receipt;
    private String payment_id, author_id, resolver_id, status_id, type_id;

    public Reimbursements(String id, Double amount, Timestamp submitted, Timestamp resolved, String description, Blob receipt, String payment_id, String author_id, String resolver_id, String status_id, String type_id) {
        this.id = id;
        this.amount = amount;
        this.submitted = submitted;
        this.resolved = resolved;
        this.description = description;
        this.receipt = receipt;
        this.payment_id = payment_id;
        this.author_id = author_id;
        this.resolver_id = resolver_id;
        this.status_id = status_id;
        this.type_id = type_id;
    }
    public Reimbursements(String id, Double amount, Timestamp submitted, String description, String author_id, String status_id, String type_id) {
        this.id = id;
        this.amount = amount;
        this.submitted = submitted;
        this.resolved = null;
        this.description = description;
        this.receipt = null;
        this.payment_id = null;
        this.author_id = author_id;
        this.resolver_id = null;
        this.status_id = status_id;
        this.type_id = type_id;
    }

    public Reimbursements(Double amount, Timestamp submitted, String description, String author_id, String status_id, String type_id) {
        this.amount = amount;
        this.submitted = submitted;
        this.resolved = null;
        this.description = description;
        this.receipt = null;
        this.payment_id = null;
        this.author_id = author_id;
        this.resolver_id = null;
        this.status_id = status_id;
        this.type_id = type_id;
    }
    public Reimbursements() {}


    //<editor-fold desc="Get/Set">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blob getReceipt() {
        return receipt;
    }

    public void setReceipt(Blob receipt) {
        this.receipt = receipt;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
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

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
    //</editor-fold>


}
