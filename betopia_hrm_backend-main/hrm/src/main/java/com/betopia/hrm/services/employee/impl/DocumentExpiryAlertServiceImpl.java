package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.*;
import com.betopia.hrm.domain.employee.enums.DocumentExpiryAlertStatus;
import com.betopia.hrm.domain.employee.exception.employeedocumentstatus.EmployeeDocumentStatusNotFoundException;
import com.betopia.hrm.domain.employee.repository.DocumentExpiryAlertRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeDocumentRepository;
import com.betopia.hrm.domain.employee.request.DocumentExpiryAlertRequest;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.DocumentExpiryAlertService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentExpiryAlertServiceImpl implements DocumentExpiryAlertService {


    private final DocumentExpiryAlertRepository documentExpiryAlertRepository;
    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final UserRepository userRepository;


    public DocumentExpiryAlertServiceImpl(DocumentExpiryAlertRepository documentExpiryAlertRepository,
                                          EmployeeDocumentRepository employeeDocumentRepository,
                                          UserRepository userRepository) {
        this.documentExpiryAlertRepository = documentExpiryAlertRepository;
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<DocumentExpiryAlert> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<DocumentExpiryAlert> documentExpiryAlertPage = documentExpiryAlertRepository.findAll(pageable);
        List<DocumentExpiryAlert> documentExpiryAlerts = documentExpiryAlertPage.getContent();
        PaginationResponse<DocumentExpiryAlert> response = new PaginationResponse<>();

        response.setData(documentExpiryAlerts);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All verified documents fetch successful");

        Links links = Links.fromPage(documentExpiryAlertPage, "/document-expiry-alerts");
        response.setLinks(links);
        Meta meta = Meta.fromPage(documentExpiryAlertPage, "/document-expiry-alerts");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<DocumentExpiryAlert> getAllDocumentExpiryAlerts() {
        return documentExpiryAlertRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public DocumentExpiryAlert show(Long documentExpiryAlertId) {
        DocumentExpiryAlert documentExpiryAlert = documentExpiryAlertRepository.findById(documentExpiryAlertId)
                .orElseThrow(() -> new RuntimeException("document expiry alert not found with id: " + documentExpiryAlertId));

        return documentExpiryAlert;
    }

    @Override
    public DocumentExpiryAlert store(DocumentExpiryAlertRequest request) {
        EmployeeDocument employeeDocument = employeeDocumentRepository.findById(request.employeeDocumentId())
                .orElseThrow(() -> new EmployeeDocumentStatusNotFoundException("Employee document not found: " + request.employeeDocumentId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.userId()));

        DocumentExpiryAlert documentExpiryAlert = new DocumentExpiryAlert();

        documentExpiryAlert.setEmployeeDocument(employeeDocument);
        documentExpiryAlert.setAlertDate(request.alertDate());
        documentExpiryAlert.setUser(user);
        documentExpiryAlert.setSentAt(LocalDateTime.now());
        documentExpiryAlert.setStatus(request.status() == null ? DocumentExpiryAlertStatus.SCHEDULED : request.status());

        return documentExpiryAlertRepository.save(documentExpiryAlert);
    }

    @Override
    public DocumentExpiryAlert update(Long documentExpiryAlertId, DocumentExpiryAlertRequest request) {

        DocumentExpiryAlert documentExpiryAlert = documentExpiryAlertRepository.findById(documentExpiryAlertId)
                .orElseThrow(() -> new EmployeeDocumentStatusNotFoundException("document Expiry Alert Id not found with id: " + documentExpiryAlertId));

        EmployeeDocument employeeDocument = employeeDocumentRepository.findById(request.employeeDocumentId())
                .orElseThrow(() -> new EmployeeDocumentStatusNotFoundException("Employee document not found: " + request.employeeDocumentId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.userId()));

        documentExpiryAlert.setEmployeeDocument(employeeDocument != null ? employeeDocument : documentExpiryAlert.getEmployeeDocument());
        documentExpiryAlert.setAlertDate(request.alertDate() != null ? request.alertDate() : documentExpiryAlert.getAlertDate());
        documentExpiryAlert.setUser(user != null ? user : documentExpiryAlert.getUser());
        documentExpiryAlert.setSentAt(LocalDateTime.now());
        documentExpiryAlert.setStatus(request.status() != null ? request.status() : documentExpiryAlert.getStatus());

        return documentExpiryAlertRepository.save(documentExpiryAlert);
    }

    @Override
    public void destroy(Long documentExpiryAlertId) {
        documentExpiryAlertRepository.deleteById(documentExpiryAlertId);
    }

}
