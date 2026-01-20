package com.betopia.hrm.services.company.bank.impl;

import com.betopia.hrm.domain.admin.entity.Country;
import com.betopia.hrm.domain.admin.exceptions.CountryNotFound;
import com.betopia.hrm.domain.admin.repository.CountryRepository;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Bank;
import com.betopia.hrm.domain.company.exception.BankNotFound;
import com.betopia.hrm.domain.company.repository.BankRepository;
import com.betopia.hrm.domain.company.request.BankRequest;
import com.betopia.hrm.services.company.bank.BankService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final CountryRepository countryRepository;

    public BankServiceImpl(BankRepository bankRepository, CountryRepository countryRepository) {
        this.bankRepository = bankRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public PaginationResponse<Bank> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<Bank> bankPage = bankRepository.findAll(pageable);
        List<Bank> banks = bankPage.getContent();
        PaginationResponse<Bank> response = new PaginationResponse<>();

        response.setData(banks);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All banks fetch successful");

        Links links = Links.fromPage(bankPage, "/banks");
        response.setLinks(links);
        Meta meta = Meta.fromPage(bankPage, "/banks");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Bank store(BankRequest request) {

        Bank bank = new Bank();

        if (request.countryId() != null) {
            Country country = countryRepository.findById(request.countryId())
                    .orElseThrow(() -> new CountryNotFound("Country not found with id: " + request.countryId()));
            bank.setCountry(country);
        } else {
            bank.setCountry(null);
        }

        bank.setName(request.name());
        bank.setShortName(request.shortName());
        bank.setBankCode(request.bankCode());
        bank.setSwiftCode(request.swiftCode());
        bank.setWebsite(request.website());
        bank.setSupportEmail(request.supportEmail());
        bank.setStatus(request.status());

        bankRepository.save(bank);

        return bank;
    }

    @Override
    public Bank show(Long bankId) {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new BankNotFound("Bank not found with id: " + bankId));

        return bank;
    }

    @Override
    public Bank update(Long bankId, BankRequest request) {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new BankNotFound("Bank not found with id: " + bankId));

        if (request.countryId() != null) {
            Country country = countryRepository.findById(request.countryId())
                    .orElseThrow(() -> new CountryNotFound("Country not found with id: " + request.countryId()));
            bank.setCountry(country);
        } else {
            bank.setCountry(null);
        }

        bank.setName(request.name() != null ? request.name() : bank.getName());
        bank.setShortName(request.shortName() != null ? request.shortName() : bank.getShortName());
        bank.setBankCode(request.bankCode() != null ? request.bankCode() : bank.getBankCode());
        bank.setSwiftCode(request.swiftCode() != null ? request.swiftCode() : bank.getSwiftCode());
        bank.setWebsite(request.website() != null ? request.website() : bank.getWebsite());
        bank.setSupportEmail(request.supportEmail() != null ? request.supportEmail() : bank.getSupportEmail());
        bank.setStatus(request.status() != null ? request.status() : bank.getStatus());

        bankRepository.save(bank);

        return bank;
    }

    @Override
    public void destroy(Long bankId) {
        bankRepository.deleteById(bankId);
    }
}
