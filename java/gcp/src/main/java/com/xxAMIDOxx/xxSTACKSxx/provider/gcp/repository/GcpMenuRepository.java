package com.xxAMIDOxx.xxSTACKSxx.provider.gcp.repository;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GcpMenuRepository extends FirestoreReactiveRepository<GcpMenu> {

  /**
   * Query is constructed OOTB- out of the box, executed and results are fetched based param
   * restaurantId. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @return page of menu
   */
  Flux<GcpMenu> findAllByRestaurantId(String restaurantId, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param searchTerm Menu name
   * @param pageable pagination
   * @return page of menu
   */
  Flux<GcpMenu> findAllByNameContaining(String searchTerm, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * restaurantId and searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @param searchTerm Menu name
   * @param pageable pagination
   * @return page of menu
   */
  Flux<GcpMenu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * restaurantId and searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId restaurant Id
   * @param name menu name
   * @param pageable paging requirements
   * @return
   */
  Flux<GcpMenu> findAllByRestaurantIdAndName(String restaurantId, String name, Pageable pageable);
}
