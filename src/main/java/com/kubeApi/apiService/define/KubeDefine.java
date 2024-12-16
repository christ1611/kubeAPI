package com.kubeApi.apiService.define;

public class KubeDefine {

    public static final String DEFAULT_NAMESPACE = "oneq";

    /** Pod 관련 URI */
    public static final String POD_URI = "api/v1/pods";
    public static final String POD_STATUS_URI = "api/v1/namespaces/%s/pods/%s/status";
    public static final String POD_NAMESPACE_URI = "api/v1/namespaces/%s/pods";
    public static final String POD_NAMESPACE_NAME_URI = "api/v1/namespaces/%s/pods/%s";
    public static final String POD_LOGS_URI = "api/v1/namespaces/%s/pods/%s/log";
    /** Job 관련 URI */
    public static final String JOB_URI = "apis/batch/v1/jobs";
    public static final String JOB_STATUS_URI = "apis/batch/v1/namespaces/%s/jobs/%s/status";
    public static final String JOB_NAMESPACE_URI = "apis/batch/v1/namespaces/%s/jobs";
    public static final String JOB_NAMESPACE_NAME_URI = "apis/batch/v1/namespaces/%s/jobs/%s";

    /** Deployment 관련 URI */
    public static final String DEPLOYMENT_URI = "apis/apps/v1/deployments";
    public static final String DEPLOYMENT_STATUS_URI = "apis/apps/v1/namespaces/%s/deployments/%s/status";
    public static final String DEPLOYMENT_NAMESPACE_URI = "apis/apps/v1/namespaces/%s/deployments";
    public static final String DEPLOYMENT_NAMESPACE_NAME_URI = "apis/apps/v1/namespaces/%s/deployments/%s";

    public static final String SERVICES_URI = "api/v1/namespaces/%s/services/";
    public static final String SERVICES_STATUS_URI = "api/v1/namespaces/%s/services/%s/status";
    public static final String SERVICES_NAMESPACE_NAME_URI = "api/v1/namespaces/%s/services/%s";

    public static final String ROUTE_URI = "apis/route.openshift.io/v1/namespaces/%s/routes";
    public static final String ROUTE_STATUS_URI = "apis/route.openshift.io/v1/namespaces/%s/routes/%s/status";
    public static final String ROUTE_NAMESPACE_NAME_URI = "apis/route.openshift.io/v1/namespaces/%s/routes/%s";

    /** Token 관련 URI */
    public static final String TOKEN_URI = "api/v1/namespaces/%s/serviceaccounts/%s/token";
    public static final String TOKEN_REVIEW = "apis/authentication.kube.io/v1/tokenreviews";
    public static final String TOKEN_INQUIRY = "api/v1/namespaces/%s/pods";
    public static final String TOKEN_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/token";
}
