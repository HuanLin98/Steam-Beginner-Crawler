package ai.preferred.crawler.steamGames.master;

import ai.preferred.crawler.steamGames.entity.Game;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ListingParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingParser.class);

    private ListingParser() {
        throw new UnsupportedOperationException();
    }

    static List<Game> parseListing(Document document, String genre) {
    
        // split into games to parse
        System.out.println("parsing document " + genre);
        genre = genre.replace("%20", " ");
        final Elements games = document.select("a");
        final ArrayList<Game> result = new ArrayList<>(games.size());
        for (final Element g : games) {
            System.out.println("parsing game " + genre);
            Game game = parseGame(g, genre);
            if (game.getName() != null) {
                result.add(game);
            }
        }
        return result;
    }

    private static String textOrNull(Element element) {
        return null == element ? null : element.text();
    }

    private static Game parseGame(Element e, String genre) {
        LOGGER.info("parsing game");
        Game game = new Game();
        
        game.setName(textOrNull(e.selectFirst("div > div.tab_item_name")));
        if (!(game.getName() == null)) {
            game.setCategory(genre);
        }
        
        game.setImageUrl(e.select("img").attr("src"));
        
        game.setPrice(textOrNull(e.selectFirst("div > div.discount_final_price")));
        
        game.setDiscount(textOrNull(e.selectFirst("div > div.discount_pct")));
        
        Elements tagElements = e.select("div.tab_item_top_tags > span.top_tag");
        // appending all the tags together
        String tags = "";
        for (Element tag : tagElements) {
            tags += tag.text();
        }

        game.setTags(tags);

        LOGGER.info("pasing finished");
        return game;
    }
}