package com.kamesh.empmgmt.employee.model;
/**
 * Employee status enum
 * @author KAMESHC
 *
 */
public enum EmployeeStatus {
	ACTIVE(true),
	INACTIVE(false);
	private boolean value;
	private EmployeeStatus(boolean value) {
		this.value = value;
	}
	public boolean getValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
}
