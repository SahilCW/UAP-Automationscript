package com.capitaworld.proxy;

public class MethodDetails {

	
	private String testCaseID;
	private String auditTrail;
	
	
	public MethodDetails(String testCaseID, String auditTrail) {
		this.testCaseID = testCaseID;
		this.auditTrail = auditTrail;

	}


	public String getTestCaseID() {
		return testCaseID;
	}


	public void setTestCaseID(String testCaseID) {
		this.testCaseID = testCaseID;
	}


	public String getAuditTrail() {
		return auditTrail;
	}


	public void setAuditTrail(String auditTrail) {
		this.auditTrail = auditTrail;
	}

	
}
