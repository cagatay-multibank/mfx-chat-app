package com.multibankfx.chatapp.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.scheduling.annotation.Async;

import java.io.Serializable;

@NoRepositoryBean
public interface AbstractRepository  <T extends MongoRepository<T,ID>, ID extends Serializable>  {


}
