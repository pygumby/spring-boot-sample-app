package com.lambdarookie.popularity.persistence;

import com.lambdarookie.popularity.models.UserName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNameRepository extends CrudRepository<UserName, String> {
}
