package com.compuware.ispw.restapi.action;

import java.io.PrintStream;

import com.compuware.ispw.model.rest.ReleaseInfo;
import com.compuware.ispw.restapi.IspwRequestBean;
import com.compuware.ispw.restapi.WebhookToken;

/**
 * Create release action
 * 
 * @author Sam Zhou
 *
 */
public class CreateReleaseAction extends AbstractPostAction {

	private static final String[] defaultProps = new String[] { application, stream, description,
			releaseId };

	private static final String contextPath = "/ispw/{srid}/releases";

	public CreateReleaseAction(PrintStream logger) {
		super(logger);
	}
	
	@Override
	public IspwRequestBean getIspwRequestBean(String srid, String ispwRequestBody,
			WebhookToken webhookToken) {

		ReleaseInfo releaseInfo = new ReleaseInfo();
		return super.getIspwRequestBean(srid, ispwRequestBody, contextPath, releaseInfo);
	}

}
