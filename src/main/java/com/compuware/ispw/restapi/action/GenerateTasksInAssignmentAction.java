package com.compuware.ispw.restapi.action;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.compuware.ces.communications.service.data.EventCallback;
import com.compuware.ces.model.BasicAuthentication;
import com.compuware.ces.model.HttpHeader;
import com.compuware.ispw.model.rest.SetInfo;
import com.compuware.ispw.restapi.Constants;
import com.compuware.ispw.restapi.IspwRequestBean;
import com.compuware.ispw.restapi.JsonGenerator;
import com.compuware.ispw.restapi.WebhookToken;
import com.compuware.ispw.restapi.util.RestApiUtils;

public class GenerateTasksInAssignmentAction implements IAction {

	public static String runtimeConfiguration = "runtimeConfiguration";
	public static String autoDeploy = "autoDeploy";
	public static String httpHeaders = "httpHeaders";
	public static String credentials = "credentials";
	public static String eventsName = "events.name";
	public static String eventsUrl = "events.url";
	public static String eventsMethod = "events.method";
	public static String eventsBody = "events.body";
	public static String eventsHttpHeaders = "events.httpHeaders";
	public static String eventsCredentials = "events.credentials";

	public static String[] defaultProps = new String[] { assignmentId, level, runtimeConfiguration,
			eventsName, eventsUrl, eventsHttpHeaders, eventsCredentials };

	public static String contextPath =
			"/ispw/{srid}/assignments/{assignmentId}/tasks/generate?level={level}";

	public static String getDefaultProps() {
		return RestApiUtils.join(Constants.LINE_SEPARATOR, defaultProps, true);
	}

	@Override
	public IspwRequestBean getIspwRequestBean(String srid, String ispwRequestBody,
			WebhookToken webhookToken) {
		IspwRequestBean bean = new IspwRequestBean();

		String path = contextPath.replace("{srid}", srid);
		SetInfo setInfo = new SetInfo();

		boolean hasEvent = false;
		ArrayList<EventCallback> events = new ArrayList<EventCallback>();
		EventCallback event = new EventCallback();
		events.add(event);

		String[] lines = ispwRequestBody.split("\n");
		for (String line : lines) {
			line = StringUtils.trimToEmpty(line);
			int indexOfEqualSign = line.indexOf("=");
			if (indexOfEqualSign != -1) {
				String name = StringUtils.trimToEmpty(line.substring(0, indexOfEqualSign));
				String value =
						StringUtils
								.trimToEmpty(line.substring(indexOfEqualSign + 1, line.length()));

				if (StringUtils.isNotBlank(value)) {

					if (name.equals(assignmentId)) {
						path = path.replace("{assignmentId}", value);
					} else if (name.equals(level)) {
						path = path.replace("{level}", value);
					} else if (name.equals(runtimeConfiguration)) {
						setInfo.setRuntimeConfig(value);
					} else if (name.equals(autoDeploy)) {
						setInfo.setAutoDeploy(autoDeploy);
					} else if (name.equals(httpHeaders)) {
						ArrayList<HttpHeader> httpHeaders = RestApiUtils.toHttpHeaders(value);
						if (!httpHeaders.isEmpty()) {
							setInfo.setHttpHeaders(httpHeaders);
						}
					} else if (name.equals(credentials)) {
						BasicAuthentication auth = RestApiUtils.toBasicAuthentication(value);
						if (auth != null) {
							setInfo.setCredentials(auth);
						}
					} else if (name.equals(eventsName)) {
						hasEvent = true;
						event.setName(value);
					} else if (name.equals(eventsMethod)) {
						hasEvent = true;
						event.setMethod(value);
					} else if (name.equals(eventsBody)) {
						hasEvent = true;
						event.setBody(value);
					} else if (name.equals(eventsHttpHeaders)) {
						ArrayList<HttpHeader> httpHeaders = RestApiUtils.toHttpHeaders(value);
						if (!httpHeaders.isEmpty()) {
							hasEvent = true;
							event.setHttpHeaders(httpHeaders);
						}
					} else if (name.equals(eventsCredentials)) {
						BasicAuthentication auth = RestApiUtils.toBasicAuthentication(value);
						if (auth != null) {
							hasEvent = true;
							event.setCredentials(auth);
						}
					}

				}
			}
		}

		if (webhookToken != null && hasEvent) {
			event.setUrl(webhookToken.getURL());
			setInfo.setEventCallbacks(events);
		}

		bean.setContextPath(path);

		JsonGenerator jsonGenerator = new JsonGenerator();
		String jsonRequest = jsonGenerator.generate(setInfo);
		bean.setJsonRequest(jsonRequest);
		return bean;
	}
}