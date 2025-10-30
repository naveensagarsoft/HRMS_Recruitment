package com.bob.jobportal.util;

public class AppConstants {
    public static final String APPROVAL_STATUS_WORKFLOW = "Workflow";


    public static final String UNDERSCORE = "_";

    public static final String API_APPROVAL_APPROVED = "approved";
    public static final String API_APPROVAL_REJECTED = "rejected";


    public static final String REQ_APPROVAL_PENDING = "pending_approval";
    public static final String REQ_APPROVAL_PUBLISHED = "Published";
    public static final String REQ_APPROVAL_APPROVED = "Approved";
    public static final String REQ_APPROVAL_REJECTED = "Rejected";
    public static final String REQ_PENDING_APPROVAL_L1 = "Pending L1 Approval";
    public static final String REQ_PENDING_APPROVAL_L2 = "Pending L2 Approval";
    public static final String REQ_PENDING = "Pending for Approval";

    //User Roles
    public static final String L1 = "L1";
    public static final String L2 = "L2";

    public static final String JOB_REQUISITIONS = "job_requisitions";


    public static final String WORKFLOW_STATUS_PENDING = "Pending";
    public static final String WORKFLOW_STATUS_COMPLETED = "Completed";
    public static final String WORKFLOW_ACTION_PENDING = "Pending";
    public static final String WORKFLOW_ACTION_APPROVED = "Approved";
    public static final String WORKFLOW_ACTION_REJECTED = "Rejected";

    public static final String WORKFLOW_COMMENTS_PENDING = "Pending for approval";

    public static final String STATUS_FIELD = "Status";
    public static final String CHANGE_TYPE_UPDATE = "UPDATE";
    public static final String REQ_APPROVAL_New = "New";


    public static final String JOB_POSITION_STATUS_ACTIVE = "Active";
    public static final String JOB_POSITION_STATUS_INACTIVE = "Inactive";

    public static final String CANDIDATE_APPLICATION_STATUS_Offered = "Offered";

    public static final String USER_ADMIN ="Admin";

    public static final String RESUME_FILE_CONTENT_LABEL = "Resume_File_Content: \n\n";

    public static final String AI_PROMPT_PARSE_RESUME = "Convert the above Resume_File_Content which was extracted using PDFTextStripper" +
            " into the JSON_SCHEMA which is provided below and follow the PARSE_RULES provided below very strictly";

    public static final String JSON_SCHEMA_LABEL = "\nJSON_SCHEMA: \n\n";

    public static final java.util.List<String> AI_PROMPT_PARSE_RESUME_RULES = java.util.Arrays.asList(
            "PARSE_RULES: \n\n",
            "- Use the JSON_SCHEMA for parsing the Resume_File_Content\n",
            "- Strictly follow the JSON_SCHEMA regex patterns for each all fields. If it is not matching or not found, skip creating the JSON key itself",
            "- Do not even add a single letter to the converted JSON on your own. Only use the Resume_File_Content as source",
            "- Once a set of text from Resume_File_Content is used in a JSON field, do not re use the same set of text as source for any other JSON field.\n",
            "- StartDate and EndDate must only be in 'DD-MM-YYYY' format. If day is unavailable, use 1 as day. if month is unavailable use January as month. \n",
            "- dateOfBirth must only be in 'DD-MM-YYYY' format. \n",
            "- After extracting mobile number, convert it to international standard and store it in mobile_intl_std json key",
            "- Return ONLY valid JSON (no markdown/code fences) \n"
    );



}
