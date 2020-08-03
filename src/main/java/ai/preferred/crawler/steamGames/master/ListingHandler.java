package ai.preferred.crawler.steamGames.master;

import ai.preferred.crawler.EntityCSVStorage;
import ai.preferred.crawler.steamGames.entity.Game;
import ai.preferred.venom.Handler;
import ai.preferred.venom.Session;
import ai.preferred.venom.Worker;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.VResponse;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

public class ListingHandler implements Handler{
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingHandler.class);

    @Override
    public void handle (Request request, VResponse response, Scheduler scheduler, Session session, Worker worker) {
        LOGGER.info("processing: {}", request.getUrl());

        String jsonString = response.getHtml();
        String url = request.getUrl();
        String genre = url.substring(url.lastIndexOf('=') + 1);

        JSONObject jsonObj = new JSONObject(jsonString);
        
        String prices = jsonObj.getString("results_html");
        Document document = Jsoup.parse(prices);

        final List<Game> gameList = ListingParser.parseListing(document, genre);

        // stop the method from writing into csv if there is no game found
        if (gameList.isEmpty()) {
            LOGGER.info("there is no results on this page, stopping: {}", request.getUrl());
            return;
        }

        // Get the CSV printer we created
        final EntityCSVStorage<Game> storage = session.get(ListingCrawler.STORAGE_KEY);

        // Use this wrapper for every IO task, this maintains CPU utilisation to speed up crawling
        worker.executeBlockingIO(() -> {
            for (final Game g : gameList) {
              LOGGER.info("storing game: {}", g.getName());
              try {
                storage.append(g);
              } catch (IOException e) {
                LOGGER.error("Unable to store listing.", e);
              }
            }
        });
    }
}