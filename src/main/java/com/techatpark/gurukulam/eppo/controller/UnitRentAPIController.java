package com.techatpark.gurukulam.eppo.controller;

import java.util.List;

import javax.validation.Valid;

import com.techatpark.gurukulam.eppo.model.UnitRent;
import com.techatpark.gurukulam.eppo.service.UnitRentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "UnitRents", description = "Resource for UnitRents", tags = { "UnitRents" })
@RestController
@RequestMapping("/api/unit_rents")
public class UnitRentAPIController {

    private final UnitRentService unitRentService;

    UnitRentAPIController(final UnitRentService unitRentService) {
        this.unitRentService = unitRentService;
    }

    @ApiOperation(value = "List all UnitRents", notes = "Can be called only by users with 'auth management' rights.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "UnitRents Listed successfully"),
            @ApiResponse(code = 400, message = "UnitRents Not Available") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<UnitRent>> findAll() {
        return ResponseEntity.ok(unitRentService.list());
    }

    @ApiOperation(value = "Creates a new UnitRent", notes = "Can be called only by users with 'auth management' rights.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "UnitRent created successfully"),
            @ApiResponse(code = 400, message = "UnitRent already in use") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<UnitRent> create(@Valid @RequestBody UnitRent unitRent) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unitRentService.create(unitRent));
    }

    @ApiOperation(value = "Finds a UnitRent with a given ID", notes = "Can be called only by users with 'auth management' rights.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "UnitRent with a given ID found successfully"),
            @ApiResponse(code = 400, message = "UnitRent Not Available") })
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<UnitRent> findById(@PathVariable Integer id) {
        return ResponseEntity.of(unitRentService.read(id));
    }

    @ApiOperation(value = "Updates a UnitRent", notes = "Can be called only by users with 'auth management' rights.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "UnitRent updated successfully"),
            @ApiResponse(code = 400, message = "UnitRent Not Available") })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public ResponseEntity<UnitRent> update(@PathVariable Integer id, @Valid @RequestBody UnitRent unitRent) {
        return ResponseEntity.ok(unitRentService.update(id, unitRent));
    }

    @ApiOperation(value = "Deletes a UnitRent with a given ID", notes = "Can be called only by users with 'auth management' rights.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "UnitRent deleted successfully"),
            @ApiResponse(code = 400, message = "UnitRent Not Available") })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public ResponseEntity<UnitRent> delete(@PathVariable Integer id) {
        unitRentService.delete(id);
        return ResponseEntity.ok().build();
    }

}