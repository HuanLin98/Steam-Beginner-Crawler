package ai.preferred.crawler.steamGames.master;

import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.Response;
import ai.preferred.venom.response.VResponse;
import ai.preferred.venom.validator.Validator;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseValidator implements Validator{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseValidator.class);

    @Override
  public Status isValid(Request request, Response response) {
    
    LOGGER.info("Validating {}", request.getUrl());

    final VResponse vResponse = new VResponse(response);

    final Document document = vResponse.getJsoup();
    
    if (document.title().contains("Welcome to Steam")) {
      return Status.VALID;
    }

    return Status.INVALID_CONTENT;
  }
}