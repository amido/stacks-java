package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MenuServiceImpl implements MenuService {

    private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> all(int pageNumber, int pageSize) {

        int currentPage = 0;
        final Sort sort = Sort.by(Sort.Direction.ASC, "Name");

        final CosmosPageRequest pageRequest = new CosmosPageRequest(
                currentPage, pageSize, null, sort);

        Page<Menu> page = this.menuRepository.findAll(pageRequest);
        logger.debug("Total Records: {}", page.getTotalElements());
        logger.debug("Total Pages: {}", page.getTotalPages());

        while (currentPage < pageNumber && page.hasNext()) {
            currentPage++;
            Pageable nextPageable = page.nextPageable();
            page = this.menuRepository.findAll(nextPageable);
        }
        return page.getContent();
    }

    public Optional<Menu> findById(UUID id) {
        return this.menuRepository.findById(id.toString());
    }
}