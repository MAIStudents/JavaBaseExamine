package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class Person {

    @JsonProperty ("full_name")
    private String fullName;

    @JsonProperty ("age")
    private Integer age;

    @JsonProperty ("gender")
    private String gender;

    @JsonProperty ("phone_number")
    private String phoneNumber;



    public Person() {}

    boolean isMoore18() {return age > 18; }

    boolean isStartA() {return fullName.startsWith("A"); }


    @Override
    public String toString() {
        return fullName + " " + age + " " + phoneNumber;
    }
}

class PersonAnalyzer implements Callable<List<Person>> {

    public List<Person> personList;

    public PersonAnalyzer(List<Person> personList) {
        this.personList = personList;
    }

    public List<Person> call() {

        List<Person> resultList = new ArrayList<>();

        for (Person person : personList) {
            if (person.isMoore18() && person.isStartA()) {
                resultList.add(person);
            }
        }

        return resultList;
    }
}


public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        ObjectMapper objectMapper = new ObjectMapper();

        List<Person> personList = objectMapper.readValue(new File("src/main/resources/data.json"), new TypeReference<List<Person>>() {});

        ExecutorService executorService = Executors.newFixedThreadPool(1);


        Future<List<Person>> resultList = executorService.submit(new PersonAnalyzer(personList));
        executorService.shutdown();
        for (Person person : resultList.get()) {
            System.out.println(person);
        }
    }
}