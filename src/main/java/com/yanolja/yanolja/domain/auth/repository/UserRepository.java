package com.yanolja.yanolja.domain.auth.repository;

import com.yanolja.yanolja.domain.auth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}