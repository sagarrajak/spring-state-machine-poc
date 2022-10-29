package sagar.springproject.springstatemachinepoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sagar.springproject.springstatemachinepoc.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
