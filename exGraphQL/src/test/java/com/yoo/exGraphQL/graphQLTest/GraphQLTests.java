package com.yoo.exGraphQL.graphQLTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest
@AutoConfigureGraphQlTester
public class GraphQLTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    public void  getListTest(){
        graphQlTester.documentName("allFindMembers")
                .execute();

    }

}
