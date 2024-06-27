package bbd.miniconomy.lifeinsurance.services.api.utils;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


@Component
public class APIHandler {

    private final RestTemplate restTemplate;

    public APIHandler(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Executes an API call once without retry logic.
     *
     * @param <T>                  Type of the request body.
     * @param <R>                  Type of the response body.
     * @param apiEndpoint          URL endpoint of the API.
     * @param httpMethod           HTTP method for the API call (GET, POST, etc.).
     * @param httpHeaders          Headers to be sent with the request.
     * @param requestBody          Request body object.
     * @param classToConvertBodyTo Class type to convert the response body to.
     * @return ResponseEntity containing the response body and HTTP status.
     */
    public <T, R> ResponseEntity<R> callAPIOnce(String apiEndpoint, HttpMethod httpMethod, HttpHeaders httpHeaders, T requestBody, Class<R> classToConvertBodyTo) {
        return callAPI(apiEndpoint, httpMethod, httpHeaders, requestBody, classToConvertBodyTo);
    }

    /**
     * Executes an API call with retry logic for 5xx server errors.
     *
     * @param <T>                  Type of the request body.
     * @param <R>                  Type of the response body.
     * @param apiEndpoint          URL endpoint of the API.
     * @param httpMethod           HTTP method for the API call (GET, POST, etc.).
     * @param httpHeaders          Headers to be sent with the request.
     * @param requestBody          Request body object.
     * @param classToConvertBodyTo Class type to convert the response body to.
     * @return ResponseEntity containing the response body and HTTP status.
     * @throws Http5xxException if the API call results in a 5xx server error after retries.
     */
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 10000), retryFor = Http5xxException.class)
    public <T, R> ResponseEntity<R> callAPIWithRetryLogic(String apiEndpoint, HttpMethod httpMethod, HttpHeaders httpHeaders, T requestBody, Class<R> classToConvertBodyTo) {
        try {
            return callAPI(apiEndpoint, httpMethod, httpHeaders, requestBody, classToConvertBodyTo);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                throw new Http5xxException(e.getMessage());
            }

            throw e;
        }
    }

    /**
     * Private method to execute the API call using RestTemplate.
     */
    private <T, R> ResponseEntity<R> callAPI(String apiEndpoint, HttpMethod httpMethod, HttpHeaders httpHeaders, T requestBody, Class<R> classToConvertBodyTo) {
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        return restTemplate.exchange(apiEndpoint, httpMethod, requestEntity, classToConvertBodyTo);
    }
}