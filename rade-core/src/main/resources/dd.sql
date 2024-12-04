DROP TABLE IF EXISTS `account_group`;
CREATE TABLE account_group
(
    `account_group_id`    bigint(20) NOT NULL PRIMARY KEY,
    `account_org_id`      bigint(20),
    `account_instance_id` bigint(20),
    `user_instance_id`    bigint(20),
    `org_name`            varchar(64),
    `account_name`        varchar(64),
    `app_id`              varchar(64),
    `descr`               varchar(64),
    `position`            varchar(30),
    `position_no`         varchar(30),
    `ext_json`            varchar(255),
    `state`               int(11) DEFAULT 0,
    `ver`                 int(11),
    `deleted`             tinyint(4),
    `dt`                  datetime,
    `account_id`          bigint(20)
);
DROP TABLE IF EXISTS `account_group_info`;
CREATE TABLE account_group_info
(
    `account_group_info_id` bigint(20) NOT NULL PRIMARY KEY,
    `account_group_id`      bigint(20),
    `account_org_id`        bigint(20),
    `account_instance_id`   bigint(20),
    `group_name`            varchar(64),
    `owner`                 varchar(64),
    `postion`               varchar(64),
    `phone`                 varchar(64),
    `district`              varchar(64),
    `address`               varchar(64),
    `descr`                 varchar(64),
    `state`                 int(11),
    `ver`                   int(11),
    `deleted`               tinyint(4),
    `dt`                    datetime,
    `account_id`            bigint(20)
);
DROP TABLE IF EXISTS `account_identifier`;
CREATE TABLE account_identifier
(
    `account_identifier_id` bigint(20) NOT NULL PRIMARY KEY,
    `account_instance_id`   bigint(20),
    `identifier`            varchar(64),
    `identifier_type`       int(11),
    `ver`                   int(11),
    `state`                 tinyint(4) DEFAULT 0,
    `deleted`               tinyint(4),
    `dt`                    datetime,
    `account_id`            bigint(20),
    `app_id`                varchar(64)
);
DROP TABLE IF EXISTS `account_instance`;
CREATE TABLE account_instance
(
    `account_instance_id` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance_id`    bigint(20),
    `account_name`        varchar(64),
    `password`            varchar(64),
    `avatar`              varchar(64),
    `source`              varchar(64),
    `descr`               varchar(64),
    `app_id`              varchar(64),
    `email`               varchar(50),
    `phone`               varchar(15),
    `name`                varchar(50),
    `state`               int(11)    DEFAULT 0,
    `ver`                 int(11),
    `deleted`             tinyint(4),
    `dt`                  datetime,
    `last_login_time`     datetime,
    `account_id`          bigint(20),
    `super_account`       tinyint(4) DEFAULT 0,
    `login_ip`            varchar(32),
    `account_token`       varchar(255)
);
DROP TABLE IF EXISTS `account_org`;
CREATE TABLE account_org
(
    `account_org_id` bigint(20) NOT NULL PRIMARY KEY,
    `pid`            bigint(20),
    `id_path`        varchar(64),
    `name_path`      varchar(64),
    `org_name`       varchar(64),
    `org_code`       varchar(64),
    `py_code`        varchar(64),
    `org_type`       varchar(64),
    `profile`        varchar(64),
    `tenant_id`      varchar(64),
    `app_id`         varchar(64),
    `descr`          varchar(64),
    `indate`         datetime,
    `ext_json`       varchar(255),
    `level`          int(11),
    `expdate`        datetime,
    `sorted`         int(11),
    `state`          int(11) DEFAULT 0,
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             datetime,
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE account_role
(
    `account_role_id` bigint(20) NOT NULL PRIMARY KEY,
    `rbac_role_id`    bigint(20),
    `principal_id`    bigint(20),
    `principal_name`  varchar(64),
    `role_name`       varchar(64),
    `role_code`       varchar(64),
    `app_id`          varchar(64),
    `descr`           varchar(64),
    `principal_type`  int(11),
    `state`           int(11) DEFAULT 0,
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_apply`;
CREATE TABLE app_apply
(
    `app_apply_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_categ_id` varchar(64),
    `apply_no`     varchar(64),
    `app_id`       varchar(64),
    `app_name`     varchar(64),
    `applicant`    varchar(64),
    `tenant_id`    varchar(64),
    `tenant_name`  varchar(64),
    `applied_time` datetime,
    `ver`          int(11),
    `deleted`      tinyint(4) NOT NULL DEFAULT 0,
    `dt`           datetime,
    `account_id`   bigint(20)
);
DROP TABLE IF EXISTS `app_apply_item`;
CREATE TABLE app_apply_item
(
    `app_apply_item_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_id`            varchar(64),
    `app_name`          varchar(64),
    `apply_order_no`    varchar(64),
    `platform_order_no` varchar(64),
    `platform_account`  varchar(64),
    `platform_app_name` varchar(64),
    `cert_no`           varchar(64),
    `cert_name`         varchar(64),
    `legal_name`        varchar(64),
    `contact_phone`     varchar(64),
    `contact_name`      varchar(64),
    `ver`               int(11),
    `deleted`           tinyint(4),
    `dt`                datetime,
    `account_id`        bigint(20)
);
DROP TABLE IF EXISTS `app_audit`;
CREATE TABLE app_audit
(
    `app_audit_id`      bigint(20) NOT NULL PRIMARY KEY,
    `app_id`            varchar(64),
    `checked_user`      varchar(64),
    `checked_time`      datetime,
    `checked_descr`     varchar(64),
    `checked_user_name` varchar(64),
    `account_name`      varchar(64),
    `remark`            varchar(64),
    `check_state`       int(11),
    `state`             tinyint(4),
    `ver`               int(11),
    `deleted`           tinyint(4),
    `dt`                datetime,
    `account_id`        bigint(20)
);
DROP TABLE IF EXISTS `app_bridger`;
CREATE TABLE app_bridger
(
    `app_bridger_id`      bigint(20) NOT NULL PRIMARY KEY,
    `app_id`              varchar(64),
    `platform`            varchar(64),
    `thirdparty_order_no` varchar(64),
    `thirdparty_app_id`   varchar(64),
    `thirdparty_app_name` varchar(64),
    `ver`                 int(11),
    `deleted`             tinyint(4),
    `dt`                  datetime,
    `account_id`          bigint(20)
);
DROP TABLE IF EXISTS `app_categ`;
CREATE TABLE app_categ
(
    `app_categ_id` bigint(20) NOT NULL PRIMARY KEY,
    `pid`          bigint(20),
    `id_path`      varchar(64),
    `name_path`    varchar(64),
    `categ_name`   varchar(64),
    `categ_code`   varchar(64),
    `descr`        varchar(64),
    `sorted`       int(11),
    `ver`          int(11),
    `deleted`      tinyint(4),
    `dt`           datetime,
    `account_id`   bigint(20)
);
DROP TABLE IF EXISTS `app_datav`;
CREATE TABLE app_datav
(
    `app_datav_id` bigint(20)   NOT NULL PRIMARY KEY,
    `app_uri_id`   bigint(20),
    `app_id`       varchar(255) NOT NULL,
    `api_group`    varchar(32),
    `view_name`    varchar(255) NOT NULL,
    `from_json`    text         NOT NULL,
    `columns_json` text         NOT NULL,
    `params_json`  text,
    `order_json`   text,
    `api_json`     text,
    `limit_json`   text,
    `view_state`   int(11) DEFAULT 0,
    `ver`          int(11),
    `deleted`      tinyint(1),
    `dt`           datetime,
    `account_id`   bigint(20)
);
DROP TABLE IF EXISTS `app_dependon`;
CREATE TABLE app_dependon
(
    `app_dependon_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_instance_id` bigint(20),
    `dependon_name`   varchar(64),
    `app_id`          varchar(64),
    `app_name`        varchar(64),
    `env`             varchar(64),
    `run_on`          varchar(64),
    `depend_on`       varchar(64),
    `config_json`     varchar(1024),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_dsl`;
CREATE TABLE app_dsl
(
    `app_dsl_id`    bigint(20) NOT NULL PRIMARY KEY,
    `app_uri_id`    bigint(20) NOT NULL,
    `app_depend_id` bigint(20),
    `dsl_json`      text,
    `params_json`   varchar(255),
    `order_json`    varchar(255),
    `limit_json`    varchar(255),
    `plugin_jsons`  varchar(255),
    `ver`           int(11) DEFAULT 0,
    `deleted`       tinyint(4),
    `dt`            datetime,
    `account_id`    bigint(20)
);
DROP TABLE IF EXISTS `app_env`;
CREATE TABLE app_env
(
    `app_env_id`      bigint(20)   NOT NULL PRIMARY KEY,
    `app_uri_id`      bigint(20)   NOT NULL,
    `app_dependon_id` bigint(20)   NOT NULL,
    `app_id`          varchar(255) NOT NULL,
    `env`             varchar(255) NOT NULL,
    `ver`             int(11)      NOT NULL DEFAULT 0,
    `deleted`         tinyint(4)   NOT NULL,
    `dt`              datetime     NOT NULL,
    `account_id`      bigint(20)   NOT NULL
);
DROP TABLE IF EXISTS `app_field`;
CREATE TABLE app_field
(
    `app_field_id`    bigint(20) NOT NULL PRIMARY KEY,
    `app_instance_id` bigint(20),
    `app_id`          varchar(64),
    `app_widget_id`   bigint(20),
    `field_name`      varchar(64),
    `field_code`      varchar(64),
    `input_type`      varchar(64),
    `placeholder`     varchar(64),
    `datasource`      varchar(64),
    `notnull`         tinyint(4),
    `seq`             int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_instance`;
CREATE TABLE app_instance
(
    `app_instance_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_categ_id`    bigint(20),
    `app_id`          varchar(128),
    `app_name`        varchar(64),
    `app_code`        varchar(64),
    `home_url`        varchar(64),
    `descr`           varchar(64),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_language`;
CREATE TABLE app_language
(
    `app_language_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_page_id`     bigint(20),
    `app_id`          varchar(64),
    `json_i18n`       varchar(64),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_licence`;
CREATE TABLE app_licence
(
    `app_licence_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_id`         varchar(64),
    `secret_id`      varchar(64),
    `secret_key`     varchar(64),
    `app_key`        varchar(64),
    `account_id`     varchar(64),
    `tenant_id`      varchar(64),
    `state`          tinyint(4),
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             date
);
DROP TABLE IF EXISTS `app_menu`;
CREATE TABLE app_menu
(
    `app_menu_id`     bigint(20) NOT NULL PRIMARY KEY,
    `rbac_menu_id`    bigint(20),
    `pid`             bigint(20),
    `app_instance_id` bigint(20),
    `app_id`          varchar(64),
    `menu_name`       varchar(64),
    `menu_code`       varchar(64),
    `menu_path`       varchar(64),
    `icon`            varchar(64),
    `id_path`         varchar(64),
    `name_path`       varchar(64),
    `open_type`       int(11),
    `target_url`      varchar(64),
    `level`           int(11),
    `sorted`          int(11),
    `state`           int(11) DEFAULT 0,
    `menu_type`       int(11),
    `iframe`          int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_menus`;
CREATE TABLE app_menus
(
    `app_menus_id`    bigint(20) NOT NULL PRIMARY KEY,
    `rbac_menu_id`    bigint(20),
    `pid`             bigint(20),
    `app_instance_id` bigint(20),
    `app_id`          varchar(64),
    `menu_name`       varchar(64),
    `menu_code`       varchar(64),
    `menu_path`       varchar(64),
    `component_path`  varchar(255),
    `icon`            varchar(64),
    `id_path`         varchar(1024),
    `name_path`       varchar(64),
    `open_type`       int(11),
    `target_url`      varchar(64),
    `level`           int(11),
    `sorted`          int(11)    DEFAULT 0,
    `state`           int(11)    DEFAULT 0,
    `ver`             int(11),
    `iframe`          int(11),
    `deleted`         tinyint(4) DEFAULT 0,
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_mind`;
CREATE TABLE app_mind
(
    `app_mind_id`  bigint(20) NOT NULL PRIMARY KEY,
    `app_id`       varchar(64),
    `api_json`     varchar(64),
    `ddl_json`     varchar(64),
    `build_json`   varchar(64),
    `config_json`  varchar(64),
    `mind_version` varchar(64),
    `ver`          int(11),
    `deleted`      tinyint(4),
    `dt`           datetime,
    `account_id`   bigint(20)
);
DROP TABLE IF EXISTS `app_module`;
CREATE TABLE app_module
(
    `app_module_id` bigint(20) NOT NULL PRIMARY KEY,
    `module_app_id` bigint(20),
    `module_name`   varchar(32),
    `module_code`   varchar(32),
    `ver`           int(11),
    `deleted`       tinyint(1),
    `dt`            datetime
);
DROP TABLE IF EXISTS `app_page`;
CREATE TABLE app_page
(
    `app_page_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_id`      varchar(64),
    `page_no`     varchar(64),
    `page_name`   varchar(64),
    `page_path`   varchar(64),
    `page_code`   varchar(64),
    `page_url`    varchar(64),
    `descr`       varchar(64),
    `sorted`      int(11),
    `ver`         int(11),
    `dt`          datetime,
    `deleted`     tinyint(4),
    `account_id`  bigint(20)
);
DROP TABLE IF EXISTS `app_resource`;
CREATE TABLE app_resource
(
    `app_resource_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_instance_id` bigint(20),
    `app_page_id`     bigint(20),
    `extations`       varchar(64),
    `resources`       mediumtext,
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_service`;
CREATE TABLE app_service
(
    `app_service_id` bigint(20) NOT NULL PRIMARY KEY,
    `service_name`   varchar(64),
    `service_code`   varchar(64),
    `service_host`   varchar(64),
    `service_descr`  varchar(64),
    `app_id`         varchar(64),
    `app_type`       int(11),
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             datetime,
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `app_setting`;
CREATE TABLE app_setting
(
    `app_setting_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_id`         varchar(64),
    `setting_key`    varchar(64),
    `setting_json`   varchar(64),
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             datetime,
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `app_signature`;
CREATE TABLE app_signature
(
    `app_signature_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_instance_id`  bigint(20),
    `app_id`           varchar(64),
    `secret_id`        varchar(255),
    `secret_key`       varchar(512),
    `encryption`       varchar(255),
    `config_json`      varchar(1024),
    `nonce`            varchar(255),
    `mode`             int(11),
    `deleted`          tinyint(1),
    `expiration`       datetime,
    `dt`               datetime
);
DROP TABLE IF EXISTS `app_snapshot`;
CREATE TABLE app_snapshot
(
    `app_snapshot_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_id`          varchar(64),
    `snapshot_name`   varchar(64),
    `snapshot_code`   varchar(64),
    `schema_code`     varchar(64),
    `ddl`             longtext,
    `dml`             longtext,
    `md5`             varchar(64),
    `data`            mediumtext,
    `snapshot_type`   int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_sql`;
CREATE TABLE app_sql
(
    `app_sql_id`   bigint(20)   NOT NULL PRIMARY KEY,
    `app_uri_id`   bigint(20)   NOT NULL,
    `sql_text`     varchar(255) NOT NULL,
    `plugin_jsons` varchar(255) NOT NULL,
    `app_id`       varchar(255) NOT NULL,
    `ver`          int(11)      NOT NULL DEFAULT 0,
    `deleted`      tinyint(4)   NOT NULL,
    `dt`           datetime     NOT NULL,
    `account_id`   bigint(20)   NOT NULL
);
DROP TABLE IF EXISTS `app_uri`;
CREATE TABLE app_uri
(
    `app_uri_id`      bigint(20) NOT NULL PRIMARY KEY,
    `app_instance_id` bigint(20),
    `app_module_id`   bigint(20),
    `app_datav_id`    bigint(20),
    `app_id`          varchar(64),
    `protocol`        varchar(64),
    `format`          varchar(64),
    `content_type`    varchar(64),
    `operation`       varchar(255),
    `method_code`     varchar(255),
    `http_method`     varchar(10),
    `proxy_method`    varchar(255),
    `api_group`       varchar(20),
    `uri`             varchar(64),
    `descr`           varchar(64),
    `tag`             varchar(64),
    `inputs`          text,
    `output`          text,
    `uri_version`     varchar(64),
    `doc_json`        text,
    `dependon_json`   varchar(512),
    `plugin_json`     varchar(512),
    `uri_state`       int(11)                      DEFAULT 0,
    `ver`             int(11),
    `transaction`     tinyint(1) unsigned zerofill DEFAULT 0,
    `deleted`         tinyint(1) unsigned zerofill DEFAULT 0,
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_uris`;
CREATE TABLE app_uris
(
    `app_uris_id`      bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `module_menu_id`   bigint(20),
    `app_menus_id`     bigint(20),
    `module_uri_id`    bigint(20),
    `app_signature_id` bigint(20),
    `uri_name`         varchar(255),
    `uri_code`         varchar(255),
    `uri_method`       varchar(255),
    `label`            varchar(64),
    `url`              varchar(64),
    `config_json`      varchar(64),
    `descr`            varchar(2048),
    `http_method`      varchar(19),
    `app_id`           varchar(64),
    `scope`            int(11),
    `state`            int(11)    DEFAULT 0,
    `ver`              int(11),
    `customized`       tinyint(1) DEFAULT 0,
    `shared`           tinyint(4) DEFAULT 0,
    `deleted`          tinyint(4) DEFAULT 0,
    `dt`               datetime,
    `sorted`           int(11)    DEFAULT 0
);
DROP TABLE IF EXISTS `app_user_test`;
CREATE TABLE app_user_test
(
    `user_id`     bigint(20) NOT NULL PRIMARY KEY,
    `name`        varchar(30),
    `age`         int(11),
    `user_change` decimal(10, 0),
    `desc_json`   text,
    `address`     varchar(255)
);
DROP TABLE IF EXISTS `app_widget`;
CREATE TABLE app_widget
(
    `app_widget_id`   bigint(20) NOT NULL PRIMARY KEY,
    `app_instance_id` bigint(20),
    `app_id`          varchar(64),
    `widget_name`     varchar(64),
    `widget_code`     varchar(64),
    `widget_version`  varchar(64),
    `protocol`        varchar(64),
    `api`             varchar(64),
    `data`            text,
    `widget_style`    varchar(64),
    `widget_type`     varchar(64),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `app_window`;
CREATE TABLE app_window
(
    `app_window_id`   bigint(20) NOT NULL PRIMARY KEY,
    `app_instance_id` bigint(20),
    `app_widget_id`   bigint(20),
    `app_page_id`     bigint(20),
    `app_id`          varchar(64),
    `widget_type`     varchar(64),
    `seq`             int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `dict_data`;
CREATE TABLE dict_data
(
    `app_dict_data_id` bigint(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `app_dict_type_id` bigint(20)   NOT NULL,
    `dict_sort`        int(11)      NOT NULL DEFAULT 0,
    `dict_label`       varchar(100) NOT NULL,
    `dict_value`       varchar(100) NOT NULL,
    `dict_type`        varchar(100) NOT NULL,
    `is_default`       int(11)      NOT NULL DEFAULT 1,
    `remark`           varchar(255),
    `state`            int(11)      NOT NULL DEFAULT 0,
    `ver`              int(11)      NOT NULL DEFAULT 0,
    `deleted`          tinyint(1)   NOT NULL DEFAULT 0,
    `dt`               datetime     NOT NULL
);
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE dict_type
(
    `app_dict_type_id` bigint(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `dict_type_descr`  varchar(100) NOT NULL,
    `dict_type_code`   varchar(100) NOT NULL,
    `remark`           varchar(255),
    `state`            int(11)      NOT NULL DEFAULT 0,
    `ver`              int(11)      NOT NULL DEFAULT 0,
    `deleted`          tinyint(1)   NOT NULL DEFAULT 0,
    `dt`               datetime     NOT NULL
);
DROP TABLE IF EXISTS `eqpt_api`;
CREATE TABLE eqpt_api
(
    `eqpt_api_id`      bigint(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `eqpt_instance_id` bigint(20),
    `account_id`       bigint(20),
    `api`              varchar(255) NOT NULL,
    `method`           varchar(32),
    `eqpt_no`          varchar(255),
    `action`           varchar(255),
    `secret_key`       varchar(255),
    `param_json`       text,
    `ver`              int(11)    DEFAULT 0,
    `deleted`          tinyint(1) DEFAULT 0,
    `dt`               datetime
);
DROP TABLE IF EXISTS `eqpt_catge`;
CREATE TABLE eqpt_catge
(
    `eqpt_catge_id` bigint(20) NOT NULL PRIMARY KEY,
    `pid`           bigint(20),
    `categ_name`    varchar(64),
    `categ_code`    varchar(64),
    `id_path`       varchar(64),
    `name_path`     varchar(64),
    `ver`           int(11),
    `deleted`       tinyint(1) NOT NULL DEFAULT 0,
    `dt`            datetime            DEFAULT current_timestamp(),
    `account_id`    bigint(20),
    `app_id`        varchar(64)
);
DROP TABLE IF EXISTS `eqpt_exception`;
CREATE TABLE eqpt_exception
(
    `eqpt_exception_id` bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_work_id`      bigint(20),
    `eqpt_instance_id`  bigint(20),
    `exception_code`    varchar(64),
    `descr`             varchar(64),
    `happen_time`       datetime,
    `recover_time`      datetime,
    `ver`               int(11),
    `deleted`           tinyint(1) NOT NULL DEFAULT 0,
    `dt`                datetime            DEFAULT current_timestamp(),
    `account_id`        bigint(20)
);
DROP TABLE IF EXISTS `eqpt_instance`;
CREATE TABLE eqpt_instance
(
    `eqpt_instance_id` bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_catge_id`    bigint(20),
    `group_id`         bigint(20),
    `owner_id`         bigint(20),
    `eqpt_name`        varchar(64),
    `eqpt_no`          varchar(64),
    `eqpt_model`       varchar(64),
    `eqpt_type`        int(11),
    `brand`            varchar(64),
    `state`            int(11)             DEFAULT 0,
    `ver`              int(11),
    `deleted`          tinyint(1) NOT NULL DEFAULT 0,
    `dt`               datetime            DEFAULT current_timestamp(),
    `account_id`       bigint(20),
    `app_id`           varchar(64)
);
DROP TABLE IF EXISTS `eqpt_maint`;
CREATE TABLE eqpt_maint
(
    `eqpt_maint_id`     bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_instance_id`  bigint(20),
    `liaison`           varchar(64),
    `phone`             varchar(64),
    `manufacturer`      varchar(64),
    `construction_unit` varchar(64),
    `filings`           varchar(64),
    `factory_number`    varchar(64),
    `power`             varchar(64),
    `manufacture_date`  datetime,
    `registration_date` datetime,
    `installation_date` datetime,
    `maintenance_date`  datetime,
    `useful_life`       decimal(11, 2),
    `ver`               int(11),
    `deleted`           tinyint(1) NOT NULL DEFAULT 0,
    `dt`                datetime            DEFAULT current_timestamp(),
    `account_id`        bigint(20)
);
DROP TABLE IF EXISTS `eqpt_model`;
CREATE TABLE eqpt_model
(
    `eqpt_model_id`    bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_catge_id`    bigint(20),
    `eqpt_instance_id` bigint(20),
    `model_name`       varchar(64),
    `modle_params`     text,
    `ver`              int(11),
    `deleted`          tinyint(1) NOT NULL DEFAULT 0,
    `dt`               datetime            DEFAULT current_timestamp(),
    `account_id`       bigint(20)
);
DROP TABLE IF EXISTS `eqpt_monitor`;
CREATE TABLE eqpt_monitor
(
    `eqpt_monitor_id`  bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `eqpt_instance_id` bigint(20),
    `ws_tree_id`       bigint(20),
    `eqpt_no`          varchar(255),
    `principal_no`     varchar(255),
    `app_id`           varchar(64),
    `data_json`        text,
    `monitor_type`     int(11),
    `eqpt_type`        int(11),
    `ver`              int(11)    DEFAULT 0,
    `deleted`          tinyint(1) DEFAULT 0,
    `dt`               datetime
);
DROP TABLE IF EXISTS `eqpt_pair`;
CREATE TABLE eqpt_pair
(
    `eqpt_pair_id`     bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_catge_id`    bigint(20),
    `eqpt_instance_id` bigint(20),
    `attr_name`        varchar(64),
    `attr_val`         varchar(64),
    `attr_type`        varchar(64),
    `unit`             varchar(64),
    `seq`              int(11),
    `ver`              int(11),
    `deleted`          tinyint(1) NOT NULL DEFAULT 0,
    `dt`               datetime            DEFAULT current_timestamp(),
    `account_id`       bigint(20)
);
DROP TABLE IF EXISTS `eqpt_runtime`;
CREATE TABLE eqpt_runtime
(
    `eqpt_runtime_id`      bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_work_id`         bigint(20),
    `eqpt_instance_id`     bigint(20),
    `task_order_id`        bigint(20),
    `task_tree_id`         bigint(20),
    `material_instance_id` bigint(20),
    `current_x`            decimal(11, 2),
    `current_y`            decimal(11, 2),
    `current_z`            decimal(11, 2),
    `height`               decimal(11, 2),
    `distance_y`           decimal(11, 2),
    `distance_x`           decimal(11, 2),
    `voltage`              decimal(11, 2),
    `current`              decimal(11, 2),
    `grade`                varchar(64),
    `ver`                  int(11),
    `deleted`              tinyint(1) NOT NULL DEFAULT 0,
    `dt`                   datetime            DEFAULT current_timestamp(),
    `account_id`           bigint(20)
);
DROP TABLE IF EXISTS `eqpt_state`;
CREATE TABLE eqpt_state
(
    `eqpt_state_id`    bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_instance_id` bigint(20),
    `eqpt_runtime_id`  bigint(20),
    `state`            int(11),
    `dt`               datetime DEFAULT current_timestamp(),
    `exception_code`   varchar(64),
    `account_id`       bigint(20),
    `eqpt_no`          varchar(64),
    `eqpt_name`        varchar(64)
);
DROP TABLE IF EXISTS `eqpt_trigger`;
CREATE TABLE eqpt_trigger
(
    `eqpt_trigger_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `from_eqpt_id`    bigint(20),
    `to_eqpt_id`      bigint(20),
    `account_id`      bigint(20),
    `trigger_name`    varchar(255),
    `processor_bean`  varchar(255),
    `trigger_param`   text,
    `eqpt_api_jsons`  text,
    `from_eqpt_no`    varchar(255),
    `to_eqpt_no`      varchar(255),
    `callback`        varchar(255),
    `app_id`          varchar(64),
    `descr`           text,
    `ver`             int(11)    DEFAULT 0,
    `deleted`         tinyint(1) DEFAULT 0,
    `dt`              datetime
);
DROP TABLE IF EXISTS `eqpt_vehicle`;
CREATE TABLE eqpt_vehicle
(
    `eqpt_vehicle_id` bigint(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `account_id`      bigint(20),
    `app_id`          varchar(255),
    `vehicle_no`      varchar(255) NOT NULL,
    `contract`        varchar(255),
    `cellphone`       varchar(255),
    `capacity`        varchar(255),
    `load_weight`     decimal(10, 2),
    `dead_weight`     decimal(10, 2),
    `state`           int(11),
    `ver`             int(11)    DEFAULT 0,
    `deleted`         tinyint(1) DEFAULT 0,
    `dt`              datetime,
    `vehicle_kind`    int(11),
    `vehicle_type`    int(11),
    `allow_ws_descr`  varchar(255),
    `allow_ws_id`     bigint(20),
    `descr`           varchar(255)
);
DROP TABLE IF EXISTS `eqpt_work`;
CREATE TABLE eqpt_work
(
    `eqpt_work_id`         bigint(20) NOT NULL PRIMARY KEY,
    `eqpt_instance_id`     bigint(20),
    `task_order_id`        bigint(20),
    `task_tree_id`         bigint(20),
    `material_instance_id` bigint(20),
    `weight`               decimal(11, 2),
    `action_code`          varchar(64),
    `action_data`          varchar(64),
    `action_seq`           int(11),
    `mode`                 int(11),
    `state`                int(11),
    `ver`                  int(11),
    `deleted`              tinyint(1) NOT NULL DEFAULT 0,
    `dt`                   datetime            DEFAULT current_timestamp(),
    `account_id`           bigint(20)
);
DROP TABLE IF EXISTS `inventory_bill`;
CREATE TABLE inventory_bill
(
    `inventory_bill_id` bigint(20) NOT NULL PRIMARY KEY,
    `account_id`        bigint(20),
    `project_id`        varchar(64),
    `ship_id`           varchar(64),
    `sub_id`            varchar(64),
    `ver`               int(11),
    `deleted`           tinyint(4) NOT NULL DEFAULT 0,
    `dt`                datetime   NOT NULL DEFAULT current_timestamp(),
    `app_id`            varchar(64)
);
DROP TABLE IF EXISTS `inventory_detail`;
CREATE TABLE inventory_detail
(
    `inventory_detail_id`   bigint(20) NOT NULL PRIMARY KEY,
    `inventory_receipts_id` bigint(20),
    `material_instance_id`  bigint(20),
    `ws_position_id`        bigint(20),
    `account_id`            bigint(20),
    `material_no`           varchar(64),
    `material_categ_name`   varchar(64),
    `from_position_no`      varchar(64),
    `to_position_no`        varchar(64),
    `roll_no`               varchar(64),
    `height`                decimal(11, 2),
    `width`                 decimal(11, 2),
    `length`                decimal(11, 2),
    `weight`                decimal(11, 2),
    `quantity`              decimal(11, 2),
    `sequence`              int(11),
    `ver`                   int(11),
    `deleted`               tinyint(4) NOT NULL DEFAULT 0,
    `dt`                    datetime   NOT NULL DEFAULT current_timestamp(),
    `app_id`                varchar(64)
);
DROP TABLE IF EXISTS `inventory_receipt_view`;
CREATE TABLE inventory_receipt_view
(
    `inventory_receipts_id` bigint(20) NOT NULL,
    `material_no`           varchar(64),
    `sub_id`                varchar(64)
);
DROP TABLE IF EXISTS `inventory_receipts`;
CREATE TABLE inventory_receipts
(
    `inventory_receipts_id` bigint(20) NOT NULL PRIMARY KEY,
    `inventory_bill_id`     bigint(20),
    `account_id`            bigint(20),
    `receipt_type`          int(11),
    `state`                 int(11),
    `ver`                   int(11),
    `deleted`               tinyint(4) NOT NULL DEFAULT 0,
    `dt`                    datetime   NOT NULL DEFAULT current_timestamp(),
    `inventory_time`        datetime   NOT NULL DEFAULT current_timestamp(),
    `app_id`                varchar(64)
);
DROP TABLE IF EXISTS `inventory_stock`;
CREATE TABLE inventory_stock
(
    `inventory_stock_id`   bigint(20) NOT NULL PRIMARY KEY,
    `ws_position_id`       bigint(20),
    `material_instance_id` bigint(20),
    `account_id`           bigint(20),
    `position_no`          varchar(64),
    `material_categ_name`  varchar(64),
    `sub_id`               varchar(64),
    `ship_id`              varchar(64),
    `material_no`          varchar(64),
    `material_spec_json`   varchar(64),
    `sequence`             int(11),
    `quantity`             decimal(11, 2),
    `locks`                decimal(11, 2),
    `ver`                  int(11)             DEFAULT 0,
    `deleted`              tinyint(4) NOT NULL DEFAULT 0,
    `dt`                   datetime   NOT NULL DEFAULT current_timestamp(),
    `app_id`               varchar(64)
);
DROP TABLE IF EXISTS `log_action`;
CREATE TABLE log_action
(
    `log_action_id` bigint(20)   NOT NULL PRIMARY KEY,
    `uri`           varchar(255) NOT NULL,
    `action`        varchar(255) NOT NULL,
    `method`        varchar(50)  NOT NULL,
    `signature`     varchar(255),
    `descr`         varchar(255),
    `tables`        varchar(255),
    `enable`        tinyint(1)   NOT NULL DEFAULT 1,
    `ver`           int(11)      NOT NULL DEFAULT 1,
    `deleted`       tinyint(1)   NOT NULL DEFAULT 0,
    `dt`            datetime     NOT NULL DEFAULT 0000 - 00 - 00 00:00:00,
    `account_id`    bigint(20)
);
DROP TABLE IF EXISTS `log_column`;
CREATE TABLE log_column
(
    `log_column_id` bigint(20) NOT NULL PRIMARY KEY,
    `log_table_id`  bigint(20),
    `app_id`        varchar(64),
    `column_json`   text,
    `ver`           int(11),
    `deleted`       tinyint(4),
    `dt`            datetime,
    `account_id`    bigint(20)
);
DROP TABLE IF EXISTS `log_setting`;
CREATE TABLE log_setting
(
    `log_setting_id` bigint(20) NOT NULL PRIMARY KEY,
    `app_id`         varchar(64),
    `database_name`  varchar(64),
    `database_json`  varchar(64),
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             datetime,
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `log_table`;
CREATE TABLE log_table
(
    `log_table_id`   bigint(20) NOT NULL PRIMARY KEY,
    `log_setting_id` bigint(20),
    `app_id`         varchar(64),
    `table_name`     varchar(64),
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             datetime,
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `material_attr_name`;
CREATE TABLE material_attr_name
(
    `material_attr_name_id` bigint(20) NOT NULL PRIMARY KEY,
    `material_categ_id`     bigint(20),
    `attr_name`             varchar(64),
    `attr_code`             varchar(64),
    `attr_type`             varchar(64),
    `unit`                  varchar(64),
    `attr_typ`              int(11),
    `select_typ`            int(11),
    `input_typ`             int(11),
    `ver`                   int(11),
    `deleted`               tinyint(1) NOT NULL DEFAULT 0,
    `dt`                    datetime            DEFAULT current_timestamp(),
    `account_id`            bigint(20)
);
DROP TABLE IF EXISTS `material_attr_val`;
CREATE TABLE material_attr_val
(
    `material_attr_val_id`  bigint(20) NOT NULL PRIMARY KEY,
    `material_attr_name_id` bigint(20),
    `attr_val`              varchar(64),
    `ver`                   int(11),
    `deleted`               tinyint(1) NOT NULL DEFAULT 0,
    `dt`                    datetime            DEFAULT current_timestamp(),
    `account_id`            bigint(20)
);
DROP TABLE IF EXISTS `material_categ`;
CREATE TABLE material_categ
(
    `material_categ_id`    bigint(20) NOT NULL PRIMARY KEY,
    `pid`                  bigint(20),
    `account_id`           bigint(20),
    `categ_name`           varchar(64),
    `categ_code`           varchar(64),
    `id_path`              varchar(64),
    `name_path`            varchar(64),
    `icon`                 varchar(64),
    `details`              varchar(1024),
    `descr`                varchar(255),
    `app_id`               varchar(64),
    `unit`                 varchar(12),
    `density`              decimal(10, 0),
    `material_amount`      int(11),
    `ver`                  int(11),
    `deleted`              int(11)    NOT NULL DEFAULT 0,
    `material_categ_state` int(11)             DEFAULT 0,
    `dt`                   datetime            DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `material_instance`;
CREATE TABLE material_instance
(
    `material_instance_id` bigint(20)  NOT NULL PRIMARY KEY,
    `material_categ_id`    bigint(20),
    `group_id`             bigint(20),
    `owner_id`             bigint(20),
    `material_categ_name`  varchar(64),
    `material_no`          varchar(64) NOT NULL PRIMARY KEY,
    `material_name`        varchar(64),
    `descr`                varchar(64),
    `details`              varchar(1024),
    `material_state`       int(11)              DEFAULT 0,
    `ver`                  int(11),
    `deleted`              tinyint(1)  NOT NULL DEFAULT 0,
    `dt`                   datetime             DEFAULT current_timestamp(),
    `account_id`           bigint(20)
);
DROP TABLE IF EXISTS `material_pair`;
CREATE TABLE material_pair
(
    `material_pair_id`     bigint(20) NOT NULL PRIMARY KEY,
    `material_categ_id`    bigint(20),
    `material_instance_id` bigint(20),
    `attr_name`            varchar(64),
    `attr_val`             varchar(64),
    `attr_type`            varchar(64),
    `unit`                 varchar(64),
    `seq`                  int(11),
    `ver`                  int(11),
    `deleted`              tinyint(1) NOT NULL DEFAULT 0,
    `dt`                   datetime            DEFAULT current_timestamp(),
    `account_id`           bigint(20)
);
DROP TABLE IF EXISTS `module_namespace`;
CREATE TABLE module_namespace
(
    `module_namespace_id` bigint(20) NOT NULL PRIMARY KEY,
    `pid`                 bigint(20),
    `module_name`         varchar(255),
    `module_code`         varchar(64),
    `menu_name`           varchar(64),
    `menu_code`           varchar(64),
    `menu_path`           varchar(255),
    `id_path`             varchar(255),
    `name_path`           varchar(64),
    `code_path`           varchar(64),
    `config_json`         varchar(64),
    `descr`               varchar(1024),
    `icon`                varchar(64),
    `component_path`      varchar(255),
    `sorted`              int(11)                      DEFAULT 0,
    `state`               int(11)                      DEFAULT 0,
    `ver`                 int(11),
    `visible`             tinyint(4)                   DEFAULT 0,
    `iframe`              tinyint(4),
    `deleted`             tinyint(1) unsigned zerofill DEFAULT 0,
    `dt`                  datetime,
    `level`               int(11),
    `open_type`           int(11),
    `menu_type`           int(11)
);
DROP TABLE IF EXISTS `module_uri`;
CREATE TABLE module_uri
(
    `module_uri_id`       bigint(20) NOT NULL PRIMARY KEY,
    `module_namespace_id` bigint(20),
    `uri_name`            varchar(1024),
    `uri_code`            varchar(255),
    `label`               varchar(64),
    `url`                 varchar(64),
    `config_json`         varchar(64),
    `descr`               varchar(1024),
    `method_name`         varchar(19),
    `ver`                 int(11),
    `state`               int(11)    DEFAULT 0,
    `scope`               int(11),
    `shared`              tinyint(1) DEFAULT 0,
    `deleted`             tinyint(1) DEFAULT 0,
    `dt`                  datetime
);
DROP TABLE IF EXISTS `my_data`;
CREATE TABLE my_data
(
    `id`   bigint(20) NOT NULL PRIMARY KEY,
    `name` varchar(255)
);
DROP TABLE IF EXISTS `my_data_detail`;
CREATE TABLE my_data_detail
(
    `id`      bigint(20) NOT NULL PRIMARY KEY,
    `detail`  varchar(255),
    `data_id` bigint(20)
);
DROP TABLE IF EXISTS `rbac_group`;
CREATE TABLE rbac_group
(
    `rbac_group_id` bigint(20) NOT NULL PRIMARY KEY,
    `group_name`    varchar(64),
    `group_code`    varchar(64),
    `state`         int(11),
    `ver`           int(11),
    `deleted`       tinyint(1) DEFAULT 1,
    `dt`            datetime,
    `account_id`    bigint(20)
);
DROP TABLE IF EXISTS `rbac_menu`;
CREATE TABLE rbac_menu
(
    `rbac_menu_id` bigint(20) NOT NULL PRIMARY KEY,
    `pid`          bigint(20),
    `app_menu_id`  bigint(20),
    `name`         varchar(64),
    `code`         varchar(64),
    `id_path`      varchar(255),
    `name_path`    varchar(64),
    `code_path`    varchar(64),
    `path`         varchar(255),
    `config_json`  varchar(64),
    `app_id`       varchar(64),
    `descr`        varchar(64),
    `sorted`       int(11)                      DEFAULT 0,
    `ver`          int(11),
    `visible`      tinyint(4)                   DEFAULT 0,
    `isframe`      tinyint(4),
    `state`        tinyint(4)                   DEFAULT 0,
    `deleted`      tinyint(1) unsigned zerofill DEFAULT 0,
    `dt`           datetime,
    `level`        int(11),
    `account_id`   bigint(20),
    `icon`         varchar(64),
    `open_type`    int(11)
);
DROP TABLE IF EXISTS `rbac_module`;
CREATE TABLE rbac_module
(
    `rbac_module_id` bigint(20) NOT NULL PRIMARY KEY,
    `module_name`    varchar(64),
    `module_code`    varchar(64),
    `app_id`         varchar(64),
    `state`          int(11),
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             datetime,
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `rbac_permission`;
CREATE TABLE rbac_permission
(
    `rbac_permission_id` bigint(20) NOT NULL PRIMARY KEY,
    `rbac_role_id`       bigint(20),
    `role_pid`           bigint(20),
    `resource_id`        bigint(20),
    `role_code`          varchar(64),
    `role_name`          varchar(64),
    `authority`          varchar(255),
    `app_id`             varchar(64),
    `descr`              varchar(64),
    `resource_type`      int(11),
    `state`              int(11) DEFAULT 0,
    `ver`                int(11),
    `deleted`            tinyint(4),
    `dt`                 datetime,
    `account_id`         bigint(20)
);
DROP TABLE IF EXISTS `rbac_resources`;
CREATE TABLE rbac_resources
(
    `rbac_resources_id` bigint(20) NOT NULL PRIMARY KEY,
    `rbac_module_id`    bigint(20),
    `resource_id`       bigint(20),
    `name`              varchar(64),
    `code`              varchar(64),
    `app_id`            varchar(64),
    `resource_type`     int(11),
    `state`             int(11),
    `ver`               int(11),
    `deleted`           tinyint(4),
    `dt`                datetime,
    `account_id`        bigint(20)
);
DROP TABLE IF EXISTS `rbac_role`;
CREATE TABLE rbac_role
(
    `rbac_role_id` bigint(20) NOT NULL PRIMARY KEY,
    `pid`          bigint(20),
    `role_name`    varchar(64),
    `py_code`      varchar(64),
    `role_code`    varchar(64),
    `id_path`      varchar(255),
    `name_path`    varchar(64),
    `code_path`    varchar(64),
    `icon`         varchar(64),
    `app_id`       varchar(64),
    `descr`        varchar(64),
    `role_level`   int(11),
    `inherit`      tinyint(4),
    `state`        int(11)    DEFAULT 0,
    `deleted`      tinyint(1) DEFAULT 0,
    `dt`           datetime,
    `account_id`   bigint(20)
);
DROP TABLE IF EXISTS `rbac_rule`;
CREATE TABLE rbac_rule
(
    `rbac_rule_id`    bigint(20) NOT NULL PRIMARY KEY,
    `rbac_role_id`    bigint(20),
    `role_code`       varchar(255),
    `rule_descr`      varchar(255),
    `data_table`      varchar(255),
    `app_id`          varchar(255),
    `expression`      varchar(2048),
    `last_expression` varchar(255),
    `selects`         varchar(255),
    `data_scop`       int(11),
    `sorted`          int(11),
    `ver`             int(11)    DEFAULT 0,
    `deleted`         tinyint(4) DEFAULT 0,
    `dt`              datetime,
    `rule_level`      int(11),
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `rbac_uri`;
CREATE TABLE rbac_uri
(
    `rbac_uri_id`  bigint(20) NOT NULL PRIMARY KEY,
    `app_menu_id`  bigint(20),
    `app_uri_id`   bigint(20),
    `rbac_menu_id` bigint(20),
    `name`         varchar(64),
    `code`         varchar(255),
    `label`        varchar(64),
    `url`          varchar(64),
    `config_json`  varchar(64),
    `app_id`       varchar(64),
    `descr`        varchar(64),
    `method_name`  varchar(19),
    `ver`          int(11),
    `shared`       tinyint(4) DEFAULT 0,
    `state`        tinyint(4) DEFAULT 0,
    `deleted`      tinyint(4) DEFAULT 0,
    `dt`           datetime,
    `account_id`   bigint(20)
);
DROP TABLE IF EXISTS `statistic_entry`;
CREATE TABLE statistic_entry
(
    `statistic_entry_id` bigint(19) NOT NULL PRIMARY KEY,
    `counter`            bigint(20) DEFAULT 0,
    `from_no`            varchar(64),
    `to_no`              varchar(64),
    `app_id`             varchar(64),
    `material_type`      bigint(20),
    `year`               int(11),
    `month`              int(11),
    `day`                int(11),
    `interval`           int(11),
    `period`             int(11),
    `work_order_type`    int(11),
    `uploaded`           decimal(10, 0),
    `ver`                int(10),
    `dt`                 datetime
);
DROP TABLE IF EXISTS `statistic_setting`;
CREATE TABLE statistic_setting
(
    `statistic_setting_id` bigint(19) NOT NULL PRIMARY KEY,
    `account_id`           bigint(19),
    `setting_key`          varchar(64),
    `config_json`          varchar(1024),
    `app_id`               varchar(64),
    `ver`                  int(10),
    `deleted`              int(10),
    `dt`                   date
);
DROP TABLE IF EXISTS `statistic_workload`;
CREATE TABLE statistic_workload
(
    `statistic_workload_id` bigint(19) NOT NULL PRIMARY KEY,
    `work_order_id`         bigint(19),
    `counter`               bigint(20) DEFAULT 0,
    `material_type`         bigint(20),
    `ws_tree_id`            bigint(20),
    `eqpt_type`             varchar(255),
    `eqpt_no`               varchar(64),
    `app_id`                varchar(64),
    `work_order_type`       int(11),
    `year`                  int(11),
    `month`                 int(11),
    `day`                   int(11),
    `interval`              int(11),
    `period`                int(11),
    `order_type_count`      int(11)    DEFAULT 0,
    `uploaded`              decimal(10, 0),
    `moved`                 decimal(10, 0),
    `unloaded`              decimal(10, 0),
    `flat_moved`            decimal(10, 0),
    `truck_loaded`          decimal(10, 0),
    `remnant`               decimal(10, 0),
    `ver`                   int(11)    DEFAULT 0,
    `dt`                    datetime
);
DROP TABLE IF EXISTS `task_notice`;
CREATE TABLE task_notice
(
    `task_notice_id`   bigint(19) NOT NULL PRIMARY KEY,
    `auth_json`        varchar(64),
    `app_id`           varchar(64),
    `from_func`        varchar(64),
    `bean_class`       varchar(64),
    `notice_func`      varchar(64),
    `notice_protocol`  varchar(64),
    `return_json`      varchar(64),
    `subject_name`     varchar(64),
    `notice_signature` varchar(64),
    `request_json`     text,
    `subject_func`     varchar(64),
    `ver`              int(2),
    `deleted`          tinyint(4) NOT NULL DEFAULT 0,
    `dt`               datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `task_order`;
CREATE TABLE task_order
(
    `task_order_id`  bigint(20) NOT NULL PRIMARY KEY,
    `account_id`     bigint(20),
    `order_name`     varchar(64),
    `order_source`   varchar(64),
    `order_from`     varchar(64),
    `app_id`         varchar(64),
    `order_priority` int(11),
    `order_type`     int(11),
    `order_state`    int(11),
    `ver`            int(11),
    `deleted`        tinyint(1),
    `dt`             datetime DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `task_participator`;
CREATE TABLE task_participator
(
    `task_participator_id` bigint(20) NOT NULL PRIMARY KEY,
    `task_tree_id`         bigint(20),
    `principal_id`         bigint(20),
    `participator_type`    int(11),
    `ver`                  int(11),
    `deleted`              tinyint(1) NOT NULL DEFAULT 0,
    `dt`                   datetime            DEFAULT current_timestamp(),
    `account_id`           bigint(20)
);
DROP TABLE IF EXISTS `task_progress`;
CREATE TABLE task_progress
(
    `task_progress_id` bigint(20) NOT NULL PRIMARY KEY,
    `task_id`          bigint(20),
    `progress_type`    int(11),
    `progress`         decimal(11, 2),
    `ver`              int(11),
    `deleted`          tinyint(1) NOT NULL DEFAULT 0,
    `dt`               datetime            DEFAULT current_timestamp(),
    `account_id`       bigint(20)
);
DROP TABLE IF EXISTS `task_setting`;
CREATE TABLE task_setting
(
    `task_setting_id` bigint(19) NOT NULL PRIMARY KEY,
    `account_id`      bigint(19),
    `setting_key`     varchar(64),
    `config_json`     text,
    `app_id`          varchar(64),
    `ver`             int(10),
    `deleted`         int(11) DEFAULT 0,
    `dt`              date
);
DROP TABLE IF EXISTS `task_statistics`;
CREATE TABLE task_statistics
(
    `task_statistics_id` bigint(19) NOT NULL PRIMARY KEY,
    `work_order_id`      bigint(19),
    `counter`            bigint(20) DEFAULT 0,
    `work_order_type`    int(10),
    `material_type`      int(10),
    `eqpt_type`          varchar(255),
    `eqpt_no`            varchar(64),
    `app_id`             varchar(64),
    `month`              int(11),
    `day`                int(11),
    `interval`           int(11),
    `period`             int(11),
    `uploaded`           decimal(10, 0),
    `moved`              decimal(10, 0),
    `unloaded`           decimal(10, 0),
    `remnant`            decimal(10, 0),
    `ver`                int(10),
    `dt`                 datetime
);
DROP TABLE IF EXISTS `task_time`;
CREATE TABLE task_time
(
    `task_time_id`   bigint(20) NOT NULL PRIMARY KEY,
    `task_id`        bigint(20),
    `task_type`      int(11),
    `read_date`      datetime,
    `active_date`    datetime,
    `allot_date`     datetime,
    `exec_date`      datetime,
    `finish_date`    datetime,
    `cancle_date`    datetime,
    `end_time`       datetime,
    `exception_date` datetime,
    `ver`            int(11),
    `deleted`        tinyint(1) NOT NULL DEFAULT 0,
    `dt`             datetime            DEFAULT current_timestamp(),
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `task_tree`;
CREATE TABLE task_tree
(
    `task_tree_id`  bigint(20) NOT NULL PRIMARY KEY,
    `pid`           bigint(20),
    `task_order_id` bigint(20),
    `ws_fun_id`     bigint(20),
    `group_id`      bigint(20),
    `owner_id`      bigint(20),
    `task_code`     varchar(64),
    `id_path`       varchar(64),
    `name_path`     varchar(64),
    `task_name`     varchar(64),
    `task_descr`    varchar(64),
    `percent`       decimal(11, 2),
    `task_type`     int(11),
    `task_priority` int(11),
    `task_state`    int(11),
    `seq`           int(11),
    `ver`           int(11),
    `deleted`       tinyint(1) NOT NULL DEFAULT 0,
    `dt`            datetime            DEFAULT current_timestamp(),
    `account_id`    bigint(20)
);
DROP TABLE IF EXISTS `task_vehicle`;
CREATE TABLE task_vehicle
(
    `task_vehicle_id`   bigint(19) NOT NULL PRIMARY KEY,
    `material_categ_id` bigint(19),
    `account_id`        bigint(19),
    `task_order_id`     bigint(20),
    `ws_tree_id`        bigint(19),
    `park_no`           varchar(64),
    `vehicle_no`        varchar(64),
    `app_id`            varchar(64),
    `weight`            decimal(11, 2),
    `start_time`        datetime,
    `end_time`          datetime,
    `work_time`         datetime   NOT NULL DEFAULT current_timestamp(),
    `ver`               int(2),
    `state`             int(11)             DEFAULT 0,
    `deleted`           tinyint(4) NOT NULL DEFAULT 0,
    `dt`                datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `task_vehicle_plan`;
CREATE TABLE task_vehicle_plan
(
    `task_vehicle_plan_id`   bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `account_id`             bigint(20),
    `plan_date`              date,
    `vehicle_no`             varchar(20),
    `contact_person`         varchar(100),
    `contact_phone`          varchar(20),
    `load_material_categ`    varchar(255),
    `app_id`                 varchar(64),
    `load_material_categ_id` bigint(20),
    `planned_weight`         decimal(10, 2),
    `actual_weight`          decimal(10, 2),
    `plan_status`            int(11)    DEFAULT 0,
    `remarks`                text,
    `ver`                    int(11)    DEFAULT 0,
    `deleted`                tinyint(1) DEFAULT 0,
    `dt`                     date
);
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE user_account
(
    `user_account_id`  bigint(20) NOT NULL PRIMARY KEY,
    `user_instance_id` bigint(20),
    `app_id`           varchar(64),
    `state`            int(11),
    `ver`              int(11),
    `deleted`          tinyint(4),
    `dt`               datetime,
    `account_id`       bigint(20)
);
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE user_address
(
    `user_address_id` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance`   bigint(20),
    `country_no`      varchar(64),
    `country_name`    varchar(64),
    `country_code`    varchar(64),
    `province_no`     varchar(64),
    `province_name`   varchar(64),
    `province_code`   varchar(64),
    `city_no`         varchar(64),
    `city_name`       varchar(64),
    `city_code`       varchar(64),
    `address`         varchar(64),
    `street_no`       varchar(64),
    `street_name`     varchar(64),
    `district_no`     varchar(64),
    `district_name`   varchar(64),
    `zip_code`        varchar(64),
    `bizline`         varchar(64),
    `typ`             int(11),
    `state`           int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `user_company`;
CREATE TABLE user_company
(
    `user_company_id` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance`   bigint(20),
    `user_address_id` bigint(20),
    `company_name`    varchar(64),
    `company_code`    varchar(64),
    `cert_no`         varchar(64),
    `license_no`      varchar(64),
    `legal_person`    varchar(64),
    `biz_scope`       varchar(64),
    `license_pic`     varchar(64),
    `merchant_no`     varchar(64),
    `reg_fund`        decimal(11, 2),
    `company_typ`     int(11),
    `state`           int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `user_contact`;
CREATE TABLE user_contact
(
    `user_contact_id` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance`   bigint(20),
    `contact`         varchar(64),
    `contact_num`     varchar(64),
    `contact_typ`     int(11),
    `sorted`          int(11),
    `self`            int(11),
    `state`           int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `user_dwelling`;
CREATE TABLE user_dwelling
(
    `user_dwelling` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance` bigint(20),
    `address`       varchar(64),
    `telephone`     varchar(64),
    `zip_code`      varchar(64),
    `acreage`       varchar(64),
    `house_type`    varchar(64),
    `state`         int(11),
    `ver`           int(11),
    `deleted`       tinyint(4),
    `dt`            datetime,
    `account_id`    bigint(20)
);
DROP TABLE IF EXISTS `user_education`;
CREATE TABLE user_education
(
    `user_education_id` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance`     bigint(20),
    `degree`            varchar(64),
    `org_name`          varchar(64),
    `start_time`        datetime,
    `end_time`          datetime,
    `state`             int(11),
    `ver`               int(11),
    `deleted`           tinyint(4),
    `dt`                datetime,
    `account_id`        bigint(20)
);
DROP TABLE IF EXISTS `user_entity`;
CREATE TABLE user_entity
(
    `id`         int(11) NOT NULL PRIMARY KEY,
    `user_name`  varchar(255),
    `account_id` bigint(20)
);
DROP TABLE IF EXISTS `user_extinfo`;
CREATE TABLE user_extinfo
(
    `user_extinfo_id` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance`   bigint(20),
    `character`       varchar(64),
    `features`        varchar(64),
    `intro`           varchar(64),
    `married`         tinyint(4),
    `state`           int(11),
    `ver`             int(11),
    `deleted`         tinyint(4),
    `dt`              datetime,
    `account_id`      bigint(20)
);
DROP TABLE IF EXISTS `user_family`;
CREATE TABLE user_family
(
    `user_family_id` bigint(20) NOT NULL PRIMARY KEY,
    `user_instance`  bigint(20),
    `member_id`      bigint(20),
    `relation`       varchar(64),
    `build_time`     datetime,
    `householder`    tinyint(4),
    `state`          int(11),
    `ver`            int(11),
    `deleted`        tinyint(4),
    `dt`             datetime,
    `account_id`     bigint(20)
);
DROP TABLE IF EXISTS `user_instance`;
CREATE TABLE user_instance
(
    `user_instance_id` bigint(20) NOT NULL PRIMARY KEY,
    `group_id`         bigint(20),
    `owner_id`         bigint(20),
    `user_id`          varchar(64),
    `user_name`        varchar(64),
    `id_no`            varchar(64),
    `age`              int(11),
    `nation`           varchar(64),
    `sign_org`         varchar(64),
    `domicile`         varchar(64),
    `birthday`         datetime,
    `indate`           datetime,
    `expdate`          datetime,
    `sex`              int(11),
    `state`            int(11),
    `ver`              int(11),
    `deleted`          tinyint(4),
    `dt`               datetime,
    `account_id`       bigint(20)
);
DROP TABLE IF EXISTS `user_job`;
CREATE TABLE user_job
(
    `user_job_id`   bigint(20) NOT NULL PRIMARY KEY,
    `user_instance` bigint(20),
    `profession`    varchar(64),
    `org_name`      varchar(64),
    `unit`          varchar(64),
    `start_time`    datetime,
    `end_time`      datetime,
    `duration`      int(11),
    `state`         int(11),
    `ver`           int(11),
    `deleted`       tinyint(4),
    `dt`            datetime,
    `account_id`    bigint(20)
);
DROP TABLE IF EXISTS `vac_barrier`;
CREATE TABLE vac_barrier
(
    `vac_barrier_id` bigint(19) NOT NULL PRIMARY KEY,
    `barrier_no`     varchar(64),
    `state`          int(2),
    `ver`            int(2),
    `deleted`        tinyint(4) NOT NULL DEFAULT 0,
    `dt`             datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_func`;
CREATE TABLE vac_func
(
    `vac_func_id` bigint(19) NOT NULL PRIMARY KEY,
    `vac_sdk_id`  bigint(19),
    `func_code`   varchar(64),
    `func_name`   varchar(64),
    `descr`       varchar(64),
    `input_json`  varchar(1024),
    `output_json` varchar(1024),
    `ver`         int(2),
    `mock`        tinyint(4),
    `deleted`     tinyint(4) NOT NULL DEFAULT 0,
    `dt`          datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_init`;
CREATE TABLE vac_init
(
    `vac_init_id`      bigint(19) NOT NULL PRIMARY KEY,
    `account_id`       bigint(19),
    `app_id`           varchar(64),
    `channel`          varchar(64),
    `eqpt_no`          varchar(64),
    `properties_class` varchar(64),
    `init_class`       varchar(64),
    `config_json`      varchar(1024),
    `seq`              int(2),
    `state`            int(11),
    `ver`              int(2),
    `deleted`          tinyint(4) NOT NULL DEFAULT 0,
    `dt`               datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_manage`;
CREATE TABLE vac_manage
(
    `vac_manage_id` bigint(20) NOT NULL PRIMARY KEY,
    `account_id`    bigint(19),
    `app_id`        varchar(64),
    `channel`       varchar(64),
    `eqpt_no`       varchar(64),
    `func`          varchar(64),
    `bean_class`    varchar(128),
    `config_json`   varchar(64),
    `seq`           int(11),
    `state`         int(11),
    `ver`           int(2),
    `mock`          tinyint(1),
    `deleted`       tinyint(4) NOT NULL DEFAULT 0,
    `dt`            datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_mock`;
CREATE TABLE vac_mock
(
    `vac_mock_id` bigint(19) NOT NULL PRIMARY KEY,
    `vac_func_id` bigint(19),
    `account_id`  bigint(19),
    `app_id`      varchar(64),
    `channel`     varchar(64),
    `func_name`   varchar(64),
    `bean_class`  varchar(64),
    `ver`         int(2),
    `deleted`     tinyint(4) NOT NULL DEFAULT 0,
    `dt`          datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_notice`;
CREATE TABLE vac_notice
(
    `vac_notice_id`   bigint(19) NOT NULL PRIMARY KEY,
    `app_id`          varchar(64),
    `eqpt_no`         varchar(255),
    `from_class`      varchar(64),
    `from_func`       varchar(64),
    `auth_json`       varchar(64),
    `notice_protocol` varchar(64),
    `return_json`     varchar(64),
    `subject_name`    varchar(64),
    `subject_func`    varchar(64),
    `request_json`    text,
    `descr`           varchar(512),
    `ver`             int(2),
    `deleted`         tinyint(4) NOT NULL DEFAULT 0,
    `dt`              datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_park`;
CREATE TABLE vac_park
(
    `vac_park_id` bigint(19) NOT NULL PRIMARY KEY,
    `vehicle_no`  varchar(64),
    `stall_no`    varchar(64),
    `ver`         int(2),
    `deleted`     tinyint(4) NOT NULL DEFAULT 0,
    `dt`          datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_record`;
CREATE TABLE vac_record
(
    `vac_record_id`  bigint(19) NOT NULL PRIMARY KEY,
    `vac_vehicle_id` bigint(19),
    `vac_barrier_id` bigint(19),
    `vac_stall_id`   bigint(19),
    `vehicle_no`     varchar(64),
    `barrier_no`     varchar(64),
    `stall_no`       varchar(64),
    `enter_time`     datetime   NOT NULL DEFAULT current_timestamp(),
    `level_time`     datetime   NOT NULL DEFAULT current_timestamp(),
    `ver`            int(2),
    `deleted`        tinyint(4) NOT NULL DEFAULT 0,
    `dt`             datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_sdk`;
CREATE TABLE vac_sdk
(
    `vac_sdk_id` bigint(19) NOT NULL PRIMARY KEY,
    `descr`      varchar(64),
    `channel`    varchar(64),
    `bean_name`  varchar(64),
    `bean_class` varchar(64),
    `bean_role`  int(11),
    `ver`        int(2),
    `deleted`    tinyint(4) NOT NULL DEFAULT 0,
    `dt`         datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_stall`;
CREATE TABLE vac_stall
(
    `vac_stall_id` bigint(19) NOT NULL PRIMARY KEY,
    `stall_no`     varchar(64),
    `usage`        int(2),
    `ver`          int(2),
    `deleted`      tinyint(4) NOT NULL DEFAULT 0,
    `dt`           datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_validity`;
CREATE TABLE vac_validity
(
    `vac_validity_id` bigint(19) NOT NULL PRIMARY KEY,
    `vac_vehicle_id`  bigint(19),
    `vehicle_no`      varchar(64),
    `start_time`      datetime   NOT NULL DEFAULT current_timestamp(),
    `stop_time`       datetime   NOT NULL DEFAULT current_timestamp(),
    `ver`             int(2),
    `deleted`         tinyint(4) NOT NULL DEFAULT 0,
    `dt`              datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `vac_vehicle`;
CREATE TABLE vac_vehicle
(
    `vac_vehicle_id` bigint(19) NOT NULL PRIMARY KEY,
    `vehicle_no`     varchar(64),
    `vehicle_weight` varchar(64),
    `load_weight`    varchar(64),
    `lenght`         decimal(11, 2),
    `wide`           decimal(11, 2),
    `height`         decimal(11, 2),
    `plate_color`    int(2),
    `plate_type`     int(2),
    `state`          int(2),
    `ver`            int(2),
    `deleted`        tinyint(4) NOT NULL DEFAULT 0,
    `dt`             datetime   NOT NULL DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `ws_area_relation`;
CREATE TABLE ws_area_relation
(
    `ws_area_relation_id` bigint(20)   NOT NULL PRIMARY KEY,
    `area_id`             bigint(20)   NOT NULL,
    `relation_area_id`    bigint(20)   NOT NULL,
    `app_id`              varchar(255) NOT NULL,
    `dt`                  datetime     NOT NULL,
    `account_id`          bigint(20)
);
DROP TABLE IF EXISTS `ws_eqpt`;
CREATE TABLE ws_eqpt
(
    `ws_eqpt_id`       bigint(20) NOT NULL PRIMARY KEY,
    `account_id`       bigint(20),
    `ws_factory_id`    bigint(20),
    `ws_tree_id`       bigint(20),
    `eqpt_instance_id` bigint(20),
    `app_id`           varchar(255),
    `state`            int(11),
    `eqpt_no`          varchar(255),
    `eqpt_categ_code`  varchar(50),
    `ws_no`            varchar(20),
    `ver`              int(11),
    `deleted`          tinyint(1) NOT NULL DEFAULT 0,
    `dt`               datetime            DEFAULT current_timestamp()
);
DROP TABLE IF EXISTS `ws_factory`;
CREATE TABLE ws_factory
(
    `ws_factory_id` bigint(20) NOT NULL PRIMARY KEY,
    `factory_name`  varchar(64),
    `address`       varchar(64),
    `descr`         varchar(64),
    `ver`           int(11),
    `deleted`       tinyint(1) NOT NULL DEFAULT 0,
    `dt`            datetime            DEFAULT current_timestamp(),
    `account_id`    bigint(20),
    `app_id`        varchar(255)
);
DROP TABLE IF EXISTS `ws_flow`;
CREATE TABLE ws_flow
(
    `ws_flow_id`    bigint(20) NOT NULL PRIMARY KEY,
    `ws_factory_id` bigint(20),
    `factory_name`  varchar(64),
    `flow_name`     varchar(64),
    `ws_fun_ids`    varchar(64),
    `ver`           int(11),
    `deleted`       tinyint(1) NOT NULL DEFAULT 0,
    `dt`            datetime            DEFAULT current_timestamp(),
    `account_id`    bigint(20),
    `app_id`        varchar(255)
);
DROP TABLE IF EXISTS `ws_fun`;
CREATE TABLE ws_fun
(
    `ws_fun_id`     bigint(20) NOT NULL PRIMARY KEY,
    `ws_factory_id` bigint(20),
    `fun_name`      varchar(64),
    `fun_code`      varchar(64),
    `descr`         varchar(64),
    `ver`           int(11),
    `deleted`       tinyint(1) NOT NULL DEFAULT 0,
    `dt`            datetime            DEFAULT current_timestamp(),
    `account_id`    bigint(20),
    `app_id`        varchar(255)
);
DROP TABLE IF EXISTS `ws_material`;
CREATE TABLE ws_material
(
    `ws_material_id`       bigint(20) NOT NULL PRIMARY KEY,
    `ws_factory_id`        bigint(20),
    `ws_tree_id`           bigint(20),
    `material_instance_id` bigint(20),
    `factory_name`         varchar(64),
    `material_name`        varchar(64),
    `space_type`           int(11),
    `ver`                  int(11),
    `deleted`              tinyint(1) NOT NULL DEFAULT 0,
    `dt`                   datetime            DEFAULT current_timestamp(),
    `account_id`           bigint(20),
    `app_id`               varchar(255),
    `material_categ_id`    bigint(20)
);
DROP TABLE IF EXISTS `ws_org`;
CREATE TABLE ws_org
(
    `ws_org_id`      bigint(20) NOT NULL PRIMARY KEY,
    `ws_factory_id`  bigint(20),
    `account_org_id` bigint(20),
    `ver`            int(11),
    `deleted`        tinyint(1) NOT NULL DEFAULT 0,
    `dt`             datetime            DEFAULT current_timestamp(),
    `account_id`     bigint(20),
    `app_id`         varchar(255)
);
DROP TABLE IF EXISTS `ws_position`;
CREATE TABLE ws_position
(
    `ws_position_id` bigint(20) NOT NULL PRIMARY KEY,
    `ws_tree_id`     bigint(20),
    `ws_factory_id`  bigint(20),
    `ws_fun_id`      bigint(20),
    `ws_no`          varchar(64),
    `position_no`    varchar(64),
    `position_name`  varchar(64),
    `position_code`  varchar(64),
    `state`          int(11)             DEFAULT 0,
    `ver`            int(11),
    `deleted`        tinyint(1) NOT NULL DEFAULT 0,
    `dt`             datetime            DEFAULT current_timestamp(),
    `account_id`     bigint(20),
    `x`              varchar(64),
    `y`              varchar(64),
    `z`              varchar(64),
    `ws_layer`       varchar(11),
    `ws_row`         varchar(11),
    `ws_column`      varchar(11),
    `app_id`         varchar(255),
    `data_json`      text
);
DROP TABLE IF EXISTS `ws_setting`;
CREATE TABLE ws_setting
(
    `ws_setting_id` bigint(20) NOT NULL PRIMARY KEY,
    `domain_id`     bigint(20),
    `domain_name`   varchar(64),
    `key`           varchar(64),
    `descr`         varchar(64),
    `json_value`    varchar(64),
    `ver`           int(11),
    `deleted`       tinyint(1) NOT NULL DEFAULT 0,
    `dt`            datetime            DEFAULT current_timestamp(),
    `account_id`    bigint(20),
    `app_id`        varchar(255)
);
DROP TABLE IF EXISTS `ws_tree`;
CREATE TABLE ws_tree
(
    `ws_tree_id`    bigint(20) NOT NULL PRIMARY KEY,
    `pid`           bigint(19) unsigned zerofill,
    `ws_factory_id` bigint(20),
    `group_id`      bigint(20),
    `owner_id`      bigint(20),
    `id_path`       varchar(64),
    `name_path`     varchar(64),
    `ws_name`       varchar(64),
    `ws_no`         varchar(64),
    `state`         int(11),
    `space_type`    int(11),
    `ver`           int(11),
    `deleted`       tinyint(1) NOT NULL DEFAULT 0,
    `dt`            datetime            DEFAULT current_timestamp(),
    `account_id`    bigint(20),
    `app_id`        varchar(255)
);
DROP TABLE IF EXISTS `ws_vehicle`;
CREATE TABLE ws_vehicle
(
    `ws_vehicle_id`    bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `ws_factory_id`    bigint(20),
    `eqpt_instance_id` bigint(20),
    `ws_tree_id`       bigint(20),
    `account_id`       bigint(20),
    `app_id`           varchar(255),
    `vehicle_no`       varchar(255),
    `ver`              int(11)    DEFAULT 0,
    `deleted`          tinyint(1) DEFAULT 0,
    `dt`               datetime
);
DROP TABLE IF EXISTS `ws_zone_vehicle`;
CREATE TABLE ws_zone_vehicle
(
    `ws_zone_vehicle_id` bigint(20) NOT NULL PRIMARY KEY,
    `ws_no`              varchar(30),
    `vehicle_no`         varchar(30),
    `account_id`         bigint(20),
    `app_id`             varchar(255),
    `ver`                int(11)    DEFAULT 0,
    `deleted`            tinyint(1) DEFAULT 0,
    `dt`                 datetime,
    `state`              int(11)    DEFAULT 0,
    `occupy`             int(11)    DEFAULT 0,
    `stall_no`           varchar(30),
    `stall_name`         varchar(255),
    `ws_no_descr`        varchar(255),
    `stall_type`         int(11),
    `descr`              varchar(255)
);
