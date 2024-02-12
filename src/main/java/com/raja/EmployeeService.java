package com.raja;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.raja.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private ElasticsearchClient client;

    public void save() {
        Employee employee = new Employee(4L, "Rajasekhar", "Yarraguntla", "Java");
        final co.elastic.clients.elasticsearch.core.IndexRequest.Builder<Object> indexRequest = new co.elastic.clients.elasticsearch.core.IndexRequest.Builder<>();
        indexRequest.index("employee");
        indexRequest.id(String.valueOf(employee.getId()));
        indexRequest.document(employee);

        IndexResponse response;
        try {
            response = client.index(indexRequest.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info(response.id());
    }
}
