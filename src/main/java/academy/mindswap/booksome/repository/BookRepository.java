package academy.mindswap.booksome.repository;

import academy.mindswap.booksome.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    @Query("{ $or: [ { title: { $regex: /?0/, $options: 'i' } }, { authors: { $in: [ /^?1/i ] } }, " +
            "{ category: { $in: [ /^?2/i ] } }, { isbn: { $regex: /?3/, $options: 'i' } } ] }")
    List<Book> findAll(String title, String authors, String category, String isbn);

    Boolean existsByIsbn(String isbn);
    
    Book findByIsbn(String isbn);
}
