package com.revature.reimbursement.daos;
import java.util.List;

public interface CrudDAO<T> {
    void save(T obj);
    void update(T obj);
    void delete(String id);
    T getById(String id);
    List<T> getAll();
    T getRowByColumnValue(String column, String value);
    List<T> getAllRowsByColumnValue(String column, String value);
}