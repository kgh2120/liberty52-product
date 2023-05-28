package com.liberty52.product.service.entity.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.StringTokenizer;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VBank {

    @Id
    private final String id = UUID.randomUUID().toString();
    @Enumerated(EnumType.STRING)
    private BankType bank;
    private String account;
    private String holder;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    private VBank(BankType bank, String account, String holder) {
        this.bank = bank;
        this.account = account;
        this.holder = holder;
    }

    public static VBank of(BankType bank, String account, String holder) {
        return new VBank(bank, account, holder);
    }

    public static VBank ofFullInfo(String vBankInfo) {
        StringTokenizer stn = new StringTokenizer(vBankInfo);
        BankType bank = BankType.getBankType(stn.nextToken());
        String account = stn.nextToken();
        String holder = stn.nextToken();
        return VBank.of(bank, account, holder);
    }

    public void update(BankType bank, String account, String holder) {
        this.bank = bank;
        this.account = account;
        this.holder = holder;
        this.updatedAt = LocalDateTime.now();
    }

    public String getOneLineVBankInfo() {
        return this.bank.getKoName() + " " + this.account + " " + this.holder;
    }
}
