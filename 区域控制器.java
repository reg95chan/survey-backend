package com.survey.controller;

import com.survey.dto.RegionDTO;
import com.survey.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    public ResponseEntity<RegionDTO> createRegion(@RequestBody RegionDTO regionDTO) {
        RegionDTO createdRegion = regionService.createRegion(regionDTO);
        return new ResponseEntity<>(createdRegion, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable Long id) {
        return regionService.getRegionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions(
            @RequestParam(required = false) String projectId) {
        List<RegionDTO> regions = projectId != null ? 
                regionService.getRegionsByProjectId(projectId) : 
                regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(
            @PathVariable Long id, 
            @RequestBody RegionDTO regionDTO) {
        return ResponseEntity.ok(regionService.updateRegion(id, regionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}