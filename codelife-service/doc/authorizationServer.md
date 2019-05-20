授权服务主要完成以下四个核心功能：
- 客户端授权关系管理；
- 接收用户或客户端登录请求，生成accessToken
- Token存储
- Token校验

本文将从源码层面分析授权服务如何实现上述四个功能。
关于Spring Oauth2的一些基础概念及示例，可参考：http://liumoran.cn/topic/myTopics/1

# 1. 概述
授权服务包含有以下几个关键类：
- EnableAuthorizationServer
  启用授权服务，并引入AuthorizationServerEndpointsConfiguration及AuthorizationServerSecurityConfiguration两个配置类；
- AuthorizationServerEndpointsConfiguration/AuthorizationServerEndpointsConfigurer
  授权服务Endpoints配置类；
- AuthorizationServerSecurityConfiguration/AuthorizationServerSecurityConfigurer
  授权服务访问授权配置类；
- AuthorizationServerConfigurerAdapter(AuthorizationServerConfigurer)
  授权服务配置适配器，开发人员可定义其实现类来完成对授权服务的各项配置；
- ClientDetailsServiceConfigurer
  客户端配置类；

其中比较费解的是AuthorizationServerEndpointsConfiguration/AuthorizationServerEndpointsConfigurer
与AuthorizationServerSecurityConfiguration/AuthorizationServerSecurityConfigurer。
简单来说Configuration类可以看成是Configurer的代理类和配置类；而Configurer是存储配置项的类。

这几个类之间的关系如下所示：


# 2. AuthorizationServerEndpointsConfiguration
它完成以下功能：
- 配置AuthorizationServerEndpointsConfigurer：通过init方法完成AuthorizationServerEndpointsConfigurer的创建与配置。
- 创建Endpoint：通过Bean注解，往Spring容器中托管各个Endpoint（如用于处理/oauth/token请求的TokenEndpoint、用于处理/oauth/authorize请求的AuthorizationEndpoint等；
  Endpoint可以看成是控制器，即平常我们所定义的Controller。

## 2.1 AuthorizationServerEndpointsConfigurer的创建与配置
init方法定义如下：
```java
@Autowired
private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

@PostConstruct
public void init() {
    for (AuthorizationServerConfigurer configurer : configurers) {
        try {
            configurer.configure(endpoints);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot configure enpdoints", e);
        }
    }
    endpoints.setClientDetailsService(clientDetailsService);
}
```
看到它就是使用容器中的所有类型为AuthorizationServerConfigurer的Bean，然后调用其configure方法对endpoints进行配置。
因此如果我们在应用中定义了实现AuthorizationServerConfigurer接口或者继承AuthorizationServerConfigurerAdapter的配置类，
并且这个配置类通过Configuration注解或者其它方式被注入到Spring中，那么我们在方法configure(AuthorizationServerEndpointsConfigurer)中
定义的代码将会被执行。
通过这种方式我们可以对授权服务所使用的TokenStore、TokenService等进行配置，见4.3节内容。具体配置项内容可看AuthorizationServerEndpointsConfigurer源码。

## 2.2 创建Endpoints
AuthorizationServerEndpointsConfiguration通过Bean往Spring容器中注入了多个Endpoint，用于处理OAuth相关的授权请求。主要包含的Endpoint如下：
- AuthorizationEndpoint: 用于处理资源授权请求（/oauth/authorize）
- TokenEndpoint: 用于处理Token申请请求（/oauth/token)
- CheckTokenEndpoint：用于处理Token校验请求（/oauth/check_token)
- 其它：默认的授权页面等Endpoint；

以TokenEndpoint为例，它的托管过程如下：
```java
@Bean
public TokenEndpoint tokenEndpoint() throws Exception {
    TokenEndpoint tokenEndpoint = new TokenEndpoint();
    tokenEndpoint.setClientDetailsService(clientDetailsService);
    tokenEndpoint.setProviderExceptionHandler(exceptionTranslator());
    tokenEndpoint.setTokenGranter(tokenGranter());
    tokenEndpoint.setOAuth2RequestFactory(oauth2RequestFactory());
    tokenEndpoint.setOAuth2RequestValidator(oauth2RequestValidator());
    tokenEndpoint.setAllowedRequestMethods(allowedTokenEndpointRequestMethods());
    return tokenEndpoint;
}
```
TokenEndpoint的关键方法如下：
```java
@RequestMapping(value = "/oauth/token", method=RequestMethod.POST)
public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam
Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

    if (!(principal instanceof Authentication)) {
        throw new InsufficientAuthenticationException(
                "There is no client authentication. Try adding an appropriate authentication filter.");
    }

    String clientId = getClientId(principal);
    ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);

    TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);

    if (clientId != null && !clientId.equals("")) {
        // Only validate the client details if a client authenticated during this
        // request.
        if (!clientId.equals(tokenRequest.getClientId())) {
            // double check to make sure that the client ID in the token request is the same as that in the
            // authenticated client
            throw new InvalidClientException("Given client ID does not match authenticated client");
        }
    }
    if (authenticatedClient != null) {
        oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
    }
    if (!StringUtils.hasText(tokenRequest.getGrantType())) {
        throw new InvalidRequestException("Missing grant type");
    }
    if (tokenRequest.getGrantType().equals("implicit")) {
        throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
    }

    if (isAuthCodeRequest(parameters)) {
        // The scope was requested or determined during the authorization step
        if (!tokenRequest.getScope().isEmpty()) {
            logger.debug("Clearing scope of incoming token request");
            tokenRequest.setScope(Collections.<String> emptySet());
        }
    }

    if (isRefreshTokenRequest(parameters)) {
        // A refresh token has its own default scopes, so we should ignore any added by the factory here.
        tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
    }

    OAuth2AccessToken token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
    if (token == null) {
        throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
    }

    return getResponse(token);

}
```
它就是通过RequestMapping对外暴露了一个接口，用于处理Token申请请求。

## 2.3 AuthorizationServerEndpointsConfigurer的生效
然而现在还有一个问题：2.1中所配置的AuthorizationServerEndpointsConfigurer对象，在哪个地方产生作用？
在AuthorizationServerEndpointsConfiguration类中，使用到endpoints的地方只有两个
```java
@Bean
public FactoryBean<AuthorizationServerTokenServices> defaultAuthorizationServerTokenServices() {
    return new AuthorizationServerTokenServicesFactoryBean(endpoints);
}

public AuthorizationServerEndpointsConfigurer getEndpointsConfigurer() {
    if (!endpoints.isTokenServicesOverride()) {
        try {
            endpoints.tokenServices(endpoints.getDefaultAuthorizationServerTokenServices());
        }
        catch (Exception e) {
            throw new BeanCreationException("Cannot create token services", e);
        }
    }
    return endpoints;
}
```
然后看到getEndpointsConfigurer方法在AuthorizationServerEndpointsConfiguration类中有多次调用，还是以TokenEndpoints的配置为例：
```java
@Bean
public TokenEndpoint tokenEndpoint() throws Exception {
    TokenEndpoint tokenEndpoint = new TokenEndpoint();
    tokenEndpoint.setClientDetailsService(clientDetailsService);
    tokenEndpoint.setProviderExceptionHandler(exceptionTranslator());
    tokenEndpoint.setTokenGranter(tokenGranter());
    tokenEndpoint.setOAuth2RequestFactory(oauth2RequestFactory());
    tokenEndpoint.setOAuth2RequestValidator(oauth2RequestValidator());
    tokenEndpoint.setAllowedRequestMethods(allowedTokenEndpointRequestMethods());
    return tokenEndpoint;
}
```
它的每个配置，如setTokenGranter方法所接收参数（tokenGranter()），都有使用getEndpointsConfigurer方法来获取相关配置，如tokenGranter()的定义：
```java
private TokenGranter tokenGranter() throws Exception {
    return getEndpointsConfigurer().getTokenGranter();
}
```

可见，在AuthorizationServerEndpointsConfiguration中定义的AuthorizationServerEndpointsConfiguration对象，就在定义各个Endpoint的时候会使用到。

# 3. AuthorizationServerSecurityConfiguration
它主要完成以下功能：
- 配置客户端信息：从Spring容器中加载所有AuthorizationServerConfigurer配置类来完成ClientDetailsServiceConfigurer的配置；
- 通过HttpSecurity来完成/oauth/token等请求授权配置；

关键方法如下：
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    AuthorizationServerSecurityConfigurer configurer = new AuthorizationServerSecurityConfigurer();
    FrameworkEndpointHandlerMapping handlerMapping = endpoints.oauth2EndpointHandlerMapping();
    http.setSharedObject(FrameworkEndpointHandlerMapping.class, handlerMapping);
    configure(configurer);
    http.apply(configurer);
    String tokenEndpointPath = handlerMapping.getServletPath("/oauth/token");
    String tokenKeyPath = handlerMapping.getServletPath("/oauth/token_key");
    String checkTokenPath = handlerMapping.getServletPath("/oauth/check_token");
    if (!endpoints.getEndpointsConfigurer().isUserDetailsServiceOverride()) {
        UserDetailsService userDetailsService = http.getSharedObject(UserDetailsService.class);
        endpoints.getEndpointsConfigurer().userDetailsService(userDetailsService);
    }
    // @formatter:off
    http
        .authorizeRequests()
            .antMatchers(tokenEndpointPath).fullyAuthenticated()
            .antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())
            .antMatchers(checkTokenPath).access(configurer.getCheckTokenAccess())
    .and()
        .requestMatchers()
            .antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath)
    .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    // @formatter:on
    http.setSharedObject(ClientDetailsService.class, clientDetailsService);
}
```
代码比较直观，就是通过httpSecurity对象配置/oauth/token等请求的访问权限。从以上代码中可以看到，默认情况下/oauth/token_key是需要登录后才能访问的，
有时候需要通过AuthorizationServerConfigurerAdapter来修改这个权限。下一节我们将对这个类进行分析。

# 4. AuthorizationServerConfigurerAdapter
它实现了AuthorizationServerConfigurer接口。在第三节中，可以看到AuthorizationServerConfigurer接口在多个地方有使用到。
这个接口有三个方法，通过这些方法，我们可以实现客户端配置、接口权限配置、以及各个Endpoint的相关配置。

## 4.1 客户端配置
通过configure(ClientDetailsServiceConfigurer clients)  方法可以配置客户端信息；如：
```java
@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
            .withClient("codelife")
            .resourceIds("testResource")
            .authorizedGrantTypes("password")
            .authorities("ROLE_CLIENT")
            .scopes("read", "write")
            .secret("secret")
            .redirectUris("http://localhost:8080");
}
```
以上代码在内存中维护了一个客户端信息，指定这个客户端能够使用的连接模式（password)、客户端所拥有的权限（可访问的资源、对资源的读与写权限）、客户端的密码以及
授权成功后跳向的页面。

客户端信息也可以也可以存储到数据库中去，通过clients.jdbc来指定存储的数据库信息。

## 4.2 接口权限配置
通过方法configure(AuthorizationServerSecurityConfigurer security)完成，如默认情况下/oauth/token_key是要登录后才能访问的，可以通过这个方法修改其权限：
```java
@Override
public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.tokenKeyAccess("permitAll()")
            .checkTokenAccess("hasAuthority('ROLE_CLIENT')")
            .allowFormAuthenticationForClients();
}
```

## 4.3 Endpoints配置
通过configure(AuthorizationServerEndpointsConfigurer endpoints)方法可完成各个Endpoint的配置，示例如下：
```java
@Override
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    // 后端接口默认添加/api前缀，以方便在前后端分离模式下nginx区分前后端请求
    endpoints.pathMapping("/oauth/token", "/api/oauth/token");

    // 登录时的异常特殊处理
    endpoints.exceptionTranslator(new DefaultWebResponseExceptionTranslator() {
        @Override
        public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
            if (e instanceof RealAuthenticationException) {
                return new ResponseEntity<>(new OAuth2Exception(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return super.translate(e);
        }
    });
    endpoints.authenticationManager(authentication -> realAuthenticationProvider.authenticate(authentication));
}
```
上述代码中分别对授权服务所使用的路径、异常处理及授权管理器进行了自定义配置。

# 5. 总结
可见，授权服务主要完成以下功能：
- 通过ClientDetailsServiceConfigurer完成客户端信息维护管理；
- 通过AuthorizationServerEndpointsConfiguration完成授权接口管理（/oauth/token、/oauth/token_key等接口的定义及管理）；
- 通过AuthorizationServerSecurityConfiguration完成请求授权配置；
- 开发者可以通过AuthorizationServerConfigurerAdapter来引入自定义配置；

简单来说，授权服务就是定义了几个控制器，然后再针对访问这些控制器所需要的权限进行了维护管理。它与资源服务实现机制上不一样的是资源服务引入了新的Filter来进行业务处理，
而授权服务并未引入新的Filter。
