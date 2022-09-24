package academy.mindswap.booksome.util.book;

import java.util.Map;
import java.util.stream.Collectors;

public class BookSearchFilter {

    public static Map<String, String> filterSearch(Map<String, String> allParams) {
        return allParams.entrySet().stream()
                .filter(p -> p.getKey().contains("title")
                        || p.getKey().contains("authors")
                        || p.getKey().contains("category")
                        || p.getKey().contains("isbn"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
