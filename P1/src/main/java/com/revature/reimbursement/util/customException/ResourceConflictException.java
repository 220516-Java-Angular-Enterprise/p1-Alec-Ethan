package com.revature.reimbursement.util.customException;

public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException() {
        super();
    }

    public ResourceConflictException(String message) {
        super(message);
    }
}