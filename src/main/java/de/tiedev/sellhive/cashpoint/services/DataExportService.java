package de.tiedev.sellhive.cashpoint.services;


import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.SellHiveGame;

import org.json.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//curl -i -X POST -H 'Content-Type: application/json' -d '{"item_ids": [81, 82]}'

@Service
public class DataExportService {

    @Autowired
    GameService gameService;

    @Autowired
    ConfigurationService configurationService;

    public void updateSellHiveSoldStatus() {
        String result = getApi(configurationService.getExportURLSoldStatus(), HttpMethod.POST);
        System.out.println("Result of DataExport " + result);
    }

    private String getApi(final String path, final HttpMethod method) {
        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(createGames().toString(), httpHeaders);
        return restTemplate.postForObject(path,request, String.class);

    }

    private JSONObject createGames() {
        JSONObject gameJsonObject = new JSONObject();
        List<Long> idsOfSoldGames = gameService.gamesSold().stream().
                //Multiple sold articles are not part of the sell-hive database, so they are filtered out.
                filter(game -> !StringUtils.startsWithIgnoreCase(game.getBarcode(), configurationService.getBarcodePrefixForMultipleSoldArticles())).
                        map(Game::getExternalId).toList();

        List<Long> analogItems = new ArrayList<Long>();
        gameJsonObject.put("item_ids", idsOfSoldGames);
        gameJsonObject.put("analog_items", analogItems);
        return gameJsonObject;
    }
}
