/**
 * These materials contain confidential information and trade secrets of Compuware Corporation. You shall maintain the materials
 * as confidential and shall not disclose its contents to any third party except as may be required by law or regulation. Use,
 * disclosure, or reproduction is prohibited without the prior express written permission of Compuware Corporation.
 * 
 * All Compuware products listed within the materials are trademarks of Compuware Corporation. All other company or product
 * names are trademarks of their respective owners.
 * 
 * Copyright (c) 2020 Compuware Corporation. All rights reserved.
 * (c) Copyright 2020 BMC Software, Inc.
 */
package com.compuware.ispw.restapi.action;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;

import com.compuware.ispw.model.rest.BuildResponse;
import com.compuware.ispw.restapi.Constants;
import com.compuware.ispw.restapi.IspwContextPathBean;
import com.compuware.ispw.restapi.IspwRequestBean;
import com.compuware.ispw.restapi.JsonProcessor;
import com.compuware.ispw.restapi.WebhookToken;
import com.compuware.ispw.restapi.util.RestApiUtils;
import hudson.FilePath;

/**
 * 
 */
public class BuildReleaseAction extends SetInfoPostAction implements IBuildAction
{
	private static final String[] defaultProps = new String[]{releaseId, level, runtimeConfiguration};

	private static final String contextPath = "/ispw/{srid}/releases/{releaseId}/tasks/build?level={level}&assignmentId={assignmentId}"; //$NON-NLS-1$

	public static String getDefaultProps()
	{
		return RestApiUtils.join(Constants.LINE_SEPARATOR, defaultProps, true);
	}

	public BuildReleaseAction(PrintStream logger)
	{
		super(logger);
	}

	@Override
	public IspwRequestBean getIspwRequestBean(String srid, String ispwRequestBody, WebhookToken webhookToken)
	{
		return getIspwRequestBean(srid, ispwRequestBody, webhookToken, contextPath);
	}

	@SuppressWarnings("nls")
	@Override
	public void startLog(PrintStream logger, IspwContextPathBean ispwContextPathBean, Object jsonObject)
	{
		logger.println("ISPW: Building tasks in release " + ispwContextPathBean.getReleaseId() + " at level "
				+ ispwContextPathBean.getLevel());
	}

	@SuppressWarnings("nls")
	@Override
	public Object endLog(PrintStream logger, IspwRequestBean ispwRequestBean, String responseJson)
	{
		BuildResponse buildResp = new JsonProcessor().parse(responseJson, BuildResponse.class);
		
		if (buildResp.getSetId() == null && !buildResp.getMessage().trim().isEmpty())
		{
			logger.println("ISPW: " + buildResp.getMessage());
		}
		else
		{
			logger.println("ISPW: Set " + buildResp.getSetId() + " - created to build tasks in release "
				+ ispwRequestBean.getIspwContextPathBean().getReleaseId());
		}

		return buildResp;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.ispw.restapi.action.IBuildAction#getIspwRequestBean(java.lang.String, java.lang.String, com.compuware.ispw.restapi.WebhookToken, java.io.File)
	 */
	@Override
	public IspwRequestBean getIspwRequestBean(String srid, String ispwRequestBody, WebhookToken webhookToken,
			FilePath buildParmPath) throws IOException, InterruptedException
	{
		ispwRequestBody = getRequestBody(ispwRequestBody, buildParmPath, this.getLogger());
		
		if (StringUtils.isNotBlank(ispwRequestBody))
		{
			return getIspwRequestBean(srid, ispwRequestBody, webhookToken);
		}
		else
		{
			return null;
		}
	}
}
