package by.logonuk.repository;

import by.logonuk.domain.User;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<K, T> {

    int DEFAULT_FIND_ALL_LIMIT = 10;
    int DEFAULT_FIND_ALL_OFFSET = 0;

    T findById(K id);

    List<T> findByLine(String line);

    Optional<T> findOne(K id);

    List<T> findAll();

    List<T> findAll(int limit, int offset);

    T create(T object);

    T update(T object);

    K delete(K id);
}
