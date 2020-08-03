package ai.preferred.crawler.steamGames.master;

import ai.preferred.venom.Handler;
import ai.preferred.venom.Session;
import ai.preferred.venom.Worker;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.VResponse;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.nodes.Element;

import java.util.List;

public class BaseHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseHandler.class);

    @Override
    public void handle(Request request, VResponse response, Scheduler scheduler, Session session, Worker worker) {
        // Log when there's activity
        LOGGER.info("Processing {}", request.getUrl());

        // The array list to put your results
        final List<String> links = session.get(BaseCrawler.LINK_LIST_KEY);

        // Some vars you may need
        final Document document = response.getJsoup();
        
        // filter out game genres from the steam website
        Elements tabs = document.select("div.popup_menu_twocol > div.popup_menu > a.popup_menu_item");
                    
        for (Element tab : tabs) {
            String url = tab.attr("href");
            if (url.contains("tags")) {
                links.add(tab.text());
            }
        }
    }
}