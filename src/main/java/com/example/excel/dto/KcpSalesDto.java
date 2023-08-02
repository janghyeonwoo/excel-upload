package com.example.excel.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class KcpSalesDto {

    //    Long idx;
    @NotNull(message = "승인일시")
    String approvalDt;
    @NotNull(message = "주문번호")
    String ordNo;
    @NotNull(message = "주문자명")
    String userName;
    @NotNull(message = "카드사")
    String cardName;
    @NotNull(message = "매입사")
    String acquirer;
    @NotNull(message = "카드번호")
    String cardNo;
    @NotNull(message = "승인번호")
    String approvalNo;
    @NotNull(message = "거래금액")
    String transAmount;
    @NotNull(message = "신용카드거래금액")
    String cardTransAmount;
    @NotNull(message = "거래상태")
    String transStatus;
    @NotNull(message = "승인번호")
    String transNo;
    @NotNull(message = "마감일자")
    String dueDt;
    @NotNull(message = "정산일자")
    String settlementDt;
}
