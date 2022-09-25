#授權客戶端基本信息表
create table oauth_client_details (
  client_id varchar(256) primary key comment '用於標識客戶端,類似於appKey',
  resource_ids varchar(256) comment '客戶端能訪問的資源ID集合,以逗號分割,例如order-resource,pay-resource',
  client_secret varchar(256) comment '客戶端訪問密鑰類似於appSecret,必須要有前綴代表加密方式,例如：{bcrypt}10gY/Hauph1tqvVWiH4atxteSH8sRX03IDXRIQi03DVTFGzKfz8ZtGi',
  scope varchar(256) comment '用於指定客戶端的權限範圍,如讀寫權限、移動端或web端等,例如：read,write/web,mobile',
  authorized_grant_types varchar(256) comment '可選值,如授權碼模式:authorization_code；密碼模式:password；刷新token:refresh_token；隱式模式:implicit；客戶端模式:client_credentials,支持多種方式以逗號分隔,例如：password,refresh_token',
  web_server_redirect_uri varchar(256) comment '客戶端重定向URL,authorization_code和implicit模式時需要該值進行校驗,例如:http://baidu.com',
  authorities varchar(256) comment '可爲空,指定用戶的權限範圍,如果授權過程需要用戶登錄,該字段不生效,implicit和client_credentials模式時需要；例如：ROLE_ADMIN,ROLE_USER',
  access_token_validity integer comment '可爲空,設置access_token的有效時間(秒),默認爲12小時,例如：3600',
  refresh_token_validity integer comment '可爲空,設置refresh_token有效期(秒）,默認30天,例如7200',
  additional_information varchar(4096) comment '可爲空,值必須是JSON格式,例如{"key", "value"}',
  autoapprove varchar(256) comment '默認false,適用於authorization_code模式,設置用戶是否自動approval操作,設置true跳過用戶確認授權操作頁面,直接跳到redirect_uri'
);

#客戶端Token信息表
create table oauth_client_token (
  token_id varchar(256) comment '從服務器端獲取到的access_token的值',
  token blob comment '這是一個二進制的字段, 存儲的數據是OAuth2AccessToken.java對象序列化後的二進制數據',
  authentication_id varchar(256) primary key comment '該字段具有唯一性, 是根據當前的username(如果有),client_id與scope通過MD5加密生成的(參考->DefaultClientKeyGenerator.java類)',
  user_name varchar(256) comment '登錄時的用戶名',
  client_id varchar(256) comment '客戶端ID'
);

#授權訪問Token信息表
create table oauth_access_token (
  token_id varchar(256) comment '該字段的值是將access_token的值通過MD5加密後存儲的',
  token blob comment '存儲將OAuth2AccessToken.java對象序列化後的二進制數據, 是真實的AccessToken的數據值',
  authentication_id varchar(256) primary key comment '該字段具有唯一性, 是根據當前的username(如果有),client_id與scope通過MD5加密生成的(參考->DefaultClientKeyGenerator.java類)',
  user_name varchar(256) comment '登錄時的用戶名,若客戶端沒有用戶名(如grant_type="client_credentials"),則該值等於client_id',
  client_id varchar(256) comment '客戶端ID',
  authentication blob comment '存儲將OAuth2Authentication.java對象序列化後的二進制數據',
  refresh_token varchar(256) comment '該字段的值是將refresh_token的值通過MD5加密後存儲的'
);

#授權刷新Token信息表
create table oauth_refresh_token (
  token_id varchar(256) comment '該字段的值是將refresh_token的值通過MD5加密後存儲的',
  token blob comment '存儲將OAuth2RefreshToken.java對象序列化後的二進制數據',
  authentication binary comment '存儲將OAuth2Authentication.java對象序列化後的二進制數據'
);

#授權碼信息表
create table oauth_code (
  code varchar(256) comment '存儲服務端系統生成的code的值(未加密)',
  authentication blob comment '存儲將AuthorizationRequestHolder.java對象序列化後的二進制數據'
);

#用戶授權歷史表
create table oauth_approvals (
  userId varchar(256) comment '授權用戶ID',
  clientId varchar(256) comment '客戶端ID',
  scope varchar(256) comment '授權訪問',
  status varchar(10) comment '授權狀態,例如：APPROVED',
  expiresAt timestamp comment '授權失效時間',
  lastModifiedAt timestamp default current_timestamp comment '最後修改時間'
);

#生成供資源服務器使用的受信任Client配置
insert into `auth`.`oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) values ('resourceclient', null, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.google.com', 'role_trusted_client', 7200, 7200, null, 'true');

#生成接入方測試Client配置信息
insert intO `auth`.`oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('accessDemo', NULL, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.google.com', 'ROLE_TRUSTED_CLIENT', 7200, 7200, NULL, 'true');