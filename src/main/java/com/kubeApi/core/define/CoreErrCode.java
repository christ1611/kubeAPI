package com.kubeApi.core.define;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CoreErrCode {

    /* Core System Error Area 00001 ~ 02000  */
    UNKNOWN_SYSTEM_ERROR                         (0001, "unknown system error"),
    UNKNOWN_DBMS_ERROR                           (0002, "unknown dbms error"),

    DB_SELECT_ERROR                              (0101, "db select error"),
    DB_INSERT_ERROR                              (0102, "db insert error"),
    DB_UPDATE_ERROR                              (0103, "db update error"),
    DB_DELETE_ERROR                              (0104, "db delete error"),

    IMG_NOT_FOUND                                (1101, "Image Not Found"),
    CON_CREATE_FAIL                              (1102, "Cannot Created Container"),
    CON_START_FAIL                               (1103, "Cannot Start Container"),

    REGISTERED_VERSION                          (2101, "Image with the same version has been registered"),
    LATEST_REPO_NOT_FOUND                       (2102, "Latest Image is not found in the repository"),

    MANDATORY_BATCH_NAME                        (3101, "Batch Name is Mandatory"),
    MANDATORY_BATCH_CATEGORY                    (3102, "Batch Category is Mandatory"),
    MANDATORY_BATCH_TYPE                        (3103, "Batch Type is Mandatory"),
    MANDATORY_APP_DV_CD                         (3104, "Application Division Code is Mandatory"),
    MANDATORY_PARAMETER_NAME                    (3105, "Parameter Name is Mandatory"),
    MANDATORY_DEFAULT_VERSION                   (3106, "Default Version is Mandatory"),
    MANDATORY_USER_ID                           (3107, "User Id is Mandatory"),

    INVALID_BATCH_CATEGORY                      (3110, "Batch Category is Invalid"),
    INVALID_BATCH_TYPE                          (3111, "Batch Type is Invalid"),
    INVALID_APP_DV_CD                           (3112, "Application Division Code is Invalid"),

    DUPLICATE_BATCH                             (3120, "Batch Name is duplicate"),
    ALREADY_EXIST_APP_VER                       (3121, "Already Exist Application Version"),
    NOT_EXIST_IMAGE                             (3122, "Not Exist Harbor Image"),
    NOT_EXIST_TOKEN                             (3123, "Not Exist Token Inforamtion"),
    NOT_EXIST_APP_INFO                          (3124, "Not Exist Application Information"),
    NOT_EXIST_BATCH_SERVER                      (3125, "Batch Server Name is not correct(K8s, Podman)"),
    NOT_EXIST_POD                               (3126, "Token is not exist. Not Pod"),
    NOT_EXIST_SERVER_INFO                       (3127, "Batch Server Information is not exist"),
    INACTIVE_SERVER                             (3128, "Batch Server is Inactive"),

    CANNOT_DELETE_SCH_LIST                      (4001, "Cannot change status in schedule his"),
    CANNOT_DELETE_QRZ_SCH                       (4002, "Schedule In quartz cannot be deleted"),
    NOT_EXIST_BEAN                              (4003, "Cannot found job bean"),
    NOT_EXIST_JOB                               (4004, "Cannot found the job in job details");
    private final int code;
    private final String desc;

    public int getCode() { return this.code; }
    public String getDesc() { return this.desc; }

}