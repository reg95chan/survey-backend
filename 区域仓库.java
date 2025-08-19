package com.survey.repository;

import com.survey.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    
    List<Region> findByProjectId(String projectId);
    
    // 可以根据需要添加更多查询方法
    List<Region> findByType(String type);
    
    List<Region> findByProjectIdAndType(String projectId, String type);
}