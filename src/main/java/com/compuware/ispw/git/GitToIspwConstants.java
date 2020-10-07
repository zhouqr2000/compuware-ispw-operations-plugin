package com.compuware.ispw.git;

public class GitToIspwConstants
{
	public static final String VAR_REF_ID = "refId"; //$NON-NLS-1$
	public static final String VAR_REF = "ref"; //$NON-NLS-1$
	public static final String VAR_TO_HASH = "toHash"; //$NON-NLS-1$
	public static final String VAR_FROM_HASH = "fromHash"; //$NON-NLS-1$
	public static final String BUILD_PARMS_FILE_NAME = "automaticBuildParams.txt"; //$NON-NLS-1$
	public static final String CONTAINER_PREF_PER_COMMIT = "per-commit"; //$NON-NLS-1$
	public static final String CONTAINER_PREF_PER_BRANCH = "per-branch"; //$NON-NLS-1$
	public static final String CONTAINER_PREF_CUSTOM = "custom"; //$NON-NLS-1$

	public static final String SCM_DOWNLOADER_CLI_BAT = "IspwCLI.bat"; //$NON-NLS-1$
	public static final String SCM_DOWNLOADER_CLI_SH = "IspwCLI.sh"; //$NON-NLS-1$

	public static final String ISPW_OPERATION_PARAM = "-operation"; //$NON-NLS-1$

	public static final String ISPW_SERVER_CONFIG_PARAM = "-ispwServerConfig"; //$NON-NLS-1$
	public static final String ISPW_SERVER_STREAM_PARAM = "-ispwServerStream"; //$NON-NLS-1$
	public static final String ISPW_SERVER_APP_PARAM = "-ispwServerApp"; //$NON-NLS-1$
	public static final String ISPW_SERVER_CHECKOUT_LEV_PARAM = "-ispwCheckoutLevel"; //$NON-NLS-1$
	
	public static final String ISPW_CONFIG_PATH = "-ispwConfigPath"; //$NON-NLS-1$

	public static final String GIT_REPO_URL_PARAM = "-gitRepoUrl"; //$NON-NLS-1$
	public static final String GIT_USERID_PARAM = "-gitUsername"; //$NON-NLS-1$
	public static final String GIT_PW_PARAM = "-gitPassword"; //$NON-NLS-1$
	public static final String GIT_REF_PARAM = "-gitBranch"; //$NON-NLS-1$
	public static final String GIT_HASH_PARAM = "-gitCommit"; //$NON-NLS-1$
	public static final String GIT_LOCAL_PATH_ARG_PARAM = "-gitLocalPath";  //$NON-NLS-1$
	public static final String GIT_FROM_HASH_PARAM = "-gitFromHash"; //$NON-NLS-1$
	public static final String GIT_HASH_PARAM_FILE = "-gitCommitFile"; //$NON-NLS-1$

	public static final String CONTAINER_CREATION_PREF_ARG_PARAM = "-ispwContainerCreation"; //$NON-NLS-1$
	public static final String CONTAINER_DESCRIPTION_ARG_PARAM = "-ispwContainerDescription"; //$NON-NLS-1$
	
	public static final String VAR_FROM_HASH_TYPE_CHANGESET = "-1";
	
	public static final String BRANCH_MAPPING_DEFAULT = "#The following comments show how to use the 'Branch Mapping' field.\n"
			+ "#Click on the help button to the right of the screen for more details on how to populate this field\n"
			+ "#\n" + "#**/dev1 => DEV1, per-commit\n" + "#**/dev2 => DEV2, per-branch\n"
			+ "#**/dev3 => DEV3, custom, a description\n";
}
