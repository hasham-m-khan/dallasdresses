package com.dallasdresses.repositories;

import com.dallasdresses.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    public Optional<List<Address>> findByUserId(Long userId);

    Optional<Address> findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
            Long userId,
            String addressLine1,
            String city,
            String state,
            String postalCode
    );

    Optional<Address> findByUserIdAndAddressType(Long userId, String addressType);

    boolean existsByUserId(Long userId);
}
