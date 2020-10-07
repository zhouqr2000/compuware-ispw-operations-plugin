package com.compuware.ispw.restapi.action;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import com.compuware.ispw.model.rest.TaskInfo;
import com.compuware.ispw.model.rest.TaskListResponse;
import com.compuware.ispw.restapi.IspwContextPathBean;
import com.compuware.ispw.restapi.IspwRequestBean;
import com.compuware.ispw.restapi.JsonProcessor;
import com.compuware.ispw.restapi.WebhookToken;
import com.compuware.ispw.restapi.util.RestApiUtils;

/**
 * Action to list all tasks in the specified assignment
 * 
 * @author Sam Zhou
 *
 */
public class GetAssignmentTaskListAction extends AbstractGetAction {

	private static final String[] defaultProps = new String[] { assignmentId, level };
	private static final String contextPath =
			"/ispw/{srid}/assignments/{assignmentId}/tasks?level={level}";

	public GetAssignmentTaskListAction(PrintStream logger) {
		super(logger);
	}
	
	@Override
	public IspwRequestBean getIspwRequestBean(String srid, String ispwRequestBody,
			WebhookToken webhookToken) {
		List<String> pathTokens = Arrays.asList(defaultProps);
		return super.getIspwRequestBean(srid, ispwRequestBody, contextPath, pathTokens);
	}

	@Override
	public void startLog(PrintStream logger, IspwContextPathBean ispwContextPathBean, Object jsonObject)
	{
		logger.println("Listing tasks in Assignment " + ispwContextPathBean.getAssignmentId());
	}

	@Override
	public Object endLog(PrintStream logger, IspwRequestBean ispwRequestBean, String responseJson)
	{
		String fixedResponseJson = RestApiUtils.fixCesTaskListResponseJson(responseJson);
		TaskListResponse listResponse = new JsonProcessor().parse(fixedResponseJson, TaskListResponse.class);
		
		logger.println("TaskId, Module, Type, UserId, Version, Status, Application/Stream/Level, Release");
		for(TaskInfo taskInfo: listResponse.getTasks()) {
			logger.println(" " + taskInfo.getTaskId() + ", " + taskInfo.getModuleName() + ", "
					+ taskInfo.getModuleType() + ", " + taskInfo.getUserId() + ", "
					+ taskInfo.getVersion() + ", " + taskInfo.getStatus() + ", "
					+ taskInfo.getApplication() + "/" + taskInfo.getStream() + "/"
					+ taskInfo.getLevel() + ", " + taskInfo.getRelease());
		}
		
		return listResponse;
	}

}
