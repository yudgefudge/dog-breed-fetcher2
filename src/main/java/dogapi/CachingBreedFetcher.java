package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private int callsMade = 0;
    private HashMap<String, List<String>> breeds = new HashMap<>();
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (breeds.containsKey(breed)) {
            return breeds.get(breed);
        } else {
            callsMade++;
            try {
                List<String> subBreeds = fetcher.getSubBreeds(breed);
                breeds.put(breed, new ArrayList<>(subBreeds)); // only cache successful responses
                return new ArrayList<>(subBreeds);
            } catch (BreedNotFoundException e) {
                throw e;
            }
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}