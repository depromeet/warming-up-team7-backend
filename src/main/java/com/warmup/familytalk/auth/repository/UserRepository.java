package com.warmup.familytalk.auth.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import com.warmup.familytalk.auth.model.Role;
import com.warmup.familytalk.auth.model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository {
    private AtomicLong userIdCount = new AtomicLong(0);
    private final Map<Long, User> userMapById = new ConcurrentHashMap<>();
    private final Map<String, User> userMapByName = new ConcurrentHashMap<>();

    public Mono<User> getUserById(final long id) {
        return Mono.just(userMapById.get(id));
    }

    public Mono<User> getUserByUsername(final String username) {
        return Mono.just(userMapByName.get(username));
    }

    public Mono<User> save(final User user) {
        try {
            FileWriter pw = new FileWriter(new ClassPathResource("/user.csv").getFile(), true);
            user.setUserId(userIdCount.getAndIncrement());
            pw.append("\n");
            pw.append(user.toCsvFormat());
            pw.flush();
            userMapByName.put(user.getUsername(), user);
            userMapById.put(user.getUserId(), user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(user);
    }

    @PostConstruct
    private void initialize() {
        try {
            Reader in = new FileReader(new ClassPathResource("/user.csv").getFile());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader("userId", "username", "password", "country", "profileImageNumber", "enabled", "roles")
                    .withTrim()
                    .withFirstRecordAsHeader()
                    .parse(in);
            for (CSVRecord record : records) {
                long userId = Long.parseLong(record.get("userId"));
                String username = record.get("username");
                String password = record.get("password");
                String country = record.get("country");
                String profileImageNumber = record.get("profileImageNumber");
                String enabled = record.get("enabled");
                String roles = record.get("roles");

                User user = new User(userId,
                                     username,
                                     password,
                                     country,
                                     profileImageNumber,
                                     Boolean.parseBoolean(enabled),
                                     Role.valueOf(roles));
                userMapById.put(userId, user);
                userMapByName.put(username, user);

                userIdCount.incrementAndGet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
