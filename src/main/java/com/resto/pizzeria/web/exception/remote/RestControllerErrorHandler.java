package com.resto.pizzeria.web.exception.remote;

import com.resto.pizzeria.web.exception.ApiResponseException;
import com.resto.pizzeria.web.exception.ApiValidationException;
import com.resto.pizzeria.web.model.error.ErrorResponseDto;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Gestionnaire des erreurs des réponses de l'API REST.
 */
@Slf4j
public class RestControllerErrorHandler implements ResponseErrorHandler {

    /**
     * Vérifie si la réponse contient une erreur.
     *
     * @param response réponse HTTP
     * @return true si une erreur est présente
     * @throws IOException en cas d'erreur d'entrée/sortie
     */
    @Override
    public boolean hasError(
            final ClientHttpResponse response
    ) throws IOException {
        return response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError();
    }

    /**
     * Gère les erreurs de la réponse HTTP.
     *
     * @param url URL de la requête
     * @param method méthode HTTP
     * @param response réponse HTTP
     * @throws IOException en cas d'erreur d'entrée/sortie
     */
    @Override
    public void handleError(
            final URI url,
            final HttpMethod method,
            final ClientHttpResponse response
    ) throws IOException {
        final HttpStatusCode status = response.getStatusCode();

        final ObjectMapper mapper = new ObjectMapper();
        final ErrorResponseDto error
                = mapper.readValue(response.getBody(), ErrorResponseDto.class);

        final RemoteErrorType type = error.getCodeExtended();
        final String message = error.getMessage();
        final Map<String, String> fieldsErrors = error.getFieldsErrors();

        switch (type) {
            case CODE_NOT_FOUND -> throw new ApiResponseException(
                    "Ressource non trouvée",
                    status.value());

            case CODE_DB_CONSTRAINT_VIOLATION,
                 CODE_NOT_VALIDATED,
                 CODE_INVALID_FORMAT,
                 CODE_UNKNOWN,
                 CODE_DB_INTEGRITY_VIOLATION ->
                    throw new ApiValidationException(
                         message, fieldsErrors);

            default -> {
                if (status.is5xxServerError()) {
                    log.error("Erreur 500 : {}", status.value());
                } else {
                    log.error("{} n'est pas géré : {}", type, message);
                }

                throw new ApiResponseException("Erreur", 400);
            }
        }
    }
}
