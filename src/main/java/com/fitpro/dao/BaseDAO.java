package com.fitpro.dao;

import com.fitpro.exception.AppException;

import java.util.List;
import java.util.Optional;

/**
 * Generic DAO contract (GENERICS). T = entity type, ID = primary key type.
 * Each DAO implements it for its own table.
 */
public interface BaseDAO<T, ID> {
    Optional<T> findById(ID id) throws AppException;

    List<T> findAll() throws AppException;
}
