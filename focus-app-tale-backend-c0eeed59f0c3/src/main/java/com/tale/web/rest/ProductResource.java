package com.tale.web.rest;

import com.tale.domain.Product;
import com.tale.repository.ProductRepository;
import com.tale.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Product}.
 */
@RestController
@RequestMapping("/api/products")
@Transactional
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductRepository productRepository;

    public ProductResource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * {@code POST  /products} : Create a new company.
     *
     * @param product the company to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new company, or with status {@code 400 (Bad Request)} if the company has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to save Company : {}", product);
        if (product.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        // Convert base64 logo back to byte array if present
        byte[] logoBytes = null;
        if (product.getLogo() != null && product.getLogo().length == 0) {
            logoBytes = Base64.getDecoder().decode(product.getLogo());
            product.setLogo(logoBytes);  // Save the byte array logo in the company entity
        }

        Product result = productRepository.save(product);
        return ResponseEntity
            .created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /products/:id} : Updates an existing company.
     *
     * @param id the id of the company to save.
     * @param product the company to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated company,
     * or with status {@code 400 (Bad Request)} if the company is not valid,
     * or with status {@code 500 (Internal Server Error)} if the company couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Product product
    ) throws URISyntaxException {
        log.debug("REST request to update Company : {}, {}", id, product);
        if (product.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, product.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Product result = productRepository.save(product);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, product.getId().toString()))
            .body(result);
    }

//    /**
//     * {@code PATCH  /companies/:id} : Partial updates given fields of an existing company, field will ignore if it is null
//     *
//     * @param id the id of the company to save.
//     * @param product the company to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated company,
//     * or with status {@code 400 (Bad Request)} if the company is not valid,
//     * or with status {@code 404 (Not Found)} if the company is not found,
//     * or with status {@code 500 (Internal Server Error)} if the company couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<Product> partialUpdateProduct(
//        @PathVariable(value = "id", required = false) final Long id,
//        @NotNull @RequestBody Product product
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update Company partially : {}, {}", id, product);
//        if (product.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, product.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!productRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<Product> result = productRepository
//            .findById(product.getId())
//            .map(existingCompany -> {
//                if (product.getCompanyId() != null) {
//                    existingCompany.setCompanyId(company.getCompanyId());
//                }
//                if (product.getCompanyName() != null) {
//                    existingCompany.setCompanyName(company.getCompanyName());
//                }
//                if (product.getIndustry() != null) {
//                    existingCompany.setIndustry(company.getIndustry());
//                }
//                if (product.getRevenue() != null) {
//                    existingCompany.setRevenue(company.getRevenue());
//                }
//                if (company.getNumberOfEmployees() != null) {
//                    existingCompany.setNumberOfEmployees(company.getNumberOfEmployees());
//                }
//                if (company.getAddress() != null) {
//                    existingCompany.setAddress(company.getAddress());
//                }
//                if (company.getWebsite() != null) {
//                    existingCompany.setWebsite(company.getWebsite());
//                }
//                if (company.getStatus() != null) {
//                    existingCompany.setStatus(company.getStatus());
//                }
//
//                return existingCompany;
//            })
//            .map(companyRepository::save);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, company.getId().toString())
//        );
//    }

    /**
     * {@code GET  /products} : get all the companies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Companies");
        Page<Product> page = productRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /products/:id} : get the "id" company.
     *
     * @param id the id of the company to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the company, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        log.debug("REST request to get Company : {}", id);
        Optional<Product> company = productRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(company);
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" company.
     *
     * @param id the id of the company to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        log.debug("REST request to delete Product : {}", id);
        productRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
