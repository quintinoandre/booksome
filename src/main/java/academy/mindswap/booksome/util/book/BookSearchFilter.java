package academy.mindswap.booksome.util.book;

import java.util.Map;
import java.util.stream.Collectors;

import static academy.mindswap.booksome.util.book.BookConstant.*;

public final class BookSearchFilter {
    private BookSearchFilter() {
    }

    public static Map<String, String> filterSearch(Map<String, String> allParams) {
        return allParams.entrySet().stream()
                .filter(p -> p.getKey().contains(TITLE)
                        || p.getKey().contains(AUTHORS)
                        || p.getKey().contains(CATEGORY)
                        || p.getKey().contains(ISBN))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
