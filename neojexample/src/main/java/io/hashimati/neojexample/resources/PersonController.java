package io.hashimati.neojexample.resources;

import java.util.HashMap;

import io.hashimati.neojexample.domain.Person;
import io.hashimati.neojexample.repository.PersonRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import io.micronaut.http.annotation.PathVariable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Controller
public class PersonController {


    private final PersonRepository personRepository; 
    public PersonController( PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @Get("/add")
    public Single<Person> save(String n, String i, String a)
    {
        final Person p = new Person(); 

        p.setAge(Integer.parseInt(a)); 
        p.setName(n); 
        p.setId(i); 
        return personRepository.save(p); 
    }

    @Post("/save")
    public Single<Person> save(@Body Person person)
    {
        return personRepository.save(person); 
    }
    @Get("/findById")
    public Single<Person> findById(String id)
    {
        return personRepository.findById(id); 
        
    }
    @Get("/get")
    public Flowable<Person> getName(String n)
    {
        return personRepository.findNameContains(n); 
    }

    @Get("/deleteGraph")
    public boolean cleanCollections()
    {
        return personRepository.cleanUp(); 
    }

    @Delete("/delete/{id}")
    public boolean deleteById(@PathVariable("id") String id){
        
        return personRepository.deleteById(id); 

    }
}