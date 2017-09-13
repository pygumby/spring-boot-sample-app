package com.lambdarookie.popularity.persistence;

import com.lambdarookie.popularity.models.UserName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
// `UserNameRepository` inherits methods to save, delete and find `UserName` entities by extending `CrudRepository`.
// Spring Data JPA provides an implementation of this interface at run-time, so it does not have to be provided.
public interface UserNameRepository extends CrudRepository<UserName, String> {
}
