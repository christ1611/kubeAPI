CREATE TABLE app_exec_his (
	app_id varchar(255) NOT NULL,
	seq varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	server_id varchar(255) NULL,
	exec_user_id varchar(10) NOT NULL,
	exec_dt timestamp NOT NULL,
	exec_tm varchar(10) NULL,
	exec_st_cd varchar(50) NOT NULL,
	exec_ver varchar(255) NULL,
	exec_param varchar(255) NULL,
	error_code varchar(50) NULL,
	job_execution_id int8 NULL,
	pod_name varchar(50) NULL,
	exec_env varchar(50) NULL,
	CONSTRAINT app_job_exec_his_pkey PRIMARY KEY (app_id, seq)
);

CREATE TABLE app_info (
	app_id varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	app_nm varchar(255) NOT NULL,
	app_st_cd varchar(10) NOT NULL,
	app_info varchar(20) NOT NULL,
	app_dv_cd varchar(20) NOT NULL,
	app_category varchar(20) NOT NULL,
	server_id varchar(255) NULL,
	lst_exec_dt timestamp NULL,
	lst_job_execution_id int8 NULL,
	CONSTRAINT app_info_app_nm_key UNIQUE (app_nm),
	CONSTRAINT app_info_pkey PRIMARY KEY (app_id)
);

CREATE TABLE app_queue_base (
	queue_id varchar(255) NOT NULL,
	reg_user_id varchar(10) NOT NULL,
	reg_dtm timestamp NOT NULL,
	upd_user_id varchar(10) NULL,
	upd_dtm timestamp NULL,
	call_target_nm varchar(10) NULL,
	app_name varchar(255) NULL,
	app_param varchar(255) NULL,
	app_tag varchar(255) NOT NULL,
	app_type varchar(10) NOT NULL,
	biz_ctgr_cd varchar(10) NOT NULL,
	req_dtm timestamp NULL,
	exec_dtm timestamp NULL,
	lst_polling_dtm timestamp NULL,
	relaunch_exec_yn varchar(1) NULL,
	relaunch_try_cnt int8 NULL,
	queue_st_cd varchar(10) NULL,
	polling_mn int8 NULL,
	task_execution_id int8 NULL,
	job_execution_id int8 NULL,
	dup_chk_cd varchar(10) NULL,
	dup_chk_param varchar(255) NULL,
	global_id varchar(10) NULL,
	parent_queue_id varchar(255) NULL,
	relaunch_exec_cnt int8 NULL,
	CONSTRAINT app_queue_base_pkey PRIMARY KEY (queue_id)
);


CREATE TABLE app_schedule_his (
	sch_id varchar(255) NOT NULL,
	seq varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	app_id varchar(255) NULL,
	server_id varchar(244) NULL,
	exec_user_id varchar(10) NOT NULL,
	exec_dt timestamp NOT NULL,
	exec_tm varchar(10) NULL,
	exec_st_cd varchar(50) NOT NULL,
	exec_ver varchar(255) NULL,
	exec_param varchar(255) NULL,
	error_code varchar(50) NULL,
	job_execution_id int8 NULL,
	pod_name varchar(50) NULL,
	CONSTRAINT app_schedule_his_pkey PRIMARY KEY (sch_id, seq)
);

CREATE TABLE app_schedule_info (
	sch_id varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	sch_nm varchar(255) NOT NULL,
	trigger_nm varchar(255) NOT NULL,
	app_id varchar(255) NOT NULL,
	sch_st_cd varchar(10) NOT NULL,
	sch_grp varchar(255) NOT NULL,
	sch_param varchar(255) NULL,
	CONSTRAINT app_schedule_info_pkey PRIMARY KEY (sch_id)
);

CREATE TABLE app_token_info (
	pod_namespace varchar(255) NOT NULL,
	service_acct varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	token_info varchar(2000) NOT NULL,
	token_created_dt timestamp NOT NULL,
	token_expi_dt timestamp NOT NULL,
	CONSTRAINT app_token_info_pkey PRIMARY KEY (pod_namespace, service_acct)
);

CREATE TABLE app_ver (
	app_id varchar(255) NOT NULL,
	app_ver varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	created_dt timestamp NOT NULL,
	default_yn varchar(1) NOT NULL,
	image_uri varchar(255) NOT NULL,
	CONSTRAINT app_ver_pkey PRIMARY KEY (app_id, app_ver)
);

CREATE TABLE batch_server_info (
	server_id varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	server_nm varchar(255) NOT NULL,
	url varchar(255) NOT NULL,
	app_category varchar(20) NULL,
	status varchar(10) NOT NULL,
	req_setting_info varchar(255) NULL,
	CONSTRAINT batch_server_info_pkey PRIMARY KEY (server_id)
);

CREATE TABLE daemon_server_info (
	server_id varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	server_nm varchar(255) NOT NULL,
	daemon_ver varchar(50) NOT NULL,
	url varchar(255) NOT NULL,
	app_category varchar(20) NULL,
	status varchar(10) NOT NULL,
	req_setting_info varchar(255) NULL,
	deployment_name varchar(255) NULL,
	CONSTRAINT daemon_server_info_pkey PRIMARY KEY (server_id)
);

CREATE TABLE nogt_sequence (
	nogt_kind_cd varchar(10) NOT NULL,
	nogt_dv_cd varchar(50) NOT NULL,
	basc_dt varchar(8) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	seq_no int8 NULL,
	CONSTRAINT nogt_sequence_pkey PRIMARY KEY (nogt_kind_cd, nogt_dv_cd, basc_dt)
);

CREATE TABLE param_info (
	param_nm varchar NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	"type" varchar NULL,
	CONSTRAINT newtable_pk PRIMARY KEY (param_nm)
);

CREATE TABLE param_rel (
	app_id varchar(255) NOT NULL,
	param_nm varchar(255) NOT NULL,
	reg_user_id varchar(20) NOT NULL,
	sys_reg_dtm timestamp NOT NULL,
	upd_user_id varchar(20) NULL,
	sys_upd_dtm timestamp NULL,
	"type" varchar(50) NULL,
	active_cd varchar(50) NOT NULL,
	mandatory_cd varchar(50) NOT NULL,
	default_setting varchar(255) NOT NULL,
	CONSTRAINT param_rel_pkey PRIMARY KEY (app_id, param_nm)
);