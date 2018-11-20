package com.knu.ssingssing2.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JPAReservationRepository {

  @PersistenceContext
  private EntityManager entityManager;

//  // location도 고려해야 함
//  public List<Scooter> findAllAvailableReservationScootersWithModelName(String modelName, Instant now) {
//    TypedQuery<Scooter> query = entityManager.createQuery(
//        "select o from Scooter o " +
//            "where o.modelName = :modelName " +
//            "and "
//    )
//  }

}
