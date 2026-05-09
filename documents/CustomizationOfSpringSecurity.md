<h1>Customization of Spring Security</h1>
<h2>Overview of Spring Security</h2>

<div style="width: 90%;">
<p>
In Spring MVC, all HTTP requests are channeled through the DispatcherServlet,
which directs the requests to controller classes. If Spring Security is implemented in an application, a filter chain will be placed before the HTTP requests reach the DispatcherServlet.</p>
<p>
AuthenticationFilter creates an authentication object and passes user data (usually a user name and a password) to an authentication manager. Then the authentication manager passes the authentication object to an authentication provider. (In this app, DAOAuthentication Provider is used.)</p>
<p>
DAOAuthenticationProvider 1) encodes the password using a password encoder,
2) gets UserDetails object from DB through UserDetailsService and verifies the username and the password. 
If the username and the password are valid, user roles will be set to the authentication object. The authentication object will be returned to the authentication filter via the authentication manager and will be set to SecurityContext.
While the request is being handled, the user roles will be available through security context.</p>

<h2>Customization of Spring Security in this Application</h2>

<div style="width: 90%;">
<h3>WebSecurityConfig.java</h3>
In order to implement authentication mechanism by JWT and refresh tokens, the security filter was customized in the method filterChain(HttpSecurity http) in WebSecurityConfig.java.
(The default logic of the security filter is defined in SpringBootWebSecurityConfiguration class in the package org.springframework.boot.autoconfigure.security.servlet.)

- BCryptPasswordEncoder will be set to password encoder of AuthenticationProvider.

With BCryptPasswordEncoder, a string (e.g. a password string) will be added with a salt (a random string) and will be hashed using BCrypt algorhythm.  Security will be strengthened by adding the salt.

<h3>Method filterChain</h3>
- SessionCreationPolicy is set stateless (jsessionid won't be set to the Cookie.)
- In case of exceptions, requests will be handled by AuthEntryPoint.java.