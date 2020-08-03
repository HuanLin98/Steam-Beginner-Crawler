package ai.preferred.crawler.steamGames.master;

import ai.preferred.venom.Crawler;
import ai.preferred.venom.Handler;
import ai.preferred.venom.Session;
import ai.preferred.venom.fetcher.Fetcher;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.fetcher.AsyncFetcher;
import ai.preferred.venom.validator.EmptyContentValidator;
import ai.preferred.venom.validator.StatusOkValidator;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseCrawler {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BaseCrawler.class);

    public static Fetcher createFetcher() {
        // Create a new fetcher here
        final Fetcher fetcher = AsyncFetcher.builder()
          .setValidator(
              new EmptyContentValidator(),
              new StatusOkValidator(),
              new BaseValidator())
          .build();
    
        return fetcher;
    }

    static final Session.Key<List<String>> LINK_LIST_KEY = new Session.Key<>();

    public static Session createSession(List<String> links) {
        // Create a session here
        
        final Session session = Session.builder().put(LINK_LIST_KEY, links).build();
    
        return session;
    }

    public static Crawler createCrawler(Fetcher fetcher, Session session) {
        // Create a new crawler here
        final Crawler crawler = Crawler.builder().setFetcher(fetcher).setSession(session).build();
    
        return crawler.start();
    }

    public static List<String> linkGetter (String url) throws Exception{
        List <String> links = new ArrayList<>();
        try (Crawler crawler = createCrawler(createFetcher(), createSession(links))){
            final Request request = new VRequest(url);
            final Handler handler = new BaseHandler();

            crawler.getScheduler().add(request, handler);
        } 
        LOGGER.info("You hvae crawled {} game genre links", links.size());
        return links;
    }
}