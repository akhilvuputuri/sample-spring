package com.example.samplespring.person;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPersons(@RequestParam Map<String, String> req) {
        Float min = new Float(req.getOrDefault("min", "0"));
        Float max = new Float(req.getOrDefault("max", "4000"));
        Integer offset = new Integer(req.getOrDefault("offset", "0"));
        Integer limit = new Integer(req.getOrDefault("limit", "99999999"));
        if (req.containsKey("sort")) {
            if (req.get("sort").equals("NAME")) {
                List<Person> persons = personRepository.getPersonsByConstraintsName(min, max);
                return persons.subList(
                        Math.min(persons.size(), offset),
                        Math.min(persons.size(), offset + limit));
            } else if (req.get("sort").equals("SALARY")) {
                List<Person> persons = personRepository.getPersonsByConstraintsSal(min, max);
                return persons.subList(
                        Math.min(persons.size(), offset),
                        Math.min(persons.size(), offset + limit));
            }
        }
        List<Person> persons = personRepository.getPersonsByConstraints(min, max);
        return persons.subList(
                Math.min(persons.size(), offset),
                Math.min(persons.size(), offset + limit));
    }

    public void addNewPerson(Person person) {
        Optional<Person> personOptional = personRepository
                .findPersonByName(person.getName());
        if (personOptional.isPresent()) {
            throw new IllegalStateException("name exists");
        }
        personRepository.save(person);
    }

    public void addFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Empty File");
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Person> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Person.class)
                        .withSeparator(',')
                        .withSkipLines(1)
//                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<Person> persons = csvToBean.parse().stream()
                        .filter(p -> p.getSalary() > 0).collect(Collectors.toList());
                System.out.println(persons);
                // TODO: save users in DB?
                for (Person person: persons) {
                    Optional<Person> personOptional = personRepository
                            .findPersonByName(person.getName());
                    if (personOptional.isPresent()) {
                        personRepository.delete(personOptional.get());
                    }
                    personRepository.save(person);
                }
                // save users list on model
//                model.addAttribute("users", users);
//                model.addAttribute("status", true);

            } catch (Exception ex) {
                throw new IllegalStateException("Error parsing CSV");
            }
        }
    }
}
