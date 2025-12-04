package com.ihb.mytalentbackend.dto.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;   // 1페이지부터 시작

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    @Positive
    private int size = 10;  // 페이지당 개수

    private String link;
    private String type;
    private String keyword;

    // 검색 타입 문자열 분리 (예: "tc" -> ["t", "c"])
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        } else {
            return type.split("");
        }
    }

    // 정렬 기준 컬럼명을 받아서 Pageable 생성 (내림차순)
    public Pageable getPageable(String... props) {
        return PageRequest.of(this.page - 1, size, Sort.by(props).descending());
    }

    // page/size/type/keyword를 포함한 쿼리스트링 생성 (필요하면 사용)
    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=").append(this.page);
            builder.append("&size=").append(this.size);
            if (type != null && type.length() > 0) {
                builder.append("&type=").append(this.type);
            }
            if (keyword != null) {
                try {
                    builder.append("&keyword=").append(URLEncoder.encode(this.keyword, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            link = builder.toString();
        }
        return link;
    }
}
