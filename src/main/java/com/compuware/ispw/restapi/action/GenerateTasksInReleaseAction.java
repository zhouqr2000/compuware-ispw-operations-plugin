package com.compuware.ispw.restapi.action;

import java.io.PrintStream;
import com.compuware.ispw.model.rest.TaskResponse;
import com.compuware.ispw.restapi.Constants;
import com.compuware.ispw.restapi.IspwContextPathBean;
import com.compuware.ispw.restapi.IspwRequestBean;
import com.compuware.ispw.restapi.JsonProcessor;
import com.compuware.ispw.restapi.WebhookToken;
import com.compuware.ispw.restapi.util.RestApiUtils;

/**
 * Action to generate tasks in the specified release
 * 
 * @author Sam Zhou
 *
 */
public class GenerateTasksInReleaseAction extends SetInfoPostAction {

	private static final String[] defaultProps =
			new String[] { releaseId, level, runtimeConfiguration };

	private static final String contextPath =
			"/ispw/{srid}/releases/{releaseId}/tasks/generate?level={level}&mname={mname}&mtype={mtype}";

	public static String getDefaultProps() {
		return RestApiUtils.join(Constants.LINE_SEPARATOR, defaultProps, true);
	}

	public GenerateTasksInReleaseAction(PrintStream logger) {
		super(logger);
	}
	
	@Override
	public IspwRequestBean getIspwRequestBean(String srid, String ispwRequestBody,
			WebhookToken webhookToken) {
		return getIspwRequestBean(srid, ispwRequestBody, webhookToken, contextPath);
	}

	@Override
	public void startLog(PrintStream logger, IspwContextPathBean ispwContextPathBean, Object jsonObject)
	{
		logger.println("ISPW: Generating tasks in Release " + ispwContextPathBean.getReleaseId()
		+ " at level " + ispwContextPathBean.getLevel());
	}

	@Override
	public Object endLog(PrintStream logger, IspwRequestBean ispwRequestBean, String responseJson)
	{
		TaskResponse taskResp = new JsonProcessor().parse(responseJson, TaskResponse.class);
		logger.println("ISPW: Set "+taskResp.getSetId()+" - created to generate Release "+ispwRequestBean.getIspwContextPathBean().getReleaseId());
		
		return taskResp;
	}
}
