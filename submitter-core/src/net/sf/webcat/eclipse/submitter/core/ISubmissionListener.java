package net.sf.webcat.eclipse.submitter.core;

public interface ISubmissionListener
{
	void submissionStarted(SubmissionParameters params);
	
	void submissionSucceeded(SubmissionParameters params, String response);
	
	void submissionFailed(SubmissionParameters params, Throwable exception);
}
