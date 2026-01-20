package com.betopia.hrm.services.company.bankbranch.impl;

import com.betopia.hrm.domain.admin.entity.Location;
import com.betopia.hrm.domain.admin.exceptions.LocationNotFound;
import com.betopia.hrm.domain.admin.repository.LocationRepository;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Bank;
import com.betopia.hrm.domain.company.entity.BankBranch;
import com.betopia.hrm.domain.company.exception.BankBranchNotFound;
import com.betopia.hrm.domain.company.exception.BankNotFound;
import com.betopia.hrm.domain.company.repository.BankBranchRepository;
import com.betopia.hrm.domain.company.repository.BankRepository;
import com.betopia.hrm.domain.company.request.BankBranchRequest;
import com.betopia.hrm.services.company.bankbranch.BankBranchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankBranchServiceImpl implements BankBranchService {

    private final BankBranchRepository bankBranchRepository;
    private final BankRepository bankRepository;
    private final LocationRepository locationRepository;

    public BankBranchServiceImpl(BankBranchRepository bankBranchRepository,
                                 BankRepository bankRepository,
                                 LocationRepository locationRepository)
    {
        this.bankBranchRepository = bankBranchRepository;
        this.bankRepository = bankRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public PaginationResponse<BankBranch> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<BankBranch> bankBranchPage = bankBranchRepository.findAll(pageable);
        List<BankBranch> bankBranches = bankBranchPage.getContent();
        PaginationResponse<BankBranch> response = new PaginationResponse<>();

        response.setData(bankBranches);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All banks fetch successful");

        Links links = Links.fromPage(bankBranchPage, "/bank-branches");
        response.setLinks(links);
        Meta meta = Meta.fromPage(bankBranchPage, "/bank-branches");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<BankBranch> getAllBankBranches() {
        return bankBranchRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public BankBranch store(BankBranchRequest request) {

        Bank bank = bankRepository.findById(request.bankId())
                .orElseThrow(() -> new BankNotFound("Bank not found"));

        Location location = null;
        if (request.locationId() != null) {
            location = locationRepository.findById(request.locationId()).orElseThrow(() -> new LocationNotFound("Location not found"));
        }

        BankBranch bankBranch = new BankBranch();

        bankBranch.setBank(bank);
        bankBranch.setLocation(location);
        bankBranch.setBranchName(request.branchName());
        bankBranch.setBranchCode(request.branchCode());
        bankBranch.setRoutingNo(request.routingNo());
        bankBranch.setSwiftCode(request.swiftCode());
        bankBranch.setEmail(request.email());
        bankBranch.setAddressLine1(request.addressLine1());
        bankBranch.setAddressLine2(request.addressLine2());
        bankBranch.setDistrict(request.district());
        bankBranch.setStatus(request.status());

        bankBranchRepository.save(bankBranch);

        return bankBranch;
    }

    @Override
    public BankBranch show(Long bankBranchId) {
        BankBranch bankBranch = bankBranchRepository.findById(bankBranchId)
                .orElseThrow(() -> new BankBranchNotFound("Branch not found with id: " + bankBranchId));

        return bankBranch;
    }

    @Override
    public BankBranch update(Long bankBranchId, BankBranchRequest request) {
        BankBranch bankBranch = bankBranchRepository.findById(bankBranchId)
                .orElseThrow(() -> new BankBranchNotFound("Bank Branch not found with id: " + bankBranchId));

        Bank bank = bankRepository.findById(request.bankId())
                .orElseThrow(() -> new BankNotFound("Bank not found"));

        Location location = null;
        if (request.locationId() != null) {
            location = locationRepository.findById(request.locationId()).orElseThrow(() -> new LocationNotFound("Location not found"));
        }

        bankBranch.setBank(bank != null ? bank : bankBranch.getBank());
        bankBranch.setLocation(location != null ? location : bankBranch.getLocation());
        bankBranch.setBranchName(request.branchName() != null ? request.branchName() : bankBranch.getBranchName());
        bankBranch.setBranchCode(request.branchCode() != null ? request.branchCode() : bankBranch.getBranchCode());
        bankBranch.setRoutingNo(request.routingNo() != null ? request.routingNo() : bankBranch.getRoutingNo());
        bankBranch.setSwiftCode(request.swiftCode() != null ? request.swiftCode() : bankBranch.getSwiftCode());
        bankBranch.setEmail(request.email() != null ? request.email() : bankBranch.getEmail());
        bankBranch.setAddressLine1(request.addressLine1() != null ? request.addressLine1() : bankBranch.getAddressLine1());
        bankBranch.setAddressLine2(request.addressLine2() != null ? request.addressLine2() : bankBranch.getAddressLine2());
        bankBranch.setDistrict(request.district() != null ? request.district() : bankBranch.getDistrict());
        bankBranch.setStatus(request.status() != null ? request.status() : bankBranch.getStatus());

        bankBranchRepository.save(bankBranch);

        return bankBranch;
    }

    @Override
    public void destroy(Long bankBranchId) {
        bankBranchRepository.deleteById(bankBranchId);
    }
}
