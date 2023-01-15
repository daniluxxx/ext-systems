package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequset;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;

import java.sql.*;

public class PersonCheckDao {

    private static final String SQL_REQUEST = "select temporal, upper(p.sur_name) from cr_address_person ap " +
            "inner join cr_person p on p.person_id = ap.person_id " +
            "inner join cr_address a on a.address_id = ap.address_id " +
            "where " +
            "CURRENT_DATE >= ap.start_date and (CURRENT_DATE <= ap.end_date or ap.end_date is null)" +
            "and upper(p.sur_name) = upper(?) " +
            "and upper(p.given_name) = upper(?) " +
            "and upper(p.patronymic) = upper(?) " +
            "and p.date_of_birth = ? " +
            "and a.street_code = ? " +
            "and upper(a.building) = upper(?) ";

    private ConnectionBuilder connectionBuilder;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    private Connection getConnection() throws SQLException {
        return connectionBuilder.getConnection();
    }

    public PersonResponse checkPerson(PersonRequset requset) throws PersonCheckException {
        PersonResponse response = new PersonResponse();

        String sql = SQL_REQUEST;
        if (requset.getExtension() != null) {
            sql += "and upper(a.extension) = upper(?) ";
        } else {
            sql += "and a.extension is null ";
        }
        if (requset.getApartment() != null) {
            sql += "and upper(a.apartment) = upper(?) ";
        } else {
            sql += "and a.apartment is null ";
        }

        try(Connection con = getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {

            int count = 1;
            stmt.setString(count++, requset.getSurName());
            stmt.setString(count++, requset.getGivenName());
            stmt.setString(count++, requset.getPatronymic());
            stmt.setDate(count++, Date.valueOf(requset.getDateOfBirth()));
            stmt.setInt(count++, requset.getStreetCode());
            stmt.setString(count++, requset.getBuilding());
            if (requset.getExtension() != null) {
                stmt.setString(count++, requset.getExtension());
            }
            if (requset.getApartment() != null) {
                stmt.setString(count++, requset.getApartment());
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                response.setRegistered(true);
                response.setTemporal(rs.getBoolean("temporal"));
            }

        } catch (SQLException ex) {
            throw new PersonCheckException();
        }

        return response;
    }


}
