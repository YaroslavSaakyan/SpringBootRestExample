package com.yaroslav.springboottest.dao;

import com.yaroslav.springboottest.dao.entity.ServiceResponseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yaroslav
 * Date: 24.06.16
 */
@Repository
public interface ServiceResponseRepository extends CrudRepository<ServiceResponseEntity, String> {
}
