package com.compuware.ispw.restapi.action;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import com.compuware.ispw.restapi.IspwRequestBean;
import com.compuware.ispw.restapi.WebhookToken;

/**
 * Action to get the task information in the specified release
 * 
 * @author Sam Zhou
 *
 */
public class GetReleaseTaskInfoAction extends AbstractGetAction {

	private static final String[] defaultProps = new String[] { releaseId, taskId };
	private static final String contextPath = "/ispw/{srid}/releases/{releaseId}/tasks/{taskId}";

	public GetReleaseTaskInfoAction(PrintStream logger) {
		super(logger);
	}
	
	@Override
	public IspwRequestBean getIspwRequestBean(String srid, String ispwRequestBody,
			WebhookToken webhookToken) {

		List<String> pathTokens = Arrays.asList(defaultProps);
		return super.getIspwRequestBean(srid, ispwRequestBody, contextPath, pathTokens);
	}

}
