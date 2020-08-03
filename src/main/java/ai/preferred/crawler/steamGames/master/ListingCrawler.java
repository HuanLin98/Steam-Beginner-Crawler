package ai.preferred.crawler.steamGames.master;

import ai.preferred.crawler.EntityCSVStorage;
import ai.preferred.venom.Crawler;
import ai.preferred.venom.Session;
import ai.preferred.venom.SleepScheduler;
import ai.preferred.venom.fetcher.AsyncFetcher;
import ai.preferred.venom.fetcher.Fetcher;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.validator.EmptyContentValidator;
import ai.preferred.venom.validator.StatusOkValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.preferred.crawler.steamGames.entity.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class ListingCrawler {
    // Create session keys for CSV printer to print from handler
    static final Session.Key<EntityCSVStorage<Game>> STORAGE_KEY = new Session.Key<>();

    // You can use this to log to console
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingCrawler.class);

    public static void main(String[] args) {
        // Get file to save to
        final String filename = "data/games.csv";

        // Start CSV printer
        try (EntityCSVStorage<Game> storage = new EntityCSVStorage<>(filename)) {

            final Session session = Session.builder().put(STORAGE_KEY, storage).build();

            // Start crawler
            try (Crawler crawler = createCrawler(createFetcher(), session).start()) {
                LOGGER.info("starting crawler...");
                
                String startUrl = "https://store.steampowered.com/";

                List<String> links = new ArrayList<>();

                links = BaseCrawler.linkGetter(startUrl);
                for (String link : links) {
                    for (int i = 0; i <= 30; i += 15) {
                        link = link.replace(" ", "%20");
                        String specialUrl = "https://store.steampowered.com/contenthub/querypaginated/tags/NewReleases/render/?query=&start=" + i + "&count=15&cc=SG&l=english&v=4&tag=" + link;
                        crawler.getScheduler().add(new VRequest(specialUrl), new ListingHandler());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Could not run crawler: ", e);
            }
        } catch (IOException e) {
            LOGGER.error("unable to open file: {}, {}", filename, e);
        }
    }

    private static Fetcher createFetcher() {
        return AsyncFetcher.builder()
                .setValidator(new EmptyContentValidator(), new StatusOkValidator(), new ListingValidator()).build();
    }

    private static Crawler createCrawler(Fetcher fetcher, Session session) {

        return Crawler.builder().setFetcher(fetcher).setSession(session)
                .setSleepScheduler(new SleepScheduler(1500, 3000)).build();
    }

}