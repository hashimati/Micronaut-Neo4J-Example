package io.hashimati.neojexample.repository;

import static java.util.Collections.singletonMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;

import io.hashimati.neojexample.domain.Person;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.reactivex.Flowable;
import io.reactivex.Single;  
@Singleton
public class PersonRepository {


    @Inject
    private Driver driver; 
    public Single<Person> save(final Person person)
    {
        try(final Session s = driver.session())
        {
            final String statement = "MERGE (person:Person {id:$person.id}) ON CREATE SET person+=$person";
            final ObjectMapper om = new ObjectMapper(); 
        
            final String result =   s.writeTransaction(
                t-> t.run(statement, singletonMap("person", om.convertValue(person,HashMap.class))))
                .summary().toString(); 
                
            System.out.println(result); 
            return Single.just(person); 
        }
        catch(Exception ex)
        {

            return null; 
        }
    }

    public Single<Person> findById(String id)
    {

        try(Session session = driver.session()){
            final ObjectMapper om= new ObjectMapper();
            final String query = "MATCH (person: Person) WHERE person.id = $id RETURN person"; 
           
            Map objectAsMap =session.readTransaction(
                t->t.run(query, singletonMap("id", id))
            ).single()
            .get("person")
            .asMap(); 
            
            Person person = om.convertValue(objectAsMap, Person.class); 
            
            return Single.just(person); 
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null; 

        }
    }

    public Flowable<Person> findNameContains(final String name)
    {
        
        try(final Session s = driver.session())
        {

            final ObjectMapper om = new ObjectMapper(); 
            final String query = "Match (person: Person) where person.name contains $name return person" ; 
    
          List<Person> people =  
          s.readTransaction(
              t -> t.run(query, singletonMap("name", name))
            )
           .list()
           .stream()
           .map(x->om.convertValue(x.get("person").asMap(), Person.class))
           .collect(Collectors.toList()); 
            
           return  Flowable.fromIterable(people); 
        }
    }
    public boolean cleanUp(){

        try(Session s = driver.session())
        {
            s.writeTransaction(t-> t.run("MATCH (p:Person) DELETE p")); 

            return true; 
        }
        catch(Exception ex)
        {

            return false; 
        }
    }
    @EventListener
    private void setIndex(StartupEvent event)
    {
        try(final Session s= driver.session())
        {
            final String statement = "CREATE INDEX [id_index] FOR (person:Person) ON (person.id)"; 
            s.writeTransaction(t-> t.run(statement)); 

        }

    }
	public boolean deleteById(String id) {
        try(Session s = driver.session())
        {
            s.writeTransaction(t-> t.run("MATCH (p:Person) where p.id =$id DELETE p", singletonMap("id", id))); 
            return true; 
        }
        catch(Exception ex)
        {

            return false; 
        }

    }

   
}