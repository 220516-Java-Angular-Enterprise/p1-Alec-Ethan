package com.revature.reimbursement.util.customException;

public class InvalidSQLException extends RuntimeException{
    public InvalidSQLException(String message) { super(message); }
}