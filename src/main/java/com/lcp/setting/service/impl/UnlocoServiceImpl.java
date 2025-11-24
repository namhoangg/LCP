package com.lcp.setting.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.dto.ValidationError;
import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.common.enumeration.ValidationLevel;
import com.lcp.excel.ExcelHelper;
import com.lcp.exception.ApiException;
import com.lcp.setting.dto.ImportUnlocoDto;
import com.lcp.setting.dto.UnlocoListRequest;
import com.lcp.setting.dto.UnlocoRequestDto;
import com.lcp.setting.dto.UnlocoResponseDto;
import com.lcp.setting.entity.Unloco;
import com.lcp.setting.mapper.UnlocoMapper;
import com.lcp.setting.repository.UnlocoRepository;
import com.lcp.setting.service.UnlocoService;
import com.lcp.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.ContentDisposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

@Service
@RequiredArgsConstructor
public class UnlocoServiceImpl implements UnlocoService {
    private final UnlocoRepository unlocoRepository;
    private final String COUNTRY_CODE = "VN";
    private final String COUNTRY_NAME = "Viet Nam";
    private final String[] HEADER_ROW = {"No.", "Code", "City Name", "City Code", "Country Code", "Country Name", "Display Name", "Status"};
    private final String defaultHeaderColor = "#32a860";

    private byte[] hexToRgb(String colorStr) {
        return new byte[] {
            (byte) Integer.parseInt(colorStr.substring(1, 3), 16),
            (byte) Integer.parseInt(colorStr.substring(3, 5), 16),
            (byte) Integer.parseInt(colorStr.substring(5, 7), 16)
        };
    }

    @Override
    public PageResponse<UnlocoResponseDto> list(UnlocoListRequest request) {
        Page<Unloco> unlocodes = unlocoRepository.list(request);
        return PageResponse.buildPageDtoResponse(unlocodes,
                UnlocoMapper::createResponse);
    }

    @Override
    public UnlocoResponseDto detail(Long id) {
        Unloco unloco = get(id);
        return UnlocoMapper.createResponse(unloco);
    }

    private Unloco get(Long id) {
        Optional<Unloco> unlocoOptional = unlocoRepository.findById(id);
        if (unlocoOptional.isEmpty()) {
            throw new ApiException("Unloco not found");
        }
        return unlocoOptional.get();
    }

    @Override
    @Transactional
    public UnlocoResponseDto create(UnlocoRequestDto request) {
        // Check if the city code already exists
        Optional<Unloco> existingUnloco = unlocoRepository.findByCityCodeAndStatus(request.getCityCode(), GeneralStatus.ACTIVE);
        if (existingUnloco.isPresent()) {
            throw new ApiException("There is already a city with this code");
        }

        Unloco unloco = new Unloco();
        unloco.setCityName(request.getCityName());
        unloco.setCityCode(request.getCityCode());
        unloco.setCountryCode(COUNTRY_CODE);
        unloco.setCountryName(COUNTRY_NAME);
        unlocoRepository.save(unloco);
        return UnlocoMapper.createResponse(unloco);
    }

    @Override
    @Transactional
    public UnlocoResponseDto update(Long id, UnlocoRequestDto request) {
        // Check if the city code already exists
        Optional<Unloco> existingUnloco = unlocoRepository.findByCityCodeAndStatus(request.getCityCode(), GeneralStatus.ACTIVE);
        if (existingUnloco.isPresent() && !existingUnloco.get().getId().equals(id)) {
            throw new ApiException("There is already a city with this code");
        }

        Unloco unloco = get(id);
        unloco.setCityName(request.getCityName());
        unloco.setCityCode(request.getCityCode());
        unloco.setStatus(request.getStatus());
        unlocoRepository.save(unloco);
        return UnlocoMapper.createResponse(unloco);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Unloco unloco = get(id);
        unloco.setStatus(GeneralStatus.DELETED);
        unlocoRepository.save(unloco);
    }

    @Override
    public ResponseEntity<byte[]> export() {
        List<Unloco> unlocos = unlocoRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook(); 
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet(ExcelHelper.UNLOCOS_SHEET_NAME);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            
            // Set background color
            headerStyle.setFillForegroundColor(new XSSFColor(hexToRgb(defaultHeaderColor), null));
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // Set borders
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            // Set font
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            
            // Create header cells with style
            for (int i = 0; i < HEADER_ROW.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADER_ROW[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowIdx = 1;
            for (Unloco unloco : unlocos) {
                Row row = sheet.createRow(rowIdx);
                
                row.createCell(0).setCellValue(rowIdx); // No.
                row.createCell(1).setCellValue(unloco.getCode());
                row.createCell(2).setCellValue(unloco.getCityName());
                row.createCell(3).setCellValue(unloco.getCityCode());
                row.createCell(4).setCellValue(unloco.getCountryCode());
                row.createCell(5).setCellValue(unloco.getCountryName());
                row.createCell(6).setCellValue( String.format("%s - %s, %s", unloco.getCode(), unloco.getCityName(), unloco.getCountryName())); // Display Name
                row.createCell(7).setCellValue(unloco.getStatus() == GeneralStatus.ACTIVE ? "Active" : "Inactive");
                
                rowIdx++;
            }
            
            // Resize all columns to fit content
            for (int i = 0; i < HEADER_ROW.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("unlocos_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx")
                    .build());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());
                    
        } catch (IOException e) {
            throw new ApiException("Failed to export data to Excel file: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<ImportUnlocoDto> validate(MultipartFile file) {
        List<ImportUnlocoDto> results = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row
            int rowNum = 1;
            while (rowNum <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    rowNum++;
                    continue;
                }

                ImportUnlocoDto dto = new ImportUnlocoDto();
                
                // Read cells
                try {
                    Cell cityNameCell = row.getCell(1);
                    Cell cityCodeCell = row.getCell(2);

                    dto.setCityName(getCellStringValue(cityNameCell));
                    dto.setCityCode(getCellStringValue(cityCodeCell));

                    // Validate required fields
                    List<ValidationError> errors = new ArrayList<>();
                    if (StringUtils.isEmpty(dto.getCityName())) {
                        errors.add(ValidationUtil.createValidationError("cityName", "City Name", dto.getCityName(), "City name is required", ValidationLevel.ERROR.getValue()));
                    }
                    if (StringUtils.isEmpty(dto.getCityCode())) {
                        errors.add(ValidationUtil.createValidationError("cityCode", "City Code", dto.getCityCode(), "City code is required", ValidationLevel.ERROR.getValue()));
                    }

                    if (!StringUtils.isEmpty(dto.getCityCode())) {
                        Optional<Unloco> existingUnloco = unlocoRepository.findByCityCodeAndStatus(dto.getCityCode(), GeneralStatus.ACTIVE);
                        if (existingUnloco.isPresent()) {
                            errors.add(ValidationUtil.createValidationError("cityCode", "City Code", dto.getCityCode(), "There is already a city with this code", ValidationLevel.ERROR.getValue()));
                        }
                    }

                    dto.setErrors(errors);
                    
                } catch (Exception e) { 
                    List<ValidationError> errors = new ArrayList<>();
                    errors.add(ValidationUtil.createValidationError("row", "Row", String.valueOf(rowNum), "Invalid row format: " + e.getMessage(), ValidationLevel.ERROR.getValue()));
                    dto.setErrors(errors);
                }
                
                results.add(dto);
                rowNum++;
            }
            
        } catch (Exception e) {
            throw new ApiException("Failed to process Excel file: " + e.getMessage());
        }
        
        return results;
    }
    
    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    @Override
    @Transactional
    public void importUnlocos(List<ImportUnlocoDto> request) {
        for (ImportUnlocoDto dto : request) {
            if (dto.getErrors().isEmpty() || !dto.getErrors().stream().anyMatch(error -> error.getLevel() == ValidationLevel.ERROR.getValue())) {
                Unloco unloco = new Unloco();
                unloco.setCityName(dto.getCityName());
                unloco.setCityCode(dto.getCityCode());
                unloco.setCountryCode(COUNTRY_CODE);
                unloco.setCountryName(COUNTRY_NAME);
                unloco.setStatus(GeneralStatus.ACTIVE);
                unlocoRepository.save(unloco);
            }
        }
    }
}