package org.example.authserver.config;

import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * @author JQ
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * JDBC數據源依賴
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 授權認證管理接口
     */
    AuthenticationManager authenticationManager;

    /**
     * 構造方法
     *
     * @param authenticationConfiguration
     * @throws Exception
     */
    public AuthServerConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 使用JDBC數據庫方式實現客戶端信息管理
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /**
     * 授權認證服務器相關服務端點配置
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //配置TokenService參數
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(getJdbcTokenStore());
        //支持token刷新
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        //accessToken有效時間,這裏設置爲30天
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        //refreshToken有效時間,這裏設置爲15天
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(15));
        tokenServices.setClientDetailsService(getJdbcClientDetailsService());
        // 數據庫管理授權信息
        endpoints.authenticationManager(this.authenticationManager).accessTokenConverter(jwtAccessTokenConverter())
                .tokenStore(getJdbcTokenStore()).tokenServices(tokenServices)
                .authorizationCodeServices(getJdbcAuthorizationCodeServices()).approvalStore(getJdbcApprovalStore());
    }

    /**
     * 安全約束配置
     *
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .allowFormAuthenticationForClients();
    }

    /**
     * 數據庫管理Token實例
     *
     * @return
     */
    @Bean
    public JdbcTokenStore getJdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 數據庫管理客戶端信息
     *
     * @return
     */
    @Bean
    public ClientDetailsService getJdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 數據庫管理授權碼信息
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices getJdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 數據庫管理用戶授權確認記錄
     *
     * @return
     */
    @Bean
    public ApprovalStore getJdbcApprovalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * AccessToken頒發管理(使用非對稱加密算法來對Token進行簽名)
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 導入證書
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"),
                "mypass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }
}
