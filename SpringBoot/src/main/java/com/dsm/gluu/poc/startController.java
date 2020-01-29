package com.dsm.gluu.poc;

import java.io.IOException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServlet;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper; 
@Controller
public class startController {
	
	private static final Logger log = LoggerFactory.getLogger(GluuPoc1Application.class);
	private ObjectMapper objectMapper;
	
	@RequestMapping("/authorize/callback")
	  public String test(@RequestParam Map<?, ?> param) throws NoSuchAlgorithmException,
	      KeyStoreException, KeyManagementException, IOException {

	    log.info("authorize - callback");

	    log.info("scope:"+param.get("scope"));
	    log.info("code:"+param.get("code"));
	    log.info(pocClientCredential());
//	    if (param.isEmpty()) {
//	      return Map.of("authorize_response", "empty");
//	    }

	    RestTemplate restTemplate = sslIgnoreRestTemplate();

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("scope", param.get("scope"));
	    body.add("code", param.get("code"));
	    body.add("grant_type", "authorization_code");
	    body.add("redirect_uri", "http://localhost:8080/oauth/test");

	    Map<String, Object> out = new HashMap<>();
	    out.put("authorize_response", param);

//	    try {
//	      @SuppressWarnings("Convert2Diamond")
//	      ResponseEntity<HashMap<?, ?>> response = restTemplate.exchange(
//	          RequestEntity
//	              .post(URI.create("https://gluu.dsmcorps.com/oxauth/restv1/token"))
//	              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//	              .header("Authorization", "Basic " + pocClientCredential())
//	              .body(body),
//	          new ParameterizedTypeReference<HashMap<?, ?>>() {});
//	      out.put("token_reponse", response.getBody());
//
//	      @SuppressWarnings("ConstantConditions")
//	      final String accessToken = (String) response.getBody().get("access_token");
//	      log.info("accessToken:"+accessToken);
//	      final String payload =
//	          new String(Base64.getDecoder().decode(accessToken.split("\\.")[1]));
//
//	      out.put("access_token_decoding", objectMapper.readValue(payload, HashMap.class));
//
//	    } catch (HttpClientErrorException e) {
//	      out.put("token_response",
//	          objectMapper.readValue(
//	              e.getResponseBodyAsString(), new TypeReference<HashMap<String, Object>>() {}));
//	    } catch (RestClientException e) {
//	      log.error("error", e);
//	    }
	    
	    
	    return "redirect:http://localhost:8080/callback.php?&code="+param.get("code").toString();
	    
	    
	  }
	
	@ResponseBody
	@RequestMapping("/oauth/token")
	  public Object token(@RequestParam Map<?, ?> param) throws NoSuchAlgorithmException,
	      KeyStoreException, KeyManagementException, IOException {

		log.info("authorize - token");
		
		 log.info("scope:"+param.get("scope"));
		 log.info("code:"+param.get("code"));
		
	    String tokenUrl = "https://gluu.dsmcorps.com/oxauth/restv1/token";

	    RestTemplate restTemplate = sslIgnoreRestTemplate();

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("scope", param.get("scope"));
	    body.add("code", param.get("code"));
	    body.add("grant_type", "authorization_code");
	    body.add("redirect_uri", "http://localhost:8080/oauth/test");

	    Map<String, Object> out = new HashMap<>();
	    out.put("authorize_response", param);

	    try {
	      @SuppressWarnings("Convert2Diamond")
	      ResponseEntity<HashMap<?, ?>> response = restTemplate.exchange(
	          RequestEntity
	              .post(URI.create(tokenUrl))
	              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	              .header("Authorization", "Basic " + pocClientCredential())
	              .body(body),
	          new ParameterizedTypeReference<HashMap<?, ?>>() {});
	      out.put("token_reponse", response.getBody());

	      @SuppressWarnings("ConstantConditions")
	      final String idToken = (String) response.getBody().get("id_token");
	      log.info("idToken:"+idToken);
	      
	      final String payload =
	          new String(Base64.getDecoder().decode(idToken.split("\\.")[1]));

	      out.put("access_token_decoding", objectMapper.readValue(payload, HashMap.class));

	    } catch (HttpClientErrorException e) {
	      out.put("token_response",
	          objectMapper.readValue(
	              e.getResponseBodyAsString(), new TypeReference<HashMap<String, Object>>() {}));
	    } catch (RestClientException e) {
	      log.error("error", e);
	    }

	    return out;
	  }
	
	
	@RequestMapping("/oauth/test")
	  public Object test2(@RequestParam Map<?, ?> param) {
	    log.info("test = {}", param);
	    return param;
	  }
	
	@ResponseBody
	@RequestMapping("/oauth/resource")
	  public Object test3(@RequestParam("username") String username,
	                      @RequestParam("password") String password) throws NoSuchAlgorithmException,
	      KeyStoreException, KeyManagementException, IOException {

		log.info("authorize - resource");
		
		String tokenUrl = "https://gluu.dsmcorps.com/oxauth/restv1/token";

	    RestTemplate restTemplate = sslIgnoreRestTemplate();

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("scope", "openid profile");
	    body.add("grant_type", "password");
	    body.add("username", username);
	    body.add("password", password);
	    body.add("redirect_uri", "http://localhost:8080/oauth/test");

	    Map<String, Object> out = new HashMap<>();

	    try {
	      @SuppressWarnings("Convert2Diamond")
	      ResponseEntity<HashMap<String, Object>> responseEntity =
	          restTemplate.exchange(
	              RequestEntity.post(URI.create(tokenUrl))
	                  .header("Authorization", "Basic " + passwordClientCredential())
	                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                  .body(body),
	              new ParameterizedTypeReference<HashMap<String, Object>>() {});
	      out.put("response", responseEntity.getBody());
	    } catch (HttpClientErrorException e) {
	      out.put("token_response",
	          objectMapper.readValue(
	              e.getResponseBodyAsString(), new TypeReference<HashMap<String, Object>>() {}));
	    } catch (RestClientException e) {
	      log.error("error", e);
	    }

	    return out;
	  }
	
	 @RequestMapping("/oauth/client")
	  public Object test4() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

	    String tokenUrl = "https://gluu.dsmcorps.com/oxauth/restv1/token";

	    RestTemplate restTemplate = sslIgnoreRestTemplate();

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("scope", "openid");
	    body.add("grant_type", "client_credentials");

	    Map<String, Object> out = new HashMap<>();

	    try {
	      @SuppressWarnings("Convert2Diamond")
	      ResponseEntity<HashMap<String, Object>> responseEntity =
	          restTemplate.exchange(
	              RequestEntity.post(URI.create(tokenUrl))
	                  .header("Authorization", "Basic " + pocClientCredential())
	                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                  .body(body),
	              new ParameterizedTypeReference<HashMap<String, Object>>() {});
	      out.put("response", responseEntity.getBody());
	    } catch (HttpClientErrorException e) {
	      out.put("token_response",
	          objectMapper.readValue(
	              e.getResponseBodyAsString(), new TypeReference<HashMap<String, Object>>() {}));
	    } catch (RestClientException e) {
	      log.error("error", e);
	    }

	    return out;
	  }
	 
	  @ResponseBody
	  @RequestMapping("/oauth/userInfo")
	  public Object userInfo(@RequestParam("accessToken") String accessToken
			  ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

	    log.info("userInfo");  
	    String userInfoUrl = "https://gluu.dsmcorps.com/oxauth/restv1/userinfo";

	    boolean systemCheck = true;
	    RestTemplate restTemplate = sslIgnoreRestTemplate();

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    Map<String, Object> out = new HashMap<>();
//	    Map<String, Object> out1 = new HashMap<>();
//	    out1 = verify(accessToken);
//	    log.info("verify: "+out1.toString());
	    
	    boolean check =  verifySignature("qahiUQ1dXFkzQjlEeXxIsWhE",
	    		accessToken.split("\\.")[0]+"."+accessToken.split("\\.")[1],accessToken.split("\\.")[2]);
	    
	    if (check){
	    try {
	      @SuppressWarnings("Convert2Diamond")
	      ResponseEntity<HashMap<String, Object>> responseEntity =
	          restTemplate.exchange(
	              RequestEntity.post(URI.create(userInfoUrl))
	                  .header("Authorization", "Bearer " + accessToken)
	                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                  .body(body),
	              new ParameterizedTypeReference<HashMap<String, Object>>() {});
	      out.put("response", responseEntity.getBody());
	      
	      Map<String,Object> map = new HashMap<>();
		    map = (Map) out.get("response");
		    log.info("response :"+map.toString());
		    
			/*
			 * ArrayList array = (ArrayList)map.get("Sys_Permission");
			 * 
			 * String[] systems = new String[array.size()];
			 * 
			 * for(int i=0; i < array.size(); i++) { systems[i] = (String) array.get(i);
			 * log.info(systems[i]); if(systems[i].equals("developer")) { systemCheck =
			 * true; } }
			 */
	      
	    } catch (HttpClientErrorException e) {
	      out.put("userInfo_response",
	          objectMapper.readValue(
	              e.getResponseBodyAsString(), new TypeReference<HashMap<String, Object>>() {}));
	      return out;
	    } catch (RestClientException e) {
	      log.error("error", e);
	    }
	    
	    if(systemCheck) {
	    	return out;
	    }
	    else {
	    	out.clear();
	    	out.put("Sys_Permission", "Permission denied!!");
	    	return out;
	    }
	    }
	    else {
	    	out.clear();
	    	out.put("verifySignature", "invalid token");
	    	return out;
	    }
	    
	  }
	  
	  @ResponseBody
	  @RequestMapping("/oauth/refreshAccessToken")
	  public Object refreshAccessToken(@RequestParam("refreshToken") String refreshToken
			  ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

	    log.info("refreshAccessToken");  
	    String tokenUrl = "https://gluu.dsmcorps.com/oxauth/restv1/token";

	    RestTemplate restTemplate = sslIgnoreRestTemplate();

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("grant_type", "refresh_token");
	    body.add("refresh_token", refreshToken );

	    Map<String, Object> out = new HashMap<>();

	    
	    try {
	      @SuppressWarnings("Convert2Diamond")
	      ResponseEntity<HashMap<String, Object>> responseEntity =
	          restTemplate.exchange(
	              RequestEntity.post(URI.create(tokenUrl))
	                  .header("Authorization", "Basic " + pocClientCredential())
	                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                  .body(body),
	              new ParameterizedTypeReference<HashMap<String, Object>>() {});
	      out.put("response", responseEntity.getBody());
	    } catch (HttpClientErrorException e) {
	      out.put("token_response",
	          objectMapper.readValue(
	              e.getResponseBodyAsString(), new TypeReference<HashMap<String, Object>>() {}));
	    } catch (RestClientException e) {
	      log.error("error", e);
	    }

	    return out;
	    
	  }


	 
	 @Autowired
	  public void setObjectMapper(ObjectMapper objectMapper) {
	    this.objectMapper = objectMapper;
	  }

	  private String passwordClientCredential() {
	    return Base64.getEncoder().encodeToString(
	        "023c3a02-6a97-4318-bec8-68444753aa61:qahiUQ1dXFkzQjlEeXxIsWhE".getBytes());
	  }
	  
	  private String pocClientCredential() {
		    return Base64.getEncoder().encodeToString(
		        "023c3a02-6a97-4318-bec8-68444753aa61:qahiUQ1dXFkzQjlEeXxIsWhE".getBytes());
		  }
	
	private RestTemplate sslIgnoreRestTemplate()
	      throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
	    TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
	
	    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
	        .loadTrustMaterial(null, acceptingTrustStrategy)
	        .build();
	
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
	
	    CloseableHttpClient httpClient = HttpClients.custom()
	        .setSSLSocketFactory(csf)
	        .build();
	
	    HttpComponentsClientHttpRequestFactory requestFactory =
	        new HttpComponentsClientHttpRequestFactory();
	
	    requestFactory.setHttpClient(httpClient);
	    return new RestTemplate(requestFactory);
	  }
	
	
	private Map<String, Object> verify(String token
			) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException{
		
		String verifyUrl = "https://gluu.dsmcorps.com/oxauth/restv1/introspection";

	    RestTemplate restTemplate = sslIgnoreRestTemplate();

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("response_as_jwt", "false");
	    body.add("token", token );

	    Map<String, Object> out = new HashMap<>();

	    
	    try {
	      @SuppressWarnings("Convert2Diamond")
	      ResponseEntity<HashMap<String, Object>> responseEntity =
	          restTemplate.exchange(
	              RequestEntity.post(URI.create(verifyUrl))
	                  .header("Authorization", "Basic " + pocClientCredential())
	                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                  .body(body),
	              new ParameterizedTypeReference<HashMap<String, Object>>() {});
	      out.put("response", responseEntity.getBody());
	    } catch (HttpClientErrorException e) {
	      out.put("verify_response",
	          objectMapper.readValue(
	              e.getResponseBodyAsString(), new TypeReference<HashMap<String, Object>>() {}));
	    } catch (RestClientException e) {
	      log.error("error", e);
	    }
		
	    return out;
	  }
	
public static boolean verifySignature(String secret, String headerAndpayload, String signature) {
        
        log.info("verifySignature");
        String match = "[^\\uAC00-\\uD7A3xfe0-9a-zA-Z\\\\s]";
        String signature_A = "";
        String signature_B = "";
        
        signature_A = signature.replaceAll(match, "");
        
        
        try {
            final String resultHex = getHexHash(secret, headerAndpayload);
            signature_B = resultHex.replaceAll(match, "");
            if (!signature_B.equals(signature_A)) {
                log.info("HMAC does not match.");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }

    private static String getHexHash(String secret, String headerAndpayload) throws NoSuchAlgorithmException, InvalidKeyException {
        final String hmacSHA256 = "HmacSHA256";
        final Mac hasher = Mac.getInstance(hmacSHA256);
        hasher.init(new SecretKeySpec(secret.getBytes(), hmacSHA256));
        final byte[] hash = hasher.doFinal(headerAndpayload.getBytes());

        String encode = Base64.getEncoder().encodeToString(hash);
        return encode;
    }

	
	}


