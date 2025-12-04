package com.ihb.mytalentbackend.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;          // 현재 페이지 번호
    private int size;          // 한 페이지당 개수
    private int total;         // 전체 데이터 개수
    private int pageBlockSize; // 한 번에 표시할 페이지 번호 개수 (ex. 3)

    private int start;         // 페이지 블록 시작 번호
    private int end;           // 페이지 블록 마지막 번호
    private boolean prev;      // 이전 블록 존재 여부
    private boolean next;      // 다음 블록 존재 여부
    private int first;         // 첫 페이지(보통 1)
    private int last;          // 마지막 페이지 번호

    private List<E> dtoList;   // 실제 데이터 목록

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO,
                           List<E> dtoList,
                           int total) {

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.pageBlockSize = 3; // 한 번에 몇 개의 페이지 번호를 보여줄지
        this.dtoList = dtoList;

        this.first = 1;

        // 전체 마지막 페이지 (total이 0이면 1페이지로 간주)
        this.last = Math.max(1, (int) Math.ceil((double) total / this.size));

        // 현재 페이지가 속한 "블록"의 끝 번호
        int tempEnd = (int) (Math.ceil(this.page / (double) pageBlockSize) * pageBlockSize);

        // 블록의 시작 번호 = 끝 번호 - (블록 크기 - 1)
        this.start = tempEnd - (pageBlockSize - 1);

        // 실제 마지막 페이지를 넘어가지 않도록 조정
        this.end = Math.min(tempEnd, this.last);

        // 보정: 시작 페이지가 1보다 작지 않도록
        if (this.start < 1) this.start = 1;

        // 이전/다음 블록 존재 여부
        this.prev = this.start > 1;
        this.next = this.end < this.last;
    }
}
