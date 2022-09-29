package ru.MaxResh.CreditCalculator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.MaxResh.CreditCalculator.model.MonthlyAmortizationSchedule;
import ru.MaxResh.CreditCalculator.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepo extends JpaRepository<MonthlyAmortizationSchedule, Long> {
    Optional<MonthlyAmortizationSchedule> findById(final Long id);
    List<MonthlyAmortizationSchedule> findByUser(final User user);
    @Query("select c from MonthlyAmortizationSchedule c where status is null order by startDate")
    List<MonthlyAmortizationSchedule> findConsidered();
    @Query("select c from MonthlyAmortizationSchedule c where status is true order by startDate")
    List<MonthlyAmortizationSchedule> findConfirmed();
    @Query("select c from MonthlyAmortizationSchedule c where status is false order by startDate")
    List<MonthlyAmortizationSchedule> findRejected();

}

