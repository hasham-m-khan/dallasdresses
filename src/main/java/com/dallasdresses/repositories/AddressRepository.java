package com.dallasdresses.repositories;

import com.dallasdresses.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<List<Address>> findByUserId(Long userId);

    @Query("SELECT a FROM Address a JOIN FETCH a.country WHERE a.user.id = :userId")
    List<Address> findByUserIdWithCountry(Long userId);

    Optional<Address> findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
            Long userId,
            String addressLine1,
            String city,
            String state,
            String postalCode
    );
}
