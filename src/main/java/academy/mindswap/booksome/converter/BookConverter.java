package academy.mindswap.booksome.converter;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.book.SaveBookDto;

import academy.mindswap.booksome.model.Book;
import org.modelmapper.ModelMapper;

public class BookConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static BookDto convertBookToBookDto(Book book){
        return modelMapper.map(book, BookDto.class);
    }

    public static Book convertBookDtoToBook(BookDto bookDto){
        return modelMapper.map(bookDto, Book.class);
    }

    public static Book convertBookClientDtoToBook(BookClientDto bookClientDto){
        return modelMapper.map(bookClientDto, Book.class);
    }



}
