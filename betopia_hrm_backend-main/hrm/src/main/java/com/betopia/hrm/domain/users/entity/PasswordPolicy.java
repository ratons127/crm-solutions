package com.betopia.hrm.domain.users.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "password_policy")
public class PasswordPolicy extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_length", nullable = false, length = 8)
    private String minLength;

    @Column(name = "max_length", nullable = false, length = 25)
    private String maxLength;

    @Column(name = "expiration")
    private Integer expiration;

    @Column(name = "rotation")
    private Integer rotation;

    @Column(name = "grace_period")
    private Integer gracePeriod;

    @Column(name = "pass_recovery_param")
    private String passRecoveryParam;

    @Column(name = "token_validity")
    private Integer tokenValidity;

    @Column(name = "login_attempt_allowed")
    private Integer loginAttemptAllowed;

    @Column(name = "password_lock_duration")
    private Integer passwordLockDuration;

    @Column(name = "brute_force_protection")
    private Integer bruteForceProtection;

    @Column(name = "throttle_attempt")
    private Integer throttleAttempt;

    @Column(name = "year")
    private Integer year;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getPassRecoveryParam() {
        return passRecoveryParam;
    }

    public void setPassRecoveryParam(String passRecoveryParam) {
        this.passRecoveryParam = passRecoveryParam;
    }

    public Integer getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(Integer tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public Integer getLoginAttemptAllowed() {
        return loginAttemptAllowed;
    }

    public void setLoginAttemptAllowed(Integer loginAttemptAllowed) {
        this.loginAttemptAllowed = loginAttemptAllowed;
    }

    public Integer getPasswordLockDuration() {
        return passwordLockDuration;
    }

    public void setPasswordLockDuration(Integer passwordLockDuration) {
        this.passwordLockDuration = passwordLockDuration;
    }

    public Integer getBruteForceProtection() {
        return bruteForceProtection;
    }

    public void setBruteForceProtection(Integer bruteForceProtection) {
        this.bruteForceProtection = bruteForceProtection;
    }

    public Integer getThrottleAttempt() {
        return throttleAttempt;
    }

    public void setThrottleAttempt(Integer throttleAttempt) {
        this.throttleAttempt = throttleAttempt;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
