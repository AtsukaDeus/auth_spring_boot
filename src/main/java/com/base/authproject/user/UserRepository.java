package com.base.authproject.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<_User, Long>{

    Optional<_User> findByUsername(String username);

}
