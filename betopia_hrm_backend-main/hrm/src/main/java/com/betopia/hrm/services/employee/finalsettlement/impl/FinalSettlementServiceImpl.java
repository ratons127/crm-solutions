package com.betopia.hrm.services.employee.finalsettlement.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.FinalSettlementDTO;
import com.betopia.hrm.domain.dto.employee.mapper.FinalSettlementMapper;
import com.betopia.hrm.domain.employee.entity.FinalSettlement;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.FinalSettlementRepository;
import com.betopia.hrm.domain.employee.request.FinalSettlementRequest;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.finalsettlement.FinalSettlementService;
import com.betopia.hrm.webapp.util.UploadFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class FinalSettlementServiceImpl implements FinalSettlementService {

    private final FinalSettlementRepository finalSettlementRepository;
    private final FinalSettlementMapper finalSettlementMapper;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final UserRepository userRepository;
    private final UploadFiles uploadFiles;

    public FinalSettlementServiceImpl(FinalSettlementRepository finalSettlementRepository,
                                      FinalSettlementMapper finalSettlementMapper, EmployeeSeparationsRepository employeeSeparationsRepository,
                                      UserRepository userRepository, UploadFiles uploadFiles) {
        this.finalSettlementRepository = finalSettlementRepository;
        this.finalSettlementMapper = finalSettlementMapper;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.userRepository = userRepository;
        this.uploadFiles = uploadFiles;
    }

    @Override
    public PaginationResponse<FinalSettlementDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<FinalSettlement> settlementPage = finalSettlementRepository.findAll(pageable);

        // Get content from page
        List<FinalSettlement> finalSettlements = settlementPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<FinalSettlementDTO> finalSettlementDTOS = finalSettlementMapper.toDTOList(finalSettlements);

        // Create pagination response
        PaginationResponse<FinalSettlementDTO> response = new PaginationResponse<>();
        response.setData(finalSettlementDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All final settlement successfully");

        // Set links
        Links links = Links.fromPage(settlementPage, "/settlement");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(settlementPage, "/settlement");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<FinalSettlementDTO> getAll() {
        List<FinalSettlement> finalSettlements = finalSettlementRepository.findAll();
        return finalSettlementMapper.toDTOList(finalSettlements);
    }

    @Override
    public FinalSettlementDTO store(FinalSettlementRequest request) {
        return null;
    }

    @Override
    public FinalSettlementDTO show(Long id) {
        FinalSettlement finalSettlement = finalSettlementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Final settlement not found with id: " + id));
        return finalSettlementMapper.toDTO(finalSettlement);
    }

    @Override
    public FinalSettlementDTO update(Long id, FinalSettlementRequest request, List<MultipartFile> files) {
        FinalSettlement finalSettlement = finalSettlementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Final settlement not found with id: " + id));

        finalSettlement.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        finalSettlement.setUnpaidSalary(request.unpaidSalary());
        finalSettlement.setSalaryArrears(request.salaryArrears());
        finalSettlement.setOvertimeDues(request.overtimeDues());
        finalSettlement.setLeaveEncashment(request.leaveEncashment());
        finalSettlement.setBonusDues(request.bonusDues());
        finalSettlement.setOtherEarnings(request.otherEarnings());
        finalSettlement.setTotalEarnings(request.totalEarnings());

        finalSettlement.setNoticeBuyout(request.noticeBuyout());
        finalSettlement.setLoanRecovery(request.loanRecovery());
        finalSettlement.setAdvanceRecovery(request.advanceRecovery());
        finalSettlement.setTaxDeduction(request.taxDeduction());
        finalSettlement.setStatutoryDeductions(request.statutoryDeductions());
        finalSettlement.setAssetLossRecovery(request.assetLossRecovery());
        finalSettlement.setOtherDeductions(request.otherDeductions());
        finalSettlement.setTotalDeductions(request.totalDeductions());
        finalSettlement.setNetPayable(request.netPayable());
        finalSettlement.setSettlementStatus(request.settlementStatus());

        finalSettlement.setHrApprovedBy(userRepository.findById(request.hrApprovedById())
                .orElseThrow(() -> new RuntimeException("HR Approver not found")));

        finalSettlement.setHrApprovedDate(request.hrApprovedDate());

        finalSettlement.setFinanceApprovedBy(userRepository.findById(request.financeApprovedById())
                .orElseThrow(() -> new RuntimeException("Finance Approver not found")));

        finalSettlement.setFinanceApprovedDate(request.financeApprovedDate());

        finalSettlement.setDisbursementDate(request.disbursementDate());
        finalSettlement.setPaymentMethod(request.paymentMethod());
        finalSettlement.setPaymentReference(request.paymentReference());
        finalSettlement.setVoucherNumber(request.voucherNumber());

        finalSettlement.setSlaDeadline(request.slaDeadline());
        finalSettlement.setIsOverdue(request.isOverdue());
        finalSettlement.setRemarks(request.remarks());

        if (files != null && !files.isEmpty()) {
            String folder = "settlement/" + finalSettlement.getId();
            StringBuilder urls = new StringBuilder();

            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null;
                });
            }

            if (finalSettlement.getStatementPath() != null && !finalSettlement.getStatementPath().isEmpty()) {
                urls.insert(0, finalSettlement.getStatementPath() + ",");
            }
            finalSettlement.setStatementPath(urls.toString());
        }

        FinalSettlement saved = finalSettlementRepository.save(finalSettlement);
        return finalSettlementMapper.toDTO(saved);
    }

    @Override
    public void destroy(Long id) {

        FinalSettlement finalSettlement = finalSettlementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Final settlement not not found"));

        finalSettlementRepository.delete(finalSettlement);

    }

    @Override
    public FinalSettlementDTO saveSettlement(FinalSettlementRequest request, List<MultipartFile> files) {
        FinalSettlement finalSettlement = new FinalSettlement();

        finalSettlement.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        finalSettlement.setUnpaidSalary(request.unpaidSalary());
        finalSettlement.setSalaryArrears(request.salaryArrears());
        finalSettlement.setOvertimeDues(request.overtimeDues());
        finalSettlement.setLeaveEncashment(request.leaveEncashment());
        finalSettlement.setBonusDues(request.bonusDues());
        finalSettlement.setOtherEarnings(request.otherEarnings());
        finalSettlement.setTotalEarnings(request.totalEarnings());

        finalSettlement.setNoticeBuyout(request.noticeBuyout());
        finalSettlement.setLoanRecovery(request.loanRecovery());
        finalSettlement.setAdvanceRecovery(request.advanceRecovery());
        finalSettlement.setTaxDeduction(request.taxDeduction());
        finalSettlement.setStatutoryDeductions(request.statutoryDeductions());
        finalSettlement.setAssetLossRecovery(request.assetLossRecovery());
        finalSettlement.setOtherDeductions(request.otherDeductions());
        finalSettlement.setTotalDeductions(request.totalDeductions());
        finalSettlement.setNetPayable(request.netPayable());
        finalSettlement.setSettlementStatus(request.settlementStatus());

        finalSettlement.setHrApprovedBy(userRepository.findById(request.hrApprovedById())
                .orElseThrow(() -> new RuntimeException("HR Approver not found")));

        finalSettlement.setHrApprovedDate(request.hrApprovedDate());

        finalSettlement.setFinanceApprovedBy(userRepository.findById(request.financeApprovedById())
                .orElseThrow(() -> new RuntimeException("Finance Approver not found")));

        finalSettlement.setFinanceApprovedDate(request.financeApprovedDate());

        finalSettlement.setDisbursementDate(request.disbursementDate());
        finalSettlement.setPaymentMethod(request.paymentMethod());
        finalSettlement.setPaymentReference(request.paymentReference());
        finalSettlement.setVoucherNumber(request.voucherNumber());
        finalSettlement.setSlaDeadline(request.slaDeadline());
        finalSettlement.setIsOverdue(request.isOverdue());
        finalSettlement.setRemarks(request.remarks());

        FinalSettlement saved = finalSettlementRepository.save(finalSettlement);

        if (files != null && !files.isEmpty()) {
            String folder = "settlement/" + saved.getId();
            StringBuilder urls = new StringBuilder();

            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null;
                });
            }

            saved.setStatementPath(urls.toString());
            saved = finalSettlementRepository.save(saved);
        }

        return finalSettlementMapper.toDTO(saved);
    }
}
