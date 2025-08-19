package com.survey.service;

import com.survey.dto.RegionDTO;
import com.survey.entity.Region;
import com.survey.repository.RegionRepository;
import com.survey.util.GeometryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    @Autowired
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public RegionDTO createRegion(RegionDTO regionDTO) {
        // 计算面积
        double area = calculateArea(regionDTO);
        regionDTO.setArea(area);
        
        Region region = mapToEntity(regionDTO);
        Region savedRegion = regionRepository.save(region);
        return mapToDTO(savedRegion);
    }

    public Optional<RegionDTO> getRegionById(Long id) {
        return regionRepository.findById(id).map(this::mapToDTO);
    }

    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RegionDTO> getRegionsByProjectId(String projectId) {
        return regionRepository.findByProjectId(projectId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RegionDTO updateRegion(Long id, RegionDTO regionDTO) {
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + id));
        
        // 更新字段
        existingRegion.setType(regionDTO.getType());
        existingRegion.setDimensions(regionDTO.getDimensions());
        
        // 重新计算面积
        double area = calculateArea(regionDTO);
        existingRegion.setArea(area);
        
        Region updatedRegion = regionRepository.save(existingRegion);
        return mapToDTO(updatedRegion);
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    private double calculateArea(RegionDTO regionDTO) {
        String type = regionDTO.getType();
        if ("rectangle".equals(type)) {
            return GeometryUtils.calculateRectangleArea(regionDTO.getDimensions());
        } else if ("polygon".equals(type)) {
            return GeometryUtils.calculatePolygonArea(regionDTO.getDimensions());
        }
        return 0.0;
    }

    private RegionDTO mapToDTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setId(region.getId());
        dto.setProjectId(region.getProjectId());
        dto.setType(region.getType());
        dto.setDimensions(region.getDimensions());
        dto.setArea(region.getArea());
        dto.setCreatedAt(region.getCreatedAt());
        return dto;
    }

    private Region mapToEntity(RegionDTO dto) {
        Region region = new Region();
        region.setProjectId(dto.getProjectId());
        region.setType(dto.getType());
        region.setDimensions(dto.getDimensions());
        region.setArea(dto.getArea());
        return region;
    }
}