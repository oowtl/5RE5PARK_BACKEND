package com.oreo.finalproject_5re5_be.user.repository;

import com.oreo.finalproject_5re5_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
