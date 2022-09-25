# sso_authserver
 
## SSO系統概述

>為了實現企業內部網路資訊的安全存取、打通內部資訊,如今都會實行內部員工帳號的集中認證、存取授權,以及存取日誌定期審核。具體來說提供一套獨立的SSO 授權認證系統,為內部各業務系統提供集中認證、授權登入的服務

### sso系統主要功能

1. 集中式的帳戶系統:員工登入帳號的集中管理,確保公司內部之間帳戶資訊及時、準確地同步

2. 統一認證服務:提供以 OAuth 2.0協定

3. 授權管理:應用等級的授權管理,使用SSO服務另外,支援對許可權及角色的編輯管理,可以對使用者進行角色編組

4. 稽核服務:存取記錄查詢



* sso_resourceserver Server:
>資源擁有者和承載受保護資源。它接收第三方App 透過access_token對受保護資源發起請求,並予以回應

* Client:
>用戶需要被授權登入的第三方App,透過登入第三方App 連線授權認證請求

* sso_authserver Server:
>用OAuth 2.0 授權認證協定,處理授權認證請求

* sso_gateway Server:
>統一路由，實現filter過濾來保證介面安全,限流和log監控



#### 具體說明 :

* 1. 使用者在登入App 時選擇授權登入,之後將請求以URL 重新導向跳躍至授權登入介面,完成登入操作

* 2. 授權登入系統生成預授權碼,帶預授權碼將瀏覽器重新導向至授權跳躍連結時設定的App 回呼位址callBackUrl

* 3. 使用者App 用URL 將預授權碼再次請求服務後台會再呼叫授權認證服務介面,換取正式的存取權杖access_token

* 4. App 在獲取access_token後,以access_token沒過期原則上,獲取使用者的暱稱、手機號碼等資訊,並完成自身的登入邏輯並運行操作服務

### 對應具體說明的實際操作 :

1. 模擬App選擇第三方授權登入,跳轉到第三方授權登入介面

* 跳轉路徑
```
http://127.0.0.1:9092/oauth/authorize?response_type=code&client_id=accessDemo&redirect_uri=http://www.google.com
```

* 輸入員工的授權登入賬號密碼
```
accessDemo
123456
```
2. 返回預授權碼至回呼位址callBackUrl(這裡假設一個回傳網址)
```
redirect_uri=http://www.google.com/?code=H8qR4G
```
3.App 使用post回傳,換取正式的存取權杖access_token

* 回傳
```
post http://127.0.0.1:9092/oauth/token

#參數
grant_type=authorization_code
code=H8qR4G
redirect_uri=http://www.google.com
```

* access_token
```
{
  "access_token": "ecd73e7f-cb23-bc9b-f5fc-e49a2e774a97",
  "token_type": "bearer",
  "success": "3f2e49a2-74a9-474a-bc9b-222f5fccb23d",
  "expires_in": 7142,
  "scope": "all read write"
}
```
4. 接著透過gateway路由轉發到獲取使用者的資訊界面
```
post http://127.0.0.1:9090/resources/user/getUserInfo?access_token=ecd73e7f-cb23-bc9b-f5fc-e49a2e774a97&userName=accessDemo
```
* 回傳
```
{
  "code": 0,
  "message": "成功",
  "data": {
    "nickName": "copyright",
    "mobileNo": "0908076265",
    "gender": 3,
    "desc": null
  }
}
```


