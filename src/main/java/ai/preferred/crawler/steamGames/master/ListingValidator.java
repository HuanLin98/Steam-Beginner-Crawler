package ai.preferred.crawler.steamGames.master;

import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.Response;
import ai.preferred.venom.response.VResponse;
import ai.preferred.venom.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListingValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingValidator.class);

    @Override
    public Validator.Status isValid(Request request, Response response) {

        final VResponse vResponse = new VResponse(response);
        String jsonString = vResponse.getHtml();

        if (jsonString.substring(0,10).contains("success")){
            return Status.VALID;
        }
        LOGGER.info("Invalid content");
        return Status.INVALID_CONTENT;
    }

}