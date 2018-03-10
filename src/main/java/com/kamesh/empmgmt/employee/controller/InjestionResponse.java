/**
 * Inner class to generate employee file injest response.
 * 
 *
 */
package com.kamesh.empmgmt.employee.controller;
class InjestionResponse {
    int requested;
    int success;
    int failure;
    String message;

    public InjestionResponse(int requested) {
        super();
        this.requested = requested;
        this.success = 0;
        this.failure = 0;
        this.message = "Error, Employees not created!";
    }

    public InjestionResponse(int requested, int processed, int success, int failure) {
        super();
        this.requested = requested;
        this.success = success;
        this.failure = failure;
        this.message = new String("Total " + this.success + " out of " + this.requested + "new Employees created!");
    }

    public int getRequested() {
        return requested;
    }

    public void setRequested(int requested) {
        this.requested = requested;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}