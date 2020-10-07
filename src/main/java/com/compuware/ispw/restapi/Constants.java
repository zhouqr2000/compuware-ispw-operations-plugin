package com.compuware.ispw.restapi;

/**
 * Constants
 * 
 * @author Sam Zhou
 *
 */
public class Constants {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static final String ISPW_DEBUG_MODE = "ispwDebugMode";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String Assignment = "Assignment";
	public static final String Release = "Release";
	
	public static final String Action = "Action";
	
	public static final String SET_STATE_DISPATCHED = "Dispatched";
	public static final String SET_STATE_EXECUTING = "Executing";
	public static final String SET_STATE_COMPLETE = "Complete";
	public static final String SET_STATE_CLOSED = "Closed";
	public static final String SET_STATE_FAILED = "Failed";
	public static final String SET_STATE_HELD = "Held";
	public static final String SET_STATE_RELEASED = "Released";
	public static final String SET_STATE_TERMINATED = "Terminated";
	public static final String SET_STATE_WAITING_APPROVAL = "Waiting-Approval";
	public static final String SET_STATE_WAITING_LOCK = "Waiting-Lock";

	public static final int POLLING_COUNT = 120;
	public static final int POLLING_INTERVAL = 2000;

	public static final String ENV_VAR_WORKSPACE = "WORKSPACE";
	public static final String ENV_VAR_MASTER = "master";
	public static final String ENV_VAR_NODENAME = "NODE_NAME";
	
	public static final String TTT_CHANGESET = "changedPrograms.json";
	
	public static final String REQUEST_BODY_RTCONFIG = "runtimeConfiguration";
	
	public static final String UTF_8 = "UTF-8";
}
