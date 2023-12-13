package ir.map.g221.guisocialnetwork.persistence.pagingrepos;
import java.util.stream.Stream;

public interface Page<E> {
    Pageable getPageable();

    Pageable nextPageable();

    Stream<E> getContent();
}
