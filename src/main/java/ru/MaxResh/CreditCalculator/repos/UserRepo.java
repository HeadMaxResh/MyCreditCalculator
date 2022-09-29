package ru.MaxResh.CreditCalculator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MaxResh.CreditCalculator.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserLogin(final String userLogin);
}
