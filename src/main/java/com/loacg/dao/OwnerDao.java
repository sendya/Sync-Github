package com.loacg.dao;

import com.loacg.entity.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 8:32 PM
 */
@Repository
public class OwnerDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * get owner by id
     * @param id
     * @return
     */
    public Owner getById(int id) {
        if (id <= 0) return null;
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        Owner owner = jdbcTemplate.queryForObject("SELECT `id`, userName, repoName, syncSource, syncLast, lastSyncTime, latest FROM owner WHERE id=:id", params, Owner.class);
        return owner;
    }

    public List<Owner> getAll() {
        Map<String, Long> params = new HashMap<>();
        Long lastSyncTime = System.currentTimeMillis()/1000; // /1000+3600*24
        return jdbcTemplate.query("SELECT `id`, userName, repoName, syncSource, syncLast, lastSyncTime, latest FROM owner WHERE syncLast = 1 ORDER BY `id`", new BeanPropertyRowMapper(Owner.class));
    }

    /**
     * get owner by userName
     * @param userName
     * @return
     */
    public Owner getByUserName(String userName) {
        if (StringUtils.isEmpty(userName)) return null;
        Map<String, String> params = new HashMap<>();
        params.put(":userName", userName);
        Owner owner = jdbcTemplate.queryForObject("SELECT `id`, userName, repoName, syncSource, syncLast, lastSyncTime, latest FROM owner WHERE userName=:userName", params, Owner.class);
        return owner;
    }

    /**
     * get owners by repoName
     * @param repoName
     * @return
     */
    public List<Owner> getByRepo(String repoName) {
        if (StringUtils.isEmpty(repoName)) return null;
        Map<String, String> params = new HashMap<>();
        params.put(":repoName", repoName);
        List<Owner> owners = new ArrayList<>();

        jdbcTemplate.query("SELECT `id`, userName, repoName, syncSource, syncLast, lastSyncTime, latest FROM owner WHERE repoName=:repoName", params,
                rs -> {
                    return owners.add(
                                new Owner()
                                    .setId(rs.getInt("id"))
                                    .setUserName(rs.getString("userName"))
                                    .setRepoName(rs.getString("repoName"))
                                    .setSyncLast(rs.getBoolean("syncLast"))
                                    .setSyncSource(rs.getBoolean("syncSource"))
                                    .setLastSyncTime(rs.getInt("lastSyncTime"))
                                    .setLatest(rs.getString("latest"))
                    );
                });
        return owners;
    }

    /**
     * add owner
     * @param owner
     * @return
     */
    public int add(final Owner owner) {
        return jdbcTemplate.update("INSERT INTO owner (userName, repoName, syncSource, syncLast, lastSyncTime, latest) VALUES (:userName, :repoName, :syncSource, :syncLast, :lastSyncTime, :latest)",
                new BeanPropertySqlParameterSource(owner));
    }

    /**
     * add owners
     * @param owners
     * @return
     */
    public int[] add(final List<? extends Owner> owners) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(owners.toArray());
        int[] ii = jdbcTemplate.batchUpdate("INSERT INTO owner (userName, repoName, syncSource, syncLast, lastSyncTime, latest) VALUES (:userName, :repoName, :syncSource, :syncLast, :lastSyncTime, :latest)", batch);
        return ii;
    }

    /**
     * update owner
     * @param owner
     * @return
     */
    public int update(final Owner owner) {
        return jdbcTemplate.update("UPDATE owner SET userName=:userName, repoName=:repoName, syncSource=:syncSource, syncLast=:syncLast, lastSyncTime=:lastSyncTime, latest=:latest WHERE id=:id", new BeanPropertySqlParameterSource(owner));
    }

    /**
     * update owners
     * @param owners
     * @return
     */
    public int[] update(final List<? extends Owner> owners) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(owners.toArray());
        int[] ii = jdbcTemplate.batchUpdate("UPDATE owner SET userName=:userName, repoName=:repoName, syncSource=:syncSource, syncLast=:syncLast, lastSyncTime=:lastSyncTime, latest=:latest WHERE id=:id", batch);
        return ii;
    }

    /**
     * delete owner from owner object
     * @param owner
     * @return
     */
    public int delete(final Owner owner) {
        return jdbcTemplate.update("DELETE owner WHERE id=:id", new BeanPropertySqlParameterSource(owner));
    }

    /**
     * delete owner from id
     * @param id
     * @return
     */
    public int delete(int id) {
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.update("DELETE owner WHERE id=:id", params);
    }

    /**
     * delete owners from owner array object
     * @param owners
     * @return
     */
    public int[] delete(final List<? extends Owner> owners) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(owners.toArray());
        int[] ii = jdbcTemplate.batchUpdate("DELETE owner WHERE id=:id", batch);
        return ii;
    }

}
