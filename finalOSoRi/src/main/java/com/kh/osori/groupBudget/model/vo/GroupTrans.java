package com.kh.osori.groupBudget.model.vo;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data // Getter, Setter, ToString 등을 자동으로 생성합니다.
public class GroupTrans {
    private int tranId;          // TRAN_ID (지출번호 - PK)
    private String title;        // TITLE (내역 제목)
    private Date transDate;      // TRANS_DATE (거래 날짜)
    private int originalAmount;  // ORIGINAL_AMOUNT (금액)
    private String category;     // CATEGORY (카테고리)
    private String type;         // TYPE (수입/지출 구분: IN/OUT)
    private String nickname;     // NICKNAME (작성자 닉네임)
    private String status;       // STATUS (상태값: 'Y', 'N')
    private int groupbId;       // GROUPB_ID (소속된 그룹 가계부 ID - FK)
}