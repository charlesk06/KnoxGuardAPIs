# knox-token-client-library

| Version | Description |
| ----------- |------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Version 1.0 | Initial release                                                                                                                                        |
| Version 2.0 | knox-token-client-library-2.0.jar added to support java version 1.6 and greater. knox-token-client-library.jar supported java version 1.8 and greater. |
| Version 2.1 | knox-token-client-library-2.1.jar removes org.joda package |
| Version 3.0 | knox-token-client-library-3.0.jar <li>Encryption method supported</li> <li>Added 'not null' and 'not empty' checks for token fields when using the generateClientIdentifier or generateAccessToken method.</li> |
| Version 4.0 | knox-token-client-library-4.0.jar <li>Third party Portal Integration supported</li> <li>Added new utility methods to 1. Generate signed client Identifier using IDP(SA) token and  2. Genrate signed session token </li> |

This library provides utility methods to generate and sign Knox Tokens. The pre-requisites for using this utility are

  - You have downloaded the Knox Certificate file (certificate.json)
  - You have generated a Client Identifier (api-key) for accessing apis of Knox Cloud Services.

# Intended Use
The workflow for making api calls to Knox Cloud Services is divided into a portal workflow, and a programmatic workflow.

### Portal flow
 1. Download Certificate from Knox Api Portal
 2. Generate and Download ClientIdentifier (api-key) for a specific Knox Solution

### Programmatic flow
3. Call Knox api to generate an Api Access Token. This api call requires a **signed ClientIdentifier**, and specific contents of your Certificate (Public Key).
4. Call Knox api for your intended workflow (eg: upload device, configure device etc). This api call requires your **signed Api Access Token**, and specific contents of your Certificate (Public Key).

This utility jar helps generate signed clientIdentifiers, and signed accessTokens.

### Generate Signed ClientIdentifier

KnoxTokenUtility class provides the following method to generate a **signed client identifier**.
```
String KnoxTokenUtility.generateSignedClientIdentifierJWT(InputStream certificate, String clientIdentifier)
```
Input parameters:
- certificate: An input stream of the Knox certificate (certificate.json) that was downloaded from Knox Api portal.
- clientIdentifier: The string ClientIdentifier downloaded from Knox Api portal.

Output:
- A String representing the ClientIdentifier signed with the primary key from the downloaded Certificate.

### Generate Signed Api Access Token

KnoxTokenUtility class provides the following method to generate a **signed api access token**.
```
String KnoxTokenUtility.generateSignedAccessTokenJWT(InputStream certificate, String accessToken)
```
Input parameters:
- certificate: An input stream of the Knox certificate (certificate.json) that was downloaded from Knox Api portal.
- accessToken: The string api AccessToken returned by calling Knox Cloud Services' generateAccessToken api.

Output:
- A String representing the AccessToken signed with the primary key from the downloaded Certificate.

### Generate Encrypted Text

 To encrypt a text, use KnoxEncryptionUtility class. Instantiate said class then pass the text to be encrypted to encrypt(String text) method. This method will return the corresponding encrypted text.
```
 KnoxEncryptionUtility knoxEncryptionUtil = new KnoxEncryptionUtility();
 String encryptedText = knoxEncryptionUtil.encrypt(textToBeEncrypted);
```
Input parameter: 
- text: Text to be encrypted. This should not be greater than 245 bytes.

Output:
- String which corresponds to the encrypted text.


License
----

Apache

####Example


####Java

```java
private void knoxApi() throws FileNotFoundException {
	String myCertificateFilePath = "src/main/resources/certificate.json";
	String myClientIdentifier = "my.example.clientIdentifier";
	FileInputStream certInputStream = new FileInputStream(new File(myCertificateFilePath));

	// 1. generate signed clientIdentifier

	String signedClientIdentifier = KnoxTokenUtility
                .generateSignedClientIdentifierJWT(certInputStream, myClientIdentifier);

	// 2. call knox api to receive access token
	String apiAccessToken = getKnoxApiAccessToken(certInputStream, signedClientIdentifier);

	// 3. sign access token
	String signedApiAccessToken = KnoxTokenUtility.generateSignedAccessTokenJWT(certInputStream, apiAccessToken);

	// 4. call knox api to upload device
	// todo
}

private String getKnoxApiAccessToken(InputStream certInputStream, String signedClientIdentifier) throws FileNotFoundException {

	// knox US endpoint to generate api access token
	String knoxGenerateApiAccessTokenEndpoint = "https://us-kcs-api.samsungknox.com/ams/v0/users/accesstoken";

	// create http request entity
    Map<String, String> map = new HashMap<String, String>();

	// set base64 encoded string public key
	map.put("base64EncodedStringPublicKey", KnoxTokenUtility.generateBase64EncodedStringPublicKey(certInputStream));

	// set signed client identifier
   map.put("clientIdentifierJwt", signedClientIdentifier);

   HttpHeaders headers = new HttpHeaders();
   headers.setContentType(MediaType.APPLICATION_JSON);

   HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);

	// use spring-web RestTemplate to call REST api
	RestTemplate restTemplate = new RestTemplate();

	// call Knox api to receive api access token
   ResponseEntity<MyResponse> responseEntity = restTemplate.postForEntity(URI.create(knoxGenerateApiAccessTokenEndpoint), requestEntity, MyResponse.class);

	// extract api access token
	MyResponse myResponse = responseEntity.getBody();
	String apiAccessToken = myResponse.getApiAccessToken();

	return apiAccessToken;
}
```


########################### KCS EMM Integration ####################################



### Generate Signed ClientIdentifier - For KCS EMM Integration

KnoxTokenUtility class provides the following method to generate a **signed client identifier using IDP(SA) Access token**.
```
String KnoxTokenUtility.generateSignedClientIdentifierJWTWithIdpAccessToken(InputStream certificate, String clientIdentifier, String idpAccessToken)
```
Input parameters:
- certificate: An input stream of the Knox certificate (certificate.json) that was downloaded from Knox Api portal.
- clientIdentifier: The string ClientIdentifier downloaded from Knox Api portal.
- idpAccessToken: This is retrived when user log in on Samsung Account Portal (IDP) using his Samsung Account credentials
  Pre-requisite: SA registration and integration with Portal is required. Please refer online documentation for more details.

Output:
- A String representing the ClientIdentifier signed with the primary key from the downloaded Certificate.




### Generate Signed Session Token    - For KCS EMM Integration

KnoxTokenUtility class provides the following method to generate a **signed session token**.
```
String KnoxTokenUtility.generateSignedAccessTokenJWT(InputStream certificate, String sessionToken)
```
Input parameters:
- certificate: An input stream of the Knox certificate (certificate.json) that was downloaded from Knox Api portal.
- sessionToken: The string api AccessToken returned by calling Knox Cloud Services' generateSessionToken api.- /ams/v1/users/customer/sessiontoken

Output:
- A String representing the SessionToken signed with the primary key from the downloaded Certificate.




### Generate Signed Api Access Token -  For KCS EMM Integration

Note: This method will remain same as before. No change is required here.
KnoxTokenUtility class provides the following method to generate a **signed api access token**.
```
String KnoxTokenUtility.generateSignedAccessTokenJWT(InputStream certificate, String accessToken)
```
Input parameters:
- certificate: An input stream of the Knox certificate (certificate.json) that was downloaded from Knox Api portal.
- accessToken: The string api AccessToken returned by calling Knox Cloud Services' generateAccessToken api. - /ams/v1/users/customer/accesstoken

Output:
- A String representing the AccessToken signed with the primary key from the downloaded Certificate.



####Example


####Java

```java
private void knoxApi() throws FileNotFoundException {
	String myCertificateFilePath = "src/main/resources/certificate.json";
	String myClientIdentifier = "my.example.clientIdentifier";
	FileInputStream certInputStream = new FileInputStream(new File(myCertificateFilePath));
	String idpAccessToken = "<Retreived on log-in with IDP (SA)>";

	// 1. Generate signed clientIdentifier using Idp Access token

	String signedClientIdentifier = KnoxTokenUtility
                .generateSignedClientIdentifierJWTWithIdpAccessToken(certInputStream, myClientIdentifier, idpAccessToken);
                
    // 2. Call knox session token api to receive a session token 
    String sessionToken = getKnoxSessionToken(certInputStream, signedClientIdentifier);     
                
    // 3. Sign session token
	String signedSessionToken = KnoxTokenUtility.generateSignedSessionTokenJWT(certInputStream, sessionToken);
           
	// 4. Call knox api to receive access token
	String apiAccessToken = getKnoxApiAccessToken(certInputStream, signedSessionToken);

	// 5. sign access token
	String signedApiAccessToken = KnoxTokenUtility.generateSignedAccessTokenJWT(certInputStream, apiAccessToken);

	// 6. call knox api to upload device
	// todo
}


private String getKnoxSessionToken(InputStream certInputStream, String signedClientIdentifier) throws FileNotFoundException {

	// knox endpoint to generate session token
	String knoxGenerateSessionTokenEndpoint = "https://us-kcs-api.samsungknox.com/ams/v1/users/customer/sessiontoken";

	// create http request entity
    Map<String, String> map = new HashMap<String, String>();

	// set base64 encoded string public key
	map.put("base64EncodedStringPublicKey", KnoxTokenUtility.generateBase64EncodedStringPublicKey(certInputStream));

	// set signed client identifier
   map.put("clientIdentifierJwt", signedClientIdentifier);

   HttpHeaders headers = new HttpHeaders();
   headers.setContentType(MediaType.APPLICATION_JSON);

   HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);

	// use spring-web RestTemplate to call REST api
	RestTemplate restTemplate = new RestTemplate();

	// call Knox api to receive api access token
   ResponseEntity<MyResponse> responseEntity = restTemplate.postForEntity(URI.create(knoxGenerateApiAccessTokenEndpoint), requestEntity, MyResponse.class);

	// extract api access token
	MyResponse myResponse = responseEntity.getBody();
	String sessionToken = myResponse.getSessionToken();

	return apiAccessToken;
}

private String getKnoxApiAccessToken(InputStream certInputStream, String signedSessionToken) throws FileNotFoundException {

	// knox endpoint to generate api access token
	String knoxGenerateApiAccessTokenEndpoint = "https://us-kcs-api.samsungknox.com/ams/v1/users/customer/accesstoken";

	// create http request entity
    Map<String, String> map = new HashMap<String, String>();

	// set base64 encoded string public key
	map.put("base64EncodedStringPublicKey", KnoxTokenUtility.generateBase64EncodedStringPublicKey(certInputStream));

	// set signed client identifier
   map.put("sessionTokenJwt", signedSessionToken);

   HttpHeaders headers = new HttpHeaders();
   headers.setContentType(MediaType.APPLICATION_JSON);

   HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);

	// use spring-web RestTemplate to call REST api
	RestTemplate restTemplate = new RestTemplate();

	// call Knox api to receive api access token
   ResponseEntity<MyResponse> responseEntity = restTemplate.postForEntity(URI.create(knoxGenerateApiAccessTokenEndpoint), requestEntity, MyResponse.class);

	// extract api access token
	MyResponse myResponse = responseEntity.getBody();
	String apiAccessToken = myResponse.getApiAccessToken();

	return apiAccessToken;
}
```

