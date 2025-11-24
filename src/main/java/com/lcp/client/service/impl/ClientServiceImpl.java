package com.lcp.client.service.impl;

import com.lcp.client.common.ApiClientMessage;
import com.lcp.client.dto.ClientCreateDto;
import com.lcp.client.dto.ClientListRequest;
import com.lcp.client.dto.ClientResponseDto;
import com.lcp.client.dto.ClientUpdateDto;
import com.lcp.client.entity.Client;
import com.lcp.client.helper.ClientHelper;
import com.lcp.client.mapper.ClientMapper;
import com.lcp.client.repository.ClientRepository;
import com.lcp.client.service.ClientService;
import com.lcp.common.PageResponse;
import com.lcp.common.dto.EmailBaseDto;
import com.lcp.exception.ApiException;
import com.lcp.provider.entity.CompanyInfo;
import com.lcp.provider.entity.ContactPersonInfo;
import com.lcp.provider.mapper.CompanyInfoMapper;
import com.lcp.provider.mapper.ContactPersonInfoMapper;
import com.lcp.provider.repository.CompanyInfoRepository;
import com.lcp.provider.repository.ContactPersonInfoRepository;
import com.lcp.security.UserDetailsCustom;
import com.lcp.staff.common.ApiStaffMessage;
import com.lcp.staff.entity.Staff;
import com.lcp.staff.repository.StaffRepository;
import com.lcp.util.EmailService;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.nio.charset.Charset.defaultCharset;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final static String SITE_URL = "http://localhost:4200/login";

    private final ClientRepository clientRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final ContactPersonInfoRepository contactPersonInfoRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public ClientResponseDto create(ClientCreateDto createDto) {
        boolean existsClientWithEmail = clientRepository.existsByEmail(createDto.getEmail());
        if (existsClientWithEmail) {
            throw new ApiException("Client already exists with this email");
        }

        boolean existsStaffWithEmail = staffRepository.existsByEmail(createDto.getEmail());
        if (existsStaffWithEmail) {
            throw new ApiException("Staff already exists with this email");
        }

        CompanyInfo companyInfo = CompanyInfoMapper.createEntity(createDto.getCompanyInfo());
        companyInfoRepository.save(companyInfo);
        ContactPersonInfo contactPersonInfo = ContactPersonInfoMapper.createEntity(createDto.getContactPersonInfo());
        contactPersonInfoRepository.save(contactPersonInfo);

        createDto.setCompanyInfoId(companyInfo.getId());
        createDto.setContactPersonInfoId(contactPersonInfo.getId());
        Client client = ClientMapper.createEntity(createDto);
        Client latestClient = clientRepository.findFirstByOrderByIdDesc();
        client.setCode(ClientHelper.genClientCode(latestClient));
        String password = ClientHelper.genPassword();

        Staff staff = new Staff();
        staff.setEmail(client.getEmail());
        staff.setPassword(passwordEncoder.encode(password));
        staff.setCode(client.getCode());
        staff.setFirstname(client.getName());
        staff.setIsClient(true);
        staff.setForceChangePassword(false);
        staffRepository.save(staff);

        if (createDto.getServedBy() != null) {
            Optional<Staff> servedByStaff = staffRepository.findById(createDto.getServedBy());
            if (servedByStaff.isEmpty()) {
                client.setServedBy(UserDetailsCustom.getCurrentUserId());
            }
        }
        client.setStaffId(staff.getId());
        clientRepository.save(client);
        PersistentUtil.flushAndClear();

        try {
            String emailTemplate = StreamUtils.copyToString(
                    new ClassPathResource("email/createClient.html").getInputStream(), defaultCharset());

            Map<String, String> valuesMap = Map.of(
                    "displayName", client.getName(),
                    "email", client.getEmail(),
                    "password", password,
                    "siteURL", SITE_URL);
            StringSubstitutor sub = new StringSubstitutor(valuesMap);
            String emailHtml = sub.replace(emailTemplate);
            EmailBaseDto emailBaseDto = new EmailBaseDto();
            emailBaseDto.setSubject("New account");
            emailBaseDto.setBody(emailHtml);
            emailBaseDto.setToEmails(List.of(client.getEmail()));
            emailService.send(emailBaseDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return detail(client.getId());
    }

    @Override
    @Transactional
    public ClientResponseDto update(Long id, ClientUpdateDto updateDto) {
        Client client = get(id);
        if (updateDto.getServedBy() != null) {
            Optional<Staff> servedByStaff = staffRepository.findById(updateDto.getServedBy());
            if (servedByStaff.isEmpty()) {
                updateDto.setServedBy(client.getServedBy());
            }
        }
        MapUtil.copyProperties(updateDto, client);
        MapUtil.copyProperties(updateDto.getCompanyInfo(), client.getCompanyInfo());
        MapUtil.copyProperties(updateDto.getContactPersonInfo(), client.getContactPersonInfo());

        clientRepository.save(client);
        PersistentUtil.flushAndClear();
        return detail(client.getId());
    }

    @Override
    public ClientResponseDto detail(Long id) {
        Client client = get(id);
        return ClientMapper.createResponse(client, ClientMapper.DetailIncludeFields);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Client client = get(id);
        clientRepository.delete(client);

        // Delete corresponding staff account (client has staff account)
        staffRepository.deleteById(client.getStaffId());

        // // Delete corresponding company and contact person info
        companyInfoRepository.deleteById(client.getCompanyInfoId());
        contactPersonInfoRepository.deleteById(client.getContactPersonInfoId());
    }

    @Override
    public PageResponse<ClientResponseDto> list(ClientListRequest request) {
        Page<Client> clients = clientRepository.list(request);
        return PageResponse.buildPageDtoResponse(
                clients,
                client -> ClientMapper.createResponse(client, request.getIncludeFields())
        );
    }

    private Client get(Long id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isEmpty()) {
            throw new ApiException(ApiClientMessage.CLIENT_NOT_FOUND);
        }
        return clientOpt.get();
    }

    @Override
    public Long getCurrentClientId() {
        Long staffId = UserDetailsCustom.getCurrentUserId();
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ApiException(ApiStaffMessage.STAFF_NOT_FOUND));
        if (!staff.getIsClient()) {
            throw new ApiException(ApiClientMessage.CLIENT_NOT_FOUND);
        }
        Client client = clientRepository.findByStaffId(staffId);
        if (client == null) {
            throw new ApiException(ApiClientMessage.CLIENT_NOT_FOUND);
        }
        return client.getId();
    }
}
