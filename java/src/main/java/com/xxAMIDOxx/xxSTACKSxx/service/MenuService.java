package com.xxAMIDOxx.xxSTACKSxx.service;

import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuService {

    /**
     * Retrieve list of all available Menus
     *
     * @param pageNumber
     * @param pageSize
     *
     * @return List of MenuDTO
     */
    List<Menu> findAll(int pageNumber, int pageSize);

    /**
     * Retrieve MenuDTO by MenuDTO Id
     *
     * @param id
     *
     * @return Optional MenuDTO
     */
    Optional<Menu> findById(UUID id);

    /**
     * Retrieve MenuDTO by Restaurant Id
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId
     * @param pageSize
     * @param pageNumber
     *
     * @return List of MenuDTO
     */
    public List<Menu> findAllByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber);


    /**
     * Retrieve MenuDTO's by matching the name (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param searchTerm
     * @param pageSize
     * @param pageNumber
     *
     *
     * @return List of MenuDTO
     */
    public List<Menu> findAllByNameContaining(String searchTerm, Integer pageSize, Integer pageNumber);

    /**
     * Retrieve MenuDTO's by matching the name and the restaurantId (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId
     * @param searchTerm
     * @param pageSize
     * @param pageNumber
     *
     * @return List of MenuDTO
     */
    List<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber);
}
