package com.examly.apigateway;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApigatewayApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    public String customerToken;
    private String loanManagerToken;
    private String branchManagerToken;
    private int customerUserId;

    private ObjectMapper objectMapper = new ObjectMapper(); 

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @Order(1)
    void backend_testRegisterCustomer() throws Exception {
        int userId = 1; // Example userId for test
        
        String requestBody = "{"
            + "\"userId\": " + userId + ","
            + "\"email\": \"customer@gmail.com\","
            + "\"password\": \"customer@1234\","
            + "\"username\": \"customer\","
            + "\"userRole\": \"Customer\","
            + "\"mobileNumber\": \"1234567890\""
            + "}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        // Assert status is Created
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Parse the response body
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        int registeredUserId = responseBody.get("userId").asInt();
        Assertions.assertEquals(userId, registeredUserId, "UserId should match the provided one");

        // Store userId for further use if needed
        customerUserId = registeredUserId;
        System.out.println("Registered Customer UserId: " + customerUserId);
    }

    @Test
    @Order(2)
    void backend_testRegisterLoanManager() throws Exception {
        int userId = 2; // Example userId for test
        
        String requestBody = "{"
            + "\"userId\": " + userId + ","
            + "\"email\": \"consultant@gmail.com\","
            + "\"password\": \"consultant@1234\","
            + "\"username\": \"consultant\","
            + "\"userRole\": \"LoanManager\","
            + "\"mobileNumber\": \"1234567890\""
            + "}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        // Assert status is Created
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Parse the response body
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        int registeredUserId = responseBody.get("userId").asInt();
        Assertions.assertEquals(userId, registeredUserId, "UserId should match the provided one");

        // Store token for further use if needed
        int loanManagerUserId = registeredUserId;
        System.out.println("Registered Financial Consultant UserId: " + registeredUserId);
    }

    @Test
    @Order(3)
    void backend_testRegisterBranchManager() throws Exception {
        int userId = 3; // Example userId for test
        
        String requestBody = "{"
            + "\"userId\": " + userId + ","
            + "\"email\": \"regionalmanager@gmail.com\","
            + "\"password\": \"regionalmanager@1234\","
            + "\"username\": \"regionalmanager\","
            + "\"userRole\": \"BranchManager\","
            + "\"mobileNumber\": \"1234567890\""
            + "}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        // Assert status is Created
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Parse the response body
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        int registeredUserId = responseBody.get("userId").asInt();
        Assertions.assertEquals(userId, registeredUserId, "UserId should match the provided one");

        // Store token for further use if needed
        int branchManagerUserId = registeredUserId;
        System.out.println("Registered Regional Manager UserId: " + registeredUserId);
    }

    @Test
    @Order(4)
    void backend_testLoginCustomer() throws Exception, JsonProcessingException {
        String requestBody = "{\"email\": \"customer@gmail.com\", \"password\": \"customer@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        // Parse the response body
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        customerToken = token;
        customerUserId = responseBody.get("userId").asInt();

        // Assert status is OK
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
        Assertions.assertNotNull(customerUserId, "UserId should not be null");
    }

    @Test
    @Order(5)
    void backend_testLoginLoanManager() throws Exception, JsonProcessingException {
        String requestBody = "{\"email\": \"consultant@gmail.com\", \"password\": \"consultant@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        loanManagerToken = token;

        // Assert status is OK
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }

    @Test
    @Order(6)
    void backend_testLoginBranchManager() throws Exception, JsonProcessingException {
        String requestBody = "{\"email\": \"regionalmanager@gmail.com\", \"password\": \"regionalmanager@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        branchManagerToken = token;

        // Assert status is OK
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }


@Test
@Order(7)
void backend_testAddLoan_AsLoanManager() throws Exception {
    Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");

    int testLoanId = 1;  // Manually specify the loanId

    String requestBody = "{"
        + "\"loanId\": " + testLoanId + ","  // Include loanId in the request body
        + "\"loanType\": \"Personal\"," 
        + "\"interestRate\": 7.5," 
        + "\"maxAmount\": 50000.0," 
        + "\"minAmount\": 5000.0," 
        + "\"minTenureMonths\": 12," 
        + "\"maxTenureMonths\": 60," 
        + "\"description\": \"Personal loan for various needs.\"," 
        + "\"status\": \"Active\"," 
        + "\"processingFee\": 1.5," 
        + "\"prepaymentPenalty\": 2.0," 
        + "\"gracePeriodMonths\": 2," 
        + "\"latePaymentFee\": 0.5"
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + loanManagerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/loans", HttpMethod.POST, requestEntity, String.class);

    // Assert status is CREATED (201)
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    // Optionally log the response
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}



@Test
@Order(8)
void backend_testAddLoan_InvalidToken() throws Exception {
    Assertions.assertNotNull(customerToken, "User token should not be null");

    String requestBody = "{"
        + "\"loanType\": \"Personal\"," 
        + "\"interestRate\": 7.5,"
        + "\"maxAmount\": 50000.0,"
        + "\"minAmount\": 5000.0,"
        + "\"minTenureMonths\": 12,"
        + "\"maxTenureMonths\": 60,"
        + "\"description\": \"Personal loan for various needs.\"," 
        + "\"status\": \"Active\"," 
        + "\"processingFee\": 1.5,"
        + "\"prepaymentPenalty\": 2.0,"
        + "\"gracePeriodMonths\": 2,"
        + "\"latePaymentFee\": 0.5"
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer invalidtoken");
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/loans", HttpMethod.POST, requestEntity,
            String.class);

    // Assert status is UNAUTHORIZED
    Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
}


@Test
@Order(9)
void backend_testUpdateLoan_AsBranchManager() throws Exception {
    Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");
    
    int loanId = 1; // Replace with an actual loan ID if needed
    
    String requestBody = "{"
        + "\"loanType\": \"Home\"," 
        + "\"interestRate\": 6.0,"
        + "\"maxAmount\": 200000.0,"
        + "\"minAmount\": 10000.0,"
        + "\"minTenureMonths\": 24,"
        + "\"maxTenureMonths\": 120,"
        + "\"description\": \"Home loan for purchasing property.\"," 
        + "\"status\": \"Active\"," 
        + "\"processingFee\": 1.0,"
        + "\"prepaymentPenalty\": 1.5,"
        + "\"gracePeriodMonths\": 3,"
        + "\"latePaymentFee\": 0.4"
        + "}";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + branchManagerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/loans/{id}", HttpMethod.PUT, requestEntity, String.class, loanId);
    
    // Assert status is OK (200) or No Content (204) based on your API implementation
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()); // or HttpStatus.NO_CONTENT
    
    // Optionally log or assert the response body if needed
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}



@Test
@Order(10)
void backend_testGetLoanById_AsLoanManager() throws Exception {
    Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");
    
    int loanId = 1; // Replace with an actual loan ID if needed
    
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + loanManagerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/loans/{id}", HttpMethod.GET, requestEntity, String.class, loanId);
    
    // Assert status is OK (200)
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    // Optionally log or assert the response body if needed
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}


@Test
@Order(11)
void backend_testAccessLoans_AsCustomer() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");
    
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + customerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/loans", HttpMethod.GET, requestEntity, String.class);
    
    // Assert status is OK (200)
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    // Optionally log or assert the response body if needed
    System.out.println("Response status (Customer): " + response.getStatusCode());
    System.out.println("Response body (Customer): " + response.getBody());
}


@Test
@Order(12)
void backend_testGetAllLoans_AsLoanManager() throws Exception {
    Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");
    
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + loanManagerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/loans", HttpMethod.GET, requestEntity, String.class);
    
    // Assert status is OK (200)
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    // Optionally log or assert the response body if needed
    System.out.println("Response status (LoanManager): " + response.getStatusCode());
    System.out.println("Response body (LoanManager): " + response.getBody());
}


@Test
@Order(13)
void backend_testGetAllLoans_AsBranchManager() throws Exception {
    Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");
    
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + branchManagerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/loans", HttpMethod.GET, requestEntity, String.class);
    
    // Assert status is OK (200)
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    // Optionally log or assert the response body if needed
    System.out.println("Response status (BranchManager): " + response.getStatusCode());
    System.out.println("Response body (BranchManager): " + response.getBody());
}




@Test
@Order(14)
void backend_testAddLoanApplication_AsCustomer() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    String requestBody = "{"
        + "\"loanApplicationId\": 1,"  // Manually pass the ID as needed
        + "\"applicationDate\": \"2024-09-17\","
        + "\"loanAmount\": 15000.0,"
        + "\"tenureMonths\": 24,"
        + "\"applicationStatus\": \"Pending\","
        + "\"employmentStatus\": \"Employed\","
        + "\"annualIncome\": 50000.0,"
        + "\"remarks\": \"Urgent loan requirement.\","
        + "\"proof\": \"Base64EncodedString\","
        + "\"accountHolder\": \"John Doe\","
        + "\"accountNumber\": \"1234567890\","
        + "\"ifscCode\": \"IFSC0001234\","
        + "\"user\": {\"userId\": 1},"
        + "\"loan\": {\"loanId\": 1}"
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + customerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/loanapplications", HttpMethod.POST, requestEntity, String.class);

    // Assert status is CREATED (201)
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    // Optionally log or assert the response body if needed
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}


@Test
@Order(15)
void backend_testUpdateLoanApplication_AsCustomer() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    int loanApplicationId = 1;  // Use the ID of the loan application you want to update

    String requestBody = "{"
        + "\"loanAmount\": 20000.0,"
        + "\"tenureMonths\": 36,"
        + "\"applicationStatus\": \"Under Review\","
        + "\"employmentStatus\": \"Self-Employed\","
        + "\"annualIncome\": 60000.0,"
        + "\"remarks\": \"Updated details.\","
        + "\"proof\": \"UpdatedBase64EncodedString\","
        + "\"accountHolder\": \"John Doe\","
        + "\"accountNumber\": \"0987654321\","
        + "\"ifscCode\": \"IFSC0004321\""
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + customerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/loanapplications/" + loanApplicationId, HttpMethod.PUT, requestEntity, String.class);

    // Assert status is OK (200) or No Content (204) based on your API implementation
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()); // or HttpStatus.NO_CONTENT

    // Optionally log or assert the response body if needed
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}



@Test
@Order(16)
void backend_testGetLoanApplicationById_AsCustomer() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    int loanApplicationId = 1;  // Use the ID of the loan application you want to retrieve

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + customerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/loanapplications/" + loanApplicationId, HttpMethod.GET, requestEntity, String.class);

    // Assert status is OK (200)
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());

    String responseBody = response.getBody();
    Assertions.assertNotNull(responseBody, "Response body should not be null");

    // Optionally assert response content
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}




@Test
@Order(17)
void backend_testGetAllLoanApplications_AsLoanManager() throws Exception {
    Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + loanManagerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/loanapplications", HttpMethod.GET, requestEntity, String.class);

    // Assert status is OK (200)
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());

    String responseBody = response.getBody();
    Assertions.assertNotNull(responseBody, "Response body should not be null");

    // Optionally assert response content
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}


@Test
@Order(18)
void backend_testGetAllLoanApplications_WithInvalidToken() throws Exception {
    String invalidToken = "invalidToken123";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + invalidToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/loanapplications", HttpMethod.GET, requestEntity, String.class);

    // Assert status is UNAUTHORIZED (401)
    Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
   // Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());

    String responseBody = response.getBody();
   // Assertions.assertNotNull(responseBody, "Response body should not be null");

    // Optionally assert response content
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}


@Test
@Order(19)
    void backend_testAddLoanDisbursement_AsLoanManager() throws Exception {
        Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");

        String requestBody = "{"
            + "\"loanDisbursementId\": 1,"
            + "\"disbursementDate\": \"2024-09-01T10:15:30\"," 
            + "\"disbursementAmount\": 5000.0," 
            + "\"disbursementMethod\": \"Bank Transfer\"," 
            + "\"status\": \"Completed\"," 
            + "\"remarks\": \"Disbursement for loan application ID 1\"," 
            + "\"loanApplication\": { \"loanApplicationId\": 1 }" 
            + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + loanManagerToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements", HttpMethod.POST, requestEntity, String.class);

        // Assert status is CREATED (201)
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
      //  assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());

        String responseBody = response.getBody();
        Assertions.assertNotNull(responseBody, "Response body should not be null");

        // Optionally assert response content
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
    }

   

    @Test
    @Order(20)
            void backend_testUpdateLoanDisbursement_AsLoanManager() throws Exception {
                Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");

                int testLoanDisbursementId=1;
        
                String requestBody = "{"
                    + "\"loanDisbursementId\": " + testLoanDisbursementId + ","
                    + "\"disbursementDate\": \"2024-09-02T11:20:30\"," 
                    + "\"disbursementAmount\": 6000.0," 
                    + "\"disbursementMethod\": \"Cheque\"," 
                    + "\"status\": \"Pending\"," 
                    + "\"remarks\": \"Updated remarks\"," 
                    + "\"loanApplication\": { \"loanApplicationId\": 1 }" 
                    + "}";
        
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + loanManagerToken);
                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        
                ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements/" + testLoanDisbursementId, HttpMethod.PUT, requestEntity, String.class);
        
                // Assert status is OK (200)
                Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
                Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
        
                String responseBody = response.getBody();
                Assertions.assertNotNull(responseBody, "Response body should not be null");
        
                // Optionally assert response content
                System.out.println("Response status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
            }
        
            @Test
            @Order(21)
            void backend_testGetAllLoanDisbursements_AsLoanManager() throws Exception {
                Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");
        
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + loanManagerToken);
                HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        
                ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements", HttpMethod.GET, requestEntity, String.class);
        
                // Assert status is OK (200)
                Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
                Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
        
                String responseBody = response.getBody();
                Assertions.assertNotNull(responseBody, "Response body should not be null");
        
                // Optionally assert response content
                System.out.println("Response status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
            }
        
            @Test
            @Order(22)
            void backend_testGetLoanDisbursementById_AsBranchManager() throws Exception {
                Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");
                int testLoanDisbursementId =1;
        
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + branchManagerToken);
                HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        
                ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements/" + testLoanDisbursementId, HttpMethod.GET, requestEntity, String.class);
        
                // Assert status is OK (200)
                Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
                Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
        
                String responseBody = response.getBody();
                Assertions.assertNotNull(responseBody, "Response body should not be null");
        
                // Optionally assert response content
                System.out.println("Response status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
            } 
   

         
            @Test
            @Order(23)
            void backend_testGetAllLoanDisbursements_AsCustomer() throws Exception {
                Assertions.assertNotNull(customerToken, "Customer token should not be null");
        
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " +customerToken );
                HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        
                ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements", HttpMethod.GET, requestEntity, String.class);
        
                // Assert status is OK (200)
                Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
               // Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
        
                String responseBody = response.getBody();
                //Assertions.assertNotNull(responseBody, "Response body should not be null");
        
                // Optionally assert response content
                System.out.println("Response status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
            }
        
        

            @Test
            @Order(24)
            void backend_testAddLoan_AsCustomer_Unauthorized() throws Exception {
                // Assuming you have a valid token for a 'Customer' or any unauthorized role
                Assertions.assertNotNull(customerToken, "Customer token should not be null");
            
                int testLoanId = 2;  // Manually specify another loanId to differentiate from the authorized test
            
                String requestBody = "{"
                    + "\"loanId\": " + testLoanId + ","  // Include loanId in the request body
                    + "\"loanType\": \"Personal\"," 
                    + "\"interestRate\": 7.5," 
                    + "\"maxAmount\": 50000.0," 
                    + "\"minAmount\": 5000.0," 
                    + "\"minTenureMonths\": 12," 
                    + "\"maxTenureMonths\": 60," 
                    + "\"description\": \"Personal loan for various needs.\"," 
                    + "\"status\": \"Active\"," 
                    + "\"processingFee\": 1.5," 
                    + "\"prepaymentPenalty\": 2.0," 
                    + "\"gracePeriodMonths\": 2," 
                    + "\"latePaymentFee\": 0.5"
                    + "}";
            
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + customerToken);  // Use the customer (unauthorized) token
                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            
                // Perform the request, expecting FORBIDDEN (403) status due to insufficient permissions
                ResponseEntity<String> response = restTemplate.exchange("/api/loans", HttpMethod.POST, requestEntity, String.class);
            
                // Assert status is FORBIDDEN (403)
                Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            
                // Optionally log the response
                System.out.println("Response status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
            }
            


            @Test
            @Order(25)
            void backend_testAddLoan_AsBranchManager_Unauthorized() throws Exception {
                // Assuming you have a valid token for a 'BranchManager'
                Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");
            
                int testLoanId = 3;  // Manually specify another loanId to differentiate from other tests
            
                String requestBody = "{"
                    + "\"loanId\": " + testLoanId + ","  // Include loanId in the request body
                    + "\"loanType\": \"Personal\"," 
                    + "\"interestRate\": 7.5," 
                    + "\"maxAmount\": 50000.0," 
                    + "\"minAmount\": 5000.0," 
                    + "\"minTenureMonths\": 12," 
                    + "\"maxTenureMonths\": 60," 
                    + "\"description\": \"Personal loan for various needs.\"," 
                    + "\"status\": \"Active\"," 
                    + "\"processingFee\": 1.5," 
                    + "\"prepaymentPenalty\": 2.0," 
                    + "\"gracePeriodMonths\": 2," 
                    + "\"latePaymentFee\": 0.5"
                    + "}";
            
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + branchManagerToken);  // Use the BranchManager (unauthorized) token
                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            
                // Perform the request, expecting FORBIDDEN (403) status due to insufficient permissions
                ResponseEntity<String> response = restTemplate.exchange("/api/loans", HttpMethod.POST, requestEntity, String.class);
            
                // Assert status is FORBIDDEN (403)
                Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            
                // Optionally log the response
                System.out.println("Response status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
            }
            




@Test
@Order(26)
void backend_testAddLoanDisbursement_AsBranchManager_Unauthorized() throws Exception {
    // Assuming you have a valid token for a 'BranchManager'
    Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");

    String requestBody = "{"
        + "\"loanDisbursementId\": 2,"  // A different disbursement ID
        + "\"disbursementDate\": \"2024-09-01T10:15:30\"," 
        + "\"disbursementAmount\": 5000.0," 
        + "\"disbursementMethod\": \"Bank Transfer\"," 
        + "\"status\": \"Completed\"," 
        + "\"remarks\": \"Disbursement for loan application ID 1\"," 
        + "\"loanApplication\": { \"loanApplicationId\": 1 }" 
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + branchManagerToken);  // Use the BranchManager (unauthorized) token
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    // Perform the request, expecting FORBIDDEN (403) status due to insufficient permissions
    ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements", HttpMethod.POST, requestEntity, String.class);

    // Assert status is FORBIDDEN (403)
    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    // Optionally log the response
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}



@Test
@Order(27)
void backend_testAddLoanDisbursement_AsCustomer_Unauthorized() throws Exception {
    // Assuming you have a valid token for a 'Customer'
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    String requestBody = "{"
        + "\"loanDisbursementId\": 3,"  // A different disbursement ID
        + "\"disbursementDate\": \"2024-09-01T10:15:30\"," 
        + "\"disbursementAmount\": 5000.0," 
        + "\"disbursementMethod\": \"Bank Transfer\"," 
        + "\"status\": \"Completed\"," 
        + "\"remarks\": \"Disbursement for loan application ID 1\"," 
        + "\"loanApplication\": { \"loanApplicationId\": 1 }" 
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + customerToken);  // Use the Customer (unauthorized) token
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    // Perform the request, expecting FORBIDDEN (403) status due to insufficient permissions
    ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements", HttpMethod.POST, requestEntity, String.class);

    // Assert status is FORBIDDEN (403)
    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    // Optionally log the response
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}


@Test
@Order(28)
void backend_testUpdateLoanDisbursement_AsCustomer_Unauthorized() throws Exception {
    // Assuming you have a valid token for a 'Customer'
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    int testLoanDisbursementId = 1;  // Specify a loanDisbursementId to update

    String requestBody = "{"
        + "\"loanDisbursementId\": " + testLoanDisbursementId + "," 
        + "\"disbursementDate\": \"2024-09-02T11:20:30\"," 
        + "\"disbursementAmount\": 6000.0," 
        + "\"disbursementMethod\": \"Cheque\"," 
        + "\"status\": \"Pending\"," 
        + "\"remarks\": \"Updated remarks\"," 
        + "\"loanApplication\": { \"loanApplicationId\": 1 }" 
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + customerToken);  // Use the unauthorized Customer token
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    // Perform the PUT request, expecting FORBIDDEN (403) due to insufficient permissions
    ResponseEntity<String> response = restTemplate.exchange("/api/loandisbursements/" + testLoanDisbursementId, HttpMethod.PUT, requestEntity, String.class);

    // Assert status is FORBIDDEN (403)
    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    // Optionally log the response for debugging purposes
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}



@Test
@Order(29)
void backend_testAddLoanApplication_AsLoanManager_Unauthorized() throws Exception {
    // Assuming you have a valid token for a 'LoanManager'
    Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");

    String requestBody = "{"
        + "\"loanApplicationId\": 1,"  // Manually pass the ID as needed
        + "\"applicationDate\": \"2024-09-17\","
        + "\"loanAmount\": 15000.0,"
        + "\"tenureMonths\": 24,"
        + "\"applicationStatus\": \"Pending\","
        + "\"employmentStatus\": \"Employed\","
        + "\"annualIncome\": 50000.0,"
        + "\"remarks\": \"Urgent loan requirement.\"," 
        + "\"proof\": \"Base64EncodedString\","
        + "\"accountHolder\": \"John Doe\"," 
        + "\"accountNumber\": \"1234567890\"," 
        + "\"ifscCode\": \"IFSC0001234\","
        + "\"user\": {\"userId\": 1},"
        + "\"loan\": {\"loanId\": 1}"
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + loanManagerToken);  // Using LoanManager's token
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    // Perform the POST request, expecting FORBIDDEN (403) due to insufficient permissions
    ResponseEntity<String> response = restTemplate.exchange("/api/loanapplications", HttpMethod.POST, requestEntity, String.class);

    // Assert status is FORBIDDEN (403)
    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    // Optionally log the response for debugging purposes
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}



@Test
@Order(30)
void backend_testAddLoanApplication_AsBranchManager_Unauthorized() throws Exception {
    // Assuming you have a valid token for a 'BranchManager'
    Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");

    String requestBody = "{"
        + "\"loanApplicationId\": 1,"  // Manually pass the ID as needed
        + "\"applicationDate\": \"2024-09-17\","
        + "\"loanAmount\": 15000.0,"
        + "\"tenureMonths\": 24,"
        + "\"applicationStatus\": \"Pending\","
        + "\"employmentStatus\": \"Employed\","
        + "\"annualIncome\": 50000.0,"
        + "\"remarks\": \"Urgent loan requirement.\"," 
        + "\"proof\": \"Base64EncodedString\","
        + "\"accountHolder\": \"John Doe\"," 
        + "\"accountNumber\": \"1234567890\"," 
        + "\"ifscCode\": \"IFSC0001234\","
        + "\"user\": {\"userId\": 1},"
        + "\"loan\": {\"loanId\": 1}"
        + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + branchManagerToken);  // Using BranchManager's token
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    // Perform the POST request, expecting FORBIDDEN (403) due to insufficient permissions
    ResponseEntity<String> response = restTemplate.exchange("/api/loanapplications", HttpMethod.POST, requestEntity, String.class);

    // Assert status is FORBIDDEN (403)
    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    // Optionally log the response for debugging purposes
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}


@Test
@Order(31)
void backend_testUpdateLoan_AsCustomer_Unauthorized() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");
    
    int loanId = 1; // Replace with an actual loan ID if needed
    
    String requestBody = "{"
        + "\"loanType\": \"Home\"," 
        + "\"interestRate\": 6.0,"
        + "\"maxAmount\": 200000.0,"
        + "\"minAmount\": 10000.0,"
        + "\"minTenureMonths\": 24," 
        + "\"maxTenureMonths\": 120," 
        + "\"description\": \"Home loan for purchasing property.\"," 
        + "\"status\": \"Active\"," 
        + "\"processingFee\": 1.0," 
        + "\"prepaymentPenalty\": 1.5," 
        + "\"gracePeriodMonths\": 3," 
        + "\"latePaymentFee\": 0.4"
        + "}";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + customerToken); // Using Customer token
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    
    // Perform the PUT request, expecting FORBIDDEN (403) due to insufficient permissions
    ResponseEntity<String> response = restTemplate.exchange("/api/loans/{id}", HttpMethod.PUT, requestEntity, String.class, loanId);
    
    // Assert status is FORBIDDEN (403)
    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    // Optionally log the response for debugging purposes
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
}


@Test
@Order(32) // Ensure the order is unique and appropriate
void backend_testAddFeedbackByCustomer() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    String requestBody = "{"
            + "\"feedbackId\": 1,"
            + "\"feedbackText\": \"Great application, really user-friendly!\","
            + "\"date\": \"2024-09-15\","
            + "\"user\": {"
            + "\"userId\": " + 1
            + "}"
            + "}";

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + customerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/feedback", HttpMethod.POST, requestEntity,
            String.class);

    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
}

@Test
@Order(33) // Ensure the order is unique and appropriate
void backend_testAddFeedbackByBranchManager() throws Exception {
    Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");

    String requestBody = "{"
            + "\"feedbackId\": 1,"
            + "\"feedbackText\": \"Great application, really user-friendly!\","
            + "\"date\": \"2024-09-15\","
            + "\"user\": {"
            + "\"userId\": " + 1
            + "}"
            + "}";

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + branchManagerToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/feedback", HttpMethod.POST, requestEntity,
            String.class);

    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
}

@Test
@Order(34)
void backend_testGetAllFeedbackByLoanManager() throws Exception {
    Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + loanManagerToken);

    ResponseEntity<String> response = restTemplate.exchange("/api/feedback", HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
}

@Test
@Order(35)
void backend_testGetAllFeedbackByBranchManager() throws Exception {
    Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + branchManagerToken);

    ResponseEntity<String> response = restTemplate.exchange("/api/feedback", HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
}

@Test
@Order(36)
void backend_testGetAllFeedbackByCustomer() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + customerToken);

    ResponseEntity<String> response = restTemplate.exchange("/api/feedback", HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class);

    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
}

@Test
@Order(37)
void backend_testGetFeedbackByUserIdAsCustomer() throws Exception {
    Assertions.assertNotNull(customerToken, "Customer token should not be null");

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + customerToken);

    ResponseEntity<String> response = restTemplate.exchange(
            "/api/feedback/user/" + 1, // Adjust the userId as necessary for your test
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
}

@Test
@Order(38)
void backend_testGetFeedbackByUserIdAsLoanManager() throws Exception {
    Assertions.assertNotNull(loanManagerToken, "LoanManager token should not be null");

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + loanManagerToken);

    ResponseEntity<String> response = restTemplate.exchange(
            "/api/feedback/user/" + 1, // Adjust the userId as necessary for your test
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class);

    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
}

@Test
@Order(39)
void backend_testGetFeedbackByUserIdAsBranchManager() throws Exception {
    Assertions.assertNotNull(branchManagerToken, "BranchManager token should not be null");

    HttpHeaders headers = createHeaders();
    headers.set("Authorization", "Bearer " + branchManagerToken);

    ResponseEntity<String> response = restTemplate.exchange(
            "/api/feedback/user/" + 1, // Adjust the userId as necessary for your test
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class);

    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeaders().getContentType().toString());
}



@Test
@Order(40)
void backend_testInvalidCredentialsLoginCustomer() throws Exception {
    // Use incorrect credentials
    String requestBody = "{\"email\": \"customer@gmail.com\", \"password\": \"wrongpassword\"}";

    ResponseEntity<String> response = restTemplate.postForEntity("/api/users/login",
            new HttpEntity<>(requestBody, createHeaders()), String.class);

    // Assert status is FORBIDDEN (403)
    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    
}

}









