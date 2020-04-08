package io.hashimati.neojexample;


import javax.inject.Inject;

import com.intuit.karate.junit5.Karate;

import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;



@MicronautTest
class PersonTest {

    @Inject
    EmbeddedServer embeddedServer;


    @Karate.Test
    Karate testFullPath() {
        return Karate.run("person").relativeTo(getClass());
    }
}