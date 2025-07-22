package org.demo.repository;

import org.demo.dto.UserDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserRepository {
    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(UserDto user) throws Exception {
        String sql = """
            INSERT INTO users (
                uuid, full_name, username, email, gender, age, age_category, is_adult,
                dob, registered, country, city, street_address, latitude, longitude,
                timezone, phone, cell, profile_pic_url
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, user.getUuid());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getGender());
            stmt.setInt(6, user.getAge());
            stmt.setString(7, user.getAgeCategory());
            stmt.setBoolean(8, user.isAdult());
            stmt.setTimestamp(9, user.getDob());
            stmt.setTimestamp(10, user.getRegistered());
            stmt.setString(11, user.getCountry());
            stmt.setString(12, user.getCity());
            stmt.setString(13, user.getStreetAddress());
            stmt.setDouble(14, user.getLatitude());
            stmt.setDouble(15, user.getLongitude());
            stmt.setString(16, user.getTimezone());
            stmt.setString(17, user.getPhone());
            stmt.setString(18, user.getCell());
            stmt.setString(19, user.getProfilePicUrl());

            stmt.executeUpdate();
        }
    }
}
