package com.stockstrategyanalyzer.strategy;

import com.stockstrategy.simulator.aggregate.BuySellDetailCollector;
import com.stockstrategy.statistic.data.ConfigBasedStrategyFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dave on 2016/5/11.
 */
@RestController
@RequestMapping("/StockStrategyAnalyzer")
public class StrategyController {
    @RequestMapping(value="/strategies", method = RequestMethod.GET)
    public Map<String, Properties> getStrategies(){
        Map<String, Properties> strategies = ConfigBasedStrategyFactory.getStrategyProperties();
        Map<String, Properties> strategiesWithName = new HashMap<>();
        strategies.forEach((name, properties)->{
            Properties clonedProperties = new Properties();
            properties.forEach((key, value)-> clonedProperties.setProperty((String)key, (String)value));
            clonedProperties.setProperty("name", name);
            strategiesWithName.put(name, clonedProperties);
        });
        return strategiesWithName;
    }

    @RequestMapping(value = "/strategies", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void saveUser(@RequestBody Map<String, String> properties) {
        String name = properties.get("name");
        properties.remove("name");
        ConfigBasedStrategyFactory.saveStrategyProperties(name, properties);
    }

}
