package com.example.samplespring.person;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository
        extends JpaRepository<Person, Long> {
    @Query("SELECT p FROM Person p WHERE p.name = ?1")
    Optional<Person> findPersonByName(String email);

    @Query("SELECT p FROM Person p WHERE p.salary >= ?1 AND p.salary <= ?2")
    List<Person> getPersonsByConstraints(Float min, Float max);

    @Query("SELECT p FROM Person p WHERE p.salary >= ?1 AND p.salary <= ?2 ORDER BY p.name")
    List<Person> getPersonsByConstraintsName(Float min, Float max);

    @Query("SELECT p FROM Person p WHERE p.salary >= ?1 AND p.salary <= ?2 ORDER BY p.salary")
    List<Person> getPersonsByConstraintsSal(Float min, Float max);
}
