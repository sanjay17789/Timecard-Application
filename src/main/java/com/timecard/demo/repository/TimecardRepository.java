package com.timecard.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timecard.demo.entity.TimeCardData;

public interface TimecardRepository extends JpaRepository<TimeCardData,Long> {

}
