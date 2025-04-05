package net.engineeringdigest.journal.app.cache;

import lombok.Getter;
import net.engineeringdigest.journal.app.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journal.app.repository.ConfigJournalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum Keys{
        WEATHER_API;
    }

    @Autowired
    private ConfigJournalRepo configJournalRepo;

    @Getter
    private Map<String, String> appCache;

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> configs = configJournalRepo.findAll();
        configs.forEach(c -> appCache.put(c.getKey(), c.getValue()));
    }

}
